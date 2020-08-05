package app.meal_planner.presentation.view.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import app.meal_planner.R
import app.meal_planner.data.models.*
import app.meal_planner.presentation.contract.MealsContract
import app.meal_planner.presentation.view.fragments.TodaysMealsFragment
import app.meal_planner.presentation.viewmodel.MealsViewModel
import kotlinx.android.synthetic.main.activity_add_meal.*
import kotlinx.android.synthetic.main.add_item_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddMealActivity : AppCompatActivity(R.layout.activity_add_meal) {

    private lateinit var calledFrom: String
    private var meal: MealWithItems? = null
    private val mealsViewModel: MealsContract.ViewModel by viewModel<MealsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calledFrom = intent.getStringExtra(TodaysMealsFragment.MESSAGE_ADD_MEAL) as String
        meal = intent.getParcelableExtra(TodaysMealsFragment.MESSAGE_EDIT_MEAL)
        init()
    }

    private fun init() {
        initLayout()
        initFields()
        initListeners()
    }

    private fun initLayout() {

        when(calledFrom){
            "Today_Add" -> {

            }
            "Today_Edit" -> {
                populateFields(meal!!)
            }
            "Existing_Add" -> {
                checkbox.visibility = View.INVISIBLE
                val params = items_layout.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToTop = cancel_meal.id
                items_layout.requestLayout()
            }
            "Existing_Edit" -> {
                checkbox.visibility = View.INVISIBLE
                val params = items_layout.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToTop = cancel_meal.id
                items_layout.requestLayout()
                populateFields(meal!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFields() {
        calories.text = "0kcal"
        carbs.text = "0g"
        protein.text = "0g"
        fat.text = "0g"
    }

    @SuppressLint("SetTextI18n")
    fun onDeleteField(view: View){
        val v = view.parent as View

        val carbsOld = if(v.ingredient_carbs.text.toString() == ""){ 0 }else{ v.ingredient_carbs.text.toString().toInt() }
        val proteinOld = if(v.ingredient_protein.text.toString() == ""){ 0 }else{ v.ingredient_protein.text.toString().toInt() }
        val fatOld = if(v.ingredient_fat.text.toString() == ""){ 0 }else{ v.ingredient_fat.text.toString().toInt() }

        val kcalNew = calorieCount() - (carbsOld * 4) - (proteinOld * 4) - (fatOld * 9)
        val carbsNew = carbsCount() - carbsOld
        val proteinNew = proteinCount() - proteinOld
        val fatNew = fat.text.toString().filter {it.isDigit()}.toInt() - fatOld

        calories.text = kcalNew.toString() + "kcal"
        carbs.text = carbsNew.toString() + "g"
        protein.text = proteinNew.toString() + "g"
        fat.text = fatNew.toString() + "g"

        parent_linear_layout.removeView(v)
        window.decorView.clearFocus()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    fun onAddField(view: View) {
        parent_linear_layout.removeView(view.parent as View)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.add_item_card, null)
        parent_linear_layout!!.addView(rowView, parent_linear_layout!!.childCount)

        val inflater1 = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView1: View = inflater1.inflate(R.layout.separator_card, null)
        parent_linear_layout!!.addView(rowView1, parent_linear_layout!!.childCount)

        //TODO CREATE A FUNCTION THAT DOES THIS PROGRAMMATICALLY
        rowView.ingredient_carbs.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val kcalNew: Int
                val carbsNew: Int
                val carbsOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() - (carbsOld * 4)
                carbsNew = carbsCount() - carbsOld

                calories.text = kcalNew.toString() + "kcal"
                carbs.text = carbsNew.toString() + "g"
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kcalNew: Int
                val carbsNew: Int
                val carbsOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() + (carbsOld * 4)
                carbsNew = carbsCount() + carbsOld

                calories.text = kcalNew.toString() + "kcal"
                carbs.text = carbsNew.toString() + "g"
            }
        })

        rowView.ingredient_protein.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val kcalNew: Int
                val proteinNew: Int
                val proteinOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() - (proteinOld * 4)
                proteinNew = proteinCount() - proteinOld

                calories.text = kcalNew.toString() + "kcal"
                protein.text = proteinNew.toString() + "g"
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kcalNew: Int
                val proteinNew: Int
                val proteinOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() + (proteinOld * 4)
                proteinNew = proteinCount() + proteinOld

                calories.text = kcalNew.toString() + "kcal"
                protein.text = proteinNew.toString() + "g"
            }
        })

        rowView.ingredient_fat.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val kcalNew: Int
                val fatNew: Int
                val fatOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() - (fatOld * 9)
                fatNew = fatCount() - fatOld

                calories.text = kcalNew.toString() + "kcal"
                fat.text = fatNew.toString() + "g"
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kcalNew: Int
                val fatNew: Int
                val fatOld = if(s.toString() == ""){ 0 }else{ s.toString().toInt() }

                kcalNew = calorieCount() + (fatOld * 9)
                fatNew = fatCount() + fatOld

                calories.text = kcalNew.toString() + "kcal"
                fat.text = fatNew.toString() + "g"
            }
        })
    }

    private fun initListeners() {
        cancel_meal.setOnClickListener {
            finish()
        }
        add_meal.setOnClickListener {
            val title = title_add_meal.text.trim()
            if(title.isNotEmpty() && calorieCount() > 0){
                val list = getItems()
                val existing = checkbox.isChecked
                if (list.isEmpty()){
                    Toast.makeText(this, "Please give this meal some ingredients.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                when(calledFrom){
                    "Today_Add" -> mealsViewModel.insertMeal(MealWithItemsEntity(MealEntity(0, title.toString(), calorieCount(), carbsCount(), proteinCount(), fatCount(), true, existing), list))
                    "Today_Edit" -> {}
                    "Existing_Add" -> mealsViewModel.insertMeal(MealWithItemsEntity(MealEntity(0, title.toString(), calorieCount(), carbsCount(), proteinCount(), fatCount(), false, existing = true), list))
                    "Existing_Edit" -> {}
                }
                finish()
            }else{
                if(title.isEmpty()){
                    Toast.makeText(this, "Please give this meal a title.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Please give this meal some ingredients.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        total_drop_down.setOnClickListener {
            layout_not_dropped.visibility = View.INVISIBLE
            layout_dropped.visibility = View.VISIBLE

            val params = items_layout.layoutParams as ConstraintLayout.LayoutParams
            params.topToBottom = layout_dropped.id
            items_layout.requestLayout()
        }
        total_drop_up.setOnClickListener {
            layout_dropped.visibility = View.INVISIBLE
            layout_not_dropped.visibility = View.VISIBLE

            val params = items_layout.layoutParams as ConstraintLayout.LayoutParams
            params.topToBottom = layout_not_dropped.id
            items_layout.requestLayout()
        }
    }

    override fun onBackPressed() {
        if (calorieCount() > 0){
            val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
            builder.setTitle("Exit warning!").setMessage("Exiting this screen without saving will result in a loss of a new meal.")
                .setCancelable(false).setIcon(R.drawable.ic_warning).setPositiveButton("Exit"){_, _ -> finish()}.setNegativeButton("Cancel"){dialog, _ -> dialog.cancel()}
            val dialog: AlertDialog? = builder.create()
            dialog!!.show()
        }else{
            super.onBackPressed()
        }

    }

    private fun calorieCount(): Int{
        return calories.text.toString().filter {it.isDigit()}.toInt()
    }

    private fun carbsCount(): Int{
        return carbs.text.toString().filter {it.isDigit()}.toInt()
    }

    private fun proteinCount(): Int{
        return protein.text.toString().filter {it.isDigit()}.toInt()
    }

    private fun fatCount(): Int{
        return fat.text.toString().filter {it.isDigit()}.toInt()
    }


    private fun getItems(): List<ItemEntity>{
        val mutableList = mutableListOf<ItemEntity>()
        parent_linear_layout.children.forEach {
            if(it.ingredient_title != null && it.ingredient_carbs != null && it.ingredient_protein != null && it.ingredient_fat != null){
                val titleText = it.ingredient_title.text.toString()
                val carbsText = it.ingredient_carbs.text.toString()
                val proteinText = it.ingredient_protein.text.toString()
                val fatText = it.ingredient_fat.text.toString()
                if(titleText == ""){
                    Toast.makeText(this, "One of the ingredients doesn't have a title!", Toast.LENGTH_SHORT).show()
                    return listOf()
                }else{
                    val carbs = if(carbsText == ""){ 0 }else{ carbsText.toInt() }
                    val protein = if(proteinText == ""){ 0 }else{ proteinText.toInt() }
                    val fat = if(fatText == ""){ 0 }else{ fatText.toInt() }
                    mutableList.add(ItemEntity(0, 0, titleText,carbs, protein, fat))
                }
            }
        }
        return mutableList.toList()
    }

    @SuppressLint("InflateParams")
    private fun populateFields(mealWithItems: MealWithItems) {
        title_add_meal.setText(mealWithItems.meal.name)
        parent_linear_layout.removeView(parent_linear_layout.getChildAt(parent_linear_layout.childCount-1))
        mealWithItems.items.forEach {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.add_item_card, null)
            parent_linear_layout!!.addView(rowView, parent_linear_layout!!.childCount)
            rowView.ingredient_title.setText(it.name)
            rowView.ingredient_carbs.setText(it.carbs.toString())
            rowView.ingredient_protein.setText(it.protein.toString())
            rowView.ingredient_fat.setText(it.fat.toString())
        }
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.separator_card, null)
        parent_linear_layout!!.addView(rowView, parent_linear_layout!!.childCount)
    }
}
