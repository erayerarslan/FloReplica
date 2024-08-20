package com.erayerarslan.floreplica.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentCategoryBinding
import com.erayerarslan.floreplica.databinding.FragmentFavoriteBinding
import com.erayerarslan.floreplica.repository.UserRepository
import com.erayerarslan.floreplica.ui.home.HomeFragmentDirections
import com.erayerarslan.floreplica.ui.home.HomeViewModel
import com.erayerarslan.floreplica.ui.home.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    @Inject
    lateinit var repository: UserRepository
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


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

            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailProductFragment(
                product.id ?: 0
            )
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
        swipeRefreshLayout = binding.swipeRefreshLayout

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoriteRecyclerView.layoutManager = gridLayoutManager
        binding.favoriteRecyclerView.adapter = productAdapter

        viewModel.getFavoriteProductList()
        observeEvents()

        swipeRefreshLayout.setOnRefreshListener {
            // Veri yenileme işlemini başlat
            viewModel.getFavoriteProductList()


        }


    }

    private fun observeEvents() {

        lifecycleScope.launch {
            viewModel.favoriteProducts.observe(viewLifecycleOwner) { productList ->

                    productAdapter.updateProductList(productList)
                    swipeRefreshLayout.isRefreshing = false


            }

        }
    }
}