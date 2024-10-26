package com.example.sportsexercises

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseListViewModel: ViewModel() {
    val exercises = mutableListOf(
        Exercise("Подъем EZ-штанги на скамье Скотта","Руки:","Бицепс", ""),
        Exercise("Подъем штанги на бицепс","Руки:","Бицепс", ""),
        Exercise("Сгибание одной рукой на скамье Скотта", "Руки:", "Бицепс", ""),

    )

    fun updateExerciseImage(name: String, newImage: String) {
        val item = exercises.find { it.name == name }
        item?.imageExercise = newImage
    }
    fun getExercise(): List<Exercise> {
        return exercises
    }

}