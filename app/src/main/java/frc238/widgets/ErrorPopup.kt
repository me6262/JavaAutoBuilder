package frc238.widgets

import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.gtk.ResponseType
import ch.bailu.gtk.type.Str
import frc238.App

fun makeAndRunPopup(message: String, description: String) {
    var popup = MessageDialog(App.window, message, description)
    popup.defaultResponse = Str("OK")
    popup.show()
}