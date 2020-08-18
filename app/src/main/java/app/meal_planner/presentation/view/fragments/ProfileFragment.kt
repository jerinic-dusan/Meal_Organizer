package app.meal_planner.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.meal_planner.R
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.view.activities.CalculatorActivity
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val userDataViewModel: UserDataContract.ViewModel by sharedViewModel<UserDataViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initFields()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        userDataViewModel.getData()
    }

    @SuppressLint("SetTextI18n")
    private fun initFields() {
        userDataViewModel.exists.observe(viewLifecycleOwner, Observer {
            age.text = it.age.toString()
            height.text = if (it.metric){
                it.height.roundToInt().toString() + "cm"
            }else{
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.HALF_EVEN
                (df.format(it.height / 30.48)).toString() + "ft"
            }
            weight.text = if (it.metric){
                it.weight.toString() + "kg"
            }else{
                val df = DecimalFormat("#")
                df.roundingMode = RoundingMode.HALF_EVEN
                (df.format(it.weight / 0.45359237)).toString() + "lbs"
            }
            activity_level.text = it.activityLevel
            diet.text = it.dietType
            goal.text = it.goal

            carbs.text = it.carbohydrates.toString() + "g"
            protein.text = it.protein.toString() + "g"
            fat.text = it.fat.toString() + "g"
        })
    }

    private fun initListeners() {
        new_kcal.setOnClickListener {
            val intent = Intent(context, CalculatorActivity::class.java)
            startActivity(intent)
        }
    }
}