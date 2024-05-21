package com.rizaldi.mystoryapp

import com.rizaldi.mystoryapp.data.response.ResponseStory
import com.rizaldi.mystoryapp.data.response.StoryItem

object DataDummy {

    fun generateStories(): ResponseStory {
        val listStory = ArrayList<StoryItem>()
        for (i in 1..20) {
            val story = StoryItem(
                photoUrl = "https://lh3.googleusercontent.com/pOzD0lUsIE_V3IBFJAzXJaevwxMSVwrPEUKvRUWfnX6UEejCKZmO76nfpr5znR5pRngaeDZAz5CWagGFIJDt05wd4wOUnCIiz0ib1nBWPEf1XZPcsFk=w2880",
                createdAt = "2024-05-17T06:48:53Z",
                name = "Name -$i",
                description = "Description -$i",
                lon = i.toDouble() * 10,
                id = "id_$i",
                lat = i.toDouble() * 10

            )
            listStory.add(story)
        }

        return ResponseStory(
            listStory = listStory,
            error = false,
            message = "get Story succesfull"
        )
    }
}