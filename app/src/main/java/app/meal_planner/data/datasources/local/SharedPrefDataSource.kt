package app.meal_planner.data.datasources.local

import android.content.SharedPreferences
import androidx.core.content.edit
import app.meal_planner.data.models.Data
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import io.reactivex.Completable
import io.reactivex.Observable
import kotlin.math.roundToInt


class SharedPrefDataSource(private val sharedPreferences: SharedPreferences): SharedPrefInterface {

    companion object{
        const val CALORIES_KEY = "calories"
        const val CALORIES_LEFT_KEY = "caloriesLeft"
        const val PROTEIN_KEY = "protein"
        const val PROTEIN_LEFT_KEY = "proteinLeft"
        const val FAT_KEY = "fat"
        const val FAT_LEFT_KEY = "fatLeft"
        const val CARBOHYDRATES_KEY = "carbohydrates"
        const val CARBOHYDRATES_LEFT_KEY = "carbohydratesLeft"
        const val METRIC_KEY = "metric"
        const val AGE_KEY = "age"
        const val HEIGHT_KEY = "height"
        const val WEIGHT_KEY = "weight"
        const val ACTIVITY_KEY = "activity"
        const val GOAL_KEY = "goal"
        const val DIET_KEY = "diet"
    }

    override fun setExistingData(data: UserData): Completable {
        return Completable.fromCallable{
            if (data.metric){
                sharedPreferences.edit {
                    putInt(HEIGHT_KEY, data.height.toInt())
                    putInt(WEIGHT_KEY, data.weight)
                }
            }else{
                val height = (data.height * 30.48).roundToInt()
                val weight = (data.weight * 0.45359237).roundToInt()
                sharedPreferences.edit {
                    putInt(HEIGHT_KEY, height)
                    putInt(WEIGHT_KEY, weight)
                }
            }

            sharedPreferences.edit {
                putBoolean(METRIC_KEY, data.metric)
                putInt(AGE_KEY, data.age)
                putInt(CALORIES_KEY, data.calories)
                putInt(PROTEIN_KEY, data.protein)
                putInt(CARBOHYDRATES_KEY, data.carbohydrates)
                putInt(FAT_KEY, data.fat)
                putString(ACTIVITY_KEY, data.activityLevel)
                putString(DIET_KEY, data.dietType)
                putString(GOAL_KEY, data.goal)
                apply()
            }
        }
    }

    override fun setRemainingData(data: RemainingData): Completable {
        return Completable.fromCallable{
            sharedPreferences.edit {
                putInt(CALORIES_LEFT_KEY, data.calories)
                putInt(PROTEIN_LEFT_KEY, data.protein)
                putInt(CARBOHYDRATES_LEFT_KEY, data.carbohydrates)
                putInt(FAT_LEFT_KEY, data.fat)
                apply()
            }
        }
    }

    override fun getData(): Observable<Data> {
        return Observable.fromCallable {
            Data(
                UserData(
                    sharedPreferences.getBoolean(METRIC_KEY, false),
                    sharedPreferences.getInt(AGE_KEY, 0),
                    sharedPreferences.getInt(HEIGHT_KEY, 0).toDouble(),
                    sharedPreferences.getInt(WEIGHT_KEY, 0),
                    sharedPreferences.getInt(CALORIES_KEY, 0),
                    sharedPreferences.getInt(CARBOHYDRATES_KEY, 0),
                    sharedPreferences.getInt(PROTEIN_KEY, 0),
                    sharedPreferences.getInt(FAT_KEY, 0),
                    sharedPreferences.getString(ACTIVITY_KEY, "").toString(),
                    sharedPreferences.getString(DIET_KEY, "").toString(),
                    sharedPreferences.getString(GOAL_KEY, "").toString()
                ),
                RemainingData(
                    sharedPreferences.getInt(CALORIES_LEFT_KEY, 0),
                    sharedPreferences.getInt(CARBOHYDRATES_LEFT_KEY, 0),
                    sharedPreferences.getInt(PROTEIN_LEFT_KEY, 0),
                    sharedPreferences.getInt(FAT_LEFT_KEY, 0)
                )
            )
        }
    }


}