package frc238.background;

import java.util.ArrayList;
import java.util.List;


/**
 * mapped by the json objectmapper
 */
public class AmodeCommand {
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String val){
        this.name = val;
    }

    private List<String> parameters = new ArrayList<>();
    public List<String> getParameters(){
        return parameters;
    }
    public void setParameters(ArrayList<String> val){
        this.parameters = val;
    }

    private String isParallel;
    public String getParallelType(){
        return isParallel;
    }
    public void setParallelType(String val){
        this.isParallel = val;
    }
}
