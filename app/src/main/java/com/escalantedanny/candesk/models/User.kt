package com.escalantedanny.candesk.models

import android.app.Activity
import android.content.Context
import com.escalantedanny.candesk.utils.Constants.AUTH_EMAIL
import com.escalantedanny.candesk.utils.Constants.AUTH_ID
import com.escalantedanny.candesk.utils.Constants.AUTH_PREF
import com.escalantedanny.candesk.utils.Constants.AUTH_TOKEN

class User(val id: String, val email: String?, val authenticationToken: String) {

    companion object {

        fun setLoggedInUser(activity: Activity, user: User) {
            activity.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE).also {
                it.edit()
                    .putString(AUTH_ID, user.id)
                    .putString(AUTH_EMAIL, user.email)
                    .putString(AUTH_TOKEN, user.authenticationToken)
                    .apply()
            }
        }

        fun getLoggedInUser(activity: Activity): User? {
            val prefs =
                activity.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE) ?: return null

            val userId = prefs.getString(AUTH_ID, "")
            if (userId == ""){
                return null
            }

            return User(
                userId ?: "",
                prefs.getString(AUTH_EMAIL, "") ?: "",
                prefs.getString(AUTH_TOKEN, "") ?: ""
            )

        }

        fun logout(activity:Activity){
            activity.getSharedPreferences(
                AUTH_PREF, Context.MODE_PRIVATE
            ).also {
                it.edit().clear().clear()
            }
        }

    }

}