package java238.widgets;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.PreferencesGroup;
import java238.background.AmodeCommandList;
import java238.background.Amode;
import java238.background.ModeListInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class AmodeEditorWidget extends Bin {

    public PreferencesGroup amodeCommands;
    HashMap<String, Supplier<AmodeCommandList>> amodeCommandListMap = new HashMap<>();
    private String modeName;
    private Amode mode;


    public String getModeName() {
        return modeName;
    }
    public AmodeEditorWidget(Amode commandList) {
        setAuto(commandList);
        modeName = commandList.getName();
    }

    public Amode getUpdatedMode() {
        ArrayList<AmodeCommandList> modeList = new ArrayList<>();
        for (String key : amodeCommandListMap.keySet()) {
            modeList.add(amodeCommandListMap.get(key).get());
        }
        mode.setCommands(modeList);
        return mode;
    }

    public void setAuto(Amode commandList) {
        mode = commandList;


        amodeCommands = new PreferencesGroup();
        amodeCommands.setMarginTop(20);
        amodeCommands.setMarginEnd(20);
        amodeCommands.setMarginStart(20);
        amodeCommands.setMarginBottom(20);
        amodeCommands.setTitle(commandList.getName());
        amodeCommands.setDescription("Edit This Auto");

        for (AmodeCommandList command: commandList.getCommands()) {
            AutoCommandRow row = new AutoCommandRow();
            amodeCommandListMap.put(command.getName(), row::getUpdatedCommandList);
            row.setParameterFields(command);
            amodeCommands.add(row);
        }
        setChild(amodeCommands);

    }
    public ModeListInterface getMode() {
        return (ModeListInterface) mode;
    }
}
