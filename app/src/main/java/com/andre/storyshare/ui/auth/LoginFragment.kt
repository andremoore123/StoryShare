package com.andre.storyshare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.andre.storyshare.R
import com.andre.storyshare.data.model.LoginUser
import com.andre.storyshare.databinding.FragmentLoginBinding
import com.andre.storyshare.ui.main.HomeActivity
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.auth.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var navController: NavController
    private lateinit var user: LoginUser
    private lateinit var loadingBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val view =  binding.root
        navController = this.findNavController()

        binding.loginLabelLogin.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        loadingBar = binding.loadingLogin
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        viewModel.message.observe(viewLifecycleOwner){
            showMessage(requireContext(), it)
        }

        viewModel.isSuccessLogin.observe(viewLifecycleOwner){
            if (it){
                val intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        binding.loginButtonLogin.setOnClickListener {
            if (binding.loginTextInputEmail.text.toString().isEmpty() || binding.loginTextInputPassword.text.toString().isEmpty()){
                showMessage(requireContext(), "Email or Password field can't be empty!")
            } else if (binding.loginTextInputPassword.text.toString().length < 6) {
                showMessage(requireContext(), "Password length is less than 6 characters")
            } else{
                user = LoginUser(
                    binding.loginTextInputEmail.text.toString(),
                    binding.loginTextInputPassword.text.toString()
                )
                viewModel.login(user)
            }
        }
    }

    private fun showMessage(context: Context, errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) loadingBar.visibility = View.VISIBLE else loadingBar.visibility = View.INVISIBLE
    }
}