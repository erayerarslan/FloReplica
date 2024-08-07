package com.erayerarslan.floreplica.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentProfileBinding
import com.erayerarslan.floreplica.databinding.FragmentProfileHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference


class ProfileHomeFragment : Fragment() {
    private var _binding: FragmentProfileHomeBinding? = null
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

        _binding = FragmentProfileHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth=FirebaseAuth.getInstance()

        val uid= firebaseAuth.currentUser?.uid
        val emailLogin= firebaseAuth.currentUser?.email


        if (uid != null) {
            binding.progressBar.isVisible = true

            val databaseReference = FirebaseDatabase.getInstance().reference
            val userRef = databaseReference.child("Users").child(uid)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Kullanıcı bilgilerini alın
                    val firstName = dataSnapshot.child("firstName").getValue(String::class.java)
                    val lastName = dataSnapshot.child("lastName").getValue(String::class.java)
                    val email = dataSnapshot.child("email").getValue(String::class.java)

                    Log.d("FirebaseDatabase", "First Name: $firstName, Email: $email ,Last Name: $lastName" )
                    if (email == null) {
                        binding.textViewEmail.text = emailLogin ?: "Emailinizi güncelleyiniz"
                    }else{
                        binding.textViewEmail.text = email ?: "Emailinizi güncelleyiniz"
                    }

                    binding.textViewFirstName.text = firstName ?: "İsminizi Güncelleyiniz"
                    binding.textViewLastName.text = lastName ?: "Soy İsminizi Güncelleyiniz"

                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Hata durumunda yapılacak işlemler
                    Log.e("FirebaseDatabase", "Error getting user data", databaseError.toException())
                }
            })
        }
    }
}