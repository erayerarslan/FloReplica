package com.erayerarslan.floreplica.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentDetailProductBinding
import com.erayerarslan.floreplica.ui.home.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailProductListAdapter: DetailProductListAdapter
    private lateinit var productAdapter: ProductAdapter
    private val viewModel: DetailProductViewModel by viewModels()
    private val args by navArgs<DetailProductFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigationView()
        viewModel.getProduct(args.productId)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        productAdapter= ProductAdapter({ product ->

            product.id?.let {
                println(it)
            }
        },

            isDetailPage = true


        )

        detailProductListAdapter = DetailProductListAdapter(
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id ?: 1)
                }
               findNavController().navigate(R.id.action_detailProductFragment_self, bundle)


            }
        )

//        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
//        binding.detailRecyclerView.layoutManager = gridLayoutManager
//        binding.detailRecyclerView.adapter = detailProductListAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.detailRecyclerView.layoutManager = layoutManager
        binding.detailRecyclerView.adapter = detailProductListAdapter
        val layoutManager2 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.detailTopRecyclerview.layoutManager =layoutManager2
        binding.detailTopRecyclerview.adapter = productAdapter




//        val bundle: Bundle = Bundle()
//        bundle.putString("productId","1")
//        findNavController().navigate(
//            R.id.action_categoryProductListFragment_to_detailProductFragment,
//            bundle
//        )


        observeEvents()
    }

    private fun observeEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.errorMesssage.observe(viewLifecycleOwner) {
            binding.textViewErrorDetail.text = it
            binding.textViewErrorDetail.isVisible = true
        }
        viewModel.product.observe(viewLifecycleOwner) {
            productAdapter.updateProductDetail(it)
//            binding.textRate.text = "Rating: " + it.rating?.rate.toString()
//            binding.ratingBar.rating = it.rating?.rate.toString().toFloat()
//            binding.textViewDetailDescription.text = it.description
            binding.textViewDetailPrice.text = it.price.toString()

            binding.backButton.bringToFront()
        }
        viewModel.similarProducts.observe(viewLifecycleOwner) {
            detailProductListAdapter.updateProductList(it)

        }




    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}