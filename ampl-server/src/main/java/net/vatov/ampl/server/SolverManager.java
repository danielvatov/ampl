/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.vatov.ampl.solver.Solver;
import net.vatov.ampl.solver.SolverAdapterFactory;
import net.vatov.ampl.solver.impl.ExecutionNotFoundException;


public class SolverManager {

    private class SolverExecution implements Callable<Map<String, String>> {
        private final UUID          uuid;
        private final Solver instance;
        private final String        input;

        SolverExecution(UUID uuid, Solver instance, String input) {
            this.uuid = uuid;
            this.instance = instance;
            this.input = input;
        }

        @Override
        public Map<String, String> call() {
            return instance.solve(new ByteArrayInputStream(input.getBytes()), null);
        }

        @Override
        public int hashCode() {
            return uuid.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return true;
            }
            if (!(obj instanceof SolverExecution)) {
                return false;
            }
            return uuid.equals(((SolverExecution) obj).uuid);
        }
    }

    // TODO да се сложи в конфигурация
    private static final int                       EXEC_POOL_SIZE = 5;

    private static final SolverManager             instance       = new SolverManager();

    private ExecutorService                        executionsPool = Executors.newFixedThreadPool(EXEC_POOL_SIZE);
    //TODO чистене на executions
    private Map<UUID, Future<Map<String, String>>> executions     = new HashMap<UUID, Future<Map<String, String>>>();

    private SolverManager() {
    }

    public static SolverManager getInstance() {
        return instance;
    }

    public List<String> getSolvers() {
        return SolverAdapterFactory.getSupportedSolvers();
    }

    public UUID executeSolver(String solverName, String input) {
        Solver instance = SolverAdapterFactory.getSolverAdapter(solverName);
        UUID uuid = UUID.randomUUID();
        SolverExecution execution = new SolverExecution(uuid, instance, input);
        // TODO проверка на междинен статус
        Future<Map<String, String>> future = executionsPool.submit(execution);
        executions.put(uuid, future);
        return uuid;
    }

    public Map<String, String> getExecutionResult(UUID uuid) {
        if (!executions.containsKey(uuid)) {
            throw new ExecutionNotFoundException(uuid.toString());
        }
        Future<Map<String, String>> future = executions.get(uuid);
        if (future.isDone()) {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}