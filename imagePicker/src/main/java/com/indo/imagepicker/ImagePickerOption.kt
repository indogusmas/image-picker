package com.indo.imagepicker

sealed class ImagePickerOption {
    object Camera : ImagePickerOption()
    object Gallery : ImagePickerOption()
}