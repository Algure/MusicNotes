package com.algure.musicnotes.provider

import android.graphics.Color
import com.algure.musicnotes.objects.MusicData

object DataProvider {


    val musicTestData:List<MusicData> = mutableListOf(
        MusicData(title = "Test title 1", description = "dfvcdfvdfvd fvdfnv", color = Color.BLACK, lengthInSec = 5),
        MusicData(title = "Test title 2", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#FF0000"), lengthInSec = 6),
        MusicData(title = "Test title 3", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#11AA00"), lengthInSec = 7),
        MusicData(title = "Test title 4", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#22AA99"), lengthInSec = 4),
        MusicData(title = "Test title 5", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#22AA55"), lengthInSec = 5),
        MusicData(title = "Test title 6", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#1100FF"), lengthInSec = 8),
        MusicData(title = "Test title 7", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#FF00FF"), lengthInSec = 2),
        MusicData(title = "Test title 8", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#AA00FF"), lengthInSec = 3),
    )


}