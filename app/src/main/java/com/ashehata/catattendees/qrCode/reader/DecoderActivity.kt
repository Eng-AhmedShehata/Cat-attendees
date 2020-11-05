package com.ashehata.catattendees.qrCode.reader

import android.graphics.PointF
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.*
import com.ashehata.catattendees.qrCode.generator.Member
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_decoder.*


class DecoderActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private var documentID: String? = null
    private lateinit var mMediaPlayer: MediaPlayer
    private var qrText: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decoder)

        documentID = intent.extras?.getString("docID")
        firebaseFirestore = FirebaseFirestore.getInstance()
        mMediaPlayer = MediaPlayer.create(this, R.raw.blop)
        setup()
    }

    private fun setup() {
        qrdecoderview.apply {
            setOnQRCodeReadListener(this@DecoderActivity)
            // Use this function to enable/disable decoding
            setQRDecodingEnabled(true)

            // Use this function to change the autofocus interval (default is 5 secs)
            setAutofocusInterval(2000L)

            // Use this function to enable/disable Torch
            setTorchEnabled(true)

            // Use this function to set front camera preview
            setFrontCamera()

            // Use this function to set back camera preview
            setBackCamera()
        }

    }


    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {

        Logger.i("qrData", text.toString())
        if (!text.equals(qrText)) {
            qrText = text
            sendData(text)
        }

    }

    private fun sendData(text: String?) {
        val obj = Gson().fromJson(text, Member::class.java)

        qrdecoderview.setQRDecodingEnabled(false)
        val map = mutableMapOf<String, Any>()
        map["name"] = obj.memberName
        map["level"] = obj.memberLevel

        val dialog = showProgressDialog()
        documentID?.let {
            firebaseFirestore.collection(MEETING_COLLECTION)
                .document(it)
                .collection(MEMBER_COLLECTION)
                .add(map)
                .addOnSuccessListener { documentReference ->
                    showToast("Success")
                    makePepSound()
                    dialog?.dismiss()
                    qrdecoderview.setQRDecodingEnabled(true)
                }
                .addOnFailureListener { e ->
                    showToast("Failed $e")
                    dialog?.dismiss()
                }
        } ?: dialog?.dismiss()

    }

    private fun makePepSound() {
        mMediaPlayer.start()
    }

    private fun releasePlayer() {
        mMediaPlayer.apply {
            stop()
            release()
        }
    }

    override fun onResume() {
        super.onResume()
        qrdecoderview.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrdecoderview.stopCamera()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }
}