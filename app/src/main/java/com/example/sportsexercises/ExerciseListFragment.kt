package com.example.sportsexercises

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sportsexercises.databinding.FragmentExerciseListBinding
import com.google.android.gms.drive.Drive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class ExerciseListFragment : Fragment() {
    private var _binding: FragmentExerciseListBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: ExerciseListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseListBinding.inflate(inflater, container, false)
        val view = binding.root
        val navController = findNavController()
        viewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_,destination,_ ->
            destination.label = "Спортивные упражнения"
            binding.toolbar.title = destination.label
        }
        val adapter = ExerciseListAdapter(viewModel.getExercise(), navController)
        binding.rvListExercises.adapter = adapter

        getInfoFromServer()

        return view
    }

    private fun getInfoFromServer() {
        val client = OkHttpClient()
        val url = "https://www.googleapis.com/drive/v3/files?q='1gmTpk8sxNFFsZqXT5f-qYYWW1FMRR_h6'+in+parents&fields=files(id,name,mimeType)&key=AIzaSyAHwpSQ33uc39mapPr7pee0q_15X4YQYPQ"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("failureee", e.printStackTrace().toString())
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    response.body?.string()?.let { jsonResponse ->
                        parseResponse(jsonResponse)
                    }
                }else {
                    Log.e("fgdsd", "Response not successful: ${response.code} ${response.message}")
                }
            }
        })
    }

    private fun parseResponse(jsonResponse: String) {
        val jsonObject = JSONObject(jsonResponse)
        val files = jsonObject.getJSONArray("files")

        for (i in 0 until files.length()) {
            val file = files.getJSONObject(i)
            val fileId = file.getString("id")
            val fileName = file.getString("name")
            val mimeType = file.getString("mimeType")
            when {
                mimeType.startsWith("video/") -> {
                    val videoLink = "https://drive.google.com/uc?id=$fileId"
                    viewModel.updateExerciseImage(fileName.replace(".mp4", ""), videoLink)
                    saveVideoToCache(requireContext(), videoLink, fileName.replace(".mp4", ""))
                }
            }
        }
    }

    fun saveVideoToCache(context: Context, videoUrl: String, fileName: String) {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)

        if (file.exists()) {
            Log.d("Cache", "Файл уже существует: $fileName")
            return
        }
        try {
            val inputStream: InputStream = URL(videoUrl).openStream()
            val outputStream: OutputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Log.d("Cache", "Файл успешно сохранен: $fileName")
        } catch (e: Exception) {
            Log.e("Cache", "Ошибка при сохранении видео: ${e.message}")
        }
    }

}