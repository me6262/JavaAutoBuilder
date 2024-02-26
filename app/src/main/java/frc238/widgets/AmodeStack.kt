package frc238.widgets

import ch.bailu.gtk.adw.StatusPage
import ch.bailu.gtk.gio.SimpleAction
import ch.bailu.gtk.gio.SimpleActionGroup
import ch.bailu.gtk.gobject.ParamSpec
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Popover
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.type.Str
import frc238.App
import frc238.background.Amode
import frc238.background.AmodeList
import frc238.background.CommandInfo
import java.util.*
import java.util.function.Supplier

class AmodeStack : Stack() {
    var amodeList: AmodeList? = null
    private var noneSelected: StatusPage = StatusPage()
    private var editorWidgetSupplier: MutableMap<String, AmodeEditorWidget> = TreeMap()
    private var up: Button
    private var down: Button
    //counter used to correctly number autos labelled as "New Auto (n)" with n being the number called that
    private var unnamedCount = 0

    init {
        val action = SimpleAction("move-up", null)
        action.onActivate { println("asjdasdsda") }
        setName("amodestack")
        val group = SimpleActionGroup()
        insertActionGroup("modestack", group.asActionGroup())
        group.asActionMap().addAction(action.asAction())
        noneSelected.setIconName("system-search-symbolic")
        noneSelected.setTitle("No Auto Selected")
        noneSelected.setDescription("Please pick an auto or load a robot project")
        addNamed(noneSelected, "none")
        setVisibleChildName("none")
        up = Button.newFromIconNameButton("go-up-symbolic").also {
            it.onClicked { moveFocusedCommandUp() }
            App.header.packEnd(it)
        }
        down = Button.newFromIconNameButton("go-down-symbolic").also {
            it.onClicked { moveFocusedCommandDown() }
            App.header.packEnd(it)
        }


        onNotify { signal: ParamSpec ->
            if (signal.name.toString() == "visible-child") {
                App.title.subtitle = visibleChildName
            }
        }
    }

    private fun moveFocusedCommandDown() {
        editorWidgetSupplier[visibleChild.name.toString()]!!.moveRowDown()
    }

    val updatedAmodesList: AmodeList?
        get() {
            println("hi")
            val modesList = ArrayList<Amode>(editorWidgetSupplier.size)
            for (key in editorWidgetSupplier.keys) {
                println(key)
                modesList.add(editorWidgetSupplier[key]!!.updatedMode)
            }
            amodeList!!.setAutonomousModes(modesList)
            return amodeList
        }

    public fun renameMode() {
        val namePop = Popover()
        val entry = Entry()
        namePop.child = entry
        namePop.parent = App.title
        entry.onActivate {
            editorWidgetSupplier.run {

                val oldName = getPage(visibleChild).name.toString()
                val newName = entry.asEditable().text
                getPage(visibleChild).title = newName
                getPage(visibleChild).name = newName
                val oldMode = remove(oldName)
                oldMode!!.modeName = newName.toString()
                put(newName.toString(), oldMode)
                App.title.subtitle = newName
                println("new name: $newName")
                println("old name: $oldName")
                namePop.hide()
            }
        }
        entry.asEditable().text = visibleChildName
        namePop.popdown()
        namePop.show()
    }
    fun removeAmode() {
        editorWidgetSupplier.remove(visibleChildName.toString())
        getPage(visibleChild).run {
            title = Str("")
            this.visible= false
        }

    }

    fun addCommandToVisibleChild(info: CommandInfo?) {
        println(getPage(visibleChild).title.toString())
        editorWidgetSupplier[getPage(visibleChild).title.toString()]!!.addCommand(info!!)
    }

    fun addTitled(widget: AmodeEditorWidget?, name: String) {
        super.addTitled(widget!!, name, name)
        editorWidgetSupplier[name] = widget
    }

    fun addAmodeToList() {
        val newMode = Amode()
        newMode.name = "New Auto"
        val editor = AmodeEditorWidget(newMode)
        addTitled(editor, "New Auto")
    }

    fun moveFocusedCommandUp() {
        editorWidgetSupplier[getPage(visibleChild).title.toString()]!!.moveRowUp()
        println("WOAH")
    }
}
