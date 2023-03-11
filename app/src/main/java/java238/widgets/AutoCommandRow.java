package java238.widgets;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.gtk.Button;

public class AutoCommandRow extends ActionRow {
    private Button delete;
    public AutoCommandRow() {
        super();
        delete = new Button();
        delete.setIconName("edit-delete-symbolic");
        addSuffix(delete);


    }

}
