package java238.widgets;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.ComboRow;
import ch.bailu.gtk.adw.ExpanderRow;
import ch.bailu.gtk.adw.PreferencesRow;
import ch.bailu.gtk.gio.ListModel;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import java238.App;
import java238.background.AmodeCommandList;
import java238.background.CommandInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AutoCommandRow extends ExpanderRow {

    AmodeCommandList command;
    HashMap<String, Supplier<Str>> parametersMap = new HashMap<>();

    public AutoCommandRow() {
        super();
    }

    public void setParameterFields(AmodeCommandList commandList) {
        command = commandList;

        setTitle(commandList.getName());
        var commands = App.project.getCommands();
        CommandInfo info = null;

        for (CommandInfo commandInfo : commands) {
            if (commandInfo.getName().equals(commandList.getName())) {
                info = commandInfo;
            }
        }

        List<String> parameters = commandList.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            String parameter = parameters.get(i);
            ActionRow entryRow = new ActionRow();
            assert info != null;
            entryRow.setTitle(info.getParameters()[i].strip().substring(1, info.getParameters()[i].strip().length() - 1));



            Entry entry = new Entry();
            entry.setPlaceholderText(parameter);
            addRow(entryRow);
            entry.setBuffer(new EntryBuffer(parameter, parameter.length()));
            entryRow.addSuffix(entry);
            entryRow.setActivatableWidget(entry);
            parametersMap.put(info.getParameters()[i].strip().substring(1, info.getParameters()[i].strip().length() - 1), entry.getBuffer()::getText);

        }
        ParallelType type = ParallelType.valueOf(commandList.getParallelType());
        ComboBoxText comboBoxText = new ComboBoxText();
        comboBoxText.appendText("Parallel");
        comboBoxText.appendText("Race");
        comboBoxText.appendText("Deadline_Leader");
        comboBoxText.appendText("Deadline_Follower");
        comboBoxText.appendText("None");


        ActionRow parallelRow = new ActionRow();
        parallelRow.setActivatableWidget(comboBoxText);
        parallelRow.setActivatable(true);
        parallelRow.addSuffix(comboBoxText);


        parallelRow.setTitle("Parallel Type");
        parallelRow.setIconName("media-playlist-consecutive-symbolic");
        comboBoxText.setActive(type.ordinal());
        comboBoxText.setSensitive(true);
        parametersMap.put("ParallelType", comboBoxText::getActiveText);
        addRow(parallelRow);


    }

    public AmodeCommandList getUpdatedCommandList() {
        ArrayList<String> parameters = new ArrayList<>();
        for (String param : parametersMap.keySet()) {
            if (param.equals("ParallelType")) {
                command.setParallelType(parametersMap.get(param).get().toString());
                continue;
            }
            if (parametersMap.get(param).get() != null) {
                parameters.add(parametersMap.get(param).get().toString());
            }
        }

        command.setParameters(parameters);
        System.out.println("Set " + command.getName());
        return command;
    }

    public AmodeCommandList getCommand() {
        return command;
    }

    enum ParallelType {
        Parallel,
        Race,
        Deadline_Leader,
        Deadline_Follower,
        None
    }

}
