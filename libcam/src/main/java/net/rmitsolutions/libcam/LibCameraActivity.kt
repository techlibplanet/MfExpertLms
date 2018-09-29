package net.rmitsolutions.libcam

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lib_camera.*
import net.rmitsolutions.libcam.Constants.CROP_PHOTO
import net.rmitsolutions.libcam.Constants.TAKE_PHOTO
import net.rmitsolutions.libcam.Constants.tag

class LibCameraActivity : AppCompatActivity() {


    private val TAG = tag(this)
    private lateinit var libCamera: LibCamera

    private lateinit var libPermissions: LibPermissions
    private var imageUri: Uri? = null
    private var toolbar : Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_camera)

        toolbar = findViewById(R.id.toolbar_actionbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            }
        }

        val permissions = arrayOf<String>(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        libPermissions = LibPermissions(this, permissions)


        val runnable = Runnable {
            Constants.logD(TAG, "All permission enabled")
        }
        libPermissions.askPermissions(runnable)
        libCamera = LibCamera(this)

    }

    private fun getActionBarToolBar(): Toolbar? {
        if (toolbar == null){

        }
        return toolbar
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crop_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_load_image -> {
                val runnable = Runnable {
                    libCamera.takePhoto()
                }
                libPermissions.askPermissions(runnable, Constants.CAMERA)
                return true
            }

            R.id.menu_crop_image -> {
                if (imageUri != null) {
                    libCamera.cropImage(imageUri!!)
                } else {
                    Toast.makeText(this, "Please capture or select an Image", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            R.id.menu_upload_image -> {
                if (imageUri != null) {
                    val path = libCamera.savePhotoInDeviceMemory(imageUri!!, Constants.DEFAULT_FILE_PREFIX, true)
//                    val exifInterface = ExifInterface(path)
//                    val df = SimpleDateFormat("yyyyMMddHHmmss")
//                    val date = df.format(Calendar.getInstance().time)
//                    exifInterface.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, date)
//                    exifInterface.setAttribute(ExifInterface.TAG_DATETIME, date)
//                    exifInterface.saveAttributes()
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show()
                    Constants.logD(TAG, "Path - $path")
                    cameraImageView.setImageURI(imageUri)

                    val imageInformation = ImageInformation()
                    val data = imageInformation.getImageInformation(path!!)

                    val intent = Intent()
                    intent.putExtra(ImageData.Constants.IMAGE_LATITUDE, data?.latitude)
                    intent.putExtra(ImageData.Constants.IMAGE_LONGITUDE, data?.longitude)
                    intent.putExtra(ImageData.Constants.IMAGE_WIDTH, data?.imageWidth)
                    intent.putExtra(ImageData.Constants.IMAGE_LENGTH, data?.imageLength)
                    intent.putExtra(ImageData.Constants.IMAGE_TIME_STAMP, data?.dateTimeTakePhoto)
                    intent.putExtra(ImageData.Constants.IMAGE_STRING_BASE_64, libCamera.getBase64StringFromUri(imageUri!!, 80))
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Please capture or select an Image !", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    val resultImageUri = libCamera.getPickImageResultUri(data)
                    imageUri = resultImageUri!!
                    Constants.logD("TAG", "Result Image Uri - ${resultImageUri.path}")
                    libCamera.cropImage(imageUri!!)
                }
            }

            CROP_PHOTO -> {
                if (data != null) {
                    imageUri = libCamera.cropImageActivityResult(requestCode, resultCode, data)!!
                    Constants.logD(TAG, "Crop Image Uri - ${imageUri!!.path}")
                    cameraImageView.setImageURI(imageUri)
                }
            }
        }
    }
}
