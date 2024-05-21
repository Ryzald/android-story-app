package com.rizaldi.mystoryapp.view.addStory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rizaldi.mystoryapp.data.preferences.UserPreference
import com.rizaldi.mystoryapp.data.preferences.dataStore
import com.rizaldi.mystoryapp.data.response.ResponseFileUpload
import com.rizaldi.mystoryapp.data.retrofit.ApiConfig
import com.rizaldi.mystoryapp.databinding.ActivityAddStoryBinding
import com.rizaldi.mystoryapp.util.getImageUri
import com.rizaldi.mystoryapp.util.reduceFileImage
import com.rizaldi.mystoryapp.util.uriToFile
import com.rizaldi.mystoryapp.view.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImg.setImageURI(it)
        }

    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.tvDescription.text.toString().trim()

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val pref = UserPreference.getInstance(dataStore)
                    val token = pref.getToken().first()
                    val apiService = ApiConfig.getApiService(token)
                        val successResponse = apiService.uploadImage(multipartBody, requestBody)
                        showToast(successResponse.message)
                        showLoading(false)
                        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ResponseFileUpload::class.java)
                    showToast(errorResponse.message)
                    showLoading(false)
                }
            }
            showLoading(true)
        } ?: showToast("Silahkan masukkan berkas gambar terlebih dahulu.")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

}