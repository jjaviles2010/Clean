package com.fiap18Mob.clean.utils

import android.app.Activity
import com.fiap18Mob.clean.view.about.AboutActivity
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailActivity
import com.fiap18Mob.clean.view.forgotpassword.ForgotPasswordActivity
import com.fiap18Mob.clean.view.listServices.ListServicesActivity
import com.fiap18Mob.clean.view.login.LoginActivity
import com.fiap18Mob.clean.view.main.MainActivity
import com.fiap18Mob.clean.view.signup.SignUpActivity

class ScreenMap {
    companion object {
        val SCREEN_NOT_TRACKING = "SCREEN_NOT_TRACKING"
        private fun getScreenNameBy(className: String): String {
            val screensNames = getScreenNames()
            return if (screensNames[className] == null) SCREEN_NOT_TRACKING else screensNames[className]!!
        }
        fun getScreenNameBy(activity: Activity): String {
            return getScreenNameBy(activity::class.java.simpleName)
        }
        fun getClassName(screenName: String): String {
            val screenNames = getScreenNames()
            for (o in screenNames.keys) {
                if (screenNames[o] == screenName) {
                    return o
                }
            }
            return ""
        }

        private fun getScreenNames(): Map<String, String> {
            return mapOf(
                Pair(LoginActivity::class.java.simpleName, "Login"),
                Pair(SignUpActivity::class.java.simpleName, "SignUp"),
                Pair(MainActivity::class.java.simpleName, "Main"),
                Pair(ListServicesActivity::class.java.simpleName, "ListServices"),
                Pair(ForgotPasswordActivity::class.java.simpleName, "ForgotPassword"),
                Pair(CleanerDetailActivity::class.java.simpleName, "CleanerDetail"),
                Pair(AboutActivity::class.java.simpleName, "About")
            )
        }

    }
}