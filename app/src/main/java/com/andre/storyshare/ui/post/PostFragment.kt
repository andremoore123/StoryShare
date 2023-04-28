package com.andre.storyshare.ui.post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.andre.storyshare.R
import com.andre.storyshare.databinding.FragmentPostBinding
import com.andre.storyshare.ui.post.CameraActivity.Companion.CAMERA_X_RESULT
import com.andre.storyshare.ui.viewmodel.ViewModelFactory
import com.andre.storyshare.ui.viewmodel.post.PostViewModel
import java.io.File
import java.io.FileOutputStream

class PostFragment : Fragment() {
    
    private lateinit var viewModel: PostViewModel
    private lateinit var binding: FragmentPostBinding
    private lateinit var navController: NavController
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var captureCamera: ActivityResultLauncher<Intent>
    private lateinit var loadingBar: ProgressBar
    private lateinit var photo: Uri
    private var isBackCamera = true

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(layoutInflater)
        navController = this.findNavController()
        loadingBar = binding.postLoadingBar

        selectFromGallery()
        captureCamera()

        binding.postButtonGallery.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.postButtonPost.setOnClickListener {
            val desc = binding.postDescInput.text.toString()
            viewModel.postData(requireContext(), desc, photo, null, null)
            navController.navigate(R.id.action_postFragment_to_homeFragment)
        }

        binding.postButtonCamera.setOnClickListener {
            startCameraX()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]


        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner){
            if (it){
                showMessage(requireContext(), viewModel.message.value.toString())
            }
        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }
    private fun selectFromGallery(){
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                binding.postPicture.setImageURI(it)
                photo = it
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }
    private fun captureCamera(){
        captureCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File
                isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

                myFile?.let { file ->
                        rotateFile(file, isBackCamera)
                        val fileReduced = viewModel.reduceFileImage(file)
                    photo = fileReduced.toUri()
                        binding.postPicture.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }
    }
    private fun rotateFile(file: File, isBackCamera: Boolean) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 0f else -0f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }
    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        captureCamera.launch(intent)
    }
    private fun showMessage(context: Context, errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) loadingBar.visibility = View.VISIBLE else loadingBar.visibility = View.INVISIBLE
    }

}