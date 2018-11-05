package edu.ualr.mpkennett.restfulquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class QuizActivity : AppCompatActivity() {
    lateinit var client: OkHttpClient
    lateinit var quizID: String

    lateinit var linearLayoutQuiz: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quizID = intent.getStringExtra(KEY_QUIZ_ID)
    }

    override fun onResume() {
        super.onResume()

        linearLayoutQuiz = findViewById(R.id.linearLayoutQuiz)
        linearLayoutQuiz.removeAllViews()

        val formBody: RequestBody = FormBody.Builder()
                .add("action", "get_quiz")
                .add("quiz_id", quizID)
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

                val quizQuestions: List<QuizQuestion> =
                        gson.fromJson(requestBody,
                                object : TypeToken<List<QuizQuestion>>() {}.type)

                uiThread {
                    for (question in quizQuestions) {
                        val newTextView = TextView(this@QuizActivity)
                        newTextView.text = question.question

                        linearLayoutQuiz.addView(newTextView)
                    }
                }
            }
        }
    }
}
