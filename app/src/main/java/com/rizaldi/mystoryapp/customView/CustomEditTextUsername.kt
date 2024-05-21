package com.rizaldi.mystoryapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rizaldi.mystoryapp.R

class CustomEditTextUsername @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private lateinit var passwordIcon: Drawable

    init {
        init()
    }

    private fun init() {
        passwordIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24) as Drawable
        compoundDrawablePadding = 16
        setEmailDrawables(startOfTheText = passwordIcon)
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_NAME)
        }


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan Username"
        setTextColor(context.getColor(R.color.navy))
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


    private fun setEmailDrawables(
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
}