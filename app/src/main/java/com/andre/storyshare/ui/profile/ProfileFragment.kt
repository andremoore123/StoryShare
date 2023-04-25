package com.andre.storyshare.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andre.storyshare.MainActivity
import com.andre.storyshare.R
import com.andre.storyshare.databinding.FragmentProfileBinding
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.profile.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)


        binding.profileButtonLogout.setOnClickListener {
            viewModel.logOut()
            Intent(requireActivity(), MainActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }

        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        viewModel.getUserName()
        viewModel.userName.observe(viewLifecycleOwner){
            binding.profileName.text = it
        }
    }

}