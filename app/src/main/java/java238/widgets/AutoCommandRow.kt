package java238.widgets

import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.EntryRow
import ch.bailu.gtk.adw.ExpanderRow
import ch.bailu.gtk.gdk.ContentProvider
import ch.bailu.gtk.gdk.Drag
import ch.bailu.gtk.gdk.Paintable
import ch.bailu.gtk.gio.File
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.PointerContainer
import ch.bailu.gtk.type.Str
import java238.App
import java238.background.AmodeCommand
import java238.background.CommandInfo
import java.util.function.Supplier

/**
 * ExpanderRow that contains all the parameters of the command given to it (managed by AmodeEditorWidget)
 *
 * @see ExpanderRow
 *
 */
class AutoCommandRow(val modeIndex: Int) : ExpanderRow() {
    private var command: AmodeCommand? = null
    private val parametersMap = HashMap<String, Supplier<Str?>>()
    private lateinit var info: CommandInfo
    private val dnd: DragSource = DragSource()

    internal enum class ParallelType {
        Parallel, Race, Deadline_Leader, Deadline_Follower, None
    }

    init {
        focusable = true
        focusOnClick = true
        selectable = true
        titleSelectable = true
        addController(dnd)
        dnd.onDragBegin { drag: Drag -> onDragBegin(drag) }
        dnd.onPrepare { v: Double, v1: Double -> onPrepare(v, v1) }
        expanded = false



        onActivate { }

    }

    val self: AutoCommandRow
        get() = this


    /**
     * creates a field/dropdown/switch for each parameter given, assuming that the wpilib directory is properly set,
     * otherwise, everything is an EntryRow except for the trajectoryName parameter
     *
     * @param command information required to create the fields to edit the parameters within the expander row
     */
    fun setParameterFields(command: AmodeCommand) {
        this.command = command
        setName(command.name)
        setTitle(command.name)
        val commands = App.project.commands
        for (commandInfo in commands) {
            if (commandInfo.name == command.name) {
                info = commandInfo
            }
        }



        val parameters = command.parameters
        for (i in info.parameters.indices) {
            var parameter: String? = null
            if (parameters.size != 0) {
                parameter = parameters[i]
            }
            val entryRow = EntryRow()
            if (info.parameters.size == 0) continue
            val paramName = info.parameters[i]!!.removeSurrounding("\"")
            entryRow.setTitle(paramName)

            //if this is the parameters that need the names of the trajectories
            if (App.settings.pluginsEnabled) {
                //gets the pair from the hashmap and checks if the first element (boolean) is true
                // meaning the plugin was enabled, if so, pluginClass = that class, else, null
                val pluginClass = App.plugins.allowedPlugins[entryRow.title.toString()].run {
                    if (this?.first == true) this.second else null
                }
                if (pluginClass != null) {
                    val plugin = pluginClass.getConstructor(String::class.java).newInstance(parameter)
                    addRow(plugin.parameterWidget)
                    if (parameter != null) {
                        parametersMap[paramName] = Supplier<Str?>{ plugin.parameterAsStr!! }

                        continue
                    }
                }
            }

            //if loading classes is turned on, check the type of the current parameter in the constructor,
            //and use a dropdown/switch/entry depending on the type
            if (App.settings.classLoading) {
                //if it's an enum, load the class, iterate through its constants and add them as options to a comboBox
                if (info.parameterClasses[i]!!.isEnum) {
                    val enumBoxText = ComboBoxText()
                    val actionRow = ActionRow()
                    for (enumeration in info.parameterClasses[i]!!.enumConstants) {
                        enumBoxText.appendText(enumeration.toString())
                        if (parameter == null) {
                            enumBoxText.active = (enumeration as Enum<*>).ordinal
                            continue
                        }
                        if (enumeration.toString() == parameter.uppercase()){
                            enumBoxText.active = (enumeration as Enum<*>).ordinal
                        }
                    }
                    actionRow.addSuffix(enumBoxText)
                    actionRow.title = entryRow.title
                    addRow(actionRow)
                    parametersMap[paramName] = Supplier { enumBoxText.activeText }
                    continue

                }
                // use a switch if it's a boolean
                if (info.parameterClasses[i]!!.typeName == "boolean") {
                    val switch = Switch()
                    switch.valign = Align.CENTER
                    switch.active = parameter.toBoolean() == true

                    val actionRow = ActionRow().also {
                        it.activatableWidget = switch
                        it.addSuffix(switch)
                        addRow(it)
                        it.title = entryRow.title
                    }
                    parametersMap[paramName] = Supplier { Str(switch.active.toString()) }
                    continue
                }
                entryRow.asEditable().setText(parameter)
                parametersMap[paramName] = Supplier { entryRow.asEditable().text }
                addRow(entryRow)
            } else {
                entryRow.asEditable().setText(parameter)
                parametersMap[paramName] = Supplier { entryRow.asEditable().text }
                addRow(entryRow)
            }
        }

        //make the comboBox for the parallel type
        val type = ParallelType.valueOf(command.parallelType)
        val comboBoxText = ComboBoxText()
        comboBoxText.appendText("Parallel")
        comboBoxText.appendText("Race")
        comboBoxText.appendText("Deadline_Leader")
        comboBoxText.appendText("Deadline_Follower")
        comboBoxText.appendText("None")
        val parallelRow = ActionRow()
        parallelRow.activatableWidget = comboBoxText
        parallelRow.activatable = true
        parallelRow.addSuffix(comboBoxText)
        parallelRow.setTitle("Parallel Type")
        parallelRow.setIconName("media-playlist-consecutive-symbolic")
        comboBoxText.active = type.ordinal
        comboBoxText.isSensitive = true
        parametersMap["ParallelType"] = Supplier { comboBoxText.activeText }
        addRow(parallelRow)
    }

    /**
     * updates the AmodeCommand describing this row with the edited contents of the row
     */
    val updatedCommandList: AmodeCommand?
        get() {
            val parameters = ArrayList<String>()
            if (info.parameters.size >= 1) {
                for (i in info.parameters.indices) {
                    if (parametersMap[info.parameters[i]?.removeSurrounding("\"")]?.get() != null) {
                        parameters.add(parametersMap[info.parameters[i]?.removeSurrounding("\"")]?.get().toString())
                    }
                }
                command!!.parallelType = parametersMap["ParallelType"]!!.get().toString()
            }
            command!!.setParameters(parameters)
//            println("Set " + command!!.name)
            return command
        }


    /**
     * doesn't really do much yet
     */
    private fun onDragBegin(drag: Drag) {
        val paintable = Paintable(PointerContainer(this.asCPointer()))
        val file = File.newForPath(Str("/home/haydenm/IdeaProjects/JavaAutoBuilder/app/src/main/resources/icons.png"))
        val paint = IconPaintable.newForFileIconPaintable(file, 240, 2)
        dnd.setIcon(paint.asPaintable(), 0, 0)
    }

    private fun onPrepare(v: Double, v1: Double): ContentProvider {
        val file = File.newForPath(Str("src/main/resources/currentCommand.json"))
        return ContentProvider.newTypedContentProvider(File.getTypeID(), file.asCPointer())
    }
}