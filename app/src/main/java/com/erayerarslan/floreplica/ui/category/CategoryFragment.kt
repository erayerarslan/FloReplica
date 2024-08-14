package com.erayerarslan.floreplica.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentCategoryBinding
import com.erayerarslan.floreplica.databinding.FragmentHomeBinding
import com.erayerarslan.floreplica.databinding.FragmentSignInBinding
import com.erayerarslan.floreplica.ui.home.HomeViewModel
import com.erayerarslan.floreplica.ui.home.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private val viewModel by viewModels<CategoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        categoryAdapter = CategoryAdapter(emptyList())
        binding.categoryRecyclerView.adapter = categoryAdapter

        viewModel.getCategoryList()
        observeEvents()

    }
    private fun observeEvents() {

        viewModel.errorMessage.observe(viewLifecycleOwner) { error->
            binding.textViewHomeError.text = error
            binding.textViewHomeError.isVisible = true
        }
        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            binding.progressBar.isVisible = loading
        }

        viewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories)
            binding
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}