package com.rickyandrean.a2320j2802_submissionintermediate.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rickyandrean.a2320j2802_submissionintermediate.R

class CustomPasswordEditText : AppCompatEditText {
    private lateinit var activeBackground: Drawable
    private lateinit var inactiveBackground: Drawable
    private lateinit var visibilityOn: Drawable
    private lateinit var visibilityOff: Drawable
    private var valid: Boolean = false
    private var show: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        activeBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_password_active) as Drawable
        inactiveBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_password_inactive) as Drawable
        visibilityOn = ContextCompat.getDrawable(context, R.drawable.ic_visibility_on) as Drawable
        visibilityOff = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable

        background = inactiveBackground

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validatePassword(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun validatePassword(text: String?) {
        if (text != null) {
            valid = text.length >= 6
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Password"
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (focused) {
            background = activeBackground
            setButtonDrawables(endOfTheText = if (show) visibilityOn else visibilityOff )
        } else {
            background = inactiveBackground
            setButtonDrawables()
        }
    }
}