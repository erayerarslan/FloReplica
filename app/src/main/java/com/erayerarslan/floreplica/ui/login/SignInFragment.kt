package com.erayerarslan.floreplica.ui.login


import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.floreplica.MainActivity
import com.erayerarslan.floreplica.R
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.databinding.FragmentSignInBinding
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment() : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.hideBottomNavigationView()
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonSignIn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            viewModel.signIn(email, password)
        }
        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.guestBtn.setOnClickListener {
            viewModel.signInAnonymously()

        }


        lifecycleScope.launch {
            viewModel.signInState.collect { response ->
                when (response) {
                    is Response.Loading -> {

                        Log.d("SignInFragment", "Loading state")
                    }

                    is Response.Success -> {
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                        (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.homeFragment


                    }

                    is Response.Error -> {
                        Log.d("SignInFragment", "Error state: ${response.message}")
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Log.d("SignInFragment", "Unknown state")
                    }
                }
            }



        }
        lifecycleScope.launch {
            viewModel.signAnonymously.collect { response ->
                when(response){
                    is Response.Loading -> {
                        Log.d("SignInFragment", "Loading state")
                    }
                    is Response.Success -> {
                        Toast.makeText(requireContext(), "Sign-in successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                        (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.homeFragment


                    }
                    is Response.Error -> {
                        Log.d("SignInFragment", "Error state: ${response.message}")
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Log.d("SignInFragment", "Unknown state")
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}