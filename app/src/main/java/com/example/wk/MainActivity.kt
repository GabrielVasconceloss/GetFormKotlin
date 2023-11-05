package com.example.wk
import android.content.Intent
import com.example.wk.Database.DatabaseHelper
import com.example.wk.model.User
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        databaseHelper = DatabaseHelper(this)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = databaseHelper.getUser(username, password)

            val dataBase = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java, "getform-database").build()

            val dao = dataBase.formDao()
            val form = Form(title = "Americanas", description = "Mercado de prego foi de arrasta para cima")
            CoroutineScope(IO).launch {
                dao.insert(form)
            }



            if (user != null) {
                val snackbar = Snackbar.make(loginButton, "Usuario Conectado", Snackbar.LENGTH_SHORT)
                snackbar.show()
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            } else {
                val snackbar = Snackbar.make(loginButton, "Usuario Não encontrado", Snackbar.LENGTH_SHORT)
                snackbar.show()
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }

        }
    }
}