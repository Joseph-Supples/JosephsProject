package com.example.josephsproject

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
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

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }



        val cm = getSystemService(ConnectivityManager::class.java)
        val info = cm.activeNetworkInfo

        val avail = info?.isAvailable
        val connected = info?.isConnected


        if(info != null && avail == true && connected == true) {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val camJsonArray = response.getJSONArray("Features")
                    for (i in 0 until camJsonArray.length()) {
                        var jsonEle = camJsonArray.getJSONObject(i)
                        var camjson = jsonEle.getJSONArray("Cameras").getJSONObject(0)
                        var camId = camjson.getString("Id")
                        var camDesc = camjson.getString("Description")
                        var camUrl = camjson.getString("ImageUrl")
                        var camType = camjson.getString("Type")

                        if (camType.equals("sdot")) {
                            camUrl = "https://www.seattle.gov/trafficcams/images/" + camUrl
                        } else {
                            camUrl = "https://images.wsdot.wa.gov/nw/" + camUrl
                        }


                        var tempCam = Camera(camId, camDesc, camUrl, camType)
                        camList.add(tempCam)
                        Log.e("Traffic", "Camera added to list")

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

        }
        else{
            Toast.makeText(this,"No internet/connection available - try again!",Toast.LENGTH_LONG).show()
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}