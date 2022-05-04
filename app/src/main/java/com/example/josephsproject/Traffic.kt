package com.example.josephsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


class Traffic : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        Log.e("Traffic","started Traffic")
        val queue = Volley.newRequestQueue(this)
        val url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"
        var gson = Gson()
        var camList = ArrayList<Camera>()


        print("Json Request")
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val camJsonArray = response.getJSONArray("Features")
                for( i in 0 until camJsonArray.length())
                {
                    var jsonEle = camJsonArray.getJSONObject(i)
                    var camjson = jsonEle.getJSONArray("Cameras").getJSONObject(0)
                    var tempCam = Camera(camjson.getString("Id"),camjson.getString("Description"),
                        camjson.getString("ImageUrl"),camjson.getString("Type"))
                    camList.add(tempCam)
                    Log.e("Traffic","Camera added to list")

                }
                val recyclerView = findViewById<RecyclerView>(R.id.cam_recycle)
                recyclerView.adapter = CameraAdapter(camList)
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )




// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)


        Log.e("Traffic","Adapter set")
    }
}