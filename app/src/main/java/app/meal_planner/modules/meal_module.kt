package app.meal_planner.modules

import app.meal_planner.data.datasources.local.MainDataBase
import app.meal_planner.data.repositories.MealRepository
import app.meal_planner.data.repositories.MealRepositoryImpl
import app.meal_planner.presentation.viewmodel.MealsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mealModule = module {
    viewModel { MealsViewModel(mealRepository = get(), itemRepository = get()) }
    single<MealRepository> {MealRepositoryImpl(mealsDataSource = get())}
    single { get<MainDataBase>().getMealsDao() }
}