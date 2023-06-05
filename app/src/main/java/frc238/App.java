package frc238;

import ch.bailu.gtk.adw.*;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.HeaderBar;
import ch.bailu.gtk.gio.*;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.exception.AllocationError;
import frc238.background.Amode;
import frc238.background.RobotProject;
import frc238.plugins.PluginManager;
import frc238.widgets.AutoPicker;
import frc238.widgets.LoadFileDialog;
import frc238.widgets.Settings;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;


public class App {
    public static AutoPicker picker;
    public static Application app;
    public static ApplicationWindow window;
    public static RobotProject project;
    public static LoadFileDialog loadFileDialog;
    public static HeaderBar header;
    public static WindowTitle title;
    public static Settings settings;
    public static PluginManager plugins;

    public static void init() {
        settings = new Settings();
        if (!settings.getProjectDir().equals("")) {
            project = new RobotProject(settings.getProjectDir());
        } else {
            project = new RobotProject();
        }
        plugins = new PluginManager();
        loadFileDialog = new LoadFileDialog();
    }


    public static void main(String[] args) {
        Adw.init();
        app = new Application("org.frc238.autoBuilder", ApplicationFlags.FLAGS_NONE);
        init();
        header = new HeaderBar();
        header.addCssClass("flat");
        File gresource = File.newForPath(new Str("icons/resources.gresource.xml"));
        try {
            Gio.resourceLoad(gresource.getPath());
        } catch (AllocationError e) {
            throw new RuntimeException(e);
        }
        picker = new AutoPicker();

        app.onActivate(() -> {
            title = new WindowTitle("Autonomous Builder", "");
            StyleManager.getDefault().setColorScheme(ColorScheme.FORCE_DARK);
            header.setTitleWidget(title);
            var cssProvider = new CssProvider();

            var toggleCommandFlap = new Button();
            toggleCommandFlap.setIconName("sidebar-show-right-symbolic");
            toggleCommandFlap.onClicked(() -> {
                picker.commandSidebar.onToggleClicked();
            });
            header.packEnd(toggleCommandFlap);

            var menubutton = new MenuButton();
            menubutton.setIconName("open-menu-symbolic");
            var menu = new Menu();
            var about = new MenuItem("About", "app.about");
            menu.appendItem(about);
            var aboutAction = new SimpleAction("about", null);
            app.asActionMap().addAction(aboutAction.asAction());
            aboutAction.onActivate((signal) -> showAboutWindow());

            var properties = new MenuItem("Properties", "app.properties");
            var propertiesAction = new SimpleAction("properties", null);
            menu.appendItem(properties);
            propertiesAction.onActivate((signal) -> settings.openPrefsWindow());

            app.asActionMap().addAction(propertiesAction.asAction());



            menubutton.setMenuModel(menu);
            header.packEnd(menubutton);

            var load = new Button();
            load.setLabel("Load");
            load.onClicked(loadFileDialog::loadFolder);
            load.setIconName("folder-open-symbolic");
            header.packEnd(load);


            var save = new Button();
            save.onClicked(App::saveAmodeFile);
            save.setIconName("media-floppy-symbolic");
            header.packEnd(save);

            var toggleFlap = new Button();
            toggleFlap.setIconName("sidebar-show-symbolic");
            toggleFlap.onClicked(picker::onToggleClicked);
            header.packStart(toggleFlap);

            var addNewMode = new Button();
            addNewMode.setIconName("list-add-symbolic");
            addNewMode.onClicked(picker.stack::addAmodeToList);
            header.packStart(addNewMode);

            var removeMode = Button.newFromIconNameButton("user-trash-symbolic");
            removeMode.onClicked(picker.stack::removeAmode);
            header.packStart(removeMode);

            var rename = Button.newFromIconNameButton("document-edit-symbolic");
            rename.onClicked(picker.stack::renameMode);
            App.header.packStart(rename);

            window = new ApplicationWindow(app);
            window.setDirection(Orientation.VERTICAL);
            cssProvider.loadFromPath(Paths.get("").toAbsolutePath() + "/gtk.css");
            StyleContext.addProviderForDisplay(window.getDisplay(), cssProvider.asStyleProvider(), ToastPriority.HIGH);
//            window.addCssClass("accent");


            Box vbox = new Box(Orientation.VERTICAL, 0);
            Box hbox = new Box(Orientation.HORIZONTAL, 0);
            hbox.append(picker.flap);
            vbox.append(header);
            vbox.append(hbox);
            window.setDefaultSize(900, 800);

            window.setContent(vbox);

            window.present();
            if (project.getLoadSuceeded()) {
                initAutoList();
            }
        });
        app.run(args.length, new Strs(args));


    }

    private static void showAboutWindow() {
        var aboutMenu = new AboutWindow();
        aboutMenu.setApplication(app);
        aboutMenu.addCreditSection("Developers", new Strs(new String[]{"Hayden Mitchell"}));
        aboutMenu.show();
    }

    public static void initAutoList() {
        List<Amode> modes = project.getAutoModes().getAutonomousModes();
        picker.stack.setAmodeList(project.getAutoModes());
        try {
            project.indexCommands();
            picker.commandSidebar.generateList();

        } catch (FileNotFoundException e) {
            System.out.println("Whoops, no file here!");
        }
        picker.setModes(modes);
        System.out.println("dfghjkl");
    }

    public static void saveAmodeFile() throws NullPointerException {
        project.setAutoModes(picker.stack.getUpdatedAmodesList());

        System.out.println("saveAmodeFile");
    }
}