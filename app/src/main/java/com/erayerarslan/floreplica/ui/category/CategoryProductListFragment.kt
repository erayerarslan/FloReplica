package com.erayerarslan.floreplica.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.erayerarslan.floreplica.databinding.FragmentCategoryProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProductListFragment : Fragment() {

    private var _binding: FragmentCategoryProductListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryProductListViewModel>()
    private lateinit var categoryProductAdapter: CategoryProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Kategoriyi al ve ürünleri listelemek için ViewModel'e ilet
        val categoryName = arguments?.getString("category")
        categoryName?.let {
            viewModel.getProductListForCategory(it)
        }
    }

    private fun setupRecyclerView() {
        categoryProductAdapter = CategoryProductAdapter(emptyList()) { product ->
            //deatay sayfasına geçişte

        }

        binding.categoryProductListRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryProductAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.productList.observe(viewLifecycleOwner) { products ->
            categoryProductAdapter.updateProducts(products)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            binding.progressBar.isVisible = loading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.textViewCategoryError.isVisible = error != null
            binding.textViewCategoryError.text = error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
