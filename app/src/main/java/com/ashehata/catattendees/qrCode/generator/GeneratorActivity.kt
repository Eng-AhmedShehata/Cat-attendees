package com.ashehata.catattendees.qrCode.generator

import android.Manifest
import android.R.attr.bitmap
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.generateAndSave
import com.ashehata.catattendees.helper.generateQr
import kotlinx.android.synthetic.main.activity_generator.*


class GeneratorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        setBtnGenerate()
        setBtnGenerateSave()
        setBtnGenerateSaveShare()

    }

    private fun setBtnGenerateSaveShare() {

    }

    private fun setBtnGenerateSave() {
        btn_generate_save.setOnClickListener {
            val memberName = et_name.text.toString().trim()
            val memberLevel = et_level.text.toString().trim()

            if (memberName.isNotEmpty() && memberLevel.isNotEmpty()) {
                generateAndSave(memberName, memberLevel)
                et_name.setText("")
                et_level.setText("")
            }
        }

    }

    private fun setBtnGenerate() {
        btn_generate.setOnClickListener {
            val memberName = et_name.text.toString().trim()
            val memberLevel = et_level.text.toString().trim()

            if (memberName.isNotEmpty() && memberLevel.isNotEmpty()) {
                generateQr(memberName, memberLevel)
            }
        }
    }


}