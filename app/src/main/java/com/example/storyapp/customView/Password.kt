package com.example.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class Password : AppCompatEditText {
    private var txtColor: Int = 0
    private var hintColor: Int = 0

    private lateinit var password: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,attrs,defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
        setTextColor(txtColor)
        setHintTextColor(txtColor)
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        setHint("Please set your password")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setAutofillHints(AUTOFILL_HINT_PASSWORD)
        }

        txtColor = ContextCompat.getColor(context, android.R.color.background_light)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8)
                    error = context.getString(R.string.pass)
            }
        })
    }
}