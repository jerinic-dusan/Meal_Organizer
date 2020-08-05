package app.meal_planner.modules

import app.meal_planner.data.datasources.local.KcalDataSource
import app.meal_planner.data.datasources.local.KcalInterface
import app.meal_planner.data.repositories.KcalDataRepository
import app.meal_planner.data.repositories.KcalDataRepositoryImpl
import app.meal_planner.presentation.viewmodel.KcalDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val kcalModule = module {

    viewModel { KcalDataViewModel(kcalDataRepository = get()) }
    single<KcalDataRepository> {KcalDataRepositoryImpl(kcalInterface = get())}
    single<KcalInterface> {KcalDataSource()}

}