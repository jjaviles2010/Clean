package com.fiap18Mob.clean

//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import com.fiap18Mob.clean.utils.CleanTracker
import com.fiap18Mob.clean.utils.ScreenMap

open class BaseActivity : AppCompatActivity() {
    open fun getScreenName(): String {
        return ScreenMap.getScreenNameBy(this)
    }
    override fun onStart() {
        super.onStart()
        CleanTracker.trackScreen(this, getScreenName())
    }
}