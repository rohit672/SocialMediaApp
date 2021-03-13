package com.example.socialmedia

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.models.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    lateinit var addpost : View
    lateinit var binding : ActivityMainBinding

    var posts : ArrayList<Post> = ArrayList()
    val myadapter : myadapter = myadapter(posts,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addpost = findViewById(R.id.addPost)

        addpost.setOnClickListener {
            val intent = Intent(this , AddPost::class.java)
            startActivity(intent)
        }


        val layoutManager = LinearLayoutManager(applicationContext)
        binding.rcv.layoutManager = layoutManager
        binding.rcv.adapter = myadapter

        FirebaseDatabase.getInstance().reference.child("posts")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                            posts.clear()
                            for (data in snapshot.children) {

                                var x = data.getValue(Post::class.java)
                                if (x != null) {
                                    posts?.add(x)
                                }
                            }
                            myadapter.data = posts
                            myadapter.notifyDataSetChanged()
                    }

                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.updateProfile -> {

                  val intent = Intent(this,UpdateProfile::class.java)
                  startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}