package com.erayerarslan.floreplica.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.R
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
    private val profileUpdateViewModel : ProfileUpdateViewModel by viewModels()



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
        val maleTextView = binding.textMale
        val femaleTextView = binding.textFemale

        val sharedPreferences = requireActivity().getSharedPreferences("GenderPref", Context.MODE_PRIVATE)

        // Daha önce kaydedilen veriyi yükleyin
        var isMaleSelected = sharedPreferences.getBoolean("isMaleSelected", true)
        updateSelection(isMaleSelected)

        maleTextView.setOnClickListener {
            saveGenderSelection(true, sharedPreferences)
            isMaleSelected=true
        }

        femaleTextView.setOnClickListener {
            saveGenderSelection(false, sharedPreferences)
            isMaleSelected=false
        }


        binding.btnSaveProfile.setOnClickListener {
            lifecycleScope.launch {
                updateSelection(isMaleSelected)
                val email= auth.userEmail()
                val firstName=binding.editTextName.text.toString()
                val lastName=binding.editTextLastName.text.toString()
                val gender =isMaleSelected.toString()
                val user= User(firstName,lastName,email,gender)
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

    private fun saveGenderSelection(isMaleSelected: Boolean, sharedPreferences: SharedPreferences) {
        // Seçimi kaydedin
        sharedPreferences.edit().putBoolean("isMaleSelected", isMaleSelected).apply()
        println("isMaleSelected: $isMaleSelected")
        // UI'yı güncelleyin
        updateSelection(isMaleSelected)
    }
    fun updateSelection(isMaleSelected: Boolean) {
        val maleTextView = binding.textMale
        val femaleTextView = binding.textFemale
        if (isMaleSelected) {
            maleTextView.setBackgroundResource(R.drawable.gender_selected)
            femaleTextView.setBackgroundResource(R.drawable.gender_unselected)
            println(isMaleSelected)

        } else {
            maleTextView.setBackgroundResource(R.drawable.gender_unselected)
            femaleTextView.setBackgroundResource(R.drawable.gender_selected)
            println(isMaleSelected)
        }
    }

}