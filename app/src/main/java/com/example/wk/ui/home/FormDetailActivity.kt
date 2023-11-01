package com.example.wk.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.wk.Form
import com.example.wk.R
import java.text.Normalizer

class FormDetailActivity : AppCompatActivity() {

    private lateinit var form: Form

    companion object{
        private val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, form: Form): Intent {
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
        setSupportActionBar(toolbar)

        form = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Form
        val tvTitle = findViewById<TextView>(R.id.tv_form_title_details)

        tvTitle.text = form.title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_form_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_form -> {
                var intent = Intent()
                    .apply {
                        val actionType = ActionType.DELETE
                        val formAction = FormAction(form, actionType)
                        putExtra(FORM_ACTION_RESULT, formAction)
                    }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}