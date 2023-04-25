package java238.widgets

import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.EntryRow
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.adw.PreferencesPage
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.FileChooserAction
import ch.bailu.gtk.gtk.FileChooserNative
import ch.bailu.gtk.gtk.ResponseType
import ch.bailu.gtk.gtk.Switch
import ch.bailu.gtk.type.Str
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import java.nio.file.Paths


class Settings {
    private var json: JSONObject? = null
    private var acessClassesSuffix: Switch = Switch()
    private var prefWindow: PreferencesWindow = PreferencesWindow()
    private var preferencesGroup: PreferencesGroup = PreferencesGroup()
    private var preferencesPage: PreferencesPage = PreferencesPage()
    var wpilibVersion: String
        get() = json!!["wpilibVersion"].toString()
        set(value) {
            json!!["wpilibVersion"] = value
        }
    var wpiDirectory: String
        get() = json!!["wpilibDirectory"].toString()
        set(value) {
            json!!["wpilibDirectory"] = value
        }
    var classLoading: Boolean
        get() = json!!["canLoadClasses"] as Boolean
        set(value) {
            json!!["canLoadClasses"] = value
        }
    var projectDir: String
        get() = json!!["projectDir"].toString()
        set(value) {
            json!!["projectDir"] = value
        }
    var pluginsEnabled: Boolean
            get() = json!!["pluginsEnabled"] as Boolean
            set(value) {
                json!!["pluginsEnabled"] = value
            }

    init {
        prefWindow.hideOnClose = true
        createListFromJson()
    }

    private fun createListFromJson() {
        try {
            BufferedReader(
                FileReader(
                    File(
                        Paths.get("").toAbsolutePath().toString() + "/src/main/resources/settings.json"
                    )
                )
            ).use { br ->

                val fileContentBuilder = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    fileContentBuilder.append(line)
                }
                val fileContent = fileContentBuilder.toString()
                val json = JSONParser().parse(fileContent) as JSONObject
                if (this.json == null) this.json = json
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        prefWindow.add(preferencesPage)
        preferencesPage.add(preferencesGroup)


        val versionRow = EntryRow()
        versionRow.setTitle("WPILib Project Version Number")
        versionRow.asEditable().setText(wpilibVersion)
        preferencesGroup.add(versionRow)

        val canAccessClasses = ActionRow()
        canAccessClasses.activatableWidget = acessClassesSuffix
        canAccessClasses.title = Str("Load Classes From Robot Project")
        canAccessClasses.subtitle = Str("makes cooler parameter fields for booleans and enums")
        acessClassesSuffix.active = classLoading
        acessClassesSuffix.valign = Align.CENTER
        canAccessClasses.addSuffix(acessClassesSuffix)
        preferencesGroup.add(canAccessClasses)
        prefWindow.onHide { classLoading = acessClassesSuffix.active }
        val chooserRow = EntryRow()
        val chooser =
            FileChooserNative("Pick a Folder", prefWindow, FileChooserAction.SELECT_FOLDER, "Select", "Cancel")
        chooser.asFileChooser().currentFolder = ch.bailu.gtk.gio.File.newForPath(Str(wpiDirectory))
        chooser.onResponse {
            if (it == ResponseType.ACCEPT) {
                chooser.asFileChooser().currentFolder.let { file ->
                    wpiDirectory = file.toString()
                    chooserRow.asEditable().text = file.parseName
                }
            }
        }
        val chooserButton = Button.newFromIconNameButton("folder-open-symbolic")
        chooserButton.onClicked { chooser.show() }
        chooserButton.valign = Align.CENTER

        chooserRow.run {
            showApplyButton = false
            asEditable().text = Str(wpiDirectory)
            addSuffix(chooserButton)
            asEditable().onChanged { wpiDirectory = this.toString() }
            setTitle("WPILib installation directory")
            preferencesGroup.add(this)
        }

        prefWindow.onHide {
            wpilibVersion = versionRow.asEditable().text.toString()
            wpiDirectory = chooserRow.asEditable().text.toString()
            classLoading = acessClassesSuffix.active
            saveSettings()
        }
    }

    fun openPrefsWindow() {
        prefWindow.show()
    }

    fun saveSettings() {
        println(json)
        val writer = FileWriter(Paths.get("").toAbsolutePath().toString() + "/src/main/resources/settings.json")
        writer.write(json!!.toJSONString())
        writer.flush()
    }


}