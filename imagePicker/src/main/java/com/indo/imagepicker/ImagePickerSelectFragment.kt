package com.indo.imagepicker

import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ImagePickerSelectFragment (private val onOptionSelected: (ImagePickerOption) -> Unit) : BottomSheetDialogFragment() {
    companion object{
        var  TAG: String = ImagePickerSelectFragment::class.java.name
    }

    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): android.app.Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as com.google.android.material.bottomsheet.BottomSheetDialog
        val view = layoutInflater.inflate(R.layout.fragment_image_picker_select, null)
        dialog.setContentView(view)

        view.findViewById<android.view.View>(R.id.card_select_camera).setOnClickListener {
            onOptionSelected(ImagePickerOption.Camera)
            dismiss()
        }

        view.findViewById<android.view.View>(R.id.card_select_galery).setOnClickListener {
            onOptionSelected(ImagePickerOption.Gallery)
            dismiss()
        }

        return dialog
    }
}