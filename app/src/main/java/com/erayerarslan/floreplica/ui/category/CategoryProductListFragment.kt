package com.erayerarslan.floreplica.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.databinding.FragmentCategoryProductListBinding
import com.erayerarslan.floreplica.ui.home.HomeFragmentDirections
import com.erayerarslan.floreplica.ui.home.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProductListFragment : Fragment() {

    private var _binding: FragmentCategoryProductListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryProductListViewModel>()
    private lateinit var productAdapter: ProductAdapter

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

        val categoryName = arguments?.getString("category")
        categoryName?.let {
            viewModel.getProductListForCategory(it)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter({ product ->

            val action =
                CategoryProductListFragmentDirections.actionCategoryProductListFragmentToDetailProductFragment(product.id ?: 0)
            findNavController().navigate(action)

        },
            { product, isFavorite ->
                if (isFavorite) {
                    println("favorilere eklendi")
                } else {
                    println("favoriden çıkarıldı")
                }
            }
        )

        binding.categoryProductListRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.productList.observe(viewLifecycleOwner) { products ->
            productAdapter.updateProductList(products)
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
