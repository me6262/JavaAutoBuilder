package java238.background;

public class CommandInfo {
    private String name;
    private String[] parameters;

    public CommandInfo(String name, String[] parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public CommandInfo() {

    }
    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
}
