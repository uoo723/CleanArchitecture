package com.umanji.umanjiapp.common.util


fun String.getBundleKey(prefix: String = "com.umanji.umanjiapp.key"): String {
    return "$prefix.$this"
}
