package app.meal_planner.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.meal_planner.data.models.ItemEntity
import app.meal_planner.data.models.MealEntity
import app.meal_planner.data.models.MealWithItemsEntity
import app.meal_planner.data.repositories.ItemRepository
import app.meal_planner.data.repositories.MealRepository
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.view.states.MealState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MealsViewModel(private val mealRepository: MealRepository, private val itemRepository: ItemRepository): ViewModel(), MealsContract.ViewModel {

    override val mealState: MutableLiveData<MealState> = MutableLiveData()
    override val mealId: MutableLiveData<Long> = MutableLiveData()
    private val subscriptions = CompositeDisposable()

    override fun getMeals() {
        val subscription = mealRepository.getMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                mealState.value = MealState.Success(it)
            },
            {
                mealState.value = MealState.Error("Error happened while fetching data")
            }
        )
        subscriptions.add(subscription)
    }

    ///////////////////////////////////////////////////////////////////// INSERTING /////////////////////////////////////////////////////////////////////
    override fun insertMeal(mealWithItemsEntity: MealWithItemsEntity){
        val subscription = mealRepository.insertMeal(mealWithItemsEntity.mealEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                mealId.value = it
                mealWithItemsEntity.items.forEach { item -> item.mealId = it }
                insertItems(mealWithItemsEntity.items)
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun insertItems(items: List<ItemEntity>) {
        val subscription = itemRepository.insertItems(items).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Items inserted")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteAllMeals() {
        val subscription = mealRepository.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("All meals deleted")
                deleteAllItems()
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteAllItems() {
        val subscription = itemRepository.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("All items deleted")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteMeal(mealWithItemsEntity: MealWithItemsEntity) {
        val subscription = mealRepository.deleteMeal(mealWithItemsEntity.mealEntity, mealWithItemsEntity.items).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Items deleted")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun delete(meal: MealEntity){
        val subscription = mealRepository.delete(meal).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Meal deleted")
//                deleteItemsById(meal.id)
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteItemsById(id: Long) {
        val subscription = itemRepository.deleteById(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Items deleted")
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

    override fun updateMeal(meal: MealEntity) {
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

}