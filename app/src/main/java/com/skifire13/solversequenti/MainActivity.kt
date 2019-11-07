package com.skifire13.solversequenti

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.otaliastudios.zoom.ZoomLayout
import com.skifire13.solversequenti.solver.derivazione
import com.skifire13.solversequenti.solver.parse
import com.skifire13.solversequenti.view.DerivazioneView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val derivazioneView: DerivazioneView = findViewById(R.id.derivazione_view)
        val inputTextLayout: TextInputLayout = findViewById(R.id.input_text_layout)
        val inputText: EditText = findViewById(R.id.input_text)
        val zoomLayout: ZoomLayout = findViewById(R.id.zoom_layout)
        inputText.doOnTextChanged { text, _, _, _ ->
            val derivazione = parse(text.toString())?.derivazione()
            inputTextLayout.error = if(derivazione == null) getString(R.string.error_input) else null
            derivazioneView.derivazione = derivazione
            zoomLayout.zoomTo(1f, false)
        }
    }
}