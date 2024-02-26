import java.io.File
import java.util.ArrayList
import frc238.App
import frc238.background.RobotProject.Constants
import org.jetbrains.kotlin.util.removeSuffixIfPresent

// gets the list of trajectories
val trajectories = ArrayList<String>()
val dir = File(App.project.rootDirectory + Constants.deployDirectory + "choreo${File.separatorChar}")
val dirList: Array<File> = dir.listFiles()!!
for (child in dirList) {
    println(child.name)
    trajectories.add(child.name.removeSuffixIfPresent(".traj"))
}

//technically, this is an expression, which means awful things that i absolutely hate
// this is equivalent to this whole file being a function where trajectories is the return value
trajectories
