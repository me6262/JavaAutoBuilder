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
import ch.bailu.gtk.gtk.ListBoxRow
import ch.bailu.gtk.gtk.Widget
import frc238.App
import frc238.background.Amode
import frc238.background.AmodeCommand
import frc238.background.CommandInfo
import frc238.widgets.AutoCommandRow
import java.util.function.Supplier
import frc238.widgets.TypedListBox

class AmodeEditorWidget(commandList: Amode) : ScrolledWindow() {
    private var focusedCommandName: String? = null
    private var amodeCommands: TypedListBox<AutoCommandRow> = TypedListBox<AutoCommandRow>()

    val modeName: String
    private lateinit var mode: Amode
    private var target: DropTarget

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
    }

    val updatedMode: Amode
        get() {
            var commands = ArrayList<AmodeCommand>()
            println("updated mode")
            println(amodeCommands.size())
            for (i in amodeCommands) {
                println("stuff")
                commands += i.updatedCommandList!!
                println(i.updatedCommandList)

            }
            mode.commands = commands
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
            val row = AutoCommandRow()
            val delete = Button.newFromIconNameButton("user-trash-symbolic")

            delete.onClicked { removeCommand(row) }
            delete.valign = Align.CENTER
            delete.addCssClass("flat")
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
        val row = AutoCommandRow()
        row.setParameterFields(command)
        val delete = Button.newFromIconNameButton("user-trash-symbolic")
        delete.valign = Align.CENTER
        delete.addCssClass("flat")
        row.addPrefix(delete)
        delete.onClicked { removeCommand(row) }
        amodeCommands += row
        mode.commands = newMode
    }

    private fun removeCommand(row: AutoCommandRow) {
        mode.commands.removeAt(row.index)
        amodeCommands.remove(row)
    }

    /**
     * grabs the currently selected row, copies it, deletes the old one, and inserts the new row above
     */
    fun moveRowUp() {
        val row = amodeCommands.selectedRowTyped;
        val index: Int = row.index
        if (index == 0 || index == -1) return
        row.ref()
        amodeCommands -= row
        if (index <= 0) {


            println("adding row")
            amodeCommands += row
            println("added row")
        } else {
            amodeCommands[index - 1] = row
            println("set row at index")
        }
        row.unref()
        amodeCommands.selectRow(row)
        println(amodeCommands.size())
    }

    fun moveRowDown() {
        println("moving row down")
        val row: AutoCommandRow = amodeCommands.selectedRowTyped;
        println("got row")
        val index: Int = row.index
        println("got index")
        row.ref()
        println("refed row")
        amodeCommands -= row
        println("removed row")
        if (index >= amodeCommands.size()) {
                
                println("adding row")
                amodeCommands += row
                println("added row")
        } else {
            amodeCommands[index + 1] = row
            println("set row at index")
        }
        row.unref()
        println("selected row")
        amodeCommands.selectRow(row)
        println(amodeCommands.size())
    }

    private fun onDrop(value: Value, v: Double, v1: Double): Boolean {
        println("YAHOO")
        return false
    }
}
