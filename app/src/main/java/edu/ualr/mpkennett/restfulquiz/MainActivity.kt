package edu.ualr.mpkennett.restfulquiz

import android.content.Intent
import android.net.Network
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

const val KEY_AUTHORIZATION: String = "authorized_user_ualr"
const val KEY_QUIZ_ID: String = "edu.ualr.mpkennett.restfulquiz.QUIZ_ID"
const val URL_QUIZ: String = "http://quizapp.mattkennett.com"

class MainActivity : AppCompatActivity() {
    lateinit var client: OkHttpClient

    lateinit var linearLayoutQuizzes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = OkHttpClient()
    }

    override fun onResume() {
        super.onResume()

        linearLayoutQuizzes = findViewById(R.id.linearLayoutQuizzes)
        linearLayoutQuizzes.removeAllViews()

        val formBody: RequestBody = FormBody.Builder()
                .add("action", "list_quizzes")
                .build()

        val request: Request = Request.Builder()
                .url(URL_QUIZ)
                .header("Authorization", KEY_AUTHORIZATION)
                .post(formBody)
                .build()

        doAsync {
            var response: Response? = NetworkHelper.runRequest(request)

            if (response != null) {
                val gson = Gson()

                val requestBody: String = response.body()!!.string()
                Log.d("MPK_UTILITY", requestBody)

                val quizList: List<QuizType> =
                        gson.fromJson(requestBody,
                                object : TypeToken<List<QuizType>>() {}.type)

                uiThread {
                    val intent = Intent(this@MainActivity, QuizActivity::class.java)
                    for (quiz in quizList) {
                        val newButton = Button(this@MainActivity)
                        newButton.text = quiz.quiz_subject
                        newButton.setOnClickListener {
                            intent.putExtra(KEY_QUIZ_ID, quiz.quiz_id)
                            startActivity(intent)
                        }

                        linearLayoutQuizzes.addView(newButton)
                    }
                }
            }
        }
    }
}
