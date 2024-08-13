package com.erayerarslan.floreplica.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.databinding.FragmentProfileBinding
import com.erayerarslan.floreplica.databinding.FragmentProfileHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileHomeFragment : Fragment() {
    private var _binding: FragmentProfileHomeBinding? = null
    private val binding get() = _binding!!
    private val profileHomeViewModel : ProfileHomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnProfileHome.setOnClickListener{
            findNavController().navigate(R.id.action_profileHomeFragment_to_profileFragment)
        }

        lifecycleScope.launch {
            profileHomeViewModel.getUser()
            profileHomeViewModel.profileHomeState.collect { response ->
                when (response) {
                    is Response.Loading -> {
                        // Loading işlemleri
                        binding.progressBar.isVisible = true

                    }
                    is Response.Success -> {
                        // Kullanıcı bilgilerini göster
                        binding.progressBar.isVisible = false
                        binding.textViewFirstName.text = response.data.firstName
                        binding.textViewLastName.text = response.data.lastName
                        binding.textViewEmail.text = response.data.email
                    }
                    is Response.Error -> {
                        // Hata mesajını göster
                    }
                }
            }
        }
    }
}


