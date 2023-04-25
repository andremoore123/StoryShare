package com.andre.storyshare.ui.viewmodel.post

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.storyshare.data.model.DataStoryUpload
import com.andre.storyshare.data.remote.api.ApiConfig
import com.andre.storyshare.data.remote.repository.DicodingApiRepository
import com.andre.storyshare.datastore.DataStoreManager
import com.andre.storyshare.datastore.DataStoreSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale
private const val MAXIMAL_SIZE = 1000000

class PostViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val repository = DicodingApiRepository(ApiConfig.getInstance().getService())

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val dataStore = DataStoreSingleton.getDataStore()
    private val dataStoreManager = DataStoreManager(dataStore)

    fun postData(
        context: Context,
        desc: String,
        photo: Uri,
        lat: Float?,
        lon: Float?
        ){
        _isError.value = false
        _isLoading.value = true

        launch {
            try {
                val file = reduceFileImage(uriToFile(photo, context))
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                runBlocking {
                    val response = repository.uploadStory(
                        dataStoreManager.getToken(), DataStoryUpload(
                            desc.toRequestBody("text/plain".toMediaType()),
                            imageMultipart,
                            lat?.toString()?.toRequestBody("text/plain".toMediaType()),
                            lon?.toString()?.toRequestBody("text/plain".toMediaType())
                        )
                    )
                    _message.value = response.message
                    _isError.value = response.error
                }
            } catch (e: Exception){
                Log.d("ErrorPost", e.toString())
                _isError.value = true
                _message.value = e.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }
    fun createCustomTempFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat(
            "h:mm a",
            Locale.US
        ).format(System.currentTimeMillis())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }
    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


}