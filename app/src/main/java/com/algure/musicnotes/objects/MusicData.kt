package com.algure.musicnotes.objects

import android.graphics.Color

class MusicData(title:String="", description:String="", lengthInSec:Int=0, color: Int = Color.BLACK) {

 var title: String = ""
 var description: String = ""
 var lengthInSec: Int = 0
 var color: Int = 0

    init {
        this.title = title
        this.description = description
        this.lengthInSec = lengthInSec
        this.color = color
    }
}