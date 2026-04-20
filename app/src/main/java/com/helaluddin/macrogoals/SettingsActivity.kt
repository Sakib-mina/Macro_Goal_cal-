package com.helaluddin.macrogoals

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.helaluddin.macrogoals.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initClickListeners()

    }

    private fun initClickListeners() {
        binding.apply {
            // Back Button
            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            // Privacy Policy
            itemPrivacy.setOnClickListener {
                openUrl("https://sites.google.com/view/macrogoalcalculator-privacy-po/home")
            }

            // Terms & Conditions
            itemTerms.setOnClickListener {
                openUrl("https://sites.google.com/view/macrogoalcalculator-terms-con/home")
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }
}