package com.example.text2image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.Manifest


class MainActivity :
    AppCompatActivity(),
    InputTextDialog.InputListener {

    private fun getPermissions(): Boolean {
        val wes = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, wes) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(wes), 0)
        }

        println("--- getPermissions --- ${ContextCompat.checkSelfPermission(this, wes) == PERMISSION_GRANTED}")
        return ContextCompat.checkSelfPermission(this, wes) == PERMISSION_GRANTED
    }

    private fun showTextInputDialog() {
        val newFragment = InputTextDialog()
        newFragment.show(supportFragmentManager, "添加字符")
    }

    private fun displayTextPic(text: String) {
        val view = findViewById<View>(R.id.imageView)

        val context = this.applicationContext
        var rotatedBitmap: Bitmap?
        val t = TextPic(applicationContext, view).apply {
            this.text = text
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            rotatedBitmap = this.generatePicture()
            imageView.setImageBitmap(rotatedBitmap)

//            try {
//                val root: String = context?.getExternalFilesDir(null).toString()
//                val out = FileOutputStream("$root/Pictures/text.png")
//                rotatedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();
//            } catch (e: IOException) {
//                println("--- e --- ${e}");
//            }
        }

        val root: String = getExternalFilesDir(null).toString()
        val file = File("$root/Pictures/text.png")
//        MediaScannerConnection(this,
//            object : MediaScannerConnection.MediaScannerConnectionClient {
//                private lateinit var mediaScannerConn: MediaScannerConnection
//                private lateinit var file: File
//
//                fun SingleMediaScanner(context: Context, file: File) {
//                    this.file = file
//                    mediaScannerConn = MediaScannerConnection(context, this)
//                    mediaScannerConn.connect()
//                }
//
//                override fun onMediaScannerConnected() {
//                    mediaScannerConn.scanFile(file.absolutePath, null);
//                }
//
//
//                override fun onScanCompleted(path: String, uri: Uri) {
//                    mediaScannerConn.disconnect()
//                }
//            })

//        println("--- file --- $file")

        val resolver = applicationContext.contentResolver

        val newImageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "text.png")
            put(MediaStore.Images.Media.DISPLAY_NAME, "text.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//            put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/DemoPicture")
        }

        val imageCollection = MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL)

        val imageUri = resolver.insert(imageCollection, newImageDetails)

        println("--- before uri --- $imageCollection")

        imageUri?.let {
            resolver.openOutputStream(it).use {
                println("--- rotatedBitmap --- ${rotatedBitmap.toString()}")
                rotatedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, it);
                it?.flush()
                it?.close()
            }
        }


        println("--- URI --- ${imageUri.toString()}")
        MediaScannerConnection.scanFile(this, arrayOf("/storage/emulated/0/Pictures/1606139478005.jpg"), null
            ) { path: String, uri: Uri ->
            println("--- path? --- ${path}")
            println("--- uri? --- ${uri}")
        };

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            if (getPermissions()) {
                showTextInputDialog()
                Snackbar.make(view, "Save OK.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val text = dialog.dialog?.findViewById<EditText>(R.id.TextInput).let {
            displayTextPic(it?.text.toString())
        }

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dialog?.cancel()
    }
}