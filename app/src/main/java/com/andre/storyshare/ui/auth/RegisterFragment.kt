package com.andre.storyshare.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.andre.storyshare.R
import com.andre.storyshare.data.model.RegistUser
import com.andre.storyshare.databinding.FragmentRegisterBinding
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.auth.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    private lateinit var navController: NavController
    private lateinit var loadingBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val view =  binding.root
        navController = this.findNavController()
        loadingBar = binding.loadingRegister

        binding.registerLabelLogin.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner){
            showMessage(requireContext(), viewModel.message.value.toString())
        }

        viewModel.isSuccess.observe(viewLifecycleOwner){
            if (it){
                navController.navigate(R.id.action_registerFragment_to_loginFragment)
                showMessage(requireContext(), R.string.success_register.toString())
            }
        }
        binding.registerButtonRegister.setOnClickListener {
            val name = binding.registerInputName.text.toString()
            val email = binding.registerInputEmail.text.toString()
            val password = binding.registerInputPassword.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                viewModel.register(RegistUser(name, email, password))
            } else {
                showMessage(requireContext(), R.string.failed_register.toString())
            }
        }
    }
    private fun showMessage(context: Context, errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) loadingBar.visibility = View.VISIBLE else loadingBar.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}