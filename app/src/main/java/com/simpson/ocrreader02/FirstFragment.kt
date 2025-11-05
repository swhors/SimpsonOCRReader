package com.simpson.ocrreader02

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.googlecode.tesseract.android.TessBaseAPI
import com.simpson.ocrreader02.BmpUtil.Companion.bitmapToByteArray
import com.simpson.ocrreader02.BmpUtil.Companion.decodeUriToBitmap
import com.simpson.ocrreader02.TessUtil.Companion.checkLanguageFile
import com.simpson.ocrreader02.databinding.FragmentFirstBinding
import com.simpson.ocrreader02.model.Coupon
import com.simpson.ocrreader02.model.CouponReaderDbHelper
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    var dbHelper: CouponReaderDbHelper? = null
    var tessBaseAPI: TessBaseAPI? = null
    var korRecognizer: TextRecognizer
    var imageView: ImageView? = null
    var textView: TextView? = null

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var userChoosenTask: String? = null

    init {
//        this.recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        this.korRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    }
    val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            Log.d("photoPickerLauncher", "uri = $it")
            lifecycleScope.launch {
//                binding.imageView.setImageURI(it)
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    inJustDecodeBounds = false
                }
                val bmp = decodeUriToBitmap(this@FirstFragment.context, it, options = options)

                _binding!!.idLoadedImageView.setImageBitmap(bmp.getOrNull())

                val mat = Mat()
                val bmpImage = bmp.getOrNull()
                val useGoogleApi = true
                if (OpenCVLoader.initLocal()) {
                    Utils.bitmapToMat(bmpImage, mat)
                    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB)
                    Utils.matToBitmap(mat, bmpImage)
//                tessBaseAPI!!.setImage(new_image, )
                    if (useGoogleApi) {
                        korRecognizer.process(bmpImage!!, 0).addOnSuccessListener { text ->
                            val recognizedTextBuilder = StringBuilder()
                            for (block in text.textBlocks) {
                                val blockText = block.text
                                Log.d("photoPickerLauncher", blockText)
                            }
//                            recognizedTextBuilder.append(blockText).append("\n");
                        }
                    } else {
                        tessBaseAPI!!.setImage(bmpImage)
                        val result = tessBaseAPI!!.utF8Text
                        Log.d("photoPickerLauncher", result)
                    }
                }
            }
        }
    }

    init {
        dbHelper = CouponReaderDbHelper(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = _binding!!.idLoadedImageView
        textView = _binding!!.idContentView
        val loadBtn = _binding!!.idBtnLoad
        loadBtn.setOnClickListener { selectImage() }

        val saveBtn = _binding!!.idBtnSave
        saveBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val text = _binding!!.idContentView.text.toString()
                val drawable = _binding!!.idLoadedImageView.drawable as BitmapDrawable
                saveParseInfo(drawable.bitmap, text)
                Log.d("saveBtn.setOnClickListener", "saved")
            }

        })
        tessBaseAPI = TessBaseAPI()
        val dir = this@FirstFragment.requireContext().getFilesDir().toString() + "/tesseract"
        Log.d("MainActivity", dir)
        if (checkLanguageFile(requireContext(), dir + "/tessdata"))
            tessBaseAPI!!.init(dir, "kor")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun singlePhotoPicker() {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun saveParseInfo(bmp: Bitmap, text: String) {

        val write_handle = dbHelper!!.writableDatabase
        val title = "test"
        val dateStart = "2001"
        val dateEnd = "2002"
        val price = 2000
        val used = false
        val image = bitmapToByteArray(bmp)
        val message = "hello"
        val contentValues = ContentValues()

        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_TITLE, title)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_DATESTART, dateStart)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_DATEEND, dateEnd)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_PRICE, price)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_USED, used)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_IMAGE, image)
        contentValues.put(Coupon.CouponEntry.COLUMN_NAME_MESSAGE, message)
        write_handle.insert(Coupon.CouponEntry.TABLE_NAME, null, contentValues)
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence?>(
            "Take Photo",
            "Choose from Library",
            "Cancel"
        )
        Log.d(TAG, "selectImage")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@FirstFragment.requireContext())
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@FirstFragment.requireContext())
            if (items[item] == "Take Photo") {
                Log.d(TAG, "selected = $items[item])")
                this@FirstFragment.userChoosenTask = "Take Photo"
                if (result) cameraIntent()
            } else if (items[item] == "Choose from Library") {
                print("selected = $items[item])")
                Log.d(TAG, "selected = \"Choose from Library\"")
                Log.d(TAG, "permission = $result")
                this@FirstFragment.userChoosenTask = "Choose from Library"
                singlePhotoPicker()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    companion object {
        val REQUEST_CAMERA = 0
    }
}