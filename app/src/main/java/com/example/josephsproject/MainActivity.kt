package com.example.josephsproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class MainActivity : AppCompatActivity() {
    var myPreferences: SharedPreferences? = null
    val sharedPrefFile =
    "com.example.josephsproject.hellosharedprefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = myPreferences!!.edit()



        val userNameField = findViewById<TextView>(R.id.userNameField)
        val passwordField = findViewById<TextView>(R.id.passwordField)
        val emailField = findViewById<TextView>(R.id.emailField)

        var user1 = myPreferences.getString("userName","username")
        var pass1 = myPreferences.getString("password","password")
        var email1 = myPreferences.getString("email","email")

        userNameField.setText(String.format("%s",user1 ))
        passwordField.setText(String.format("%s",pass1))
        emailField.setText(String.format("%s",email1))

        fun validateEntry(username: String, email: String, password: String): Boolean {
            var result = true
            if (TextUtils.isEmpty(email)) {
                emailField.setError("Required");
                result = false;
            } else {
                emailField.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                passwordField.setError("Required");
                result = false;
            } else {
                passwordField.setError(null);
            }

            if(TextUtils.isEmpty(username)) {
                userNameField.setError("Required");
                result = false;
            }
            else{
                userNameField.setError(null)
            }

            return result;

        }

        fun signIn(){

            // 1 - validate display name, email, and password entries
            var email = emailField.text.toString()
            var password = passwordField.text.toString()
            var username = userNameField.text.toString()

            if(!validateEntry(username,email,password))
                return

            // 2 - save valid entries to shared preferences
            preferencesEditor.putString("userName", username.toString())
            preferencesEditor.putString("password",password.toString())
            preferencesEditor.putString("email",email.toString())
            preferencesEditor.apply()

            // 3 - sign into Firebase
            val mAuth = FirebaseAuth.getInstance()
            Log.d("SigninEmail",email)
            Log.d("SigninPassword",password)
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this
                ) { task ->
                    Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful)
                    if (task.isSuccessful) {
                        // update profile. displayname is the value entered in UI
                        val user = FirebaseAuth.getInstance().currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("FIREBASE", "User profile updated.")
                                    // Go to FirebaseActivity

                                    val intent = Intent(this,FirebaseActivity::class.java).apply{
                                    }
                                    startActivity(intent)
                                }
                            }
                    } else {
                        Log.d("FIREBASE", "sign-in failed")
                        Toast.makeText(
                            this, "Sign In Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


        val trafficButton = findViewById<Button>(R.id.button7)
        trafficButton.setOnClickListener{
            Toast.makeText(this,"Traffic",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Traffic::class.java).apply{
            }
            startActivity(intent)
        }

        val moviesButton = findViewById<Button>(R.id.button5)
        moviesButton.setOnClickListener {
            Toast.makeText(this,"Movies",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Movies::class.java).apply{
            }
            startActivity(intent)
        }

        val parksButton = findViewById<Button>(R.id.button8)
        parksButton.setOnClickListener {
            Toast.makeText(this,"Parks",Toast.LENGTH_SHORT).show()
        }

        val cameraMap = findViewById<Button>(R.id.button4)
        cameraMap.setOnClickListener {
            Toast.makeText(this,"Camera Map",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CameraMap::class.java).apply{
            }
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.button)
        loginButton.setOnClickListener {
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show()
           signIn()
        }





    }

}




