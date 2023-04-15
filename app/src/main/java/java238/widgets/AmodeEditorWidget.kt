package java238.widgets

import ch.bailu.gtk.adw.Bin
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.gdk.DragAction
import ch.bailu.gtk.gio.File
import ch.bailu.gtk.gobject.Value
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.DropTarget
import ch.bailu.gtk.gtk.ListBox
import java238.background.Amode
import java238.background.AmodeCommandList
import java238.background.CommandInfo
import java.util.function.Supplier

class AmodeEditorWidget(commandList: Amode) : Bin() {
    private var focusedCommandName: String? = null
    var amodeCommands: ListBox? = null
    private var settings: PreferencesGroup? = null
    private var amodeCommandListMap = ArrayList<Supplier<AutoCommandRow>>()
    @JvmField
    val modeName: String
    private var mode: Amode? = null
    var target: DropTarget
    val self: AmodeEditorWidget
        get() = this

    init {
        setAuto(commandList)
        modeName = commandList.name
        setName(modeName)
        target = DropTarget(File.getTypeID(), DragAction.MOVE)
        target.onDrop { value: Value, v: Double, v1: Double -> onDrop(value, v, v1) }
        addController(target)
    }

    val updatedMode: Amode?
        get() {
            val modeList = ArrayList<AmodeCommandList>()
            for (amodeCommandListSupplier in amodeCommandListMap) {
                modeList.add(amodeCommandListSupplier.get().updatedCommandList)
            }
            mode!!.commands = modeList
            return mode
        }

    fun setAuto(commandList: Amode) {
        focusOnClick = true
        mode = commandList
        settings = PreferencesGroup()
        amodeCommands = ListBox()
        amodeCommands!!.onMoveFocus { handler: Int ->
            println(focusChild.name.toString())
            focusedCommandName = focusChild.name.toString()
        }
        amodeCommands!!.marginTop = 20
        amodeCommands!!.marginEnd = 20
        amodeCommands!!.marginStart = 20
        amodeCommands!!.marginBottom = 20
//        amodeCommands!!.setTi(commandList.name)
//        amodeCommands!!.setDescription("Edit This Auto")
        for (i in commandList.commands.indices) {
            val row = AutoCommandRow(i)
            val delete = Button.newFromIconNameButton("user-trash-symbolic")
            delete.onClicked { removeCommand(row) }
            amodeCommandListMap.add(Supplier { row.self })
            row.addPrefix(delete)
            row.setParameterFields(commandList.commands[i])
            amodeCommands!!.append(row)
        }
        child = amodeCommands
    }

    fun addCommand(info: CommandInfo) {
        val command = AmodeCommandList()
        command.parallelType = "None"
        command.setParameters(info.parameters)
        command.name = info.name
        val newMode = mode!!.commands
        newMode.add(command)
        val row = AutoCommandRow(amodeCommandListMap.size)
        amodeCommandListMap.add(Supplier { row.self })
        row.setParameterFields(command)
        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        row.addPrefix(delete)
        delete.onClicked { removeCommand(row) }
        amodeCommands!!.append(row)
        mode!!.commands = newMode
    }

    fun removeCommand(row: AutoCommandRow) {
        val newCommands = mode!!.commands
        newCommands.removeAt(row.modeIndex)
        amodeCommandListMap.removeAt(row.modeIndex)
        row.hide()
    }

    fun moveRowUp() {
        var row: AutoCommandRow
        //        System.out.println(name);
        val name = amodeCommands!!.focusChild.name.toString()
        for (autoCommandRowSupplier in amodeCommandListMap) {
            if (autoCommandRowSupplier.get().name.toString() != name) continue
            println("oh yeah")
            row = autoCommandRowSupplier.get()
            val rowUp = row.prevSibling
            amodeCommands!!.remove(row)
            row.insertBefore(amodeCommands!!, rowUp)
            amodeCommandListMap.removeAt(row.modeIndex)
            amodeCommandListMap.add(row.modeIndex - 1, Supplier { row.self })
        }
    }

    private fun onDrop(value: Value, v: Double, v1: Double): Boolean {
        println("YAHOO")
        return false
    }
}
