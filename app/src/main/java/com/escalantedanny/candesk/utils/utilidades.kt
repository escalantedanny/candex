package com.escalantedanny.candesk.utils

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.isNotEmpty()
}
