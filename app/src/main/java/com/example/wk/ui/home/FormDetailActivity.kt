package com.example.wk.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.wk.data.Form
import com.example.wk.R
import com.google.android.material.snackbar.Snackbar

class FormDetailActivity : AppCompatActivity() {

    private var form: Form? = null
    private lateinit var btnDone: Button


    companion object{
        private val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, form: Form?): Intent {
            val intent = Intent(context, FormDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, form)
                }
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)


        form = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Form?



        var edtTitle = findViewById<EditText>(R.id.edt_Form_Title)
        var edtDescrption = findViewById<EditText>(R.id.edt_Form_Description)
        btnDone = findViewById(R.id.btn_done)

        if(form != null){
            edtTitle.setText(form!!.title)
            edtDescrption.setText(form!!.description)
            setSupportActionBar(toolbar)
        }

        btnDone.setOnClickListener{
            val title = edtTitle.text.toString()
            val description = edtDescrption.text.toString()

            if(title.isNotEmpty() && description.isNotEmpty()){
                if(form == null){
                    addOrUpdateForm(0, title, description, ActionType.CREATE)
                }else{
                    addOrUpdateForm(form!!.id, title, description, ActionType.UPDATE)
                }
            }else{
                showMessage(it, "Please fill in all fields")
            }

        }
    }

    private fun addOrUpdateForm(id: Int,title: String, description: String, actionType: ActionType){
        val form = Form(id, title, description)
        returnAction(form, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_form_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_form -> {
                if(form != null){
                    returnAction(form!!, ActionType.DELETE)
                }
                else{
                    showMessage(btnDone, "Item not found")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
    private fun returnAction(form: Form, actionType: ActionType){
        var intent = Intent()
            .apply {
                val formAction = FormAction(form!!, actionType.name)
                putExtra(FORM_ACTION_RESULT, formAction)
            }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun showMessage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }


}