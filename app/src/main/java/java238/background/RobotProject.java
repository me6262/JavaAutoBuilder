/**
 * loads the frc robot project and exposes methods to get basic info
 */

package java238.background;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RobotProject {

    private String rootDirectory;
    private ObjectMapper mapper;
    private String jsonString;
    private static AmodeList amodesList;
    private static List<CommandInfo> commands = new ArrayList<>();

    public RobotProject(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static OSName getOSName() {
            return System.getProperty("os.name").toLowerCase().contains("win") ? OSName.Windows : OSName.Unix;
    }

    public void indexCommands() throws FileNotFoundException {
        Pattern pattern = Pattern.compile(Constants.autoModeAnnotationRegex);

        File dir = new File(rootDirectory + Constants.commandDirectory);
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                CommandInfo info;
                String[] params;
                Scanner scanner = new Scanner(child);
                var line = scanner.nextLine();
                // Do something with child
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    var findPattern = scanner.findInLine(pattern);
                    if (findPattern != null) {
                        params = findPattern.substring(44, findPattern.length() - 2).split(", ");
                        info = new CommandInfo(child.getName().substring(0, child.getName().length() - 5), params);
                        System.out.println(info.getName() + " with parameters " + Arrays.toString(info.getParameters()).strip());
                        commands.add(info);
                        break;
                    }

                }


            }
            }



    }


    public RobotProject() {
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
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

    public List<CommandInfo> getCommands() {
        return commands;
    }

    public AmodeList getAutoModes() {
        if (amodesList != null) {
            return amodesList;
        } else {
            try {
                amodesList = mapper.readValue(jsonString, new TypeReference<AmodeList>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return amodesList;
        }
    }

    public void setAutoModes(AmodeList list) {
        amodesList = list;
        saveOut();
    }

    public void saveOut() {
        try {
            System.out.println(mapper.writeValueAsString(amodesList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




    public static class Constants {
        public static final String amodeFile = "amode238.txt";
        public static final String deployDirectory = getOSName() == OSName.Unix ? "/src/main/deploy/" : "\\src\\main\\deploy\\";
        public static final String pathPlannerDir = getOSName() == OSName.Unix ? "pathplanner/" : "pathplanner\\";
        public static final String commandDirectory = getOSName() == OSName.Unix ? "/src/main/java/frc/robot/commands/" : "\\src\\main\\java\\frc\\robot\\commands\\";
        public static final String commandsRegex = getOSName() == OSName.Unix ? ".*/(.*).java" : ".*\\\\(.*).java";
        public static final String pathFileRegex = getOSName() == OSName.Unix ? ".*/(.*)\\.path" : ".*\\\\(.*)\\.path";
        public static final String autoModeAnnotationRegex = "@AutonomousModeAnnotation\\(parameterNames = \\{(.*)}\\)";

    }

        public static enum OSName {
            Unix,
            Windows
        }
    }







