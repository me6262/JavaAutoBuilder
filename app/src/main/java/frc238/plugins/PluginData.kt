package frc238.plugins

data class PluginData(val parameterOpts: Array<String>, val pararameterName: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PluginData

        if (!parameterOpts.contentEquals(other.parameterOpts)) return false
        return pararameterName == other.pararameterName
    }

    override fun hashCode(): Int {
        var result = parameterOpts.contentHashCode()
        result = 31 * result + pararameterName.hashCode()
        return result
    }
}
