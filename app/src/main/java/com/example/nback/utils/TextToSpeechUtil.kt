package com.example.nback.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class TextToSpeechUtil {
    private var textToSpeech: TextToSpeech? = null

    fun initialize(appContext: Context?) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(
                appContext
            ) { status: Int ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech!!.language = Locale.UK
                }
            }
        }
    }

    fun speakNow(utterance: String?) {
        if (textToSpeech != null) {
            textToSpeech!!.speak(
                utterance, TextToSpeech.QUEUE_FLUSH,
                null, "" + utteranceId
            )
        }
    }

    fun shutdown() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
    }

    companion object {
        private const val utteranceId = 42
    }
}