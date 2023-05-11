/**
 * loads the frc robot project and exposes methods to get basic info
 */
package frc238.background

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.jna.Platform
import frc238.App
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.reflect.Constructor
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern

class RobotProject(var rootDirectory: String) {
    private val mapper: ObjectMapper = ObjectMapper()
    private var jsonString: String? = null
    var trajectories: ArrayList<String>? = null
        private set

    constructor() : this("")

    init {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        if (rootDirectory.isNotEmpty()) {
            loadRobotProject()

        }
    }
    @Throws(FileNotFoundException::class)
    fun indexCommands() {
        val pattern = Pattern.compile(Constants.autoModeAnnotationRegex)
        val dir = File(rootDirectory + Constants.commandDirectory)
        val directoryListing = dir.listFiles() ?: return
        for (child in directoryListing) {
            val info: CommandInfo
            val params: Array<String?>
            val scanner = Scanner(child)
            var line = scanner.nextLine()
            var commandConstructor : Constructor<*>? = null
            var foundAnnotation: Boolean = false
            var paramClasses = arrayListOf<Class<Any>>()
            var findPattern:String? = null
            // Do something with child
            while (scanner.hasNextLine()) {
                line = scanner.nextLine()
                findPattern = scanner.findInLine(pattern) ?: continue
                foundAnnotation = true
            }
            if (!foundAnnotation) continue

            val name = child.name.substring(0, child.name.length - 5)

            params = findPattern!!.substring(44, findPattern.length - 2).split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val paramsList: ArrayList<String?> = params.toMutableList() as ArrayList<String?>
            var parameterTypes: Array<Class<*>?> = Array(1){null}
            if (App.settings.classLoading) {
            val urls: Array<URL> = Array(4){File("$rootDirectory/build/classes/java/main/").toURI().toURL()}
            urls[1] =  File( "${App.settings.wpiDirectory}/2023/maven/edu/wpi/first/wpilibj/wpilibj-java/${App.settings.wpilibVersion}/wpilibj-java-${App.settings.wpilibVersion}.jar/".apply {
                if (Platform.isWindows()) this.replace("/", "\\")
            }).toURI().toURL()
            urls[2] =  File( "${App.settings.wpiDirectory}/2023/maven/edu/wpi/first/wpilibNewCommands/wpilibNewCommands-java/${App.settings.wpilibVersion}/wpilibNewCommands-java-${App.settings.wpilibVersion}.jar/".apply {
                if (Platform.isWindows()) this.replace("/", "\\")
            }).toURI().toURL()
            urls[3] =  File( "${App.settings.wpiDirectory}/2023/maven/edu/wpi/first/wpiutil/wpiutil-java/${App.settings.wpilibVersion}/wpiutil-java-${App.settings.wpilibVersion}.jar/".apply {
                if (Platform.isWindows()) this.replace("/", "\\")
            }).toURI().toURL()
            val loader: ClassLoader = URLClassLoader(urls)
            val loadedClass = loader.loadClass("frc.robot.commands.$name")
            for (constructor in loadedClass.constructors) {
                if (constructor.parameterCount == params.size) {
                    commandConstructor = constructor
                }
            }
            parameterTypes = commandConstructor!!.parameterTypes
            }
            info = CommandInfo(name, paramsList, parameterTypes)
            Companion.commands.add(info)
            println(info.name + " with parameters " + info.parameters.toString().strip())
        }
    }


    fun loadRobotProject() {
        jsonString = try {
            Files.readString(Paths.get(rootDirectory + Constants.deployDirectory + Constants.amodeFile))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val commands: List<CommandInfo>
        get() = Companion.commands
    var autoModes: AmodeList?
        get() = if (amodesList != null) {
            amodesList
        } else {
            try {
                amodesList = mapper.readValue(jsonString, object : TypeReference<AmodeList?>() {})
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
            amodesList
        }
        set(list) {
            amodesList = list
            saveOut()
        }

    private fun saveOut() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(File(rootDirectory + Constants.deployDirectory + "amode238.txt"), amodesList)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    fun tempSaveDraggedCommand() {}

    object Constants {
        val amodeFile = "amode238.txt"
        val deployDirectory = if (Platform.isLinux()) "/src/main/deploy/" else "\\src\\main\\deploy\\"
        val pathPlannerDir = if (Platform.isLinux()) "pathplanner/" else "pathplanner\\"
        val commandDirectory = if (Platform.isLinux()) "/src/main/java/frc/robot/commands/" else "\\src\\main\\java\\frc\\robot\\commands\\"
        val commandsRegex = if (Platform.isLinux()) ".*/(.*).java" else ".*\\\\(.*).java"
        val pathFileRegex = if (Platform.isLinux()) ".*/(.*)\\.path" else ".*\\\\(.*)\\.path"
        val autoModeAnnotationRegex = "@AutonomousModeAnnotation\\(parameterNames = \\{(.*)}\\)"
    }

    enum class OSName {
        Unix, Windows
    }

    companion object {
        private var amodesList: AmodeList? = null
        private val commands: MutableList<CommandInfo> = ArrayList()
        val oSName: OSName
            get() = if (System.getProperty("os.name").lowercase().contains("win")) OSName.Windows else OSName.Unix
    }

}