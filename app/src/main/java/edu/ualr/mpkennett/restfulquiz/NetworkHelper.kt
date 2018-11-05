package edu.ualr.mpkennett.restfulquiz

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkHelper {
    companion object {
        val client: OkHttpClient = OkHttpClient()

        fun runRequest(request: Request): Response? {
            var response: Response? = null

            try {
                response = client.newCall(request).execute()
            } catch (e: Exception) {
                Log.d("MPK_UTILITY", "Network Error")
                Log.d("MPK_UTILITY", e.toString())
            }

            return response
        }
    }
}