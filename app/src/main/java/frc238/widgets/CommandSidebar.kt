package frc238.widgets

import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.Flap
import ch.bailu.gtk.adw.FlapFoldPolicy
import ch.bailu.gtk.adw.FoldThresholdPolicy
import ch.bailu.gtk.gtk.*
import frc238.App
import frc238.background.CommandInfo
import frc238.background.RobotProject

class CommandSidebar() : Flap() {
    private var commandsGroup: ListBox = ListBox()
    private var scrolledWindow: ScrolledWindow = ScrolledWindow()

    private lateinit var commands: List<CommandInfo>

    init {
        scrolledWindow.apply {
            child = commandsGroup
            hexpand = false
            vscrollbar.hide()
            propagateNaturalWidth = true
            minContentWidth = 250
        }
        commandsGroup.addCssClass("navigation-sidebar")
        foldPolicy = FlapFoldPolicy.AUTO
        foldThresholdPolicy = FoldThresholdPolicy.NATURAL
        flap = scrolledWindow
        flapPosition = PositionType.RIGHT
        revealFlap = false
    }

    fun generateList() {
        commands = App.project.commands
        for (info in commands) {
            val row = ActionRow()
            row.setTitle(info.name)
            row.activatable = true
            row.addCssClass("raised")
            val addButton = Button.newFromIconNameButton("list-add-symbolic")
            addButton.valign = Align.CENTER
            addButton.addCssClass("circular")
            row.addPrefix(addButton)
            addButton.onClicked { row.activate() }
            row.onActivate { App.picker.stack.addCommandToVisibleChild(info) }
            commandsGroup.append(row)
            revealFlap = true
        }
    }

    fun onToggleClicked() {
        revealFlap = !revealFlap
    }
}
