package com.ifanzalukhu.fetchgithubuser

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val GITHUB_ID_INTENT = "github_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCekProfile.setOnClickListener {
            val githubID = txtGithubID.text.toString()

            if (githubID.isEmpty()) txtGithubID.error = "GithubID wajib diisi"
            else {
                val intentProfile = Intent(this@MainActivity, ProfileActivity::class.java)
                intentProfile.putExtra(GITHUB_ID_INTENT, githubID)
                startActivity(intentProfile)
            }
        }

    }
}
