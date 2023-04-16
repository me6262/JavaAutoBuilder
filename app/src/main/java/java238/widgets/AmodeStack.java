package java238.widgets;

import ch.bailu.gtk.adw.StatusPage;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.gio.SimpleActionGroup;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Stack;
import java238.App;
import java238.background.Amode;
import java238.background.AmodeList;
import java238.background.CommandInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class AmodeStack extends Stack {

    AmodeList amodeList;
    StatusPage noneSelected;
    Map<String, Supplier<AmodeEditorWidget>> editorWidgetSupplier = new TreeMap<>();
    public AmodeStack() {
        super();

        noneSelected = new StatusPage();
        SimpleAction action = new SimpleAction("move-up", null);
        action.onActivate((stuff) -> {
            System.out.println("asjdasdsda");
        });
        setName("amodestack");
        SimpleActionGroup group = new SimpleActionGroup();
        insertActionGroup("modestack", group.asActionGroup());
        group.asActionMap().addAction(action.asAction());
        noneSelected.setIconName("system-search-symbolic");
        noneSelected.setTitle("No Auto Selected");
        noneSelected.setDescription("Please pick an auto or load a robot project");
        addNamed(noneSelected, "none");
        setVisibleChildName("none");
        var up = Button.newFromIconNameButton("go-up-symbolic");
        App.header.packEnd(up);
        up.onClicked(this::moveFocusedCommandUp);

    }

    public AmodeList getAmodeList() {
        return amodeList;
    }

    public void setAmodeList(AmodeList list) {
        amodeList = list;
    }

    public AmodeList getUpdatedAmodesList() {
        ArrayList<Amode> modesList = new ArrayList<>(editorWidgetSupplier.size());
        for (String key : editorWidgetSupplier.keySet()) {
            modesList.add(editorWidgetSupplier.get(key).get().getUpdatedMode());
        }



        amodeList.setAutonomousModes(modesList);
        return amodeList;
    }

    public void renameMode(String oldName, String newName) {

    }

    public void removeAmode() {
        editorWidgetSupplier.remove(getVisibleChildName().toString());
        remove(getVisibleChild());
        setVisibleChildName("none");
    }

    public void addCommandToVisibleChild(CommandInfo info) {
        System.out.println(getVisibleChild().getName().toString());
        editorWidgetSupplier.get(getVisibleChild().getName().toString()).get().addCommand(info);
    }

    public void addTitled(AmodeEditorWidget widget, String name) {
        super.addTitled(widget, name, name);
        editorWidgetSupplier.put(name, widget::getSelf);
    }

    public void addAmodeToList() {
        Amode newMode = new Amode();
        newMode.setName("New Auto");
        AmodeEditorWidget editor = new AmodeEditorWidget(newMode);
        addTitled(editor, "New Auto");
    }

    public void moveFocusedCommandUp() {
        editorWidgetSupplier.get(getVisibleChild().getName().toString()).get().moveRowUp();
        System.out.println("WOAH");
    }
}
