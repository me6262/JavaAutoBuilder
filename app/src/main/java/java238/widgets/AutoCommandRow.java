package java238.widgets;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.gdk.ContentProvider;
import ch.bailu.gtk.gdk.Drag;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.Paintable;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.*;
import ch.bailu.gtk.gobject.TypeFlags;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.Type;
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
    CommandInfo info;
    private int modeIndex;
    DragSource dnd;
    DropTarget target;


    enum ParallelType {
        Parallel,
        Race,
        Deadline_Leader,
        Deadline_Follower,
        None
    }


    public AutoCommandRow(int i) {
        super();
        modeIndex = i;
        setFocusable(true);
        setFocusOnClick(true);
        dnd = new DragSource();
        addController(dnd);
        dnd.onDragBegin(this::onDragBegin);
        dnd.onPrepare(this::onPrepare);
    }




    public AutoCommandRow getSelf() {
        return this;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void setParameterFields(AmodeCommandList commandList) {
        command = commandList;

        setName(commandList.getName());
        setTitle(commandList.getName());
        var commands = App.project.getCommands();

        for (CommandInfo commandInfo : commands) {
            if (commandInfo.getName().equals(commandList.getName())) {
                info = commandInfo;
            }
        }

        List<String> parameters = commandList.getParameters();
        for (int i = 0; i < parameters.size(); i++) {

            String parameter = parameters.get(i);
            EntryRow entryRow = new EntryRow();

            assert info != null;
            if (info.getParameters().size() <= 1) continue;

            String[] paramName = info.getParameters().get(i).strip().split("\"");
            entryRow.setTitle(paramName.length == 1 ? paramName[0] : paramName[1]);

            if (!info.getParameters().get(i).contains("TrajectoryName")) {
                Entry entry = new Entry();
                entryRow.asEditable().setText(parameter);
                parametersMap.put(paramName.length == 1 ? paramName[0] : paramName[1], entryRow.asEditable()::getText);
                addRow(entryRow);

            } else {
                var actionRow = new ActionRow();
                ComboBoxText trajectoryName = ComboBoxText.newWithEntryComboBoxText();
                var trajectories = App.project.getTrajectories();

                for (int j = 0; j < trajectories.size(); j++) {
                    trajectoryName.appendText(trajectories.get(j));

                    if (trajectories.get(j).equals(parameter)) {
                        trajectoryName.setActiveId(trajectories.get(j));
                        trajectoryName.setActive(j);
                    }

                    parametersMap.put(paramName.length == 1 ?
                                paramName[0] : paramName[1],
                                trajectoryName::getActiveText);
                }
                actionRow.addSuffix(trajectoryName);

                addRow(actionRow);
            }
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
        if (info.getParameters().size() > 1) {
            for (int i = 0; i < info.getParameters().size(); i++) {
                if (parametersMap.get(info.getParameters().get(i).strip().split("\"")[1]).get() != null) {
                    parameters.add(parametersMap.get(info.getParameters().get(i).strip().split("\"")[1]).get().toString());
                }
            }

            command.setParallelType(parametersMap.get("ParallelType").get().toString());
        }

        command.setParameters(parameters);
        System.out.println("Set " + command.getName());
        return command;
    }


    public AmodeCommandList getCommand() {
        return command;
    }

    private void onDragBegin(Drag drag) {
        Paintable paintable = new Paintable(new PointerContainer(this.asCPointer()));
        File file = File.newForPath(new Str("/home/haydenm/IdeaProjects/JavaAutoBuilder/app/src/main/resources/icons.png"));
        IconPaintable paint = IconPaintable.newForFileIconPaintable(file, 240, 2);
        dnd.setIcon(paint.asPaintable(), 0, 0);
    }

    private ContentProvider onPrepare(double v, double v1) {
        File file = File.newForPath(new Str("src/main/resources/currentCommand.json"));

        return ContentProvider.newTypedContentProvider(File.getTypeID(), file.asCPointer());
    }

}
