package java238.widgets;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.*;

public class LoadFileDialog {
    private Dialog dialog;
    private FileChooser chooserInterface;
    File currentProjectFolder;
    FileChooserDialog chooser;
    FileChooserWidget widget;

    public LoadFileDialog() {
        dialog = new Dialog();
        widget = new FileChooserWidget(FileChooserAction.SELECT_FOLDER);
//        dialog.setChild();
        chooserInterface = new FileChooser(dialog.cast());
//        chooser = new FileChooserDialog(chooserInterface.cast());
    }

    public void loadFolder() {

        dialog.onResponse(this::onResponse);

        dialog.show();

    }

    private void onResponse(int responseID) {
        if (responseID == ResponseType.OK) {
            currentProjectFolder = chooserInterface.getCurrentFolder();
        }
    }

}
