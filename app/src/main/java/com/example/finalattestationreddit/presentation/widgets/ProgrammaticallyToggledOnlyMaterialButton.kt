package com.example.finalattestationreddit.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class ProgrammaticallyToggledOnlyMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    init {
        isToggleCheckedStateOnClick = false
    }

}