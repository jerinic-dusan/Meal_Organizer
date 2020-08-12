package app.meal_planner.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.meal_planner.data.models.UserData
import app.meal_planner.data.models.RemainingData
import app.meal_planner.data.repositories.UserDataRepository
import app.meal_planner.presentation.contract.UserDataContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserDataViewModel(private val userDataRepository: UserDataRepository): ViewModel(), UserDataContract.ViewModel {

    override val exists: MutableLiveData<UserData> = MutableLiveData()
    override val remainingData: MutableLiveData<RemainingData> = MutableLiveData()
    override val dailyData: MutableLiveData<RemainingData> = MutableLiveData()
    private val subscriptions = CompositeDisposable()

    override fun getExistingData(){
        val subscription = userDataRepository.getExistingData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                exists.value = it
                calculateDailyData()
            },
            {
                Timber.e(it)
            },
            {
//                Timber.e("On complete")
            }
        )
        subscriptions.add(subscription)
    }

    override fun setExistingData(data: UserData){
        val subscription = userDataRepository.setExistingData(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
//                Timber.e("User data inserted")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun getRemainingData(){
        val subscription = userDataRepository.getRemainingData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                remainingData.value = it
                calculateDailyData()
            },
            {
                Timber.e(it)
            },
            {
//                Timber.e("On complete")
            }
        )
        subscriptions.add(subscription)
    }

    override fun setRemainingData(data: RemainingData){
        val subscription = userDataRepository.setRemainingData(data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
//                Timber.e("User remaining data inserted")

            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }

    override fun saveDailyProgress() {
        //TODO SAVE TO DB
    }

    override fun getDailyData() {
        TODO("Not yet implemented")
    }

    override fun setDailyData(data: RemainingData) {
        TODO("Not yet implemented")
    }

    override fun calculateDailyData() {
        dailyData.value = RemainingData(
            exists.value!!.calories - remainingData.value!!.calories,
            exists.value!!.carbohydrates - remainingData.value!!.carbohydrates,
            exists.value!!.protein - remainingData.value!!.protein,
            exists.value!!.fat - remainingData.value!!.fat
        )
        Timber.e("${dailyData.value}")
    }

}