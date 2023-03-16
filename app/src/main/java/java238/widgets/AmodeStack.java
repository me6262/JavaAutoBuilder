package java238.widgets;

import ch.bailu.gtk.adw.StatusPage;
import ch.bailu.gtk.gtk.Stack;

public class AmodeStack extends Stack {

    StatusPage noneSelected;
    public AmodeStack() {
        super();
        noneSelected = new StatusPage();
        noneSelected.setIconName("system-search-symbolic");
        noneSelected.setTitle("No Auto Selected");
        noneSelected.setDescription("Please pick an auto or load a robot project");
        addNamed(noneSelected, "none");
        setVisibleChildName("none");
    }
}
