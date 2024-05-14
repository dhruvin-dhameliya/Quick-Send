package com.extralab.quicksend

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker


class MainActivity : AppCompatActivity() {

    private lateinit var main_layout: LinearLayout
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var edMobileLayout: TextInputLayout
    private lateinit var edMobile: TextInputEditText
    private lateinit var edMessage: TextInputEditText
    private lateinit var cardBtnWhatsapp: MaterialCardView
    private lateinit var cardBtnTelegram: MaterialCardView
    private lateinit var imgWhatsapp: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        main_layout = findViewById(R.id.main_layout)
        countryCodePicker = findViewById(R.id.countryCodePicker)
        edMobileLayout = findViewById(R.id.edMobileLayout)
        edMobile = findViewById(R.id.edMobile)
        edMessage = findViewById(R.id.edMessage)
        cardBtnWhatsapp = findViewById(R.id.cardBtnWhatsapp)
        cardBtnTelegram = findViewById(R.id.cardBtnTelegram)
        imgWhatsapp = findViewById(R.id.imgWhatsapp)

        cardBtnWhatsapp.setOnClickListener {
            sendDirectMessage("WhatsApp")
        }

        cardBtnTelegram.setOnClickListener {
            sendDirectMessage("Telegram")
        }

        edMobile.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    edMobileLayout.error = "* Required mobile number";
                    edMobileLayout.setErrorTextColor(ColorStateList.valueOf(getColor(R.color.white)))
                } else if (s.toString().length < 10) {
                    edMobileLayout.error = "* Mobile number must 10 digit";
                    edMobileLayout.setErrorTextColor(ColorStateList.valueOf(getColor(R.color.white)))
                } else {
                    edMobileLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
    }

    private fun sendDirectMessage(appName: String) {
        val countryCode: String = countryCodePicker.selectedCountryCodeWithPlus.toString()
        val mobileNumber: String = edMobile.text.toString()
        val msg: String = edMessage.text.toString()

        if (mobileNumber.isEmpty()) {
            edMobileLayout.error = "* Required mobile number"
            edMobileLayout.setErrorTextColor(ColorStateList.valueOf(getColor(R.color.white)))
            Snackbar.make(main_layout, "* Required mobile number", Snackbar.LENGTH_SHORT)
                .setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                .setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.white)).show()
            return
        }

        var url = ""
        var packageName = ""

        if (appName == "WhatsApp") {
            url = "https://wa.me/$countryCode$mobileNumber?text=$msg"
            packageName = "com.whatsapp"
        } else if (appName == "Telegram") {
            url = "https://t.me/$countryCode$mobileNumber?text=$msg"
            packageName = "org.telegram.messenger"
        }

        val uri = Uri.parse(url)
        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
        sendIntent.`package` = packageName
        startActivity(sendIntent)
    }
}