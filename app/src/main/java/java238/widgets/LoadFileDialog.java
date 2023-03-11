package java238.widgets;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.Str;
import java238.App;

public class LoadFileDialog {
    private FileChooser chooserInterface;
    Str currentProjectFolder;
    FileChooserDialog chooser;
    public LoadFileDialog() {}

    public void loadFolder() {


        chooser = new FileChooserDialog("Stuff", App.window, FileChooserAction.SELECT_FOLDER, "Open",ResponseType.ACCEPT, null);
        chooserInterface = new FileChooser(chooser.cast());
//        widget = new FileChooserWidget(chooserInterface.cast());
        chooser.onResponse(this::onResponse);
        chooser.show();
//        chooser.response(ResponseType.ACCEPT);

    }

    private void onResponse(int responseID) {
        System.out.println("WOAHAHAAHHH");
        System.out.println(responseID);
        if (responseID == ResponseType.ACCEPT) {
            currentProjectFolder =chooserInterface.getCurrentFolder().getParseName();
            System.out.println(currentProjectFolder);
            chooser.destroy();
        } else  {
            System.out.println("NOPE_______");
        }

    }

}
