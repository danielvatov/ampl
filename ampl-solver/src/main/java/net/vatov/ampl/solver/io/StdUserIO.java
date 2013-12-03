package net.vatov.ampl.solver.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class StdUserIO implements UserIO {
    private InputStream in;
    private PrintStream out;

    StdUserIO(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public Integer getChoice(List<String> options, Integer defaultOption, String question) {
        int i = 1;
        for (String option : options) {
            this.out.println(String.format("[%d]:\n%s", new Object[] { Integer.valueOf(i), option }));
            i++;
        }
        Integer ret = getInt(defaultOption, question);
        if ((null == ret) || (ret.intValue() < 1) || (ret.intValue() > options.size())) {
            throw new InvalidUserInputException("Invalid choice " + ret);
        }
        return ret;
    }

    public Integer getInt(Integer defaultValue, String question) {
        if (null != defaultValue) {
            this.out.print(String.format("%s [%d]: ", new Object[] { question, defaultValue }));
            Integer ret = getInt(true);
            if (null == ret) {
                return defaultValue;
            }
            return ret;
        }
        this.out.print(question + ": ");
        return getInt(false);
    }

    private Integer getInt(boolean hasDefault) {
        Scanner scanner = new Scanner(this.in);
        String line = scanner.nextLine();
        if (("".equals(line)) && (hasDefault))
            return null;
        try {
            return Integer.valueOf(Integer.parseInt(line));
        } catch (NumberFormatException e) {
            throw new InvalidUserInputException(e.getMessage());
        }
    }

    public Boolean getYesNo(Boolean defaultValue, String question) {
        if (null != defaultValue)
            this.out.print(String.format("%s [%s]: ", new Object[] { question,
                    defaultValue.booleanValue() ? "yes" : "no" }));
        else {
            this.out.print(question + ": ");
        }
        Scanner scanner = new Scanner(this.in);
        String line = scanner.nextLine();
        if ((line == null) || (line.trim().isEmpty())) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new InvalidUserInputException("Input missing");
        }

        return Boolean.valueOf(line.trim().equalsIgnoreCase("yes"));
    }

    public void refreshData(Object data) {
        this.out.println(data);
    }

    public void pause(String question) {
        this.out.println(question + ": ");
        Scanner scanner = new Scanner(this.in);
        scanner.nextLine();
    }

    public void message(String question) {
        this.out.println(question + ": ");
    }

    public void close() {
        try {
            this.in.close();
        } catch (IOException e) {
            throw new SolverIOException(e);
        }
        this.out.close();
    }

    @Override
    public String getString(String defaultValue, String question) {
        if (null != defaultValue && defaultValue.length() > 0)
            this.out.print(String.format("%s [%s]: ", new Object[] { question, defaultValue }));
        else {
            this.out.print(question + ": ");
        }
        Scanner scanner = new Scanner(this.in);
        String line = scanner.nextLine();
        if ((line == null) || (line.trim().isEmpty())) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new InvalidUserInputException("Input missing");
        }

        return line;
    }
}