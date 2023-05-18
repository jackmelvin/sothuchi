package com.jackmelvin.sothuchi.dialog

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jackmelvin.sothuchi.R
import com.jackmelvin.sothuchi.activity.InputActivity
import com.jackmelvin.sothuchi.adapter.CategoryAdapter
import com.jackmelvin.sothuchi.database.AppDatabase
import com.jackmelvin.sothuchi.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryDialogFragment(val onCategorySelectedListener: OnCategorySelectedListener) : DialogFragment(), CategoryAdapter.OnItemClickListener {
    val categoryList = mutableListOf<Category>()
    var categoryAdapter: CategoryAdapter? = null
    var isIncome: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        categoryAdapter = CategoryAdapter(this, categoryList)

        isIncome = arguments?.getBoolean(InputActivity.IS_INCOME)
        val db = context?.let { AppDatabase.getInstance(it) }
        val categoryDao = db?.categoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            categoryDao?.let {cd ->
                isIncome?.let {ii ->
                    categoryList.addAll(cd.findAllByType(ii))
                    categoryAdapter?.notifyDataSetChanged()
                }
            }
        }
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
        val rvCategory = view.findViewById<RecyclerView>(R.id.rvCategory)
        // Create a new category adapter suitable with selected radio button
        rvCategory.adapter = CategoryAdapter(this, categoryList)

        // Use a GridLayoutManager for grid
        val columns = 3
        rvCategory.layoutManager = GridLayoutManager(context, columns)
    }

    override fun onItemClick(position: Int) {
        onCategorySelectedListener.chooseCategory(categoryList[position])
        dismiss()
    }

    interface OnCategorySelectedListener {
        fun chooseCategory(category: Category)
    }
}