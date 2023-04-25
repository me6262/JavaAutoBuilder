package java238.widgets

import ch.bailu.gtk.adw.StatusPage
import ch.bailu.gtk.gio.SimpleAction
import ch.bailu.gtk.gio.SimpleActionGroup
import ch.bailu.gtk.glib.Variant
import ch.bailu.gtk.gobject.ParamSpec
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Popover
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackPage
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.PointerContainer
import ch.bailu.gtk.type.Str
import java238.App
import java238.background.Amode
import java238.background.AmodeList
import java238.background.CommandInfo
import java.util.*
import java.util.function.Supplier

class AmodeStack : Stack() {
    var amodeList: AmodeList? = null
    var noneSelected: StatusPage
    var editorWidgetSupplier: MutableMap<String, Supplier<AmodeEditorWidget>> = TreeMap()
    var up: Button
    var down: Button

    init {
        noneSelected = StatusPage()
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
        up = Button.newFromIconNameButton("go-up-symbolic")
        up.onClicked { moveFocusedCommandUp() }
        down = Button.newFromIconNameButton("go-down-symbolic")
        down.onClicked { moveFocusedCommandDown() }

        val rename = Button.newFromIconNameButton("document-edit-symbolic").also {
            it.onClicked {renameMode()}
            App.header.packStart(it)

        }

        onNotify { signal: ParamSpec ->
            if (signal.name.toString() == "visible-child") {
                App.title.subtitle = visibleChildName
            }
        }
    }

    private fun moveFocusedCommandDown() {
        editorWidgetSupplier[visibleChild.name.toString()]!!.get().moveRowDown()
    }

    val updatedAmodesList: AmodeList?
        get() {
            val modesList = ArrayList<Amode>(editorWidgetSupplier.size)
            for (key in editorWidgetSupplier.keys) {
                modesList.add(editorWidgetSupplier[key]!!.get().updatedMode)
            }
            amodeList!!.setAutonomousModes(modesList)
            return amodeList
        }

    private fun renameMode() {
        val namePop = Popover()
        val entry = Entry()
        namePop.child = entry
        namePop.parent = App.title
        entry.onActivate {
            val supp = editorWidgetSupplier.run {

                getPage(visibleChild).title = entry.asEditable().text.also { getPage(visibleChild).name = it }
                put(entry.asEditable().text.toString(), remove(visibleChild.name.toString())!!.also { it.get().name = visibleChildName })
                App.title.subtitle = entry.asEditable().text
                namePop.hide()
            }
        }
        entry.asEditable().text = visibleChildName
        namePop.popdown()
        namePop.show()
        println("woah")
    }
    fun removeAmode() {
        editorWidgetSupplier.remove(visibleChildName.toString())
        getPage(visibleChild).run {
            title = Str("")
            this.visible= false
        }

    }

    fun addCommandToVisibleChild(info: CommandInfo?) {
        println(visibleChild.name.toString())
        editorWidgetSupplier[visibleChild.name.toString()]!!.get().addCommand(info!!)
    }

    fun addTitled(widget: AmodeEditorWidget?, name: String) {
        super.addTitled(widget!!, name, name)
        editorWidgetSupplier[name] = Supplier { widget.self }
    }

    fun addAmodeToList() {
        val newMode = Amode()
        newMode.name = "New Auto"
        val editor = AmodeEditorWidget(newMode)
        addTitled(editor, "New Auto")
    }

    fun moveFocusedCommandUp() {
        editorWidgetSupplier[visibleChild.name.toString()]!!.get().moveRowUp()
        println("WOAH")
    }
}
