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

package com.zerebrez.zerebrez.fragments.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zerebrez.zerebrez.R
import com.zerebrez.zerebrez.adapters.SchoolAverageCanvas
import com.zerebrez.zerebrez.fragments.content.BaseContentFragment
import com.zerebrez.zerebrez.services.database.DataHelper
import com.zerebrez.zerebrez.utils.FontUtil

/**
 * Created by Jorge Zepeda Tinoco on 20/03/18.
 * jorzet.94@gmail.com
 */

class SchoolsAverageFragment : BaseContentFragment() {

    /**
     * UI accessors
     */
    private lateinit var schoolAverageCanvas : SchoolAverageCanvas
    private lateinit var mNot128ExmanQuestionDitIt : TextView
    private lateinit var mComipemsYearTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (container == null)
            return null

        val rootView = inflater.inflate(R.layout.school_average_fragment, container, false)!!

        mComipemsYearTextView = rootView.findViewById(R.id.tv_comipems_year)
        mNot128ExmanQuestionDitIt = rootView.findViewById(R.id.tv_not_128_exams_questions_currently)
        schoolAverageCanvas = rootView.findViewById(R.id.school_average)

        mComipemsYearTextView.typeface = FontUtil.getNunitoBold(context!!)
        mNot128ExmanQuestionDitIt.typeface = FontUtil.getNunitoSemiBold(context!!)

        val dataHelper = DataHelper(context!!)
        val institutes = dataHelper.getInstitutes()
        val schools = getUser()!!.getSelectedSchools()
        /*if (institutes.isNotEmpty()) {
            schoolAverageCanvas.setInstitutes(institutes)
        }*/

        if (schools.isNotEmpty()) {
            schoolAverageCanvas.setSchools(schools)
            schoolAverageCanvas.setUserHits(1)
        }

        return rootView
    }
}