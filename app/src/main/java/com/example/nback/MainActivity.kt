package com.example.nback

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.nback.model.NBackLogic
import com.example.nback.utils.TextToSpeechUtil
import java.util.*
import androidx.core.content.res.ResourcesCompat
import com.example.nback.model.MenuModel
import com.example.nback.model.SettingsModel
import com.example.nback.utils.UiUtils

class MainActivity : AppCompatActivity() {

    // variables
    private var currentEventCount = 0
    private var eventMatchCount = 0
    private var nrOfCorrectAnswers = 0
    private var started = false

    // timer
    private var eventTimer: Timer? = null
    private var handler: Handler? = null

    // images
    private lateinit var imageViews: Array<ImageView?>
    private lateinit var squareDrawable: Drawable

    // tts
    private lateinit var textToSpeechUtil: TextToSpeechUtil

    // ui
    private lateinit var mMatchCountTextView: TextView
    private lateinit var mEventCountTextView: TextView
    private lateinit var mTimeBetweenEventsTextView: TextView
    private lateinit var mNrOfEventsTextView: TextView
    private lateinit var mStimuliTextView: TextView
    private lateinit var mButton: Button

    // model/logic
    private val nBackLogic = NBackLogic()
    private val settingsModel = SettingsModel()
    private val menuModel = MenuModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI
        mMatchCountTextView = findViewById(R.id.matchCountTextView)
        mMatchCountTextView.text = "0"
        mEventCountTextView = findViewById(R.id.eventCountTextView)
        mEventCountTextView.text = "0"
        mTimeBetweenEventsTextView = findViewById(R.id.timeBetweenEventsTextView)
        mNrOfEventsTextView = findViewById(R.id.nrOfEventsTextView)
        mButton = findViewById(R.id.button)
        mButton.text = "Start"
        mStimuliTextView = findViewById(R.id.stimuliTextView)

        // tts
        textToSpeechUtil = TextToSpeechUtil()

        // model
        nBackLogic.displayAudioList()
        nBackLogic.displayVisualList()

        // timer
        handler = Handler()

        // image views
        imageViews = loadReferencesToImageViews()

        // images
        squareDrawable = ResourcesCompat.getDrawable(resources, R.drawable.visual_stimuli, null)!!

        mButton.setOnClickListener {
            if (started) {
                mButton.isEnabled = false
                //if (currentEventCount > 2) {
                if (settingsModel.stimuliSetting == "VISUAL") {
                    if (nBackLogic.visualCheck()) {
                        eventMatchCount++
                        updateMatchCount()
                        mButton.setTextColor(Color.GREEN)
                    } else {
                        mButton.setTextColor(Color.RED)
                    }
                } else if (settingsModel.stimuliSetting == "AUDIO") {
                    if (nBackLogic.audioCheck()) {
                        eventMatchCount++
                        updateMatchCount()
                        mButton.setTextColor(Color.GREEN)
                    } else {
                        mButton.setTextColor(Color.RED)
                    }
                }
                /*} else {
                    Log.i("Test123", "EventCount = $currentEventCount")
                    mButton.setTextColor(Color.RED)
                }*/
            } else {
                textToSpeechUtil.speakNow("Starting round!")
                mButton.text = "Match"
                started = startTimer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        textToSpeechUtil.shutdown()
        cancelTimer()

        val prefsEditor = getSharedPreferences("sharedPref", Context.MODE_PRIVATE).edit()
        prefsEditor.putString("saved_stimuliSetting", settingsModel.stimuliSetting)
        prefsEditor.putInt(
            "saved_timeBetweenEventsSetting",
            settingsModel.timeBetweenEventsSetting
        )
        prefsEditor.putInt("saved_nrOfEventsSetting", settingsModel.nrOfEventsSetting)
        prefsEditor.apply()
    }

    override fun onResume() {
        super.onResume()
        val mPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        settingsModel.stimuliSetting =
            mPreferences.getString("saved_stimuliSetting", "VISUAL").toString()
        settingsModel.timeBetweenEventsSetting =
            mPreferences.getInt("saved_timeBetweenEventsSetting", 2000)
        settingsModel.nrOfEventsSetting = mPreferences.getInt("saved_nrOfEventsSetting", 10)

        refreshTextViews()
        textToSpeechUtil = TextToSpeechUtil()
        textToSpeechUtil.initialize(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuModel.createMenu(menu, settingsModel, menuInflater)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.visualOption -> {
                if (started) {
                    Toast.makeText(
                        this,
                        "Can't change stimuli during round!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    settingsModel.stimuliSetting = "VISUAL"
                    item.isChecked = true
                    refreshTextViews()
                }
                return true
            }

            R.id.audioOption -> {
                if (started) {
                    Toast.makeText(
                        this,
                        "Can't change stimuli during round!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    settingsModel.stimuliSetting = "AUDIO"
                    item.isChecked = true
                    refreshTextViews()
                }
                return true
            }

            R.id.firstTimeOption -> {
                settingsModel.timeBetweenEventsSetting = 1000
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.secondTimeOption -> {
                settingsModel.timeBetweenEventsSetting = 2000
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.thirdTimeOption -> {
                settingsModel.timeBetweenEventsSetting = 5000
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.fourthTimeOption -> {
                settingsModel.timeBetweenEventsSetting = 10000
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.firstAmountOption -> {
                settingsModel.nrOfEventsSetting = 10
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.secondAmountOption -> {
                settingsModel.nrOfEventsSetting = 15
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.thirdAmountOption -> {
                settingsModel.nrOfEventsSetting = 20
                item.isChecked = true
                refreshTextViews()
                return true
            }

            R.id.stop -> {
                if (!started) {
                    Toast.makeText(this, "Not yet started", Toast.LENGTH_SHORT).show()
                } else {
                    cancelTimer()
                    resetEvent()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // starts the timer
    private fun startTimer(): Boolean {
        if (eventTimer == null) { // first, check if task is already running
            eventTimer = Timer()
            // schedule a new task: task , delay, period (milliseconds)
            eventTimer!!.schedule(
                EventTimerTask(),
                1500,
                settingsModel.timeBetweenEventsSetting.toLong()
            )
            return true // new task started
        }
        return false
    }

    // cancels the timer
    private fun cancelTimer(): Boolean {
        if (eventTimer != null) {
            eventTimer!!.cancel()
            eventTimer = null
            Log.i("MsgTask", "Timer canceled")
            return true // task canceled
        }
        return false
    }

    // the task to execute periodically
    private inner class EventTimerTask : TimerTask() {
        override fun run() {
            if (currentEventCount >= settingsModel.nrOfEventsSetting) {
                handler!!.post {
                    textToSpeechUtil.speakNow("Round over. You got $eventMatchCount out of $nrOfCorrectAnswers answers correct! ")
                    UiUtils.createDialog(
                        this@MainActivity,
                        "Round over",
                        "You got $eventMatchCount/$nrOfCorrectAnswers answers correct!"
                    ).show()
                    resetEvent()
                }
                cancel()
            } else {
                handler!!.post {
                    mButton.isEnabled = true
                }
                currentEventCount++

                Log.i("Test123", "EventCount $currentEventCount")
                // post message to main thread
                handler!!.post {
                    updateEventCount()
                    mButton.setTextColor(Color.WHITE)
                }
                if (settingsModel.stimuliSetting == "VISUAL") {
                    if (nBackLogic.visualCheck()) {
                        nrOfCorrectAnswers++
                    }
                    val index = nBackLogic.nextVisualEvent()
                    // post message to main thread
                    handler!!.post {
                        updateImageViews(index - 1)
                    }
                } else {
                    if (nBackLogic.audioCheck()) {
                        nrOfCorrectAnswers++
                    }
                    nBackLogic.nextAudioEvent(textToSpeechUtil)
                }
            }
        }
    }

    // ui helpers
    private fun updateImageViews(index: Int) {
        imageViews[index]!!.setImageDrawable(squareDrawable) // index in array imageViews
        handler!!.postDelayed(
            {
                imageViews[index]!!.setImageDrawable(null)
            },
            800 // value in milliseconds
        )
    }

    // loads image references
    private fun loadReferencesToImageViews(): Array<ImageView?> {
        val imgViews = arrayOfNulls<ImageView>(9)
        var imageViewId: String
        var viewId: Int
        for (i in 0..8) {
            imageViewId = "imageView$i"
            viewId = resources.getIdentifier(imageViewId, "id", packageName)
            imgViews[i] = findViewById(viewId)
        }
        return imgViews
    }

    // gets ready for the next round
    private fun resetEvent() {
        currentEventCount = 0
        eventMatchCount = 0
        nrOfCorrectAnswers = 0
        mButton.text = "Start"
        mButton.setTextColor(Color.WHITE)
        mButton.isEnabled = true
        started = false
        eventTimer = null
        nBackLogic.resetCacheLists()
        updateEventCount()
        updateMatchCount()
    }

    // updates ui textViews
    private fun refreshTextViews() {
        var temp = settingsModel.timeBetweenEventsSetting / 1000
        mTimeBetweenEventsTextView.text = "$temp s"
        mNrOfEventsTextView.text = settingsModel.nrOfEventsSetting.toString()
        mStimuliTextView.text = settingsModel.stimuliSetting
    }

    // updates ui number of events
    private fun updateEventCount() {
        mEventCountTextView.text = currentEventCount.toString()
    }

    // updates ui number of correct matches
    private fun updateMatchCount() {
        mMatchCountTextView.text = eventMatchCount.toString()
    }
}