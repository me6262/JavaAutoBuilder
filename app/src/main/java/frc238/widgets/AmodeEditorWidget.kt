package frc238.widgets

import ch.bailu.gtk.gdk.DragAction
import ch.bailu.gtk.gio.File
import ch.bailu.gtk.gobject.Value
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.DropTarget
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.SelectionMode
import frc238.App
import frc238.background.Amode
import frc238.background.AmodeCommand
import frc238.background.CommandInfo
import java.util.function.Supplier

class AmodeEditorWidget(commandList: Amode) : ScrolledWindow() {
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
        setAuto(commandList)
        modeName = commandList.name
        setName(modeName)
        target = DropTarget(File.getTypeID(), DragAction.MOVE)
        target.onDrop { value: Value, v: Double, v1: Double -> onDrop(value, v, v1) }
        addController(target)
        hexpand = true
        amodeCommands.addCssClass("boxed-list")
        amodeCommands.selectionMode = SelectionMode.SINGLE
//        vscrollbar.hide()
    }

    val updatedMode: Amode
        get() {
            val modeList = ArrayList<AmodeCommand>()
            for (amodeCommandListSupplier in amodeCommandListMap) {
                amodeCommandListSupplier.get().updatedCommandList?.let { modeList.add(it) }
            }
            mode.commands = modeList
            println("updated $name")
            mode.name = name.toString()
            return mode
        }

    private fun setAuto(commandList: Amode) {
        focusOnClick = true
        mode = commandList
        amodeCommands.onMoveFocus {
            println(focusChild.name.toString())
            focusedCommandName = focusChild.name.toString()
        }
        onMoveFocus {
            if (isFocus) {
                App.title.subtitle = name
            }
        }
        amodeCommands.marginTop = 20
        amodeCommands.marginEnd = 20
        amodeCommands.marginStart = 20
        amodeCommands.marginBottom = 20
        for (i in commandList.commands.indices) {
            val row = AutoCommandRow(i)
            val delete = Button.newFromIconNameButton("user-trash-symbolic")

            delete.onClicked { removeCommand(row) }
            delete.valign = Align.CENTER
            delete.addCssClass("flat")
            amodeCommandListMap.add(Supplier { row.self })
            row.addPrefix(delete)
            row.setParameterFields(commandList.commands[i])
            amodeCommands.append(row)
        }
        child = amodeCommands
    }

    fun addCommand(info: CommandInfo) {
        val command = AmodeCommand()
        command.parallelType = "None"

        command.setParameters(info.parameters)
        command.name = info.name
        val newMode = mode.commands
        newMode.add(command)
        val row = AutoCommandRow(amodeCommandListMap.size)
        amodeCommandListMap.add(Supplier { row.self })
        row.setParameterFields(command)
        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        delete.valign = Align.CENTER
        delete.addCssClass("flat")
        row.addPrefix(delete)
        delete.onClicked { removeCommand(row) }
        amodeCommands.append(row)
        mode.commands = newMode
    }

    private fun removeCommand(row: AutoCommandRow) {
        val newCommands = mode.commands
        newCommands.removeAt(row.index)
        amodeCommandListMap.removeAt(row.index)
        mode.commands = newCommands
        amodeCommands.remove(row)
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
        delete.valign = Align.CENTER
        delete.addCssClass("flat")
        amodeCommands.remove(amodeCommands.getRowAtIndex(index))
        amodeCommands.insert(newRow, index - 1)
        amodeCommandListMap.removeAt(index)
        for (command in amodeCommandListMap) {
            if (command.get().visibleIndex < index) continue
            val cmd = command.get()
            cmd.visibleIndex -= 1
        }
        amodeCommandListMap.add(index - 1, newRow::self)
        amodeCommands.activateOnSingleClick = true
        amodeCommands.selectRow(newRow)
    }

    fun moveRowDown() {
        val row = amodeCommands.selectedRow;
        val index: Int = row.index

        if (index >= amodeCommandListMap.size - 1) return

        val commlist = amodeCommandListMap[index].get().updatedCommandList
        val newRow = AutoCommandRow(index + 1)
        if (commlist != null) {
            newRow.setParameterFields(commlist)
        }

        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        newRow.addPrefix(delete)
        delete.onClicked { removeCommand(newRow) }
        delete.valign = Align.CENTER
        delete.addCssClass("flat")
        amodeCommands.remove(amodeCommands.getRowAtIndex(index))
        amodeCommands.insert(newRow, index + 1)
        amodeCommandListMap.removeAt(index)
        for (command in amodeCommandListMap) {
            if (command.get().visibleIndex < index) continue
            val cmd = command.get()
            cmd.visibleIndex -= 1
        }
        amodeCommandListMap.add(index + 1, newRow::self)
        amodeCommands.activateOnSingleClick = true
        amodeCommands.selectRow(newRow)


    }

    private fun onDrop(value: Value, v: Double, v1: Double): Boolean {
        println("YAHOO")
        return false
    }
}
