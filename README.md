# Image Picker Library

[![Release](https://jitpack.io/v/indogusmas/image-picker.svg)](https://jitpack.io/#indogusmas/image-picker/v.0.0.1)

A simple Android library to pick images from the gallery or capture from the camera.

## Features
- Pick image from gallery
- Capture image from camera
- Easy integration
- Minimal configuration

## Installation

Add the JitPack repository to your root `build.gradle` at the end of the repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```
dependencies {
  implementation 'com.github.indogusmas:image-picker:Tag'
}
```

In Activity

```
val imagePicker = ImagePickerSelectActivity(activity = this, 
            onImageSelected = { imagePath ->
                // Handle selected image
                Log.d("TAG"," path image ${imagePath}")
            },
            onError = { errorMessage ->
                // Handle errors
                Log.d("TAG"," error message ${errorMessage}")
            }
        )

findViewById<Button>(R.id.btn_image).setOnClickListener {
  imagePicker.showImagePickerDialog(supportFragmentManager)
}

```



