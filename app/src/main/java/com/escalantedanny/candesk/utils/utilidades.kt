package com.escalantedanny.candesk.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Patterns
import com.escalantedanny.candesk.R

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.isNotEmpty()
}

fun showErrorDialog(messageId: Int, context: Context){
    AlertDialog.Builder(context)
        .setTitle(R.string.there_was_an_error)
        .setMessage(messageId)
        .setPositiveButton(android.R.string.ok) { _, _ ->}
        .create()
        .show()

}
