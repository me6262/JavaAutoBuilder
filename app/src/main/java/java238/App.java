package java238;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.HeaderBar;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import java238.background.Amode;
import java238.background.RobotProject;
import java238.widgets.AutoPicker;
import java238.widgets.LoadFileDialog;

import java.io.FileNotFoundException;
import java.util.List;


public class App {
    public static AutoPicker picker;
    public static Application app;
    public static ApplicationWindow window;
    public static RobotProject project;
    public static LoadFileDialog loadFileDialog;
    public static HeaderBar header;
    public static WindowTitle title;

    public static void init() {
        loadFileDialog = new LoadFileDialog();
        project = new RobotProject();
    }

    public static void onStartup() {
//        StyleManager.getDefault().setColorScheme(ColorScheme.FORCE_LIGHT);
    }

    public static void main(String[] args) {
        Adw.init();
        app = new Application("org.frc238.autoBuilder", ApplicationFlags.FLAGS_NONE);
        init();
        header = new HeaderBar();
        picker = new AutoPicker();
        Strs accels = new Strs(new Str[]{new Str("Control+h")});
        System.out.println(Gdk.keyvalFromName(new Str("<Control>h")));

        System.out.println(Gdk.keyvalName(16777215));
        System.out.println(accels.get(0));
        app.setAccelsForAction("modestack::move-up", accels);
//        System.out.println(app.getAccelsForAction(new Str("modestack::move-up")).get(0));
        app.onStartup(App::onStartup);
        app.onActivate(() -> {
            title = new WindowTitle("Autonomous Builder", "");
            StyleManager.getDefault().setColorScheme(ColorScheme.FORCE_DARK);
            header.setTitleWidget(title);

            var toggleCommandFlap = new Button();
            toggleCommandFlap.setIconName("go-previous-symbolic");
            toggleCommandFlap.onClicked(() -> {
                picker.commandSidebar.onToggleClicked();
                toggleCommandFlap.setIconName(picker.commandSidebar.getRevealFlap() ? "go-previous-symbolic" : "go-next-symbolic");
            });
            header.packEnd(toggleCommandFlap);

            var load = new Button();
            load.setLabel("Load");
            load.onClicked(loadFileDialog::loadFolder);
            load.setIconName("folder-open-symbolic");
            header.packEnd(load);


//            var aboutButton = new MenuItem("About", );
//            var menuModel = new MenuModel(MenuModel.);
//            var pop = PopoverMenu.newFromModelPopoverMenu(menuModel);;

//            var menu = new MenuButton();
//            menu.setIconName("open-menu-symbolic");
//            menu.onActivate(App::onMenuClicked);
//            menu.setPopover(pop);
//            header.packEnd(menu);


            var save = new Button();
            save.onClicked(App::saveAmodeFile);
            save.setIconName("media-floppy-symbolic");
            header.packEnd(save);

            var toggleFlap = new Button();
            toggleFlap.setIconName("go-previous-symbolic");
            toggleFlap.onClicked(() -> {
                picker.onToggleClicked();
                toggleFlap.setIconName(toggleFlap.getIconName().toString().equals("go-next-symbolic") ? "go-previous-symbolic" : "go-next-symbolic");
            });
            header.packStart(toggleFlap);

            var addNewMode = new Button();
            addNewMode.setIconName("list-add-symbolic");
            addNewMode.onClicked(picker.stack::addAmodeToList);
            header.packStart(addNewMode);

            var removeMode = Button.newFromIconNameButton("user-trash-symbolic");
            removeMode.onClicked(picker.stack::removeAmode);
            header.packStart(removeMode);



            window = new ApplicationWindow(app);
            window.setDirection(Orientation.VERTICAL);


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

    public static void initAutoList() {
        List<Amode> modes = project.getAutoModes().getAutonomousModes();
        picker.stack.setAmodeList(project.getAutoModes());
        try {
        project.indexCommands();
        project.indexTrajectories();
        picker.commandSidebar.generateList();

        } catch (FileNotFoundException e) {
            System.out.println("Whoops, no file here!");
        }
        picker.setModes(modes);
    }

    public static void saveAmodeFile() throws NullPointerException {
        project.setAutoModes(picker.stack.getUpdatedAmodesList());

        System.out.println("saveAmodeFile");
    }
}