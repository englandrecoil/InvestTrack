package com.example.investtrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_password_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

         button.setOnClickListener {
             val login = userLogin.text.toString().trim()
             val password = userPassword.text.toString().trim()

             if (login == "" || password == "") {
                 Toast.makeText(this, "Заполните имя пользователя и пароль", Toast.LENGTH_LONG).show()
             }
             else {
                 val db = DbHelper(this, null)
                 val isAuth = db.getUser(login, password)

                 if (isAuth) {
                     Toast.makeText(this, "С возвращением!", Toast.LENGTH_SHORT).show()
                     userLogin.text.clear()
                     userPassword.text.clear()
                     val intent = Intent(this, PortfolioActivity::class.java)
                     startActivity(intent)
                 }
                 else {
                     Toast.makeText(this, "Неверный пароль или логин", Toast.LENGTH_LONG).show()
                 }
             }

         }
    }
}
