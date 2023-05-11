package frc238.background;

import java.util.ArrayList;
import java.util.List;

public class AmodeList {
    private List<Amode> AutonomousModes = new ArrayList<Amode>();
    public List<Amode> getAutonomousModes(){
        return AutonomousModes;
    }
    public void setAutonomousModes(ArrayList<Amode> val){
        AutonomousModes = val;
    }

}
