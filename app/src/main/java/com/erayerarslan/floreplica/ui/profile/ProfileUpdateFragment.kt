package com.erayerarslan.floreplica.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.databinding.FragmentProfileBinding
import com.erayerarslan.floreplica.model.User
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.erayerarslan.floreplica.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileUpdateFragment : Fragment() {
    @Inject
    lateinit var repository: UserRepository
    @Inject
    lateinit var auth: AuthenticationRepository

    private lateinit var storageReference: StorageReference
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveProfile.setOnClickListener {
            lifecycleScope.launch {

                val email= auth.userEmail()
                val firstName=binding.editTextName.text.toString()
                val lastName=binding.editTextLastName.text.toString()
                val user= User(firstName,lastName,email)
                repository.updateUserData(user).collect { response ->
                    // Handle the response
                    when(response){
                        is Response.Loading -> {
                            // Loading işlemleri
                            binding.progressBarProfileUpdate.visibility = View.VISIBLE
                        }
                        is Response.Success -> {

                            binding.progressBarProfileUpdate.visibility = View.GONE
                            // Kullanıcı bilgilerini göster
                            findNavController().popBackStack()
                        }
                        is Response.Error -> {
                            Log.e("ProfileUpdateFragment", "Error: ${response.message}")
                        }
                    }
                }
            }
        }
    }



}