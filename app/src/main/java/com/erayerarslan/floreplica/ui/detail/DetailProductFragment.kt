package com.erayerarslan.floreplica.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentDetailProductBinding
import com.erayerarslan.floreplica.util.DetailImage
import com.erayerarslan.floreplica.util.detailImage
import com.erayerarslan.floreplica.util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailProductViewModel by viewModels()
    private val args by navArgs<DetailProductFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentDetailProductBinding.inflate(inflater,container,false)

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigationView()
        viewModel.getProduct(args.productId)
        observeEvents()
    }

    private fun observeEvents(){
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible=it
        }
        viewModel.errorMesssage.observe(viewLifecycleOwner) {
            binding.textViewErrorDetail.text = it
            binding.textViewErrorDetail.isVisible = true
        }
        viewModel.product.observe(viewLifecycleOwner) {
            binding.imageViewProduct.detailImage(it.image)
            binding.textViewDetailTitle.text = it.title
            binding.textViewDetailPrice.text = it.price.toString()
            binding.textViewDetailDescription.text = it.description
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