package frc238.widgets

import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.gtk.ResponseType
import ch.bailu.gtk.gtk.ScrolledWindow
import ch.bailu.gtk.gtk.Frame
import ch.bailu.gtk.type.Str
import frc238.App

fun makeAndRunPopup(message: String, description: String) {
    val popup = MessageDialog(App.window, message, description)
    popup.defaultResponse = Str("OK")

    popup.addResponse("OK", "OK")
    popup.show()
}

