package java238.widgets

import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.EntryRow
import ch.bailu.gtk.adw.ExpanderRow
import ch.bailu.gtk.gdk.ContentProvider
import ch.bailu.gtk.gdk.Drag
import ch.bailu.gtk.gdk.Paintable
import ch.bailu.gtk.gio.File
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.lib.bridge.CSS
import ch.bailu.gtk.type.PointerContainer
import ch.bailu.gtk.type.Str
import java238.App
import java238.background.AmodeCommandList
import java238.background.CommandInfo
import java238.background.RobotProject
import java.util.function.Supplier

class AutoCommandRow(val modeIndex: Int) : ExpanderRow() {
    var command: AmodeCommandList? = null
    val parametersMap = HashMap<String, Supplier<Str?>>()
    private var info: CommandInfo? = null
    val dnd: DragSource = DragSource()

    internal enum class ParallelType {
        Parallel, Race, Deadline_Leader, Deadline_Follower, None
    }

    init {
        CSS.addProvider(this, RobotProject.rootDirectory + "/src/main/resources/gtk.css")
        focusable = true
        focusOnClick = true
        selectable = true
        titleSelectable = true
        addController(dnd)
        dnd.onDragBegin { drag: Drag -> onDragBegin(drag) }
        dnd.onPrepare { v: Double, v1: Double -> onPrepare(v, v1) }
        expanded = false
    }

    val self: AutoCommandRow
        get() = this

    fun setParameterFields(commandList: AmodeCommandList) {
        command = commandList
        setName(commandList.name)
        setTitle(commandList.name)
        val commands = App.project.commands
        for (commandInfo in commands) {
            if (commandInfo.name == commandList.name) {
                info = commandInfo
            }
        }
        val parameters = commandList.parameters
        for (i in parameters.indices) {
            val parameter = parameters[i]
            val entryRow = EntryRow()
            assert(info != null)
            if (info!!.parameters.size <= 1) continue
            val paramName = info!!.parameters[i].strip().split("\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            entryRow.setTitle(if (paramName.size == 1) paramName[0] else paramName[1])
            if (!info!!.parameters[i].contains("TrajectoryName")) {
                val entry = Entry()
                entryRow.asEditable().setText(parameter)
                parametersMap[if (paramName.size == 1) paramName[0] else paramName[1]] = Supplier { entryRow.asEditable().text }
                addRow(entryRow)
            } else {
                val actionRow = ActionRow()
                val trajectoryName = ComboBoxText.newWithEntryComboBoxText()
                val trajectories = App.project.trajectories
                if (trajectories != null) {
                    for (j in trajectories.indices) {
                        trajectoryName.appendText(trajectories[j])
                        if (trajectories[j] == parameter) {
                            trajectoryName.setActiveId(trajectories[j])
                            trajectoryName.active = j
                        }
                        parametersMap[if (paramName.size == 1) paramName[0] else paramName[1]] = Supplier { trajectoryName.activeText }
                    }
                }
                actionRow.addSuffix(trajectoryName)
                addRow(actionRow)
            }
        }
        val type = ParallelType.valueOf(commandList.parallelType)
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

    val updatedCommandList: AmodeCommandList?
        get() {
            val parameters = ArrayList<String>()
            if (info!!.parameters.size > 1) {
                for (i in info!!.parameters.indices) {
                    if (parametersMap[info!!.parameters[i].strip().split("\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]]!!.get() != null) {
                        parameters.add(parametersMap[info!!.parameters[i].strip().split("\"".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]]!!.get().toString())
                    }
                }
                command!!.parallelType = parametersMap["ParallelType"]!!.get().toString()
            }
            command!!.setParameters(parameters)
            println("Set " + command!!.name)
            return command
        }

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