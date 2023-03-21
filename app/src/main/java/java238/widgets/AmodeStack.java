package java238.widgets;

import ch.bailu.gtk.adw.StatusPage;
import ch.bailu.gtk.gtk.Stack;
import java238.background.Amode;
import java238.background.AmodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AmodeStack extends Stack {

    AmodeList amodeList;
    StatusPage noneSelected;
    HashMap<String, Supplier<Amode>> titles = new HashMap<>();
    public AmodeStack() {
        super();
        noneSelected = new StatusPage();
        noneSelected.setIconName("system-search-symbolic");
        noneSelected.setTitle("No Auto Selected");
        noneSelected.setDescription("Please pick an auto or load a robot project");
        addNamed(noneSelected, "none");
        setVisibleChildName("none");
    }

    public AmodeList getAmodeList() {
        return amodeList;
    }

    public void setAmodeList(AmodeList list) {
        amodeList = list;
    }

    public AmodeList getUpdatedAmodesList() {
        ArrayList<Amode> modesList = new ArrayList<>(titles.size());
        for (String key : titles.keySet()) {
            modesList.add(titles.get(key).get());
        }




        amodeList.setAutonomousModes(modesList);
        return amodeList;
    }

    public void renameMode(String oldName, String newName) {

    }

    public void addTitled(AmodeEditorWidget widget, String name) {
        super.addTitled(widget, name, name);
        titles.put(name, widget::getUpdatedMode);
    }
}
