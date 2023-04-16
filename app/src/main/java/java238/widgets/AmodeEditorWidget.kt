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
    private var amodeCommands: ListBox = ListBox()
    private var amodeCommandListMap = ArrayList<Supplier<AutoCommandRow>>()

    @JvmField
    val modeName: String
    private lateinit var mode: Amode
    private var target: DropTarget
    val self: AmodeEditorWidget
        get() = this

    init {
        asAccessible()
        setAuto(commandList)
        modeName = commandList.name
        setName(modeName)
        target = DropTarget(File.getTypeID(), DragAction.MOVE)
        target.onDrop { value: Value, v: Double, v1: Double -> onDrop(value, v, v1) }
        addController(target)
    }

    val updatedMode: Amode
        get() {
            val modeList = ArrayList<AmodeCommandList>()
            for (amodeCommandListSupplier in amodeCommandListMap) {
                amodeCommandListSupplier.get().updatedCommandList?.let { modeList.add(it) }
            }
            mode.commands = modeList
            return mode
        }

    fun setAuto(commandList: Amode) {
        focusOnClick = true
        mode = commandList
        amodeCommands.onMoveFocus { handler: Int ->
            println(focusChild.name.toString())
            focusedCommandName = focusChild.name.toString()
        }
        amodeCommands.marginTop = 20
        amodeCommands.marginEnd = 20
        amodeCommands.marginStart = 20
        amodeCommands.marginBottom = 20
        for (i in commandList.commands.indices) {
            val row = AutoCommandRow(i)
            val delete = Button.newFromIconNameButton("user-trash-symbolic")
            delete.onClicked { removeCommand(row) }
            amodeCommandListMap.add(Supplier { row.self })
            row.addPrefix(delete)
            row.setParameterFields(commandList.commands[i])
            amodeCommands.append(row)
        }
        child = amodeCommands
    }

    fun addCommand(info: CommandInfo) {
        val command = AmodeCommandList()
        command.parallelType = "None"
        command.setParameters(info.parameters)
        command.name = info.name
        val newMode = mode.commands
        newMode.add(command)
        val row = AutoCommandRow(amodeCommandListMap.size)
        amodeCommandListMap.add(Supplier { row.self })
        row.setParameterFields(command)
        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        row.addPrefix(delete)
        delete.onClicked { removeCommand(row) }
        amodeCommands.append(row)
        mode.commands = newMode
    }

    fun removeCommand(row: AutoCommandRow) {
        val newCommands = mode.commands
        newCommands.removeAt(row.modeIndex)
        amodeCommandListMap.removeAt(row.modeIndex)
        row.hide()
    }

    /**
     * grabs the currently selected row, copies it, deletes the old one, and inserts the new row above
     */
    fun moveRowUp() {
        val row = amodeCommands.selectedRow;
        val index: Int = row.index
        if (index == 0) return
        val commlist = amodeCommandListMap[index].get().updatedCommandList
        val newRow = AutoCommandRow(index - 1)
        if (commlist != null) {
            newRow.setParameterFields(commlist)
        }
        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        newRow.addPrefix(delete)
        delete.onClicked { removeCommand(newRow) }
        amodeCommands.remove(amodeCommands.getRowAtIndex(index))
        amodeCommands.insert(newRow, index - 1)
        amodeCommandListMap.removeAt(index)
        amodeCommandListMap.add(index - 1, newRow::self)
        amodeCommands.activateOnSingleClick = true

        println("aaaaaaaaaa")

    }

    private fun onDrop(value: Value, v: Double, v1: Double): Boolean {
        println("YAHOO")
        return false
    }
}
