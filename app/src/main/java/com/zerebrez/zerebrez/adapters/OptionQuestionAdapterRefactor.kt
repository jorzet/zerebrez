package com.zerebrez.zerebrez.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.zerebrez.zerebrez.R
import com.zerebrez.zerebrez.models.QuestionOption
import com.zerebrez.zerebrez.models.enums.QuestionType
import kotlinx.android.synthetic.main.custom_question_refactor.view.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileInputStream


class OptionQuestionAdapterRefactor(texts : List<QuestionOption>, context: Context) : BaseAdapter() {
    private val mQuestionOption : List<QuestionOption> = texts
    private val mContext : Context = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val currentOption = getItem(position) as QuestionOption

        val inflator = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val optionView = inflator.inflate(R.layout.custom_question_refactor, null)

        when (currentOption.getQuestionType()) {
            QuestionType.TEXT -> {
                optionView.tv_option.text = currentOption.getQuestion()
                optionView.tv_option.visibility = View.VISIBLE
                optionView.mv_otion.visibility = View.GONE
                optionView.iv_option.visibility = View.GONE
            }
            QuestionType.EQUATION -> {
                //optionView.mv_otion.text = "$$"+currentOption.getQuestion()+"$$"
                optionView.mv_otion.setDisplayText("$$"+currentOption.getQuestion()+"$$")
                optionView.tv_option.visibility = View.GONE
                optionView.mv_otion.visibility = View.VISIBLE
                optionView.iv_option.visibility = View.GONE
            }

            QuestionType.IMAGE -> {
                optionView.iv_option.setImageBitmap(getBitmap(currentOption.getQuestion()))
                optionView.tv_option.visibility = View.GONE
                optionView.mv_otion.visibility = View.GONE
                optionView.iv_option.visibility = View.VISIBLE
            }
        }


        return optionView
    }

    override fun getItem(position: Int): Any {
        return mQuestionOption.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mQuestionOption.size
    }

    fun getBitmap(path: String): Bitmap? {
        try {
            var bitmap: Bitmap? = null
            val mainPath = Environment.getExternalStorageDirectory().toString()
            val f = File(mainPath + "/zerebrez/" +path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
}