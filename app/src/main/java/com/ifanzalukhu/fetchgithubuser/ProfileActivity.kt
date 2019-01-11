package com.ifanzalukhu.fetchgithubuser

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {

    // Array view yang visibility = Gone di activity_profile.XML
    private lateinit var arrayInvisibleView: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Mengambil input githubID dari MainActivity
        val githubID = intent.getStringExtra(MainActivity.GITHUB_ID_INTENT)

        arrayInvisibleView = arrayOf(txtTitle, txtPublicRepos, txtFollowers, txtFollowing)

        /*
        * Load detail profile github di background thread
        * menggunakan Anko Common
        * */
        doAsync {
            val mGithubProfile = fetchGithubProfile(githubID)
            val jsonReader = JSONObject(mGithubProfile)

            if (jsonReader.has("message")) {
                // GithubID tidak ditemukan

                activityUiThread {
                    progressLoadProfile.visibility = GONE

                    val snackBarGithubIdNotFound =
                        Snackbar.make(rootLayout, "GithubID tidak ditemukan", Snackbar.LENGTH_LONG)
                            .setAction("Cari Lagi?") {
                                startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                            }
                            .setActionTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.colorPrimary))
                    snackBarGithubIdNotFound.show()
                }

            } else {
                // GithubID ditemukan
                // dan tampilkan datanya di UI
                activityUiThread {
                    showInvisibleView(arrayInvisibleView)

                    progressLoadProfile.visibility = GONE

                    txtName.text = jsonReader.getString("name")
                    Glide.with(this@ProfileActivity).load(jsonReader.getString("avatar_url")).into(avatar)
                    txtLocation.text = jsonReader.getString("location")
                    repositories.text = jsonReader.getString("public_repos")
                    followers.text = jsonReader.getString("followers")
                    following.text = jsonReader.getString("following")
                }
            }
        }
    }

    /**
     * @param loginID adalah githubID yg di input user
     * untuk ditampilkan profilenya
     */
    private fun fetchGithubProfile(loginID: String): String {
        val url = "https://api.github.com/users/$loginID"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val bodyStr = response.body()?.string()
        return bodyStr!!
    }

    /**
     * Method untuk menampilkan view yang gone / invisible
     *
     * @param arrayView array view yang Invisible / Gone
     * di activity_profile.XML
     */
    private fun showInvisibleView(arrayView: Array<TextView>) {
        for (view in arrayView) {
            view.visibility = VISIBLE
        }
    }
}
