package com.example.investtrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (login == "" || password == "") {
                Toast.makeText(this, "Заполните имя пользователя и пароль", Toast.LENGTH_LONG).show()
            }
            else {
                val user = User(login, password)

                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Вы успешно зарегистрированы!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PortfolioActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
