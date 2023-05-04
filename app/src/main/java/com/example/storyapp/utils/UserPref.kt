package com.example.storyapp.utils

import android.content.Context
import com.example.storyapp.main.LoginFragment

class UserPref(context: Context) {
    companion object {
        private const val PREFF_NAME = "PREFF_NAME"
        private const val TOKEN = "TOKEN"
    }

    private val preferences = context.getSharedPreferences(PREFF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken() : String? {
        return preferences.getString(TOKEN, null)
    }

    fun deleteToken() {
        preferences.edit().clear().commit()
    }
}
