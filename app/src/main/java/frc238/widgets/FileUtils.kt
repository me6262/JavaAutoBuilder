package frc238.widgets

import ch.bailu.gtk.gtk.FileChooserAction
import ch.bailu.gtk.gtk.FileChooserDialog
import ch.bailu.gtk.gtk.ResponseType
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.exception.AllocationError
import com.sun.jna.Platform
import frc238.App
import frc238.App.initAutoList
import frc238.background.RobotProject
import org.jetbrains.kotlin.incremental.createDirectory
import java.io.File


private var currentProjectFolder: String? = null
lateinit var chooser: FileChooserDialog

public var trajectories = ArrayList<String>()
fun loadFolder() {
    chooser = FileChooserDialog("Stuff", App.window, FileChooserAction.SELECT_FOLDER, "Open", ResponseType.ACCEPT)
    chooser.onResponse { responseID: Int ->
        println(responseID)
        if (responseID == ResponseType.ACCEPT) {
            currentProjectFolder = chooser.asFileChooser().currentFolder.parseName.toString()
            println(currentProjectFolder)
            App.project.rootDirectory = currentProjectFolder!!
            App.project.loadJson()
            //gets the names of all the .traj files and sorts them alphabetically
            if (App.settings.pluginsEnabled) {

                val dir = File(App.project.rootDirectory + RobotProject.Constants.deployDirectory + "choreo${File.separatorChar}")
                val dirList: Array<File> = dir.listFiles()!!
                for (child in dirList) {
                    println(child.name)
                    trajectories.add(child.name.replace(".traj", ""))
                }
                trajectories.sortBy { it }
            }

            chooser.destroy()
            initAutoList()
            App.settings.projectDir = currentProjectFolder!!
            App.settings.saveSettings()

        }
    }
    val startPath = ch.bailu.gtk.gio.File.newForPath(Str(System.getProperty("user.home")))
    try {
        chooser.asFileChooser().setCurrentFolder(startPath)
    } catch (e: AllocationError) {
        throw RuntimeException(e)
    }
    chooser.show()
}

fun makeOrOpenXDGDataDir(): File {
    var dir: File? = null
    if (Platform.isLinux()) {
        val homedir = System.getProperty("user.home")
        if (!File("$homedir/.local/share/AutoBuilder").isDirectory()) {
            dir = File("$homedir/.local/share/AutoBuilder")
            println( dir.createDirectory())
            println(dir.path)
            File("$homedir/.local/share/AutoBuilder/plugins").mkdir()
            val url = App.javaClass.classLoader.getResourceAsStream("plugins/TrajectoryName.kts")
            val script = File(dir.path + "/plugins/TrajectoryName.kts")
            script.createNewFile()
            url!!.transferTo(script.outputStream())

        } else {
            dir = File("$homedir/.local/share/AutoBuilder")
        }

    } else if (Platform.isWindows()) {
        val homedir = System.getProperty("user.home") + "\\AppData\\Roaming"
        if (!File("$homedir\\AutoBuilder").isDirectory) {
            dir = File("$homedir\\AutoBuilder")
            dir.createDirectory()
            println(dir.path)
            File("$homedir\\AutoBuilder\\plugins").createDirectory()
            val url = App.javaClass.classLoader.getResourceAsStream("plugins/TrajectoryName.kts")
            val script = File(dir.path + "\\plugins\\TrajectoryName.kts")
            println( script.createNewFile())
            url!!.transferTo(script.outputStream())
        } else {
            dir = File("$homedir\\AutoBuilder")
        }
    }
    return dir!!
}

fun getOrCreateDataFile(localPath: String): File {
    var file: File = File(makeOrOpenXDGDataDir().absolutePath + localPath)
    println(file.path)
    if (file.isFile) {
        println("is File")
        return file
    } else {
        println("is not file")
        file.createNewFile()
        file.writeText(settingsTemplate)
    }

    return file
}

fun getPluginFolder(): File? {
    println("returning ${makeOrOpenXDGDataDir().path}${File.separator}plugins")
    return File("${makeOrOpenXDGDataDir().path}${File.separator}plugins")
}


const val settingsTemplate = "{\"projectDir\":\"\",\"wpilibVersion\":\"2024.1.1\",\"pluginsEnabled\":false,\"wpilibDirectory\":\"\",\"canLoadClasses\":false}"
