package com.rama.tui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.rama.tui.R

class WdRange @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val container: LinearLayout

    private val allButtons = mutableListOf<Button>()

    private var selectedIndex = -1

    var onValueChanged: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL

        LayoutInflater.from(context).inflate(
            R.layout.wd_range,
            this,
            true
        )

        container = findViewById(R.id.container)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.WdRange)
            try {
                val values: List<String> = when {
                    ta.hasValue(R.styleable.WdRange_valuesArray) -> {
                        val arrayRes = ta.getResourceId(R.styleable.WdRange_valuesArray, 0)
                        if (arrayRes != 0) context.resources.getStringArray(arrayRes).toList()
                        else emptyList()
                    }

                    ta.hasValue(R.styleable.WdRange_values) -> {
                        ta.getString(R.styleable.WdRange_values)
                            ?.split(",")
                            ?.map { it.trim() }
                            ?.filter { it.isNotEmpty() }
                            ?: emptyList()
                    }

                    else -> emptyList()
                }

                setValues(values)
            } finally {
                ta.recycle()
            }
        }
    }

    private fun setValues(values: List<String>) {

        container.removeAllViews()
        allButtons.clear()

        values.forEachIndexed { index, value ->

            val button = Button(
                context,
                null,
                0,
                R.style.BTN_range
            ).apply {

                text = value

                layoutParams = LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1f
                )

                setOnClickListener {
                    select(index)
                }
            }

            allButtons.add(button)
            container.addView(button)
        }

        if (values.isNotEmpty()) {
            select(0)
        }
    }

    private fun select(index: Int) {

        selectedIndex = index

        allButtons.forEachIndexed { i, button ->

            button.isSelected = i == index

            // optional
            button.alpha =
                if (i == index) 1f
                else 0.5f
        }

        onValueChanged?.invoke(
            allButtons[index].text.toString()
        )
    }

    fun getValue(): String? {
        return allButtons
            .getOrNull(selectedIndex)
            ?.text
            ?.toString()
    }
}