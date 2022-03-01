package com.example.nback.model

import android.view.Menu
import android.view.MenuInflater
import com.example.nback.R

class MenuModel {
    // sets up the menu
    fun createMenu(menu: Menu?, settingsModel: SettingsModel, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        val itemVisual = menu?.findItem(R.id.visualOption)
        val itemAudio = menu?.findItem(R.id.audioOption)
        val itemFirstTime = menu?.findItem(R.id.firstTimeOption)
        val itemSecondTime = menu?.findItem(R.id.secondTimeOption)
        val itemThirdTime = menu?.findItem(R.id.thirdTimeOption)
        val itemFourthTime = menu?.findItem(R.id.fourthTimeOption)
        val itemFirstAmount = menu?.findItem(R.id.firstAmountOption)
        val itemSecondAmount = menu?.findItem(R.id.secondAmountOption)
        val itemThirdAmount = menu?.findItem(R.id.thirdAmountOption)

        if (settingsModel.stimuliSetting == "VISUAL") {
            itemVisual!!.isChecked = true
        } else if (settingsModel.stimuliSetting == "AUDIO") {
            itemAudio!!.isChecked = true
        }
        when (settingsModel.timeBetweenEventsSetting) {
            1000 -> {
                itemFirstTime!!.isChecked = true
            }
            2000 -> {
                itemSecondTime!!.isChecked = true
            }
            5000 -> {
                itemThirdTime!!.isChecked = true
            }
            10000 -> {
                itemFourthTime!!.isChecked = true
            }
        }
        when (settingsModel.nrOfEventsSetting) {
            10 -> {
                itemFirstAmount!!.isChecked = true
            }
            15 -> {
                itemSecondAmount!!.isChecked = true
            }
            20 -> {
                itemThirdAmount!!.isChecked = true
            }
        }
    }
}