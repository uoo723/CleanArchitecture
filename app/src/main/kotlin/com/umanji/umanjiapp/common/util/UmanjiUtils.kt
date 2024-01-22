package com.umanji.umanjiapp.common.util


enum class AdminType(val value: String) {
    WORLD("world"),
    COUNTRY("country"),
    LOCALITY("locality"),
    SUBLOCALITY1("subLocality1"),
    SUBLOCALITY2("subLocality2");

    companion object {
        fun getAdminType(zoom: Int): AdminType {
            return when (zoom) {
                in 1..5 -> AdminType.WORLD
                in 6..10 -> AdminType.COUNTRY
                in 11..13 -> AdminType.LOCALITY
                in 14..16 -> AdminType.SUBLOCALITY1
                in 17..21 -> AdminType.SUBLOCALITY2
                else -> throw IllegalArgumentException("zoom is not valid")
            }
        }

        fun getZoomLevel(type: AdminType): Int {
            return when (type) {
                AdminType.WORLD -> 4
                AdminType.COUNTRY -> 7
                AdminType.LOCALITY -> 11
                AdminType.SUBLOCALITY1 -> 16
                AdminType.SUBLOCALITY2 -> 18
            }
        }
    }
}

fun CharSequence.getWebUrl(): String? {
    val regex = Regex(pattern = """((?:https?://)?(?:\w+\.)+(?:[?=&#/.]|\w)+)""")
    return regex.find(this)?.value?.let {
        if (!it.startsWith("http://") && !it.startsWith("https://")) {
            "http://$it"
        } else {
            it
        }
    }
}
