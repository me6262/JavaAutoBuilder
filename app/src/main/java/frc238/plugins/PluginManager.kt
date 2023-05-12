package frc238.plugins


import frc238.App
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

/**
 * will evaluate kotlinscript (.kts) files and use them as custom sources for
 * parameter fields where there are limited valid options, but reflection is insufficient in locating these
 * for example, all the names of trajectories, which is given as a string, since adding and removing from
 * an enum for each trajectory would be tiresome and inconvenient
 *
 * also useful in cases where your project is not written in java/kotlin, but you still want fancy dropdowns.
 *
 */
class PluginManager {

    val plugins = HashMap<Boolean, PluginData>()
    val allowedPlugins = HashMap<String, Pair<Boolean, PluginData>>()

    init {
        if (App.settings.pluginsEnabled) {
            evalParameters()
        }
    }



    private fun evalParameters(): Any? {

        val folder = File(Paths.get("plugins/").toAbsolutePath().toUri())

        for (file in folder.listFiles()!!) {
            println(file.name)
            if (file.extension == "kts") {
                val params: ArrayList<String> = KtsObjectLoader().load<ArrayList<String>>(file.readText())
                for (str in params) {
                    allowedPlugins[file.nameWithoutExtension] = Pair(true, PluginData(params, file.nameWithoutExtension))
                }
            }
        }
        return null
    }
}