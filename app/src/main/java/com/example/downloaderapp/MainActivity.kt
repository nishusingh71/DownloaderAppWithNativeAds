@file:Suppress("DEPRECATION")

package com.example.downloaderapp

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val editTextLink = findViewById<EditText>(R.id.linkEdit)
        val buttonDownload = findViewById<Button>(R.id.button_download)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.pic2, "Download video by link", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.pic4, "Download Mp3 by link", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.pic1, "Download image by link", ScaleTypes.CENTER_CROP))
        imageSlider.setImageList(imageList)

        // for test ads
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        val adLoader=AdLoader.Builder(this,"ca-app-pub-3940256099942544/2247696110")
            .forUnifiedNativeAd { unifiedNativeAd ->
                val style =
                    NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(Color.WHITE))
                        .build()
                val templates = findViewById<TemplateView>(R.id.my_template)
                templates.setStyles(style)
                templates.setNativeAd(unifiedNativeAd)
            }.build()
        adLoader.loadAd(AdRequest.Builder().build())

        buttonDownload.setOnClickListener {
            val linkText = editTextLink.text.toString()
            if (linkText.isNotEmpty()) {
                val request = DownloadManager.Request(Uri.parse(linkText))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                request.setTitle("Download File")
                request.setDescription("Downloading.....")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    @Suppress("DEPRECATION")
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                }
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "${System.currentTimeMillis()}"
                )

                val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.enqueue(request)
            }
        }

    }
}