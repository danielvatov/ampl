package net.vatov.ampl.solver.io;

import java.util.List;

public interface UserIO {

    public Integer getChoice(List<String> options, Integer defaultOption, String question);
    
    public Integer getInt(Integer defaultValue, String question);
    
    public Boolean getYesNo(Boolean defaultValue, String question);

    public class Factory {
        public static UserIO createStdUserIO() {
            return new StdUserIO(System.in, System.out);
        }
    }
}