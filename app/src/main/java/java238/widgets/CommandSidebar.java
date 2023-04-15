package java238.widgets;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.Clamp;
import ch.bailu.gtk.adw.Flap;
import ch.bailu.gtk.adw.FlapFoldPolicy;
import ch.bailu.gtk.adw.FoldThresholdPolicy;
import ch.bailu.gtk.adw.PreferencesGroup;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.PositionType;
import ch.bailu.gtk.gtk.ScrolledWindow;
import java238.App;
import java238.background.CommandInfo;

import java.util.List;

public class CommandSidebar extends Flap {
    List<CommandInfo> commands;
    PreferencesGroup commandsGroup;
    ScrolledWindow scrolledWindow;
    Clamp clamp;

    public CommandSidebar() {
        commands = App.project.getCommands();
        scrolledWindow = new ScrolledWindow();
        commandsGroup = new PreferencesGroup();
        clamp = new Clamp();
        clamp.setChild(commandsGroup);
        scrolledWindow.setChild(clamp);
        clamp.setMaximumSize(280);
        setFoldPolicy(FlapFoldPolicy.AUTO);
        setFoldThresholdPolicy(FoldThresholdPolicy.NATURAL);
        setFlap(scrolledWindow);
        setFlapPosition(PositionType.RIGHT);
        setRevealFlap(false);
    }

    public void generateList() {
        for (CommandInfo info : commands) {
            ActionRow row = new ActionRow();
            row.setTitle(info.getName());
            row.setActivatable(true);
            Button addButton = Button.newFromIconNameButton("list-add-symbolic");
            row.addPrefix(addButton);
            addButton.onClicked(row::activate);
            row.onActivate(() -> App.picker.stack.addCommandToVisibleChild(info));
            commandsGroup.add(row);
            commandsGroup.setTitle("Commands");
            commandsGroup.setDescription("All of the commands in the robot project");
        }
    }

    public void onToggleClicked() {
        setRevealFlap(!getRevealFlap());
    }


}
