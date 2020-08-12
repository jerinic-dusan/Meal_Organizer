package app.meal_planner.modules

import app.meal_planner.data.datasources.local.MainDataBase
import app.meal_planner.data.repositories.DailyReportRepository
import app.meal_planner.data.repositories.DailyReportRepositoryImpl
import app.meal_planner.presentation.viewmodel.DailyReportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dailyReportModule = module {
    viewModel { DailyReportViewModel(dailyReportRepository = get()) }
    single<DailyReportRepository> { DailyReportRepositoryImpl(dailyReportDao = get()) }
    single { get<MainDataBase>().getDailyReportDao() }
}