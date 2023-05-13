package com.jackmelvin.sothuchi.dialog

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.activity.InputActivity
import com.jackmelvin.sothuchi.adapter.CategoryAdapter
import com.jackmelvin.sothuchi.model.Category

class CategoryDialogFragment(val onCategorySelectedListener: OnCategorySelectedListener) : DialogFragment(), CategoryAdapter.OnItemClickListener {
    val paidCategory = mutableListOf<Category>()
    val incomeCategory = mutableListOf<Category>()
    var paidCategoryAdapter: CategoryAdapter? = null
    var incomeCategoryAdapter: CategoryAdapter? = null
    var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedCategory = arguments?.getString(InputActivity.SELECTED_CATEGORY)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_selector, parent, false)

        initCategoryGrid(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (screenHeight * 4 / 5))
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initCategoryGrid(view: View) {
        paidCategory.add(Category(R.drawable.cam,"Cam"))
        paidCategory.add(Category(R.drawable.duahau,"Dưa hấu"))
        paidCategory.add(Category(R.drawable.sauchung,"Sầu riêng"))
        paidCategory.add(Category(R.drawable.cam,"Cam"))
        paidCategory.add(Category(R.drawable.duahau,"Dưa hấu"))
        paidCategory.add(Category(R.drawable.sauchung,"Sầu riêng"))
        paidCategory.add(Category(R.drawable.cam,"Cam"))
        paidCategory.add(Category(R.drawable.duahau,"Dưa hấu"))
        paidCategory.add(Category(R.drawable.sauchung,"Sầu riêng"))

        incomeCategory.add(Category(R.drawable.xoai,"Xoài"))
        incomeCategory.add(Category(R.drawable.tao,"Táo"))
        incomeCategory.add(Category(R.drawable.xoai,"Xoài"))
        incomeCategory.add(Category(R.drawable.tao,"Táo"))
        incomeCategory.add(Category(R.drawable.xoai,"Xoài"))
        incomeCategory.add(Category(R.drawable.tao,"Táo"))

        val rvCategory = view.findViewById<RecyclerView>(R.id.rvCategory)
        // Create a new category adapter suitable with selected radio button
        when(selectedCategory) {
            InputActivity.PAID_CATEGORY -> rvCategory.adapter = paidCategoryAdapter ?: CategoryAdapter(this, paidCategory)
            InputActivity.INCOME_CATEGORY -> rvCategory.adapter = incomeCategoryAdapter ?: CategoryAdapter(this, incomeCategory)
        }
        // Use a GridLayoutManager for grid
        val columns = 3
        rvCategory.layoutManager = GridLayoutManager(context, columns)
    }

    override fun onItemClick(position: Int) {
        when(selectedCategory) {
            InputActivity.PAID_CATEGORY -> onCategorySelectedListener.chooseCategory(paidCategory[position])
            InputActivity.INCOME_CATEGORY -> onCategorySelectedListener.chooseCategory(incomeCategory[position])
        }
        dismiss()
    }

    interface OnCategorySelectedListener {
        fun chooseCategory(category: Category)
    }
}