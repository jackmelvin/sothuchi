package com.jackmelvin.sothuchi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.databinding.ActivityInputBinding
import com.jackmelvin.sothuchi.dialog.CategoryDialogFragment
import com.jackmelvin.sothuchi.model.Category

private lateinit var binding: ActivityInputBinding
class InputActivity : AppCompatActivity() {
    companion object {
        val PAID_CATEGORY = "paidCategory"
        val INCOME_CATEGORY = "incomeCategory"
        val SELECTED_CATEGORY = "selectedCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initBackAction()
        initCategorySelector()

    }

    private fun initCategorySelector() {
        binding.tvCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(SELECTED_CATEGORY, if (binding.rbPaid.isChecked) PAID_CATEGORY else INCOME_CATEGORY)

            val dialogFragment = CategoryDialogFragment()
            dialogFragment.arguments = bundle
            dialogFragment.show(supportFragmentManager, "Category")
        }
    }

    private fun initBackAction() {
        binding.tvBack.setOnClickListener {
            val intent = Intent(this@InputActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}