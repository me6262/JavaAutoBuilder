package java238.background;

import java.util.ArrayList;

public class CommandInfo {
    private String name;
    private ArrayList<String> parameters;

    public CommandInfo(String name, ArrayList<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public CommandInfo() {

    }
    public String getName() {
        return name;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }
}
