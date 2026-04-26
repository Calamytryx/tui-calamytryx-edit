package com.rama.tui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.rama.tui.R

class WdNavbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class Page { HOME, ABOUT }

    private val homeBtn: FrameLayout
    private val aboutBtn: FrameLayout
    private val selectedColor = resources.getColor(R.color.button_selected_color)
    private val inactiveColor = resources.getColor(R.color.button_primary_color)

    var onNavigate: ((Page) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.wd_navbar, this, true)

        homeBtn = findViewById(R.id.home_nav)
        aboutBtn = findViewById(R.id.about_nav)

        homeBtn.setOnClickListener { onNavigate?.invoke(Page.HOME) }
        aboutBtn.setOnClickListener { onNavigate?.invoke(Page.ABOUT) }
    }

    fun setActivePage(page: Page) {
        val allButtons = listOf(homeBtn, aboutBtn)
        val activeBtn = when (page) {
            Page.HOME -> homeBtn
            Page.ABOUT -> aboutBtn
        }
        allButtons.forEach { btn ->
            val isSelected = btn === activeBtn
            btn.isEnabled = !isSelected
            btn.setBackgroundColor(
                if (isSelected) selectedColor else inactiveColor
            )
        }
    }
}
