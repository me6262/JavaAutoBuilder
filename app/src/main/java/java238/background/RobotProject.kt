/**
 * loads the frc robot project and exposes methods to get basic info
 */
package java238.background

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.internal.Intrinsics.Kotlin

class RobotProject {
    private val mapper: ObjectMapper = ObjectMapper()
    private var jsonString: String? = null
    var trajectories: ArrayList<String>? = null
        private set

    @Throws(FileNotFoundException::class)
    fun indexCommands() {
        val pattern = Pattern.compile(Constants.autoModeAnnotationRegex)
        val dir = File(rootDirectory + Constants.commandDirectory)
        val directoryListing = dir.listFiles() ?: return
        for (child in directoryListing) {
            var info: CommandInfo
            var params: Array<String?>
            val scanner = Scanner(child)
            var line = scanner.nextLine()

            // Do something with child
            while (scanner.hasNextLine()) {
                line = scanner.nextLine()
                val findPattern = scanner.findInLine(pattern) ?: continue
                params = findPattern.substring(44, findPattern.length - 2).split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val paramsList = ArrayList<String?>(params.size)
                paramsList.addAll(params)
                info = CommandInfo(child.name.substring(0, child.name.length - 5), paramsList)
                Companion.commands.add(info)
                println(info.name + " with parameters " + info.parameters.toString().strip())
                break
            }
        }
    }

    fun indexTrajectories() {
        trajectories = ArrayList()
        val pattern = Pattern.compile(rootDirectory + Constants.deployDirectory + Constants.pathPlannerDir)
        val dir = File(rootDirectory + Constants.deployDirectory + Constants.pathPlannerDir)
        val dirList = dir.listFiles()!!
        for (child in dirList) {
            println(child.name)
            trajectories!!.add(child.name.split("\\.path".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
        }
        println(trajectories)
    }

    init {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
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

    fun saveOut() {
        try {
            println(mapper.writeValueAsString(amodesList))
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    fun tempSaveDraggedCommand() {}

    object Constants {
        val amodeFile = "amode238.txt"
        val deployDirectory = if (oSName == OSName.Unix) "/src/main/deploy/" else "\\src\\main\\deploy\\"
        val pathPlannerDir = if (oSName == OSName.Unix) "pathplanner/" else "pathplanner\\"
        val commandDirectory = if (oSName == OSName.Unix) "/src/main/java/frc/robot/commands/" else "\\src\\main\\java\\u000crc\\robot\\commands\\"
        val commandsRegex = if (oSName == OSName.Unix) ".*/(.*).java" else ".*\\\\(.*).java"
        val pathFileRegex = if (oSName == OSName.Unix) ".*/(.*)\\.path" else ".*\\\\(.*)\\.path"
        val autoModeAnnotationRegex = "@AutonomousModeAnnotation\\(parameterNames = \\{(.*)}\\)"
    }

    enum class OSName {
        Unix, Windows
    }

    companion object {
        lateinit var rootDirectory: String
        val amodeBuilderDir =
        private var amodesList: AmodeList? = null
        private val commands: MutableList<CommandInfo> = ArrayList()
        val oSName: OSName
            get() = if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) OSName.Windows else OSName.Unix
    }

}