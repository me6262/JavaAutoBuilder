package frc238.widgets

import ch.bailu.gtk.adw.*
import ch.bailu.gtk.gtk.*
import frc238.App
import frc238.background.Amode
import frc238.background.CommandInfo

class AutoPicker {
    var flap: Flap = Flap()
    private var sidebar: StackSidebar? = StackSidebar()

    var stack: AmodeStack

    lateinit var commandSidebar: CommandSidebar

    init {
        sidebar?.addCssClass("background")
        val vbox = Box(Orientation.VERTICAL, 0)
        val bin = Bin()
        stack = AmodeStack()
        commandSidebar = CommandSidebar()
        flap.run {
            foldThresholdPolicy = FoldThresholdPolicy.NATURAL
            foldPolicy = FlapFoldPolicy.AUTO
            flapPosition = PositionType.LEFT
            locked = true
            modal = true
            flap = sidebar
            hexpand = true
            vexpand = true
            content = commandSidebar
            swipeToClose = true
            swipeToOpen = true
        }
        sidebar!!.stack = stack
        commandSidebar.content = stack
        bin.child = vbox
    }

    fun setModes(list: List<Amode?>) {
        for (modeList in list) {
            setMode(modeList)
        }
        flap.revealFlap = true
        stack.setVisibleChildName(list.first()!!.name)
        list
    }

    fun setMode(list: Amode?) {
        val widget = AmodeEditorWidget(list!!)
        stack.addTitled(widget, widget.modeName)
    }

    fun onToggleClicked() {
        flap.revealFlap = !flap.revealFlap
        App.window.present()
    }

}
