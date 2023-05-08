package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.text.TextWatcher
import android.text.Editable

class MainActivity : AppCompatActivity() {

    private lateinit var quantiaTotal: EditText
    private lateinit var numeroPessoas: EditText
    private lateinit var rachou: TextView
    private lateinit var compartilhar: Button
    private lateinit var reproduzir: Button
    private var resultado: Double = 0.0
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quantiaTotal = findViewById(R.id.quantia_total)
        numeroPessoas = findViewById(R.id.numero_pessoas)
        rachou = findViewById(R.id.rachou)
        compartilhar = findViewById(R.id.compartilhar)
        reproduzir = findViewById(R.id.reproduzir)

        // Inicialização do TextToSpeech
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.getDefault()
            }
        }

        // Botão Compartilhar
        compartilhar.setOnClickListener {
            val mensagem = "O valor rachado ficou de R$ $resultado para cada"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, mensagem)
            startActivity(Intent.createChooser(intent, "Compartilhar via"))
        }

        // Botão Reproduzir
        reproduzir.setOnClickListener {
            val mensagem = "O valor rachado ficou de R$ $resultado para cada"
            tts.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // Definindo ação quando houver mudança no valor da quantia total
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed
                calcularRachou()
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed
            }
        }

        quantiaTotal.addTextChangedListener(textWatcher)
        numeroPessoas.addTextChangedListener(textWatcher)
    }

    private fun calcularRachou() {
        val quantia = quantiaTotal.text.toString().toDoubleOrNull()
        val pessoas = numeroPessoas.text.toString().toIntOrNull()

        if (quantia != null && pessoas != null && quantia > 0 && pessoas > 0) {
            resultado = quantia / pessoas
            rachou.text = getString(R.string.valor_rachado, resultado.toString())
        } else {
            resultado = 0.0
            rachou.text = ""
        }
    }
}


