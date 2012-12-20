package net.vatov.ampl.solver.io;

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
            out.println(String.format("[%d]:\n%s", i, option));
            i++;
        }
        Integer ret = getInt(defaultOption, question);
        if (null == ret || ret < 1 || ret > options.size()) {
            throw new SolverIOException("Invalid choice " + ret);
        }
        return ret;
    }

    public Integer getInt(Integer defaultValue, String question) {
        if (null != defaultValue) {
            out.println(String.format("%s [%d]: ", question, defaultValue));
        } else {
            out.println(question + ": ");
        }
        return getInt();
    }

    private Integer getInt() {
        Scanner scanner = new Scanner(in);
        return scanner.nextInt();
    }

    public Boolean getYesNo(Boolean defaultValue, String question) {
        if (null != defaultValue) {
            out.println(String.format("%s [%s]: ", question, defaultValue ? "yes" : "no"));
        } else {
            out.println(question + ": ");
        }
        Scanner scanner = new Scanner(in);
        String line = scanner.nextLine();
        if ((line == null || line.trim().isEmpty())) {
            if (defaultValue != null) {
                return  defaultValue;
            } else {
                throw new SolverIOException("Input missing");
            }
        }
        return line.trim().equalsIgnoreCase("yes");
    }
}
