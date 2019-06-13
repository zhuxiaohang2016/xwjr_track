package com.xwjr.track.attend.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.xwjr.track.R

open class AttendBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .autoStatusBarDarkModeEnable(true)
                .fitsSystemWindows(true)
                .init()
    }
}
