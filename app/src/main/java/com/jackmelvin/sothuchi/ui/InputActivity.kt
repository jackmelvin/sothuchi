package com.jackmelvin.sothuchi.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.databinding.ActivityInputBinding
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.model.Money
import com.jackmelvin.sothuchi.model.MoneyWithCategory
import com.jackmelvin.sothuchi.viewmodel.InputViewModel
import java.text.SimpleDateFormat
import java.util.*

class InputActivity : AppCompatActivity(), CategoryDialogFragment.OnCategorySelectedListener {
    private lateinit var binding: ActivityInputBinding
    private var chosenCategory: Category? = null
    private var chosenDate: Calendar = Calendar.getInstance()
    private val inputViewModel: InputViewModel by lazy {
        ViewModelProvider(this, InputViewModel.Factory(application as SoThuChiApplication))
            .get(InputViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initBackAction()
        initDatePicker()
        initCategorySelector()
        initCompleteAction()
    }

    private fun initDatePicker() {
        // Set the TextView to show the current date
        updateEditTextDate() //chosenDate is current date by default
        // Show the DatePicker dialog when the TextView is clicked
        binding.tvDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun updateEditTextDate() {
        // Set date to current date
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        binding.tvDate.text = dateFormat.format(chosenDate.time)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                chosenDate.set(year, month, dayOfMonth)
                updateEditTextDate()
            },
            chosenDate.get(Calendar.YEAR),
            chosenDate.get(Calendar.MONTH),
            chosenDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun initCompleteAction() {
        binding.tvComplete.setOnClickListener {
            val amount = binding.etAmount.text.toString().toLongOrNull() ?: 0L
            if (amount <= 0) {
                showAlertDialog("Verify", "Amount must be greater than 0")
                binding.etAmount.requestFocus()
                return@setOnClickListener
            }
            if (chosenCategory == null) {
                Toast.makeText(this@InputActivity, "You need to specify a category", Toast.LENGTH_SHORT).show()
                showCategorySelector()
                return@setOnClickListener
            }
            chosenCategory?.let {
                val money = Money(
                    chosenDate,
                    amount,
                    it.id,
                    Constants.USER_ID,
                    binding.etMemo.text.toString()
                )
                val moneyWithCategory = MoneyWithCategory(money, it.imageId, it.isIncome)
                inputViewModel.insertMoney(moneyWithCategory)
                startActivity(Intent(this@InputActivity, MainActivity::class.java))
            }
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this@InputActivity)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.ok, null)
            .show()
    }

    private fun showCategorySelector() {
        val bundle = Bundle()
        bundle.putBoolean(Constants.IS_INCOME, binding.rbIncome.isChecked)

        val dialogFragment = CategoryDialogFragment(this)
        dialogFragment.arguments = bundle
        dialogFragment.show(supportFragmentManager, "Category")
    }

    private fun initCategorySelector() {
        binding.tvCategory.setOnClickListener {
            showCategorySelector()
        }
    }

    override fun chooseCategory(category: Category) {
        binding.tvCategory.text = category.name
        binding.tvCategory.setTextColor(binding.etAmount.textColors)
        this.chosenCategory = category
    }

    private fun initBackAction() {
        binding.tvBack.setOnClickListener {
            val intent = Intent(this@InputActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}