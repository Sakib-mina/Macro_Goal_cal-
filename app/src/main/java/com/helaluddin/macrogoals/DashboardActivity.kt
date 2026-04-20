package com.helaluddin.macrogoals

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.helaluddin.macrogoals.databinding.ActivityDashboardBinding

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var selectedGoal = "Maintain"
    private var activityMultiplier = 1.55

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            // Settings Button
            btnSettings.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, SettingsActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

            // Goal Selection Logic (Lose, Maintain, Gain)
            btnLose.setOnClickListener { updateGoalUI(it as TextView, "Lose") }
            btnMaintain.setOnClickListener { updateGoalUI(it as TextView, "Maintain") }
            btnGain.setOnClickListener { updateGoalUI(it as TextView, "Gain") }

            // Activity Level Selection Logic (Low, Med, High)
            tabLow.setOnClickListener { updateActivityUI(it as TextView, 1.2) }
            tabMed.setOnClickListener { updateActivityUI(it as TextView, 1.55) }
            tabHigh.setOnClickListener { updateActivityUI(it as TextView, 1.9) }

            // Calculate Button Logic
            btnCalculateMacros.setOnClickListener {
                val weightStr = etWeight.text.toString()
                if (weightStr.isEmpty()) {
                    etWeight.error = "Enter weight"
                    return@setOnClickListener
                }
                calculateMacros(weightStr.toDouble())
                hideKeyboard()
            }
        }
    }

    private fun updateGoalUI(selectedView: TextView, goal: String) {
        selectedGoal = goal
        val buttons = listOf(binding.btnLose, binding.btnMaintain, binding.btnGain)

        buttons.forEach {
            it.setBackgroundResource(R.drawable.bg_input_field)
            it.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        }

        selectedView.setBackgroundResource(R.drawable.bg_goal_selected)
        selectedView.setTextColor(ContextCompat.getColor(this, R.color.green_primary))
    }

    private fun updateActivityUI(selectedView: TextView, multiplier: Double) {
        activityMultiplier = multiplier
        val tabs = listOf(binding.tabLow, binding.tabMed, binding.tabHigh)

        tabs.forEach {
            it.background = null
            it.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            it.elevation = 0f
        }

        selectedView.setBackgroundResource(R.drawable.bg_white_rounded)
        selectedView.setTextColor(ContextCompat.getColor(this, R.color.black))
        selectedView.elevation = 4f
    }

    private fun calculateMacros(weight: Double) {
        val baseCalories = weight * 22 * activityMultiplier

        val totalCalories = when (selectedGoal) {
            "Lose" -> baseCalories - 500
            "Gain" -> baseCalories + 500
            else -> baseCalories
        }

        // Macro Logic: 2g Protein/kg, 25% Fat, Rest Carbs
        val protein = weight * 2.2
        val fats = (totalCalories * 0.25) / 9
        val carbs = (totalCalories - (protein * 4) - (fats * 9)) / 4

        showResults(totalCalories.toInt(), protein.toInt(), carbs.toInt(), fats.toInt())
    }

    @SuppressLint("SetTextI18n")
    private fun showResults(calories: Int, p: Int, c: Int, f: Int) {
        binding.apply {
            layoutMacroResult.visibility = View.VISIBLE

            // Result View with Slide-up Animation
            layoutMacroResult.alpha = 0f
            layoutMacroResult.translationY = 100f
            layoutMacroResult.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .start()

            // Update UI Texts
            tvTotalCalories.text = "$calories kcal"
            tvProteinVal.text = "${p}g"
            tvCarbsVal.text = "${c}g"
            tvFatsVal.text = "${f}g"
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}