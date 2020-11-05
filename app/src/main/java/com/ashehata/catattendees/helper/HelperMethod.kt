package com.ashehata.catattendees.helper

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Environment
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ashehata.catattendees.qrCode.generator.Member
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_generator.*


const val MEETING_COLLECTION = "MeetingDay"
const val MEMBER_COLLECTION = "MemberInfo"

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeypad() {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    with(this.currentFocus) {
        imm.hideSoftInputFromWindow(this?.windowToken, 0)
    }
}

fun Activity.generateQr(memberName: String, memberLevel: String): Bitmap? {
    val member = Member(memberName = memberName, memberLevel = memberLevel)
    val memberJson = Gson().toJson(member)
    var bitmap: Bitmap? = null

    val manager = getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
    val display = manager.defaultDisplay
    val point = Point()
    display.getSize(point)
    val width = point.x
    val height = point.y;
    var smallerDimension = if (width < height) width else height
    smallerDimension = smallerDimension * 3 / 4;

    val qrgEncoder = QRGEncoder(
        memberJson, null,
        QRGContents.Type.TEXT,
        smallerDimension
    )
    try {
        hideKeypad()
        bitmap = qrgEncoder.bitmap
        iv_qrcode.setImageBitmap(bitmap)

    } catch (e: Exception) {
    }
    return bitmap
}

fun Activity.generateAndSave(memberName: String, memberLevel: String) {

    val mBitmap = generateQr(memberName, memberLevel)
    val savePath: String =
        Environment.getExternalStorageDirectory().path
            .toString() + "/Cat attendees/"

    if (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        try {

            val save = QRGSaver().save(
                savePath,
                memberName,
                mBitmap,
                QRGContents.ImageType.IMAGE_JPEG
            )
            val result = if (save) "Image Saved" else "Image Not Saved"
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            102
        )
    }
}

fun Activity.checkCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}

fun Activity.showProgressDialog(): AlertDialog? {
    val builder = ProgressDialog(this)
    builder.setCancelable(false)
    builder.setMessage("Please Wait!")
    builder.show()
    return builder
}
fun Activity.requestPermission() {
    ActivityCompat.requestPermissions(
        this, arrayOf(Manifest.permission.CAMERA),
        101
    )
}