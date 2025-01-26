package com.indo.imagepicker

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImagePickerSelectActivity(
    private val activity: FragmentActivity,
    private val onImageSelected: (String?) -> Unit,
    private val onError: (String) -> Unit
) {
    private var cameraImagePath: String? = null
    private lateinit var getImageFromGallery: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    init {
        setupResultLaunchers()
    }

    private fun setupResultLaunchers() {
        getImageFromGallery = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val path = getRealPathFromURI(it)
                onImageSelected(path)
            } ?: onError("No image selected")
        }

        takePicture = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess: Boolean ->
            if (isSuccess) onImageSelected(cameraImagePath)
            else onError("Camera capture failed")
        }

        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                onError("Camera permission denied")
            }
        }
    }

    fun showImagePickerDialog(fragmentManager: FragmentManager) {
        val dialog = ImagePickerSelectFragment { option ->
            when (option) {
                ImagePickerOption.Camera -> checkCameraPermission()
                ImagePickerOption.Gallery -> openGallery()
            }
        }
        dialog.show(fragmentManager, ImagePickerSelectFragment.TAG)
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) -> {
                onError("Camera permission is required to take pictures")
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        try {
            val imageFile = createImageFile()
            cameraImagePath = imageFile?.absolutePath
            val imageUri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                imageFile!!
            )
            takePicture.launch(imageUri)
        } catch (ex: IOException) {
            onError("Failed to create image file: ${ex.localizedMessage}")
        }
    }

    private fun openGallery() {
        getImageFromGallery.launch("image/*")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = activity.getExternalFilesDir(null)
        return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        activity.contentResolver.query(contentUri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return null
    }
}