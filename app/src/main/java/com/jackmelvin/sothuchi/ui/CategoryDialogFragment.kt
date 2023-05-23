package com.jackmelvin.sothuchi.ui

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.jackmelvin.sothuchi.SoThuChiApplication
import com.jackmelvin.sothuchi.adapter.CategoryListAdapter
import com.jackmelvin.sothuchi.databinding.DialogCategorySelectorBinding
import com.jackmelvin.sothuchi.helper.Constants
import com.jackmelvin.sothuchi.model.Category
import com.jackmelvin.sothuchi.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

class CategoryDialogFragment(private val onCategorySelectedListener: OnCategorySelectedListener) : DialogFragment() {
    private var _binding: DialogCategorySelectorBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isIncome: Boolean = false

    private val categoryViewModel: CategoryViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after activity being created"
        }
        ViewModelProvider(this, CategoryViewModel.Factory(activity.application as SoThuChiApplication))
            .get(CategoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean(Constants.IS_INCOME)?.let {
            isIncome = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCategorySelectorBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Use a GridLayoutManager for 3-column-grid
        val columns = 3
        binding.rvCategory.layoutManager = GridLayoutManager(context, columns)
        val categoryListAdapter = CategoryListAdapter {
                onCategorySelectedListener.chooseCategory(it)
                dismiss()
        }
        binding.rvCategory.adapter = categoryListAdapter
        lifecycleScope.launch {
            categoryViewModel.getCategories(isIncome).collect {
                categoryListAdapter.submitList(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (screenHeight * 4 / 5))
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    interface OnCategorySelectedListener {
        fun chooseCategory(category: Category)
    }
}