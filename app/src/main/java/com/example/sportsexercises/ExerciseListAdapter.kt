package com.example.sportsexercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExerciseListAdapter(val listOfExercises: List<Exercise>, val navController: NavController): RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>() {
    class ExerciseListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.btnOpenOpinion)
        val tvGroupMuscles: TextView = view.findViewById(R.id.tvGroupMuscles)
        val tvMuscle: TextView = view.findViewById(R.id.tvMuscle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_exercises, parent, false)
        return ExerciseListViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = listOfExercises.size

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        holder.button.text = listOfExercises[position].name
        holder.tvGroupMuscles.text = listOfExercises[position].groupMuscle
        holder.tvMuscle.text = listOfExercises[position].muscle
        holder.button.setOnClickListener {
            val bundle = Bundle().apply {
                putString("NAME", listOfExercises[position].name)
                putString("GROUPMUSCLE", listOfExercises[position].groupMuscle)
                putString("MUSCLE", listOfExercises[position].muscle)
                putString("IMAGE", listOfExercises[position].imageExercise)
                putString("DESCRIPTION", listOfExercises[position].description)
            }
            navController.navigate(R.id.action_exerciseListFragment_to_opinionExerciseFragment, bundle)
        }
    }
}