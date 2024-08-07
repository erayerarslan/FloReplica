package com.erayerarslan.floreplica.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.buttonSignUp.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (validateForm(email, password)) {
                registerUser(email, password)
            }
        }
        binding.textViewSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    // Form validasyonu
    private fun validateForm(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailEt.error = "Email required"
            binding.emailEt.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.error = "Please enter a valid email"
            binding.emailEt.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.passEt.error = "Password cannot be empty"
            binding.passEt.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.passEt.error = "Password should be at least 6 characters"
            binding.passEt.requestFocus()
            return false
        }
        if (binding.passEt.text.toString() != binding.confirmPassEt.text.toString()) {
            binding.passEt.error = "Password is not the same"
            binding.passEt.requestFocus()
            return false
        }

        return true
    }


    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //kayıt başarılı olursa
                    binding.textViewSignUpError.setTextColor(resources.getColor(R.color.green))
                    binding.textViewSignUpError.text = "Registration successful";
                    //yazı bir süre gözüksün diye loop koydum.
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                        println("Bu kod 1.2 saniye sonra çalışacak")
                    }, 1200)



                } else {
                    //kayıt başarısız olursa mesela aynı mailden kayıt işlemi
                    binding.textViewSignUpError.text = task.exception?.message ;

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


