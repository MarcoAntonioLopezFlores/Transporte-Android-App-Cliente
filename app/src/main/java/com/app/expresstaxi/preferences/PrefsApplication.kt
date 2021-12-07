package com.app.expresstaxi.preferences

import android.app.Application

class PrefsApplication: Application() {
    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(this)
    }
}