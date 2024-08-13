package com.erayerarslan.floreplica.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentHomeBinding
import com.erayerarslan.floreplica.databinding.FragmentProfileBinding
import com.erayerarslan.floreplica.model.User
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

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
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference("Users")

        val uid= firebaseAuth.currentUser?.uid
        val email= firebaseAuth.currentUser?.email
        binding.btnSaveProfile.setOnClickListener {

            val firstName=binding.editTextName.text.toString()
            val lastName=binding.editTextLastName.text.toString()
            val user= User(firstName,lastName,email)

            if (uid != null) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful){
                            uploadProfile()

                        }else{

                        }
                }.addOnFailureListener { exception->
                    println("${exception}")
                }

            }
            findNavController().popBackStack()

        }
    }

    private fun uploadProfile(){
        storageReference= FirebaseStorage.getInstance().getReference("Users/"+firebaseAuth.currentUser?.uid)


    }

}