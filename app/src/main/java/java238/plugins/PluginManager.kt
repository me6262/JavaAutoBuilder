package java238.plugins


import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

/**
 * will evaluate kotlinscript (.kts) files and use them as custom sources for
 * parameter fields where there are limited valid options, but reflection is insufficient in locating these
 * for example, all the names of trajectories, which is given as a string, since adding and removing from
 * an enum for each trajectory would be tiresome and inconvenient
 */
class PluginManager {

    val plugins = HashMap<Boolean, Class<Plugin>>()
    val allowedPlugins = HashMap<String, Pair<Boolean, Class<Plugin>>>()

    init {
        evalParameters("TrajectoryName")
    }



    private fun evalParameters(name: String): Any? {

        val folder = File(Paths.get("plugins/").toAbsolutePath().toUri())

        for (file in folder.listFiles()!!) {
            println(file.name)
            if (file.nameWithoutExtension == name) {
                val params: ArrayList<String> = KtsObjectLoader().load<ArrayList<String>>(file.readText())
                for (str in params) {
                    print(str)
                }
            }
        }
        return null
    }
}