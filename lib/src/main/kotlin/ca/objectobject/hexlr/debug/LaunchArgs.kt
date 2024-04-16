package ca.objectobject.hexlr.debug

data class LaunchArgs(val map: Map<String, Any>) {
    val program: String by map
    val stopOnEntry = map.getOrDefault("stopOnEntry", false) as Boolean
}
