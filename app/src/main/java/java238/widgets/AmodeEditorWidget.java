package java238.widgets;

import ch.bailu.gtk.adw.PreferencesGroup;
import ch.bailu.gtk.gtk.*;
import java238.background.AmodeCommandList;
import java238.background.AmodeList;

public class AmodeEditorWidget extends ScrolledWindow {

    PreferencesGroup amodeCommands;
    private String modeName;
    private String modeTitle;


    public String getModeName() {
        return modeName;
    }
    public AmodeEditorWidget(AmodeList commandList) {
        super();
        setAuto(commandList);
        modeName = commandList.getName();
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
            AutoCommandRow row = new AutoCommandRow();
            row.setParameterFields(command);
            amodeCommands.add(row);
        }
        setChild(amodeCommands);

    }
}
