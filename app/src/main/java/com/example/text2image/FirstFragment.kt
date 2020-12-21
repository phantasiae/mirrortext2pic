package com.example.text2image

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import androidx.core.text.buildSpannedString
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: It should be move to correct position.
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(edit: Editable?) {
                val text = edit?.toString()?.let {
                    val textView = view.findViewById<View>(R.id.imageView)
                    var rotatedBitmap: Bitmap?
                    TextPic(view.context, textView).apply {
                        this.text = it
                        // TODO: It is a bug that the text can over the border of textEdit.
                        val imageView = view.findViewById<ImageView>(R.id.imageView)
                        rotatedBitmap = this.generatePicture()
                        imageView.setImageBitmap(rotatedBitmap)
                    }
                }
            }
        }

        view.findViewById<EditText>(R.id.inputTexts).addTextChangedListener(textWatcher)
    }
}

