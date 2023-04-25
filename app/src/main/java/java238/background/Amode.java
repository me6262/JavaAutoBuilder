package java238.background;

import java.util.ArrayList;
import java.util.List;

public class Amode {
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String val){
        this.name = val;
    }

    private List<AmodeCommand> commands = new ArrayList<AmodeCommand>();
    public List<AmodeCommand> getCommands(){
        return commands;
    }
    public void setCommands(List<AmodeCommand> val){
        commands = val;
    }
}
