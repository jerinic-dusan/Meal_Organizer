package app.meal_planner.modules

import app.meal_planner.data.datasources.local.SharedPrefDataSource
import app.meal_planner.data.datasources.local.SharedPrefInterface
import app.meal_planner.data.repositories.UserDataRepository
import app.meal_planner.data.repositories.UserDataRepositoryImpl
import app.meal_planner.presentation.viewmodel.UserDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    viewModel { UserDataViewModel(userDataRepository =  get()) }
    single<UserDataRepository> {UserDataRepositoryImpl(sharedPrefInterface = get())}
    single<SharedPrefInterface> {SharedPrefDataSource(sharedPreferences = get())}

}