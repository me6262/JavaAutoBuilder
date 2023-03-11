/**
 * loads the frc robot project and exposes methods to get basic info
 */

package java238.background;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RobotProject {

    private String rootDirectory;
    private ObjectMapper mapper;
    private String jsonString;
    private AmodesList amodesList;
    public RobotProject(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public RobotProject() {

    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void loadRobotProject() {
        try {
            jsonString = Files.readString(Paths.get(rootDirectory + Constants.deployDirectory + Constants.amodeFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getJsonString() {
        return jsonString;
    }

    public AmodesList getAutoModes() {
        try {
            amodesList = mapper.readValue(jsonString, new TypeReference<AmodesList>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
            return amodesList;
    }

    public


    static class Constants {
        public static final String deployDirectory = "/src/main/deploy/";
        public static final String amodeFile = "amode238.txt";
    }

}





