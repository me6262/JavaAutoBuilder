package java238.widgets;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.PreferencesGroup;
import ch.bailu.gtk.adw.StatusPage;
import ch.bailu.gtk.gtk.*;
import java238.background.AmodeCommandList;
import java238.background.AmodeList;

public class AmodeCommandsWidget extends Bin {

    PreferencesGroup amodeCommands;
    private StatusPage noAuto = new StatusPage();
    public AmodeCommandsWidget() {
        super();
        noAuto.setTitle("No Auto Selected Yet");
        noAuto.setDescription("You can open one from the left sidebar");
        noAuto.setIconName("system-help-symbolic");
        setNoneSelected();
    }

    public Widget getWidget() {
        return this;
    }

    public void setNoneSelected() {
        setChild(noAuto);
    }

    public void setAuto(AmodeList commandList) {

        amodeCommands = new PreferencesGroup();
        amodeCommands.setMarginTop(20);
        amodeCommands.setMarginEnd(20);
        amodeCommands.setMarginStart(20);
        amodeCommands.setMarginBottom(20);
        amodeCommands.setTitle(commandList.getName());
        amodeCommands.setDescription("Edit This Auto");

        for (AmodeCommandList command: commandList.getCommands()) {
            ListBoxRow row = new ListBoxRow();
            var rowButton = new Button();
            rowButton.setLabel(command.getName());
            row.setChild(rowButton);
            amodeCommands.add(row);

        }
        setChild(amodeCommands);


    }
}
