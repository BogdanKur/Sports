package com.example.sportsexercises

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sportsexercises.databinding.FragmentOpinionExerciseBinding
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL


class OpinionExerciseFragment : Fragment() {
    private var _binding: FragmentOpinionExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer

    var name = ""
    var groupMuscle = ""
    var muscle = ""
    var imageExercise = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpinionExerciseBinding.inflate(inflater, container, false)
        val view = binding.root
        val navController = findNavController()
        val loadControl =   DefaultLoadControl.Builder()
            .setBufferDurationsMs(1000, 200000, 1000, 1000)
        player = ExoPlayer.Builder(requireContext())
            .setLoadControl(loadControl.build())
            .build()
        binding.vvExercise.player = player
        binding.toolbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destination,_ ->
            destination.label = "Описание упражнения"
            binding.toolbar.title = destination.label
        }
        arguments?.let { bundle->
            name = bundle.getString("NAME").toString()
            groupMuscle = bundle.getString("GROUPMUSCLE").toString()
            muscle = bundle.getString("MUSCLE").toString()
            imageExercise = bundle.getString("IMAGE").toString()
            binding.tvName.text = name

            val cachedVideo = context?.let { getVideoFromCache(it, name) }
            if (cachedVideo != null) {
                val videoUri = Uri.fromFile(cachedVideo)
                player.setMediaItem(MediaItem.fromUri(videoUri))
                player.prepare()
                player.play()
            }
        }


        return view
    }

    fun getVideoFromCache(context: Context, fileName: String): File? {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)
        return if (file.exists()) file else null
    }

    override fun onStop() {
        super.onStop()
        player.pause()
        player.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.clearMediaItems()
    }

}