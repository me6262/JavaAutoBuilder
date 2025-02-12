/**
 * loads the frc robot project and exposes methods to get basic info
 */
package frc238.background

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import frc238.App
import frc238.widgets.makeAndRunPopup
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
import kotlin.collections.ArrayList
import kotlin.collections.orEmpty
import kotlin.collections.asList
import kotlin.arrayOf
import kotlin.io.println

class RobotProject(var rootDirectory: String) {
    private val mapper: ObjectMapper = ObjectMapper()
    private var jsonString: String? = null
    private var loadSucceeded = false
    var trajectories: ArrayList<String>? = null
        private set

    lateinit var WPILibClassLoader: URLClassLoader

    constructor() : this("")

    init {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        if (rootDirectory.isNotEmpty()) {
//            App.plugins = PluginManager()
            loadJson()
            println(jsonString)

        }
    }

    @Throws(FileNotFoundException::class)
    fun indexCommands() {
        val pattern = Pattern.compile(Constants.autoModeAnnotationRegex)
        val dir = File(rootDirectory + Constants.commandDirectory)
        val directoryListing = dir.listFiles() ?: return
        for (child in directoryListing) {
            if (child.isDirectory) {
                continue
            }
            val info: CommandInfo
            val params: Array<String?>
            val scanner = Scanner(child)
            var line = scanner.nextLine()
            var commandConstructor: Constructor<*>? = null
            var foundAnnotation: Boolean = false
            var paramClasses = arrayListOf<Class<Any>>()
            var findPattern: String? = null
            // Do something with child
            while (scanner.hasNextLine()) {
                line = scanner.nextLine()
                findPattern = scanner.findInLine(pattern) ?: continue
                foundAnnotation = true
            }
            if (!foundAnnotation) continue

            val name = child.name.substring(0, child.name.length - 5)
            //@Auto(name = {})
            if (findPattern!!.contains("name")) {
                println(findPattern)

                params = findPattern
                .substring(15, findPattern.length - 2)
                .split(", ".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
                .map { 
                    it.removeSurrounding("\"")
                }
                .toTypedArray()
            } else {
                params = arrayOf<String?>()
            }
            

            var paramsList: ArrayList<String?> = params.toMutableList() as ArrayList<String?>
            var parameterTypes: Array<Class<*>?> = Array(1) { null }
            // here begins the lovely process of using so much reflection it makes my nose bleed
            // we pretty much grab the .jar file of compiled code in the build directory of the robot project,
            // and load the current command, check for the annotated constructor, and get the types for each of its parameters for later use
            if (App.settings.classLoading) {

                reloadWPILibClassLoader()
                
                val auto = WPILibClassLoader.loadClass("frc.robot.autonomous.Auto") as Class<out Annotation>

                val loadedClass = WPILibClassLoader.loadClass("frc.robot.commands.$name")
                var parameters: ArrayList<String?> = ArrayList();
                // looks through all possible constructors in the class for the annotated one
                for (constructor in loadedClass.constructors) {
                    if (constructor.isAnnotationPresent(auto)) {
                        
                        commandConstructor = constructor
                        
                        //this snippet gets the name of each parameter directly from the annotation to get around regex formatting difficulties
                        //gets the string array in the annotation, checks its length, and if it's greater than 0, we add them all to the parameters variable
                        //I am trapped in a reflection hell of my own design
                        //help me please
                        val names = auto.getMethod("names").invoke(constructor.getAnnotation(auto)) as Array<String?>
                        if (names.size > 0) {
                            names.mapTo(parameters) { it }
                            paramsList = parameters
                        }
                    }
                }
                parameterTypes = commandConstructor!!.parameterTypes
            }
            info = CommandInfo(name, paramsList, parameterTypes)
            Companion.commands.add(info)
            println(info.name + " with parameters " + info.parameters.toString().strip())
        }
        if (commands.isEmpty()) {
            makeAndRunPopup("No commands found", "None were valid")
        }
    }


    fun loadJson() {
        jsonString = try {
            loadSucceeded = true
            Files.readString(Paths.get(rootDirectory + Constants.deployDirectory + Constants.amodeFile))
        } catch (e: IOException) {
            e.printStackTrace()
            loadSucceeded = false
            makeAndRunPopup("amode238.txt is invalid", "IOException")
            if (!File(rootDirectory + Constants.deployDirectory + Constants.amodeFile).exists()) {
                val amode = File(rootDirectory + Constants.deployDirectory + Constants.amodeFile)
                amode.createNewFile()
                amode.writeText("{\"autonomousModes\":[]}")
                amode.readText()
            } else {
                null
            }
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
                e.printStackTrace()
                makeAndRunPopup("There was a problem processing the json", "JsonProcessingException")
            }
            amodesList
        }
        set(list) {
            amodesList = list
            saveOut()
        }

    private fun saveOut() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(File(rootDirectory + Constants.deployDirectory + "amode238.txt"), amodesList)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            makeAndRunPopup("there was a problem saving to the file", "JsonProcessingException")
        }

    }

    private fun reloadWPILibClassLoader() {
        val urls: Array<URL> = Array(1) { File("$rootDirectory/build/libs").listFiles()?.first()!!.toURI().toURL() }
        WPILibClassLoader = URLClassLoader(urls)
    }

    fun tempSaveDraggedCommand() {}

    object Constants {
        val amodeFile = "amode238.txt"
        val deployDirectory = "/src/main/deploy/".replace('/', File.separatorChar)
        val pathPlannerDir = "choreo/".replace('/', File.separatorChar)
        val commandDirectory = "/src/main/java/frc/robot/commands/".replace('/', File.separatorChar)
        val commandsRegex = ".*/(.*).java"
        val pathFileRegex = ".*/(.*)\\.traj"
        val autoModeAnnotationRegex = "@Auto.*"
    }

    companion object {
        private var amodesList: AmodeList? = null
        private val commands: MutableList<CommandInfo> = ArrayList()
    }

}
