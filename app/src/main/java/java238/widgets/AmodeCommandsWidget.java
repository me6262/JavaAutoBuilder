package java238.widgets;

import ch.bailu.gtk.adw.PreferencesGroup;
import ch.bailu.gtk.adw.StatusPage;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Widget;
import java238.background.AmodeCommandList;

public class AmodeCommandsWidget {

    private Box box = new Box(Orientation.VERTICAL, 2);
    private StatusPage noAuto = new StatusPage();
    private PreferencesGroup amodeCommands = new PreferencesGroup();
    public AmodeCommandsWidget() {
        noAuto.setTitle("No Auto Selected Yet");
        noAuto.setDescription("You can open one from the left sidebar");
        noAuto.setIconName("system-help-symbolic");
        box.append(noAuto);
    }

    public Widget getWidget() {
        return box;
    }

    public void setNoneSelected() {
        box.remove(box.getFirstChild());
        box.append(noAuto);
    }

    public void setAuto(String autoName, AmodeCommandList commandList) {



        box.remove(box.getFirstChild());
        box.append(amodeCommands);
    }
}
