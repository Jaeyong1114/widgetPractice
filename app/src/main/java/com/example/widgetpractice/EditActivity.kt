package com.example.widgetpractice

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.widgetpractice.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.blood_types,
            android.R.layout.simple_list_item_1
        )
        binding.birthdatelayer.setOnClickListener{
            val listener = OnDateSetListener{
                _,year,month,dayOfMonth ->
                binding.birthdateTextView.text = "$year-${month.inc()}-$dayOfMonth"
            }
            DatePickerDialog(
                this,
                listener,
                2000,1,1
            ).show()
        }


        binding.warningCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.warningEditText.isVisible = isChecked

        }

        binding.warningEditText.isVisible = binding.warningCheckBox.isChecked

        binding.saveButton.setOnClickListener{
            saveData()
            finish()
        }


    }
    private fun saveData(){
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()){  //MODE_PRIVATE 는 이파일을 생성한 앱에서만 접근가능
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOOD_TYPE,getBloodType())
            putString(CONTACT,binding.contactEditText.text.toString())
            putString(BIRTHDATE,binding.birthdateTextView.text.toString())
            putString(WARNING,getWarning())
            apply() //commit 은 데이터를 저장하는동안 사용자가 아무동작도 하지못함 apply는 해당 쓰레드에서 동작이 이루어지는것이아닌 비동기로 이루어짐
        }


        Toast.makeText(this,"저장을 완료했습니다!", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType() : String{
        val bloodAlphabet= binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if(binding.bloodTypePlus.isChecked) "+" else "-"
        return "$bloodSign$bloodAlphabet"
    }

    private fun getWarning() : String {
        return if(binding.warningCheckBox.isChecked) binding.warningEditText.text.toString() else ""
    }
}