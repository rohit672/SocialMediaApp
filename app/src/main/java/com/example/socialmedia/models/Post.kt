package com.example.socialmedia.models

import android.location.Location
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

data class Post(
        var text : String = "",
        var postimg : String = "",
        var creator : String = "",
        var createdAt : Long = 0L,
        var postType : Int = 0,
        var location: String = "",
        var likedby : HashMap<String , Int> = HashMap()
) : Serializable