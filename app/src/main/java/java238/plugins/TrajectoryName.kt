package java238.plugins

import ch.bailu.gtk.adw.ActionRow
import ch.bailu.gtk.adw.PreferencesRow
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.type.Str
import java238.App
import java.util.function.Supplier

class TrajectoryName(parameter: String) : Plugin {


    override var parameterWidget: PreferencesRow = ActionRow()
    override val parameterName = "TrajectoryName"
    private val trajectoryName: ComboBoxText
    override var parameterAsStr: Str
        get() = trajectoryName.activeText
        set(value) {}

    init {
        parameterWidget.title = Str("Trajectory Name")
        trajectoryName = ComboBoxText()
        trajectoryName.addCssClass("flat")
        val trajectories = App.project.trajectories
        if (trajectories != null) {
            for (j in trajectories.indices) {
                trajectoryName.appendText(trajectories[j])
                if (trajectories[j] == parameter) {
                    trajectoryName.active = j
                }
            }
        }
        (parameterWidget as ActionRow).addSuffix(trajectoryName)
    }

    constructor() : this("")
}
