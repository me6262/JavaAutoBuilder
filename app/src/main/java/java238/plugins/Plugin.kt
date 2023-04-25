package java238.plugins

import ch.bailu.gtk.adw.PreferencesRow
import ch.bailu.gtk.type.Str

interface Plugin {

    /**
     * the widget that is added to the AutoCommandRow when something of the given parameterName is found
     */
    var parameterWidget: PreferencesRow

    /**
     * the name that is checked for when creating an AutoCommandRow to see if the widget is needed
     */
    val parameterName: String

    /**
     * this should have a get that supplies the parameter's value as a string
     */
    var parameterAsStr: Str
}