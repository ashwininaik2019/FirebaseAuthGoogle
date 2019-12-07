package com.example.firebaseauthgoogle

//import android.support.v7.app.AppCompatActivity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth

    var alertDialog: AlertDialog.Builder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        configureGoogleSignIn()
        setupUI()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            //startActivity(ShowImagesActivity.getLaunchIntent(this))
            val intent = Intent(this, ShowImagesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        google_button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

               // startActivity(HomeActivity.getLaunchIntent(this))
                val intent = Intent(this, ShowImagesActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


    //*********************** THIS FUNCTION IS CALLED WHEN THE BACK BUTTON IS PRESSED**********************/

    //*********************** THIS FUNCTION IS CALLED WHEN THE BACK BUTTON IS PRESSED**********************/
//    @Override
    override fun onBackPressed() { // do something on back.
        try {
            showLogoutAlert()
        } catch (e: Exception) {
            println("Exception onBackPressed " + e.message)
        } catch (err: UnsatisfiedLinkError) {
            err.stackTrace
            println("Error UnsatisfiedLinkError ")
        }
    }

    override fun onDestroy() {
        try { //you may call the cancel() method but if it is not handled in doInBackground() method

            super.onDestroy()
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            println("Exception onDestroy " + e.message)
        } catch (err: UnsatisfiedLinkError) {
            err.stackTrace
            println("Error UnsatisfiedLinkError ")
        }
    }

    fun showLogoutAlert() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        // Setting Dialog Title
        alertDialog.setTitle("Alert")
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to quit App ?")
        // On pressing Settings button
        alertDialog.setPositiveButton("OK",
            DialogInterface.OnClickListener { dialog, which ->

                Process.killProcess(Process.myPid())
                System.exit(0)
            })
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        // Showing Alert Message
        alertDialog.show()
    }

}

/*
package com.example.firebaseauthgoogle

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        firebaseAuth = FirebaseAuth.getInstance()

         fun configureGoogleSignIn() {
            mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
        }

         fun setupUI() {
            google_button.setOnClickListener {
                signIn()
            }
        }

         fun signIn() {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {

                    startActivity(HomeActivity.getLaunchIntent(this))
                } else {
                    Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onStart() {
            super.onStart()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startActivity(HomeActivity.getLaunchIntent(this))
                finish()
            }
        }

        companion object {
            fun getLaunchIntent(from: Context) = Intent(from, SignInActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }
    }
}
*/
