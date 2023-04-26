package java238.plugins

import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*


class PluginManager {

    val plugins = HashMap<Boolean, Class<Plugin>>()
    val allowedPlugins = HashMap<String, Pair<Boolean, Class<Plugin>>>()
    init {
        for (plugin in getClasses()) {
            if (plugin == null) continue

            for (iface in plugin.interfaces) {
                if (!iface.name.contains("Plugin") || plugin.isInterface) continue

                println(plugin.name)
                allowedPlugins += Pair((plugin.getConstructor().newInstance() as Plugin).parameterName, Pair(true, plugin as Class<Plugin>))
            }

        }
    }


    @Throws(ClassNotFoundException::class, IOException::class)
    private fun getClasses(): Array<Class<*>?> {
        val packageName = "java238.plugins"
        val classLoader = Thread.currentThread().contextClassLoader!!
        val path = packageName.replace('.', '/')
        val resources: Enumeration<*> = classLoader.getResources(path)
        val dirs: ArrayList<File> = ArrayList()
        while (resources.hasMoreElements()) {
            val resource: URL = resources.nextElement() as URL
            dirs.add(File(resource.file))
        }
        val classes: ArrayList<Class<*>> = ArrayList()
        for (directory in dirs) {
            classes.addAll(findClasses(directory, packageName))
        }
        return classes.toTypedArray()
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @Throws(ClassNotFoundException::class)
    private fun findClasses(directory: File, packageName: String): Collection<Class<*>> {
        val classes: ArrayList<Class<*>> = ArrayList()
        if (!directory.exists()) {
            return classes
        }
        val files = directory.listFiles()
        for (file in files!!) {
            if (file.isDirectory) {
                assert(!file.name.contains("."))
                classes.addAll(findClasses(file, packageName + "." + file.name))
            } else if (file.name.endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.name.substring(0, file.name.length - 6)))
            }
        }
        return classes
    }

//    fun addPlugin

}