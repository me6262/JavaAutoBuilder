package frc238

import ch.bailu.gtk.adw.AboutWindow
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.ToastPriority
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.gio.SimpleAction
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.CssProvider
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.StyleContext
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs
import frc238.background.RobotProject
import frc238.plugins.PluginManager
import frc238.widgets.AutoPicker
import frc238.widgets.Settings
import frc238.widgets.loadFolder
import frc238.widgets.makeAndRunPopup
import java.io.FileNotFoundException
import java.nio.file.Paths
import kotlin.system.exitProcess

object App {
    // FIXME avoid global variables
    lateinit var app: Application
    lateinit var picker: AutoPicker
    lateinit var window: ApplicationWindow
    lateinit var project: RobotProject
    lateinit var header: HeaderBar
    lateinit var title: WindowTitle
    lateinit var settings: Settings
//     lateinit var plugins: PluginManager

    @JvmStatic
    fun main(args: Array<String>) {
        // Adw.init() // This call should not be necessary if you use adw.Application instead of gtk.Application
        val theApp = Application("org.frc238.autoBuilder", ApplicationFlags.FLAGS_NONE)

        /*
        FIXME This works only with compiled resources
        Compile with `glib-compile-resources` application that is part of the gtk distribution
        See Adwaita example in java-gtk
        there is also a helper function to load compiled resource files: GResource.loadAndRegister("path/to/compiled.gresources")
        */
//        val gresource = File.newForPath(Str("icons/resources.gresource.xml"))
//        try {
//            Gio.resourceLoad(gresource.path)
//        } catch (e: AllocationError) {
//            throw RuntimeException(e)
//        }

        // This could cause crashes
//        StyleManager.getDefault().colorScheme = ColorScheme.FORCE_DARK

        theApp.onActivate { buildUI(theApp) }
        theApp.run(args.size, Strs(args))
        theApp.onShutdown { exitProcess(0) }
        app = theApp
    }

    private fun showAboutWindow() {
        val aboutMenu = AboutWindow()
        aboutMenu.application = app
        aboutMenu.addCreditSection("Developers", Strs(arrayOf("Hayden Mitchell", null)))
        aboutMenu.applicationName = Str("Autonomous Builder")
        aboutMenu.website = Str("frc238.org")
        aboutMenu.show()
    }

    private fun buildUI(app: Application) {

        println("woah")

        settings = Settings()
        header = HeaderBar()
        header.addCssClass("flat")

        project = RobotProject()
        picker = AutoPicker()
        title = WindowTitle("Autonomous Builder", "")
        header.titleWidget = title
        val cssProvider = CssProvider()
        val toggleCommandFlap = Button()
        toggleCommandFlap.setIconName("sidebar-show-right-symbolic")
        toggleCommandFlap.onClicked { picker.commandSidebar.onToggleClicked() }
        header.packEnd(toggleCommandFlap)

        val menubutton = MenuButton()
        menubutton.setIconName("open-menu-symbolic")

        val menu = Menu()
        val about = MenuItem("About", "app.about")
        menu.appendItem(about)
        val aboutAction = SimpleAction("about", null)
        app.asActionMap().addAction(aboutAction.asAction())
        aboutAction.onActivate { showAboutWindow() }
        val properties = MenuItem("Properties", "app.properties")
        val propertiesAction = SimpleAction("properties", null)
        menu.appendItem(properties)
        propertiesAction.onActivate { settings.openPrefsWindow() }
        app.asActionMap().addAction(propertiesAction.asAction())
        menubutton.menuModel = menu
        header.packEnd(menubutton)
        val load = Button()
        load.setLabel("Load")
        load.onClicked { loadFolder() }
        load.setIconName("folder-open-symbolic")
        header.packEnd(load)
        val save = Button()
        save.onClicked { saveAmodeFile() }
        save.setIconName("media-floppy-symbolic")
        header.packEnd(save)
        val toggleFlap = Button()
        toggleFlap.setIconName("sidebar-show-symbolic")
        toggleFlap.onClicked { picker.onToggleClicked() }
        header.packStart(toggleFlap)
        val addNewMode = Button()
        addNewMode.setIconName("list-add-symbolic")
        addNewMode.onClicked { picker.stack.addAmodeToList() }
        header.packStart(addNewMode)
        val removeMode = Button.newFromIconNameButton("user-trash-symbolic")
        removeMode.onClicked { picker.stack.removeAmode() }
        header.packStart(removeMode)
        val rename = Button.newFromIconNameButton("document-edit-symbolic")
        rename.onClicked { picker.stack.renameMode() }
        header.packStart(rename)
        window = ApplicationWindow(app)
        window.direction = Orientation.VERTICAL
        cssProvider.loadFromPath(Paths.get("").toAbsolutePath().toString() + "/gtk.css")
        StyleContext.addProviderForDisplay(window.display, cssProvider.asStyleProvider(), ToastPriority.HIGH)
        //            window.addCssClass("accent");
        val vbox = Box(Orientation.VERTICAL, 0)
        val hbox = Box(Orientation.HORIZONTAL, 0)
        hbox.append(picker.flap)
        vbox.append(header)
        vbox.append(hbox)
        window.setDefaultSize(900, 800)
        window.content = vbox
        window.present()
    }

    @JvmStatic
    fun initAutoList() {
        val modes = project.autoModes!!.autonomousModes
        picker.stack.amodeList = project.autoModes
        try {
            if (settings.pluginsEnabled) {
//                 plugins.evalParameters()
            }
        } catch (e: Error) {
           e.printStackTrace()
        }
        try {

            project.indexCommands()
            picker.commandSidebar.generateList()
        } catch (e: FileNotFoundException) {
            println("Whoops, no file here!")
            e.printStackTrace()
            makeAndRunPopup("Error Finding Commands to Use", "disabling class loading for next run")
            settings.classLoading = false
        }
        picker.setModes(modes)
        println("dfghjkl")
    }

    @Throws(NullPointerException::class)
    fun saveAmodeFile() {
        project.autoModes = picker.stack.updatedAmodesList
        println("saveAmodeFile")
    }
}
