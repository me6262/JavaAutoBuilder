package java238.widgets;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.Clamp;
import ch.bailu.gtk.adw.Flap;
import ch.bailu.gtk.adw.FlapFoldPolicy;
import ch.bailu.gtk.adw.FoldThresholdPolicy;
import ch.bailu.gtk.adw.PreferencesGroup;
import ch.bailu.gtk.gtk.*;
import java238.App;
import java238.background.CommandInfo;

import java.util.List;

public class CommandSidebar extends Flap {
    List<CommandInfo> commands;
    ListBox commandsGroup;
    ScrolledWindow scrolledWindow;

    public CommandSidebar() {
        commands = App.project.getCommands();
        scrolledWindow = new ScrolledWindow();
        commandsGroup = new ListBox();
        scrolledWindow.setChild(commandsGroup);
        scrolledWindow.setHexpand(false);
        scrolledWindow.getVscrollbar().hide();
        scrolledWindow.setPropagateNaturalWidth(true);
        scrolledWindow.setMinContentWidth(250);
        commandsGroup.addCssClass("navigation-sidebar");
        setFoldPolicy(FlapFoldPolicy.AUTO);
        setFoldThresholdPolicy(FoldThresholdPolicy.NATURAL);
        setFlap(scrolledWindow);
        setFlapPosition(PositionType.RIGHT);
        setRevealFlap(false);
    }

    public void generateList() {
        for (CommandInfo info : commands) {
            ActionRow row = new ActionRow();
            row.setTitle(info.name);
            row.setActivatable(true);
            row.addCssClass("raised");
            Button addButton = Button.newFromIconNameButton("list-add-symbolic");
            addButton.setValign(Align.CENTER);
//            addButton.addCssClass("flat");
            addButton.addCssClass("circular");
            row.addPrefix(addButton);
            addButton.onClicked(row::activate);
            row.onActivate(() -> App.picker.stack.addCommandToVisibleChild(info));
            commandsGroup.append(row);
        }
    }

    public void onToggleClicked() {
        setRevealFlap(!getRevealFlap());
    }


}
