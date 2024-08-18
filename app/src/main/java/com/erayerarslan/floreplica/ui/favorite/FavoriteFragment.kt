package com.erayerarslan.floreplica.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentCategoryBinding
import com.erayerarslan.floreplica.databinding.FragmentFavoriteBinding
import com.erayerarslan.floreplica.repository.UserRepository
import com.erayerarslan.floreplica.ui.home.HomeFragmentDirections
import com.erayerarslan.floreplica.ui.home.HomeViewModel
import com.erayerarslan.floreplica.ui.home.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    @Inject
    lateinit var repository: UserRepository
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductAdapter({ product ->

            val action =FavoriteFragmentDirections.actionFavoriteFragmentToDetailProductFragment(product.id ?: 0)
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

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoriteRecyclerView.layoutManager = gridLayoutManager
        binding.favoriteRecyclerView.adapter = productAdapter

        viewModel.getFavoriteProductList()
        observeEvents()



    }
    private fun observeEvents() {


        viewModel.favoriteProducts.observe(viewLifecycleOwner) { productList ->
            if (productList != null) {
                productAdapter.updateProductList(productList)
            }

        }
    }
}