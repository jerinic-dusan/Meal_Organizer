package app.meal_planner.modules

import app.meal_planner.data.datasources.local.MainDataBase
import app.meal_planner.data.repositories.ItemRepository
import app.meal_planner.data.repositories.ItemRepositoryImpl
import org.koin.dsl.module

val itemsModule = module {

    single<ItemRepository> { ItemRepositoryImpl(get()) }
    single { get<MainDataBase>().getItemsDao() }

}