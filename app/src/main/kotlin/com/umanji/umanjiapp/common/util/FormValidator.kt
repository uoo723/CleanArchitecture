package com.umanji.umanjiapp.common.util

import java.util.regex.Pattern

fun String.isCellPhoneValid(): Boolean {
    return Pattern.compile("^(01[016789])-?(\\d{3,4})-?(\\d{4})$")
            .matcher(this).matches()
}
