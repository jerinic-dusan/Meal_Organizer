package app.meal_planner.data.datasources.local

import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData
import kotlin.math.roundToInt

class KcalDataSource: KcalInterface {

    override fun calculateUserData(data: RawData): UserData {

        var calories = 0
        var carbohydrates = 0
        var protein = 0
        var fat = 0
        var height = data.height
        var weight = data.weight
        var bmr = 0.0
        var exe = 0.0

        if (!data.metric) {
            height = data.height * 30.48
            weight = (data.weight / 2.2046).roundToInt()
        }

        bmr = if (data.male) {
            (13.397 * weight) + (4.799 * height) - (5.677 * data.age) + 88.362
        } else {
            (9.247 * weight) + (3.098 * height) - (4.330 * data.age) + 447.593
        }

        when (data.activity) {
            1 -> exe = 1.2      //No exercise
            2 -> exe = 1.375    //3 days a week
            3 -> exe = 1.55     //3-5 days a week
            4 -> exe = 1.8    //5-7 days a week
        }

        when (data.goal) {
            1 -> calories = (bmr * exe).roundToInt()            //Maintain weight
            2 -> calories = ((bmr * exe) - 300).roundToInt()    //Lose weight
            3 -> calories = ((bmr * exe) + 300).roundToInt()    //Gain weight
        }

        when (data.diet) {
            1 -> {  //40% C, 30% P, 30% F - UNSURE
                carbohydrates = ((calories * 0.4) / 4).roundToInt()
                protein = ((calories * 0.3) / 4).roundToInt()
                fat = ((calories * 0.3) / 9).roundToInt()
            }
            2 -> {  //47% C, 30% P, 23% F - LOW FAT
                carbohydrates = ((calories * 0.47) / 4).roundToInt()
                protein = ((calories * 0.3) / 4).roundToInt()
                fat = ((calories * 0.23) / 9).roundToInt()
            }
            3 -> {  //25% C, 40% P, 35% F - LOW CARB
                carbohydrates = ((calories * 0.25) / 4).roundToInt()
                protein = ((calories * 0.4) / 4).roundToInt()
                fat = ((calories * 0.35) / 9).roundToInt()
            }
        }

        return UserData(data.metric, data.age, data.height, data.weight, calories, carbohydrates, protein, fat)

    }
}