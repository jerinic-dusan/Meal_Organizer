package app.meal_planner.data.repositories

import app.meal_planner.data.datasources.local.DailyReportDao
import app.meal_planner.data.models.DailyReport
import app.meal_planner.data.models.DailyReportEntity
import io.reactivex.Completable
import io.reactivex.Observable

class DailyReportRepositoryImpl(private val dailyReportDao: DailyReportDao): DailyReportRepository {

    override fun saveDailyReport(dailyReportEntity: DailyReportEntity): Completable {
        return dailyReportDao.saveDailyReport(dailyReportEntity)
    }

    override fun getLastSevenDays(): Observable<List<DailyReport>> {
        return Observable.fromCallable {
            dailyReportDao.getLastSevenDays().map {
                DailyReport(it.id, it.kcal, it.carbs, it.protein, it.fat, it.date)
            }
        }
    }

    override fun getLastThirtyDays(): Observable<List<DailyReport>> {
        return Observable.fromCallable {
            dailyReportDao.getThirtySevenDays().map {
                DailyReport(it.id, it.kcal, it.carbs, it.protein, it.fat, it.date)
            }
        }
    }

    override fun deleteOlderReports(): Completable {
        return dailyReportDao.deleteOlderReports()
    }

    override fun getTodaysReport(): Observable<DailyReportEntity> {
        return Observable.fromCallable {
            val list = dailyReportDao.getTodaysReport()
            if (list.isEmpty()){
                //If you want to remove null from the language then you get this
                DailyReportEntity(-666,0,0,0,0)
            }else{
                list.first()
            }
        }
    }

    override fun updateTodaysReport(dailyReport: DailyReportEntity): Completable {
        return dailyReportDao.updateTodaysReport(dailyReport)
    }
}