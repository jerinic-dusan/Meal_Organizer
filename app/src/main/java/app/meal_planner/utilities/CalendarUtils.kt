package app.meal_planner.utilities

import java.util.*

interface CalendarUtils {

    fun calculateDate(days: Int): Date{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

}