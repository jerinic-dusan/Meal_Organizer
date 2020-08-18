package app.meal_planner.presentation.viewmodel

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days

class DailyReportViewModel(private val dailyReportRepository: DailyReportRepository): ViewModel(), DailyReportContract.ViewModel  {

    override val lastSevenDays: MutableLiveData<DailyReportState> = MutableLiveData()
    override val lastThirtyDays: MutableLiveData<DailyReportState> = MutableLiveData()
    private val subscriptions = CompositeDisposable()

    @SuppressLint("SimpleDateFormat")
    override fun getTodaysReport(dailyReport: DailyReportEntity) {
        val subscription = dailyReportRepository.getTodaysReport().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                if (it.id == (-1).toLong()){
                    saveDailyReport(dailyReport)
                }else{
                    val dateFormatter = SimpleDateFormat("EEEMMMyy")
                    if (dateFormatter.format(it.date).compareTo(dateFormatter.format(dailyReport.date)) == 0){
                        updateTodaysReport(DailyReportEntity(it.id, dailyReport.kcal, dailyReport.carbs, dailyReport.protein, dailyReport.fat, dailyReport.date))
                    }else{
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

    override fun deleteAll() {
        val subscription = dailyReportRepository.deleteAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Timber.e("Deleted all reports")
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