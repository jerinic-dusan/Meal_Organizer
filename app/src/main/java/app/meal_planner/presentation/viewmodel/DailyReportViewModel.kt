package app.meal_planner.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.meal_planner.data.models.DailyReportEntity
import app.meal_planner.data.repositories.DailyReportRepository
import app.meal_planner.presentation.contract.DailyReportContract
import app.meal_planner.presentation.view.states.DailyReportState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DailyReportViewModel(private val dailyReportRepository: DailyReportRepository): ViewModel(), DailyReportContract.ViewModel  {

    override val lastSevenDays: MutableLiveData<DailyReportState> = MutableLiveData()
    override val lastThirtyDays: MutableLiveData<DailyReportState> = MutableLiveData()
    private val subscriptions = CompositeDisposable()

    override fun getTodaysReport(dailyReport: DailyReportEntity) {
        Timber.e("BEFORE ${dailyReport.date}")
        val subscription = dailyReportRepository.getTodaysReport().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("$it")
                if (it.id == (-666).toLong()){
                    saveDailyReport(dailyReport)
                }else{
                    Timber.e("AFTER ${it.date} ${dailyReport.date}")
                    if (it.date == dailyReport.date){
                        Timber.e("update")
                        updateTodaysReport(DailyReportEntity(it.id, dailyReport.kcal, dailyReport.carbs, dailyReport.protein, dailyReport.fat, dailyReport.date))
                    }else{
                        Timber.e("saved")
                        saveDailyReport(dailyReport)
                    }
                }
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun updateTodaysReport(dailyReport: DailyReportEntity) {
        val subscription = dailyReportRepository.updateTodaysReport(dailyReport).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Updated $dailyReport")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun saveDailyReport(dailyReport: DailyReportEntity) {
        val subscription = dailyReportRepository.saveDailyReport(dailyReport).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Inserted $dailyReport")
            },
            {
                Timber.e(it)
            }
        )
        subscriptions.add(subscription)
    }

    override fun getHistoryOfDailyReports() {
        getLastSevenDays()
        getLastThirtyDays()
    }

    override fun getLastSevenDays() {
        val subscription = dailyReportRepository.getLastSevenDays().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                lastSevenDays.value = DailyReportState.Success(it)
                Timber.e("Data fetched: $it")
            },
            {
                lastSevenDays.value = DailyReportState.Error("Error happened while fetching data")
            }
        )
        subscriptions.add(subscription)
    }

    override fun getLastThirtyDays() {
        val subscription = dailyReportRepository.getLastThirtyDays().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                lastThirtyDays.value = DailyReportState.Success(it)
                Timber.e("Data fetched: $it")
            },
            {
                lastThirtyDays.value = DailyReportState.Error("Error happened while fetching data")
            }
        )
        subscriptions.add(subscription)
    }

    override fun deleteOlderReports() {
        val subscription = dailyReportRepository.deleteOlderReports().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Deleted older reports")
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