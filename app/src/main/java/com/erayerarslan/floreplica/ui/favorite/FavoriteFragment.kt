package com.erayerarslan.floreplica.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentFavoriteBinding
import com.erayerarslan.floreplica.repository.UserRepository
import com.erayerarslan.floreplica.ui.home.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        println("emptyfavorite "+viewModel.emptyFavorite.value)
        println("user "+viewModel.guestUser.value)

    }

    private fun observeEvents() {

        lifecycleScope.launch {
            viewModel.favoriteProducts.observe(viewLifecycleOwner) { productList ->
                    productAdapter.updateProductList(productList)
                    swipeRefreshLayout.isRefreshing = false
            }
            viewModel.emptyFavorite.observe(viewLifecycleOwner){
                if(it) {

                        binding.textViewEmptyRecyclerview.visibility = View.VISIBLE
                        binding.buttonEmptyRecyclerview.visibility = View.VISIBLE
                        binding.buttonEmptyRecyclerview.setOnClickListener {
                            findNavController().navigate(R.id.action_favoriteFragment_to_homeFragment)
                            (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.homeFragment
                        }
                }
            }
            lifecycleScope.launch {
                viewModel.guestUser.observe(viewLifecycleOwner){
                    if(it){

                            binding.textViewEmptyRecyclerview.text = "Favori ürünlerinizi görebilmek için giriş yapmalısınız"
                            binding.buttonEmptyRecyclerview.text ="Giriş Yap"
                            binding.textViewEmptyRecyclerview.visibility = View.VISIBLE
                            binding.buttonEmptyRecyclerview.visibility = View.VISIBLE
                            binding.buttonEmptyRecyclerview.setOnClickListener {
                                findNavController().navigate(R.id.action_favoriteFragment_to_signInFragment)
                                (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.signInFragment

                        }
                    }
                }
            }

        }
    }
}