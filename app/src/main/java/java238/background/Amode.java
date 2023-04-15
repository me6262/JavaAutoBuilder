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

    private List<AmodeCommandList> commands = new ArrayList<AmodeCommandList>();
    public List<AmodeCommandList> getCommands(){
        return commands;
    }
    public void setCommands(List<AmodeCommandList> val){
        commands = val;
    }
}
