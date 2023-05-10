package com.example.storyapp.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityCameraBinding
import com.example.storyapp.paging.Injection
import com.example.storyapp.utils.UserPref
import com.example.storyapp.utils.createCustomTempFile
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.example.storyapp.viewModel.UniversalFactory
import com.example.storyapp.viewModel.UniversalVM
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var currentPhotoPath: String
    private lateinit var userPref: UserPref
    private lateinit var viewmodel: UniversalVM
    private var getFile: File? = null
    private var description: String? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file->
                getFile = file
                binding.photo.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {

            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CameraActivity)
            getFile = myFile
            binding.photo.setImageBitmap(BitmapFactory.decodeFile(myFile.path))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = UserPref(this)
        viewmodel = ViewModelProvider(this, UniversalFactory(this))[UniversalVM::class.java]

        binding.camera.setOnClickListener {
            takePhotoCamera()
        }

        binding.galeri.setOnClickListener {
            takePhotoGallery()
        }

        binding.post.setOnClickListener {
            when {
                binding.caption.text.isEmpty() -> {
                    binding.caption.error = "Masukkan deskripsi"
                }
            }
            description = binding.caption.text.toString()
            uploadPhoto()
        }

    }

    private fun uploadPhoto() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = description?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            if (description != null) {
                viewmodel.postStory(description, imageMultiPart)
            }

            viewmodel.uploadStory.observe(this) {
                it.getContentIfNotHandled()?.let { toastText ->
                    Toast.makeText(this@CameraActivity, toastText, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CameraActivity, HomeActivity::class.java))
                    finish()
                }
            }

        } else {
            Toast.makeText(
                this@CameraActivity,"Ambil foto dulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhotoGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePhotoCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@CameraActivity,
                "com.example.storyapp.main",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@CameraActivity, HomeActivity::class.java))
    }
}