package java238;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.HeaderBar;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Icon;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.type.Strs;
import java238.background.RobotProject;
import java238.widgets.AutoPicker;
import java238.widgets.LoadFileDialog;
/*
 * This Java source file was generated by the Gradle 'init' task.
 */

public class App {
    public static AutoPicker picker;
    public static Application app;
    public static ApplicationWindow window;
    public static Label label;
    public static RobotProject project;
    public static LoadFileDialog loadFileDialog;

    public static void init() {
        loadFileDialog = new LoadFileDialog();
    }

    public static void onStartup() {
        StyleManager.getDefault().setColorScheme(ColorScheme.FORCE_LIGHT);
    }

    public static void main(String[] args) {
        Adw.init();
        System.out.println(Gtk.getMajorVersion() + "." + Gtk.getMinorVersion() + "." + Gtk.getMicroVersion());
        app = new Application("org.frc238.autoBuilder", ApplicationFlags.FLAGS_NONE);
        init();
        picker = new AutoPicker();
        app.onStartup(App::onStartup);
        app.onActivate(() -> {
        StyleManager.getDefault().setColorScheme(ColorScheme.FORCE_DARK);
            var header = new HeaderBar();
            var toggleFlap = new Button();
            var load = new Button();
            var save = new Button();
            load.setLabel("Load");
            toggleFlap.onClicked(picker::onOpenClicked);
            load.onClicked(loadFileDialog::loadFolder);
            load.setIconName("folder-open-symbolic");
            save.setIconName("media-floppy-symbolic");
            toggleFlap.setIconName("dock-left");
            header.packEnd(save);
            header.packEnd(load);

            header.packStart(toggleFlap);
            window = new ApplicationWindow(app);
            window.setDirection(Orientation.VERTICAL);

//            window.setContent(header);

            Box vbox = new Box(Orientation.VERTICAL, 0);
            Box hbox = new Box(Orientation.HORIZONTAL, 0);
            hbox.append(picker.flap);
            vbox.append(header);
            vbox.append(hbox);
            window.setDefaultSize(900, 800);

            window.setContent(vbox);

            window.present();
        });
        app.run(args.length, new Strs(args));

    }
}