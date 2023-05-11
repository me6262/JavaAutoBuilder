import java.io.File
import java.util.ArrayList
import java.util.regex.Pattern
import java238.App
import java238.background.RobotProject.Constants

// gets the list of trajectories
val trajectories = ArrayList<String>()
val pattern = Pattern.compile((App.project.rootDirectory + Constants.deployDirectory + Constants.pathPlannerDir).replace("\\", "\\\\"))
val dir = File(App.project.rootDirectory + Constants.deployDirectory + Constants.pathPlannerDir)
val dirList = dir.listFiles()!!
for (child in dirList) {
    println(child.name)
    trajectories!!.add(child.name.split("\\.path".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
}

//technically, this is an expression, which means awful things that i absolutely hate
// this is equivalent to this whole file being a function where trajectories is the return value
trajectories