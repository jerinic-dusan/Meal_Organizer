package app.meal_planner.presentation.viewmodel



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.meal_planner.data.models.RawData
import app.meal_planner.data.models.UserData
import app.meal_planner.data.repositories.KcalDataRepository
import app.meal_planner.presentation.contract.KcalDataContract

class KcalDataViewModel(private val kcalDataRepository: KcalDataRepository): ViewModel(), KcalDataContract.ViewModel {

    override val userData: MutableLiveData<UserData> = MutableLiveData()

    override fun calculateUserData(data: RawData) {
        userData.value = kcalDataRepository.calculateUserData(data)
    }
}