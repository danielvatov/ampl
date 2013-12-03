package net.vatov.ampl.solver.io;

import java.io.PrintStream;
import java.io.InputStream;
import java.util.List;

public interface UserIO {

    public Integer getChoice(List<String> options, Integer defaultOption, String question);
    
    public Integer getInt(Integer defaultValue, String question);
    
    public Boolean getYesNo(Boolean defaultValue, String question);

    public String getString(String defaultValue, String question);

    public void refreshData(Object data);
    
    public void pause(String msg);

    public void message(String paramString);

    public void close();

    public class Factory {
        public static UserIO createStdUserIO() {
            return new StdUserIO(System.in, System.out);
        }

        public static UserIO createStreamUserIO(InputStream in, PrintStream out) {
            return new StdUserIO(in, out);
        }
    }

}
