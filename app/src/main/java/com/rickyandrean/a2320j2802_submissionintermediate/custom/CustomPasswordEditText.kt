package com.rickyandrean.a2320j2802_submissionintermediate.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.rickyandrean.a2320j2802_submissionintermediate.R


class CustomPasswordEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var inactiveBackground: Drawable
    private lateinit var validBackground: Drawable
    private lateinit var invalidBackground: Drawable
    private lateinit var visibilityOn: Drawable
    private lateinit var visibilityOff: Drawable
    private lateinit var visibilityImage: Drawable
    private lateinit var passwordImage: Drawable
    private var show: Boolean = false
    var valid: Boolean = false

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
        // Image drawable initialization
        validBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_valid) as Drawable
        invalidBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_invalid) as Drawable
        inactiveBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_inactive) as Drawable
        visibilityOn = ContextCompat.getDrawable(context, R.drawable.ic_visibility_on) as Drawable
        visibilityOff = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable
        passwordImage = ContextCompat.getDrawable(context, R.drawable.ic_key) as Drawable

        // Determine background and icon for the first time
        visibilityImage = visibilityOff
        background = inactiveBackground

        // Hide password text for the first time
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // To show the key icon for the first time
        setButtonDrawables()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validatePassword(s?.toString())
            }
        })

        setOnTouchListener(this)
    }

    private fun setButtonDrawables(
        start: Drawable? = passwordImage,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

    private fun validatePassword(text: String?) {
        if (text != null) {
            valid = text.length >= 6
        }

        background = if (valid) validBackground else invalidBackground
    }

    private fun changeVisibility() {
        visibilityImage = if (show) visibilityOff else visibilityOn
        show = !show

        // Change visibility type of edit text
        inputType = if (show) {
            InputType.TYPE_CLASS_TEXT
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // When visibility changed, the cursor is at the very end of the sentence
        setSelection(this.length())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Password"
        typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (focused) {
            background = if (valid) validBackground else invalidBackground
            setButtonDrawables(end = visibilityImage)
        } else {
            background = inactiveBackground
            setButtonDrawables()
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val visibilityButtonStart: Float
            val visibilityButtonEnd: Float
            var isVisibilityButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibilityButtonEnd = (visibilityImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < visibilityButtonEnd -> isVisibilityButtonClicked = true
                }
            } else {
                visibilityButtonStart =
                    (width - paddingEnd - visibilityImage.intrinsicWidth).toFloat()
                when {
                    event.x > visibilityButtonStart -> isVisibilityButtonClicked = true
                }
            }

            if (isVisibilityButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        setButtonDrawables(end = visibilityImage)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        changeVisibility()
                        setButtonDrawables(end = visibilityImage)
                        true
                    }
                    else -> false
                }
            }
            return false
        }
        return false
    }
}