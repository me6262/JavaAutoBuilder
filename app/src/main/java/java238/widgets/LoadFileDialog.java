package java238.widgets;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.exception.AllocationError;
import java238.App;

public class LoadFileDialog {
    String currentProjectFolder;
    FileChooserNative chooser;

    public LoadFileDialog() {

    }

    public void loadFolder() {
        chooser = new FileChooserNative("Stuff", App.window, FileChooserAction.SELECT_FOLDER, "Open", "Cancel");
        chooser.onResponse(this::onResponse);
        File startPath = File.newForPath(new Str("~/"));
        try {
            chooser.asFileChooser().setCurrentFolder(startPath);
        } catch (AllocationError e) {
            throw new RuntimeException(e);
        }
        chooser.show();

    }

    private void onResponse(int responseID) {
        System.out.println(responseID);
        if (responseID == ResponseType.ACCEPT) {
            currentProjectFolder = chooser.asFileChooser().getCurrentFolder().getParseName().toString();
            System.out.println(currentProjectFolder);
            App.project.setRootDirectory(currentProjectFolder);
            App.project.loadRobotProject();
            chooser.destroy();
            App.initAutoList();
            App.settings.setProjectDir(currentProjectFolder);
            App.settings.saveSettings();
        }

    }

}
