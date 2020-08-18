package app.meal_planner.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItemsEntity
import app.meal_planner.data.repositories.MealRepository
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.view.states.MealState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MealsViewModel(private val mealRepository: MealRepository): ViewModel(), MealsContract.ViewModel {

    override val mealState: MutableLiveData<MealState> = MutableLiveData()
    override val mealId: MutableLiveData<Long> = MutableLiveData()
    private val subscriptions = CompositeDisposable()

    override fun getMeals() { //USED TO GET ALL MEALS & ITEMS FROM DB
        val subscription = mealRepository.getMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                mealState.value = MealState.Success(it)
//                Timber.e("Data fetched: $it")
            },
            {
                mealState.value = MealState.Error("Error happened while fetching data")
            }
        )
        subscriptions.add(subscription)
    }

    override fun insertMeal(mealWithItemsEntity: MealWithItemsEntity){ //USED TO INSERT A MEAL AND ALL OF ITS ITEMS IN DB
        val subscription = mealRepository.insert(mealWithItemsEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Inserted $mealWithItemsEntity")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteAll() { //USED TO DELETE ALL MEALS AND ITEMS FROM DB (DEBUG PURPOSES)
        val subscription = mealRepository.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Deleted all meals & items")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun updateMeal(meal: MealEntity) { //USED TO UPDATE FIELDS SUCH AS 'TODAY' & 'EXISTING' IN DB
        val subscription = mealRepository.update(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Meal updated")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun updateMealAndItems(mealWithItemsEntity: MealWithItemsEntity) { //USED TO UPDATE A MEAL AND ALL OF ITS ITEMS IN DB
        val subscription = mealRepository.updateMeal(mealWithItemsEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Meal updated")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun dailyReset() { //USED TO RESET DAILY MEALS (UPDATE ALL 'TODAY' FIELDS TO FALSE AND DELETE MEALS WHICH HAVE 'TODAY' AND 'EXISTING' FIELDS ON FALSE)
        val subscription = mealRepository.dailyReset().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Daily reset")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}