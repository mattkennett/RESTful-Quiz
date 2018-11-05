package edu.ualr.mpkennett.restfulquiz

data class QuizType(var quiz_id: String? = null
                    , var quiz_subject: String? = null)

data class QuizQuestion(var question: String? = null,
                        var options: Array<String>? = null,
                        var correct: Int? = null)