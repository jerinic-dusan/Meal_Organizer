package app.meal_planner.presentation.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.meal_planner.R
import app.meal_planner.presentation.contract.UserDataContract
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash){

    private val userDataViewModel: UserDataContract.ViewModel by viewModel<UserDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        checkForFirstTime()
    }

    private fun checkForFirstTime() {
        userDataViewModel.exists.observe(this, Observer {
            intent = if (it != null){
                Intent(this, MainActivity::class.java)
            }else{
                Intent(this, IntroActivity::class.java)
            }
            startActivity(intent)
            finish()
        })
        userDataViewModel.getRemainingData()
        userDataViewModel.getExistingData()
    }
}