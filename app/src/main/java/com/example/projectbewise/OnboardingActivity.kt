package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.projectbewise.databinding.OnboardingScreenBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: OnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slides = listOf(
            R.layout.slide1,
            R.layout.slide2,
            R.layout.slide3
        )

        val adapter = OnboardingAdapter(slides, this)
        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(true, FadePageTransformer())

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val currentSlide = binding.viewPager.findViewWithTag<View>("slide_$position")
                val dotsLayout = currentSlide.findViewById<LinearLayout>(R.id.dotsLayout)
                val dot1 = dotsLayout.findViewById<View>(R.id.dot1)
                val dot2 = dotsLayout.findViewById<View>(R.id.dot2)
                val dot3 = dotsLayout.findViewById<View>(R.id.dot3)
                updateDots(dot1, dot2, dot3, position)
                if (position == slides.size - 1) {
                    val startButton: Button = currentSlide.findViewById(R.id.startButton)
                    startButton.setOnClickListener {
                        val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Initial state
        binding.viewPager.post {
            val initialSlide = binding.viewPager.findViewWithTag<View>("slide_0")
            val initialDotsLayout = initialSlide.findViewById<LinearLayout>(R.id.dotsLayout)
            val initialDot1 = initialDotsLayout.findViewById<View>(R.id.dot1)
            val initialDot2 = initialDotsLayout.findViewById<View>(R.id.dot2)
            val initialDot3 = initialDotsLayout.findViewById<View>(R.id.dot3)
            updateDots(initialDot1, initialDot2, initialDot3, 0)
        }
    }

    private fun updateDots(dot1: View, dot2: View, dot3: View, position: Int) {
        dot1.isSelected = position == 0
        dot2.isSelected = position == 1
        dot3.isSelected = position == 2
    }

    private inner class FadePageTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                alpha = when {
                    position <= -1f || position >= 1f -> 0f
                    position == 0f -> 1f
                    else -> 1f - Math.abs(position)
                }
            }
        }
    }
}
