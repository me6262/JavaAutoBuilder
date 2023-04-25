package java238.widgets

import ch.bailu.gtk.adw.*
import ch.bailu.gtk.gtk.*
import java238.App
import java238.background.Amode

class AutoPicker {
    @JvmField
    var flap: Flap = Flap()
    var sidebar: StackSidebar? = StackSidebar()

    @JvmField
    var stack: AmodeStack
    @JvmField
    var commandSidebar: CommandSidebar = CommandSidebar()

    init {
        sidebar?.addCssClass("background")
        val vbox = Box(Orientation.VERTICAL, 0)
        val bin = Bin()
        stack = AmodeStack()
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
            swipeToOpen
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