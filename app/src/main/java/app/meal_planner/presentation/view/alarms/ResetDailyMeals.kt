package app.meal_planner.presentation.view.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import timber.log.Timber

class ResetDailyMeals: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "RESET_DAILY_MEALS") {
            Timber.e("Alarm received")
            val str = intent.getStringExtra("KEY_RESET_DAILY_MEALS")
        }
    }

}