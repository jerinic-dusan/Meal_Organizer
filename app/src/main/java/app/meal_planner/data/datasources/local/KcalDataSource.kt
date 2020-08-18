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
        val bmr: Double
        var exe = 0.0
        var activity = ""
        var diet = ""
        var goal = ""

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
            1 -> {
                exe = 1.2
                activity = "No Exercise"
            }      //No exercise
            2 -> {
                exe = 1.375
                activity = "3 weekly"
            }    //3 days a week
            3 -> {
                exe = 1.55
                activity = "3+ weekly"
            }     //3-5 days a week
            4 -> {
                exe = 1.8
                activity = "5+ weekly"
            }    //5-7 days a week
        }

        when (data.goal) {
            1 -> {
                calories = (bmr * exe).roundToInt()
                goal = "Maintain weight"
            }            //Maintain weight
            2 -> {
                calories = ((bmr * exe) - 300).roundToInt()
                goal = "Lose weight"
            }    //Lose weight
            3 -> {
                calories = ((bmr * exe) + 300).roundToInt()
                goal = "Gain weight"
            }    //Gain weight
        }

        when (data.diet) {
            1 -> {  //40% C, 30% P, 30% F - UNSURE
                carbohydrates = ((calories * 0.4) / 4).roundToInt()
                protein = ((calories * 0.3) / 4).roundToInt()
                fat = ((calories * 0.3) / 9).roundToInt()
                diet = "Unsure"
            }
            2 -> {  //47% C, 30% P, 23% F - LOW FAT
                carbohydrates = ((calories * 0.47) / 4).roundToInt()
                protein = ((calories * 0.3) / 4).roundToInt()
                fat = ((calories * 0.23) / 9).roundToInt()
                diet = "Low fat"
            }
            3 -> {  //37% C, 30% P, 33% F - LOW CARB
                carbohydrates = ((calories * 0.37) / 4).roundToInt()
                protein = ((calories * 0.30) / 4).roundToInt()
                fat = ((calories * 0.33) / 9).roundToInt()
                diet = "Low carb"
            }
        }

        return UserData(data.metric, data.age, data.height, data.weight, calories, carbohydrates, protein, fat, activity, diet, goal)

    }
}