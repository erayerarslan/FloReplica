package com.erayerarslan.floreplica.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.showBottomNavigationView()




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductAdapter { product ->

            val action =HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(product.id ?: 0)
            findNavController().navigate(action)

        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.homeRecyclerView.layoutManager = gridLayoutManager
        binding.homeRecyclerView.adapter = productAdapter




        observeEvents()

        viewModel.getProductList()

    }

    private fun observeEvents() {

        viewModel.errorMessage.observe(viewLifecycleOwner) { error->
            binding.textViewHomeError.text = error
            binding.textViewHomeError.isVisible = true
        }
        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            binding.progressBar.isVisible = loading
        }

        viewModel.productList.observe(viewLifecycleOwner) { productList ->
            if (productList != null) {
                productAdapter.updateProductList(productList)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}