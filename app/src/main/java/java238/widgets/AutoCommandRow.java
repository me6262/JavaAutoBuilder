package java238.widgets;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.ComboRow;
import ch.bailu.gtk.adw.ExpanderRow;
import ch.bailu.gtk.adw.PreferencesRow;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import java238.background.AmodeCommandList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoCommandRow extends ExpanderRow {
    public AutoCommandRow() {
        super();
    }

    public void setParameterFields(AmodeCommandList commandList) {
        setTitle(commandList.getName());
        for (String parameter : commandList.getParameters()) {
            ActionRow entryRow = new ActionRow();

            Entry entry = new Entry();
            entry.setPlaceholderText(parameter);
            addRow(entryRow);
            entry.setBuffer(new EntryBuffer(parameter, parameter.length()));
            entryRow.addSuffix(entry);
        }
        ParallelType type = ParallelType.valueOf(commandList.getParallelType());
        ComboBoxText comboBoxText = new ComboBoxText();
        comboBoxText.appendText("Parallel");
        comboBoxText.appendText("Race");
        comboBoxText.appendText("Deadline_Leader");
        comboBoxText.appendText("Deadline_Follower");
        comboBoxText.appendText("None");

        ActionRow parallelRow = new ActionRow();
        parallelRow.setActivatable(true);
        parallelRow.addSuffix(comboBoxText);


        parallelRow.setTitle("Parallel Type");
        parallelRow.setIconName("media-playlist-consecutive-symbolic");
        comboBoxText.setActive(type.ordinal());
        comboBoxText.setSensitive(true);
        addRow(parallelRow);


    }

    public void getParameters() {

    }
    enum ParallelType {
        Parallel,
        Race,
        Deadline_Leader,
        Deadline_Follower,
        None
    }

}
