package com.example.nback.model

import android.util.Log
import com.example.nback.utils.TextToSpeechUtil

class NBackLogic() {

    // for visual stimuli
    private var visualCacheList: MutableList<Int> = mutableListOf(0, 0, 0)

    // for audio stimuli
    private val letters = arrayOf("A", "E", "Q", "R", "M")
    private var audioCacheList: MutableList<String> = mutableListOf("", "", "")

    fun nextVisualEvent(): Int {
        val random = (1..9).random()
        Log.i("Test123", "Random number: $random")
        visualCacheList.add(0, random)
        return random
    }

    fun nextAudioEvent(textToSpeechUtil: TextToSpeechUtil) {
        val random = (0..4).random()
        Log.i("Test123", "Random Letter: ${letters[random]}")
        audioCacheList.add(0, letters[random])
        textToSpeechUtil.speakNow(letters[random])
    }

    // A random number between 1 and 9 is placed in 2 lists
    // the cacheList is used for comparing the latest number added to the 3rd in the list
    // i.e. the number that was randomized 2 rounds ago
    // if the numbers are the same, i.e. a match, a message is displayed
    fun visualCheck(): Boolean {
        if (visualCacheList[0] == visualCacheList[2] && visualCacheList[2] != 0) {
            Log.i(
                "Test123",
                "n-back @ " + ": Number " + visualCacheList[0] + " occurred 2 rounds ago!"
            )
            return true
        }
        return false
    }

    fun audioCheck(): Boolean {
        if (audioCacheList[0] == audioCacheList[2] && audioCacheList[2] != "") {
            Log.i(
                "Test123",
                "n-back @ " + ": Letter " + audioCacheList[0] + " occurred 2 rounds ago!"
            )
            return true
        }
        return false
    }

    fun resetCacheLists() {
        visualCacheList = mutableListOf(0, 0, 0)
        audioCacheList = mutableListOf("", "", "")
    }

    // Displays both lists
    fun displayVisualList() {
        Log.i("Test123", "Cache List: $visualCacheList")
    }

    fun displayAudioList() {
        Log.i("Test123", "Audio Cache List: $audioCacheList")
    }
}