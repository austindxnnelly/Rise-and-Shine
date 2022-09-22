package com.example.alarmapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.alarmapp.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmBinding? = null

    private val binding get() = _binding!!
    private var questionPos:Int=1
    private var questionList:ArrayList<QuestionDatabase> ? = null
    private var selecedOption:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        questionList=AddToDatabase.getQuestion()

        setQuestion()

        binding.opt1.setOnClickListener{

            selectedOptionStyle(binding.opt1,1)
        }
        binding.opt2.setOnClickListener{

            selectedOptionStyle(binding.opt2,2)
        }
        binding.opt3.setOnClickListener{

            selectedOptionStyle(binding.opt3,3)
        }
        binding.opt4.setOnClickListener{

            selectedOptionStyle(binding.opt4,4)
        }

        binding.submit.setOnClickListener {
            if(selecedOption!=0)
            {
                val question=questionList!![questionPos-1]
                if(selecedOption!=question.correct_ans)
                {
                    setColor(selecedOption,R.drawable.incorrect_answer)
                }else{
                    var intent= Intent(this,QuestionResult::class.java)
                    startActivity(intent)
                    finish()
                }
                setColor(question.correct_ans,R.drawable.correct_answer)
                if(questionPos==questionList!!.size)
                    binding.submit.text="FINISH"
                else
                    binding.submit.text="Go to Next"
            }
            selecedOption=0
        }

    }

    fun setColor(opt:Int,color:Int){
        when(opt){
            1->{
                binding.opt1.background=ContextCompat.getDrawable(this,color)
            }
            2->{
                binding.opt2.background=ContextCompat.getDrawable(this,color)
            }
            3->{
                binding.opt3.background=ContextCompat.getDrawable(this,color)
            }
            4->{
                binding.opt4.background=ContextCompat.getDrawable(this,color)
            }
        }
    }


    fun setQuestion(){

        val question = questionList!![questionPos-1]
        setOptionStyle()

        binding.questionText.text=question.question
        binding.opt1.text=question.option_one
        binding.opt2.text=question.option_tw0
        binding.opt3.text=question.option_three
        binding.opt4.text=question.option_four

    }

    fun setOptionStyle(){
        var optionList:ArrayList<TextView> = arrayListOf()
        optionList.add(0,binding.opt1)
        optionList.add(1,binding.opt2)
        optionList.add(2,binding.opt3)
        optionList.add(3,binding.opt4)

        for(op in optionList)
        {
            op.setTextColor(Color.parseColor("#555151"))
            op.background=ContextCompat.getDrawable(this,R.drawable.option_for_question)
            op.typeface= Typeface.DEFAULT
        }
    }

    fun selectedOptionStyle(view:TextView,opt:Int){

        setOptionStyle()
        selecedOption=opt
        view.background=ContextCompat.getDrawable(this,R.drawable.selected_option)
        view.typeface= Typeface.DEFAULT_BOLD
        view.setTextColor(Color.parseColor("#000000"))

    }
}