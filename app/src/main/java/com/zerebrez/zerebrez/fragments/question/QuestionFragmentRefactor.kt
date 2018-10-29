/*
 * Copyright [2018] [Jorge Zepeda Tinoco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zerebrez.zerebrez.fragments.question

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import com.nishant.math.MathView // uncomment if is needed
import com.zerebrez.zerebrez.R
import com.zerebrez.zerebrez.adapters.OptionQuestionAdapterRefactor
import com.zerebrez.zerebrez.fragments.content.BaseContentFragment
import com.zerebrez.zerebrez.models.Image
import com.zerebrez.zerebrez.services.database.DataHelper
import com.zerebrez.zerebrez.ui.activities.QuestionActivity
import com.zerebrez.zerebrez.utils.FontUtil
import katex.hourglass.`in`.mathlib.MathView
import android.widget.ScrollView
import com.zerebrez.zerebrez.adapters.QuestionAnswerAdapterRefactor
import com.zerebrez.zerebrez.models.QuestionNewFormat
import com.felipecsl.gifimageview.library.GifImageView

/**
 * Created by Jorge Zepeda Tinoco on 29/05/18.
 * jorzet.94@gmail.com
 */

class QuestionFragmentRefactor : BaseContentFragment(), View.OnClickListener {

    /*
     * tags
     */
    private val OPTION_A : String = "a"
    private val OPTION_B : String = "b"
    private val OPTION_C : String = "c"
    private val OPTION_D : String = "d"

    /*
     * UI accessors
     */
    private lateinit var mQuestionList : RecyclerView
    private lateinit var mQuestion : TextView
    private lateinit var mOptionA : View
    private lateinit var mOptionB : View
    private lateinit var mOptionC : View
    private lateinit var mOptionD : View
    private lateinit var mTextAnswerA : TextView
    private lateinit var mTextAnswerB : TextView
    private lateinit var mTextAnswerC : TextView
    private lateinit var mTextAnswerD : TextView
    private lateinit var mEquationAnswerA : MathView
    private lateinit var mEquationAnswerB : MathView
    private lateinit var mEquationAnswerC: MathView
    private lateinit var mEquationAnswerD : MathView
    private lateinit var mImageAnswerA : ImageView
    private lateinit var mImageAnswerB : ImageView
    private lateinit var mImageAnswerC : ImageView
    private lateinit var mImageAnswerD : ImageView
    private lateinit var mGifImageAnswerA : GifImageView
    private lateinit var mGifImageAnswerB : GifImageView
    private lateinit var mGifImageAnswerC : GifImageView
    private lateinit var mGifImageAnswerD : GifImageView
    private lateinit var mQuestionContainerView : LinearLayout
    private lateinit var mQuestionsScrolView : ScrollView
    private lateinit var mOptionATextView: TextView
    private lateinit var mOptionBTextView: TextView
    private lateinit var mOptionCTextView: TextView
    private lateinit var mOptionDTextView: TextView

    /*
     * Adapter
     */
    private lateinit var optionQuestionAdapter : OptionQuestionAdapterRefactor
    private lateinit var questionAnswerAdapterRefactor: QuestionAnswerAdapterRefactor

    /*
     * Objects
     */
    //private var question : Question? = null
    private var questionNewFormat : QuestionNewFormat? = null
    private lateinit var mImagesPath : List<Image>

    /*
     * Variables
     */
    private var isAnswered : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (container == null)
            return null

        val rootView = inflater.inflate(R.layout.question_fragment_refactor, container, false)!!
        mQuestionList = rootView.findViewById(R.id.nslv_container)
        //mQuestion = rootView.findViewById(R.id.tv_question)
        mOptionA = rootView.findViewById(R.id.option_a)
        mOptionB = rootView.findViewById(R.id.option_b)
        mOptionC = rootView.findViewById(R.id.option_c)
        mOptionD = rootView.findViewById(R.id.option_d)
        mTextAnswerA = rootView.findViewById(R.id.tv_answer_a)
        mTextAnswerB = rootView.findViewById(R.id.tv_answer_b)
        mTextAnswerC = rootView.findViewById(R.id.tv_answer_c)
        mTextAnswerD = rootView.findViewById(R.id.tv_answer_d)
        mEquationAnswerA = rootView.findViewById(R.id.mv_answer_a)
        mEquationAnswerB = rootView.findViewById(R.id.mv_answer_b)
        mEquationAnswerC = rootView.findViewById(R.id.mv_answer_c)
        mEquationAnswerD = rootView.findViewById(R.id.mv_answer_d)
        mImageAnswerA = rootView.findViewById(R.id.iv_answer_a)
        mImageAnswerB = rootView.findViewById(R.id.iv_answer_b)
        mImageAnswerC = rootView.findViewById(R.id.iv_answer_c)
        mImageAnswerD = rootView.findViewById(R.id.iv_answer_d)
        mGifImageAnswerA = rootView.findViewById(R.id.giv_answer_a)
        mGifImageAnswerB = rootView.findViewById(R.id.giv_answer_b)
        mGifImageAnswerC = rootView.findViewById(R.id.giv_answer_c)
        mGifImageAnswerD = rootView.findViewById(R.id.giv_answer_d)
        mQuestionContainerView = rootView.findViewById(R.id.ll_question_container)
        mQuestionsScrolView = rootView.findViewById(R.id.questions_scroll)
        mOptionATextView = rootView.findViewById(R.id.tv_option_a)
        mOptionBTextView = rootView.findViewById(R.id.tv_option_b)
        mOptionCTextView = rootView.findViewById(R.id.tv_option_c)
        mOptionDTextView = rootView.findViewById(R.id.tv_option_d)

        mOptionATextView.typeface = FontUtil.getNunitoSemiBold(context!!)
        mOptionBTextView.typeface = FontUtil.getNunitoSemiBold(context!!)
        mOptionCTextView.typeface = FontUtil.getNunitoSemiBold(context!!)
        mOptionDTextView.typeface = FontUtil.getNunitoSemiBold(context!!)

        //question = (activity as QuestionActivity).getQuestion()
        questionNewFormat = (activity as QuestionActivity).getQuestionNewFormat()

        val dataHelper = DataHelper(context!!)
        mImagesPath = dataHelper.getImagesPath()

        if (questionNewFormat != null) {

            var hasEquation = false

            for (quetionType in questionNewFormat!!.questionTypes) {
                if (quetionType.equals("eq")) {
                    hasEquation = true
                }
            }

            if (hasEquation) {
                if (activity != null)
                    (activity as QuestionActivity).showLoading(true)
                mOptionA.isEnabled = false
                mOptionB.isEnabled = false
                mOptionC.isEnabled = false
                mOptionD.isEnabled = false
                if (activity != null)
                    (activity as QuestionActivity).enableDisableAnswerButton(false)

                var TIME_DELAY : Long = 0
                if (android.os.Build.VERSION.SDK_INT <= 22){
                    TIME_DELAY = 7000
                } else{
                    TIME_DELAY = 3000
                }

                Handler().postDelayed({
                    if (activity != null) {
                        (activity as QuestionActivity).showLoading(false)
                        if (questionNewFormat!!.stepByStepData.isNotEmpty()) {
                            (activity as QuestionActivity).enableDisableAnswerButton(true)
                        }
                    }
                    mOptionA.isEnabled = true
                    mOptionB.isEnabled = true
                    mOptionC.isEnabled = true
                    mOptionD.isEnabled = true
                }, TIME_DELAY)

            } else {
                if (activity != null) {
                    (activity as QuestionActivity).showLoading(false)
                    if (questionNewFormat!!.stepByStepData.isNotEmpty()) {
                        (activity as QuestionActivity).enableDisableAnswerButton(true)
                    }
                }
                mOptionA.isEnabled = true
                mOptionB.isEnabled = true
                mOptionC.isEnabled = true
                mOptionD.isEnabled = true
            }

            val realSize = questionNewFormat!!.questionData.size
            var textSize = 0

            for (i in 0 .. realSize -1 ) {

                if ((questionNewFormat!!.questionTypes[i].equals("txt") ||
                                questionNewFormat!!.questionTypes[i].equals("eq"))
                        && questionNewFormat!!.questionData.size > i) {

                    textSize += questionNewFormat!!.questionData.get(i).length

                } else if (questionNewFormat!!.questionTypes[i].equals("img") &&
                        questionNewFormat!!.questionData.size > i) {

                    textSize += 200

                }
            }

            if (realSize >= 3 || textSize > 250 ) {
                (activity as QuestionActivity).showHideExpandedQuestionButton(true)
            } else {
                (activity as QuestionActivity).showHideExpandedQuestionButton(false)
            }

            //optionQuestionAdapter = OptionQuestionAdapterRefactor(false, questionNewFormat!!, mImagesPath, context!!)
            questionAnswerAdapterRefactor = QuestionAnswerAdapterRefactor(false, questionNewFormat!!, mImagesPath, context!!)

            setOptions()
            setAnswers()
        }

        return rootView
    }

    private fun setOptions() {
        //mQuestionList.adapter = optionQuestionAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mQuestionList.setLayoutManager(linearLayoutManager)
        mQuestionList.adapter = questionAnswerAdapterRefactor
    }

    private fun setAnswers() {
        isAnswered = false

        if (questionNewFormat != null) {
            for (i in 0..questionNewFormat!!.optionsTypes.size - 1) {
                val optionType = questionNewFormat!!.optionsTypes[i]
                when (i) {
                    0 -> {
                        when (optionType) {
                            "txt" -> {
                                mTextAnswerA.setText(questionNewFormat!!.optionsData[i])
                                mTextAnswerA.visibility = View.VISIBLE
                                mTextAnswerA.typeface = FontUtil.getNunitoRegular(context!!)
                            }
                            "eq" -> {
                                mEquationAnswerA.setDisplayText("$$" + questionNewFormat!!.optionsData[i] + "$$")
                                mEquationAnswerA.visibility = View.VISIBLE
                            }
                            "img" -> {
                                val nameInStoregeA = getNameInStorage(questionNewFormat!!.optionsData[i], mImagesPath)
                                if (nameInStoregeA.contains(".gif")) {
                                    mGifImageAnswerA.setImageBitmap(getBitmap(nameInStoregeA))
                                    mGifImageAnswerA.startAnimation()
                                    mGifImageAnswerA.visibility = View.VISIBLE
                                } else {
                                    mImageAnswerA.setImageBitmap(getBitmap(nameInStoregeA))
                                    mImageAnswerA.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    1 -> {
                        when (optionType) {
                            "txt" -> {
                                mTextAnswerB.setText(questionNewFormat!!.optionsData[i])
                                mTextAnswerB.visibility = View.VISIBLE
                                mTextAnswerB.typeface = FontUtil.getNunitoRegular(context!!)
                            }
                            "eq" -> {
                                mEquationAnswerB.setDisplayText("$$" + questionNewFormat!!.optionsData[i] + "$$")
                                mEquationAnswerB.visibility = View.VISIBLE
                            }
                            "img" -> {
                                val nameInStoregeB = getNameInStorage(questionNewFormat!!.optionsData[i], mImagesPath)
                                if (nameInStoregeB.contains(".gif")) {
                                    mGifImageAnswerB.setImageBitmap(getBitmap(nameInStoregeB))
                                    mGifImageAnswerB.startAnimation()
                                    mGifImageAnswerB.visibility = View.VISIBLE
                                } else {
                                    mImageAnswerB.setImageBitmap(getBitmap(nameInStoregeB))
                                    mImageAnswerB.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    2 -> {
                        when (optionType) {
                            "txt" -> {
                                mTextAnswerC.setText(questionNewFormat!!.optionsData[i])
                                mTextAnswerC.visibility = View.VISIBLE
                                mTextAnswerC.typeface = FontUtil.getNunitoRegular(context!!)
                            }
                            "eq" -> {
                                mEquationAnswerC.setDisplayText("$$" + questionNewFormat!!.optionsData[i] + "$$")
                                mEquationAnswerC.visibility = View.VISIBLE
                            }
                            "img" -> {
                                val nameInStoregeC = getNameInStorage(questionNewFormat!!.optionsData[i], mImagesPath)
                                if (nameInStoregeC.contains(".gif")) {
                                    mGifImageAnswerC.setImageBitmap(getBitmap(nameInStoregeC))
                                    mGifImageAnswerC.startAnimation()
                                    mGifImageAnswerC.visibility = View.VISIBLE
                                } else {
                                    mImageAnswerC.setImageBitmap(getBitmap(nameInStoregeC))
                                    mImageAnswerC.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    3 -> {
                        when (optionType) {
                            "txt" -> {
                                mTextAnswerD.setText(questionNewFormat!!.optionsData[i])
                                mTextAnswerD.visibility = View.VISIBLE
                                mTextAnswerD.typeface = FontUtil.getNunitoRegular(context!!)
                            }
                            "eq" -> {
                                mEquationAnswerD.setDisplayText("$$" + questionNewFormat!!.optionsData[i] + "$$")
                                mEquationAnswerD.visibility = View.VISIBLE
                            }
                            "img" -> {
                                val nameInStoregeD = getNameInStorage(questionNewFormat!!.optionsData[i], mImagesPath)
                                if (nameInStoregeD.contains(".gif")) {
                                    mGifImageAnswerD.setImageBitmap(getBitmap(nameInStoregeD))
                                    mGifImageAnswerD.startAnimation()
                                    mGifImageAnswerD.visibility = View.VISIBLE
                                } else {
                                    mImageAnswerD.setImageBitmap(getBitmap(nameInStoregeD))
                                    mImageAnswerD.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }

            }

            mOptionA.setOnClickListener(this)
            mOptionB.setOnClickListener(this)
            mOptionC.setOnClickListener(this)
            mOptionD.setOnClickListener(this)
        }
    }

    fun setListeners() {

    }

    override fun onClick(view: View?) {
        val answer = questionNewFormat!!.answer

        when (view!!.id) {
            R.id.option_a -> {
                when (answer) {
                    OPTION_A -> {
                        mOptionA.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("a", true)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_B -> {
                        mOptionA.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionB.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        isAnswered = true
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("a", false)
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_C -> {
                        mOptionA.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionC.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        isAnswered = true
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("a", false)
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                    OPTION_D -> {
                        mOptionA.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionD.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        isAnswered = true
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("a", false)
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                }
                mOptionA.setOnClickListener(null)
                mOptionB.setOnClickListener(null)
                mOptionC.setOnClickListener(null)
                mOptionD.setOnClickListener(null)
                (activity as QuestionActivity).setNextQuestionEnable(true)
            }
            R.id.option_b -> {
                when (answer) {
                    OPTION_A -> {
                        mOptionB.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionA.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("b", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_B -> {
                        mOptionB.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("b", true)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_C -> {
                        mOptionB.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionC.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("b", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                    OPTION_D -> {
                        mOptionB.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionD.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("b", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                }
                mOptionA.setOnClickListener(null)
                mOptionB.setOnClickListener(null)
                mOptionC.setOnClickListener(null)
                mOptionD.setOnClickListener(null)
                (activity as QuestionActivity).setNextQuestionEnable(true)
            }
            R.id.option_c -> {
                when (answer) {
                    OPTION_A -> {
                        mOptionC.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionA.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("c", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_B -> {
                        mOptionC.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionB.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("c", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_C -> {
                        mOptionC.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("c", true)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                    OPTION_D -> {
                        mOptionC.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionD.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("c", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                }
                mOptionA.setOnClickListener(null)
                mOptionB.setOnClickListener(null)
                mOptionC.setOnClickListener(null)
                mOptionD.setOnClickListener(null)
                (activity as QuestionActivity).setNextQuestionEnable(true)
            }
            R.id.option_d -> {
                when (answer) {
                    OPTION_A -> {
                        mOptionD.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionA.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("d", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_B -> {
                        mOptionD.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionB.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("d", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                        }, 100L)
                    }
                    OPTION_C -> {
                        mOptionD.background = resources.getDrawable(R.drawable.answer_wrong_option_background)
                        mOptionC.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("d", false)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)
                    }
                    OPTION_D -> {
                        mOptionD.background = resources.getDrawable(R.drawable.answer_correct_option_background)
                        (activity as QuestionActivity).setQuestionNewFormatAnswer("d", true)
                        isAnswered = true
                        mQuestionsScrolView.postDelayed(Runnable {
                            //replace this line to scroll up or down
                            mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                        }, 100L)

                    }
                }
                mOptionA.setOnClickListener(null)
                mOptionB.setOnClickListener(null)
                mOptionC.setOnClickListener(null)
                mOptionD.setOnClickListener(null)
                (activity as QuestionActivity).setNextQuestionEnable(true)
            }
        }

    }

    fun getBitmap(path: String): Bitmap? {
        try {

            var bitmap: Bitmap? = null
            val f = context!!.openFileInput(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            bitmap = BitmapFactory.decodeStream(f, null, options)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    private fun getNameInStorage(imageId : String, mImagesPath : List<Image>) : String {
        var nameInStorage = ""
        for (image in mImagesPath) {
            if (imageId.equals("i"+image.getImageId())) {
                nameInStorage = image.getNameInStorage()
                return nameInStorage
            }
        }
        return ""
    }

    fun showAnswerQuestion() {
        if (!isAnswered) {
            val answer = questionNewFormat!!.answer
            when (answer) {
                OPTION_A -> {
                    mOptionA.background = resources.getDrawable(R.drawable.show_answer_option_background)
                    mOptionB.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionC.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionD.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    (activity as QuestionActivity).setQuestionNewFormatAnswer("", false)
                    mQuestionsScrolView.postDelayed(Runnable {
                        //replace this line to scroll up or down
                        mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                    }, 100L)
                }
                OPTION_B -> {
                    mOptionA.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionB.background = resources.getDrawable(R.drawable.show_answer_option_background)
                    mOptionC.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionD.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    (activity as QuestionActivity).setQuestionNewFormatAnswer("", false)
                    mQuestionsScrolView.postDelayed(Runnable {
                        //replace this line to scroll up or down
                        mQuestionsScrolView.fullScroll(ScrollView.FOCUS_UP)
                    }, 100L)
                }
                OPTION_C -> {
                    mOptionA.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionB.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionC.background = resources.getDrawable(R.drawable.show_answer_option_background)
                    mOptionD.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    (activity as QuestionActivity).setQuestionNewFormatAnswer("", false)
                    mQuestionsScrolView.postDelayed(Runnable {
                        //replace this line to scroll up or down
                        mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                    }, 100L)
                }
                OPTION_D -> {
                    mOptionA.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionB.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionC.background = resources.getDrawable(R.drawable.answer_unselected_option_background)
                    mOptionD.background = resources.getDrawable(R.drawable.show_answer_option_background)
                    (activity as QuestionActivity).setQuestionNewFormatAnswer("", false)
                    mQuestionsScrolView.postDelayed(Runnable {
                        //replace this line to scroll up or down
                        mQuestionsScrolView.fullScroll(ScrollView.FOCUS_DOWN)
                    }, 100L)
                }
            }
        }

        mOptionA.setOnClickListener(null)
        mOptionB.setOnClickListener(null)
        mOptionC.setOnClickListener(null)
        mOptionD.setOnClickListener(null)
        (activity as QuestionActivity).setNextQuestionEnable(true)
    }

    fun showExpandedQuestion(showExpanded : Boolean) {
        val param : LinearLayout.LayoutParams
        if (showExpanded) {
            param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.18f)

        } else {
            param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f)
        }
        mQuestionContainerView.layoutParams = param
    }

    fun isAnswered() : Boolean {
        return this.isAnswered
    }

}