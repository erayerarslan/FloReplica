package com.erayerarslan.floreplica.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.databinding.FragmentProfileBinding
import com.erayerarslan.floreplica.databinding.FragmentProfileHomeBinding
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileHomeFragment : Fragment() {
    private var _binding: FragmentProfileHomeBinding? = null
    private val binding get() = _binding!!
    private val profileHomeViewModel : ProfileHomeViewModel by viewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


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

        if(auth.currentUser?.isAnonymous == true){
            binding.textViewLogOut.text = "Login"
            binding.imageViewLogOut.setImageResource(R.drawable.ic_login)
            binding.logOut.setOnClickListener {
                binding.progressBar.isVisible = true
                findNavController().navigate(R.id.action_profileHomeFragment_to_signInFragment)
                binding.progressBar.isVisible = false
            }
        }
        else{
            binding.logOut.setOnClickListener {
                binding.progressBar.isVisible = true
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_profileHomeFragment_to_signInFragment)
                binding.progressBar.isVisible = false
            }
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
                        binding.textViewName.text = response.data.firstName + " " + response.data.lastName
                        binding.textViewEmail.text = response.data.email
                        if(binding.textViewName.text == "null null"){
                            binding.textViewName.text = "Misafir Kullanıcı"
                        }
                        if(auth.currentUser?.isAnonymous == true){
                            binding.myAccount.visibility = View.GONE
                        }
                        binding.myAccount.setOnClickListener{
                            if (response.data.email == null || response.data.email == ""){
                                Toast.makeText(requireContext(), "Lütfen kullanıcı girişi yapınız", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_profileHomeFragment_to_signInFragment)
                            }
                            else{
                                findNavController().navigate(R.id.action_profileHomeFragment_to_profileFragment)
                            }
                        }
                    }
                    is Response.Error -> {
                        // Hata mesajını göster
                        Log.e("ProfileHomeFragment", "Error: ${response.message}")
                    }
                }
            }
        }
    }
}


