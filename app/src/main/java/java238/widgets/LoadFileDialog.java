package java238.widgets;

import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.exception.AllocationError;
import java238.App;
import java238.background.RobotProject;
import org.checkerframework.checker.units.qual.A;

public class LoadFileDialog {
    private FileChooser chooserInterface;
    String currentProjectFolder;
    FileChooserDialog chooser;
    public LoadFileDialog() {}

    public void loadFolder() {
        chooser = new FileChooserDialog("Stuff", App.window, FileChooserAction.SELECT_FOLDER, "Open",ResponseType.ACCEPT, null);
        chooserInterface = new FileChooser(chooser.cast());
        chooser.onResponse(this::onResponse);
        File startPath = File.newForPath(new Str("~/"));
        try {
            chooserInterface.setCurrentFolder(startPath);
        } catch (AllocationError e) {
            throw new RuntimeException(e);
        }
        chooser.show();

    }

    private void onResponse(int responseID) {
        System.out.println(responseID);
        if (responseID == ResponseType.ACCEPT) {
            currentProjectFolder = chooserInterface.getCurrentFolder().getParseName().toString();
            System.out.println(currentProjectFolder);
            App.project.setRootDirectory(currentProjectFolder);
            App.project.loadRobotProject();
            chooser.destroy();
            App.setAutoList();
        }

    }

}
