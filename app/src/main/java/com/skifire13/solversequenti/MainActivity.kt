package com.skifire13.solversequenti

import android.app.Activity
import android.opengl.Visibility
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.skifire13.solversequenti.solver.derivazione
import com.skifire13.solversequenti.solver.parse
import com.skifire13.solversequenti.view.DerivazioneView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val derivazioneView: DerivazioneView = findViewById(R.id.derivazione_view)
        val inputText: EditText = findViewById(R.id.input_text)
        val errorText: TextView = findViewById(R.id.error_text)
        inputText.doOnTextChanged { text, _, _, _ ->
            val derivazione = parse(text.toString())?.derivazione()
            errorText.visibility = if(derivazione == null)VISIBLE else INVISIBLE
            derivazioneView.derivazione = derivazione
        }
    }
}