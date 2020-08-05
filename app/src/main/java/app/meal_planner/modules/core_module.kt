package app.meal_planner.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.meal_planner.data.datasources.local.MainDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val coreModule = module {

    single<SharedPreferences> {
        androidApplication().getSharedPreferences(androidApplication().packageName, Context.MODE_PRIVATE)
    }

    single { Room.databaseBuilder(androidContext(), MainDataBase::class.java, "MealOrganizer")
        .fallbackToDestructiveMigration()
        .build() }

}