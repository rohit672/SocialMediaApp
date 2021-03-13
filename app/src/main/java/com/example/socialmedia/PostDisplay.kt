package com.example.socialmedia

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.socialmedia.databinding.ActivityPostDisplayBinding
import com.example.socialmedia.models.Post
import com.example.socialmedia.models.utils
import com.google.firebase.firestore.FirebaseFirestore


class PostDisplay : AppCompatActivity() {

    lateinit var binding : ActivityPostDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val post = intent.getSerializableExtra("post_object")  as Post

        var profile : String = ""
        var name : String = ""
        val database = FirebaseFirestore.getInstance()
        val userCollection = post.creator.let { it1 -> database.collection("users").document(it1) }

        val userData = userCollection?.get()?.addOnSuccessListener {

            profile = it.data!!.get("imageUrl") as String
            name  = it.data!!.get("userName") as String

            binding.postUser.text = name
            Glide.with(this).load(profile)
                    .placeholder(R.drawable.background4)
                    .into(binding.postprofile)

        }

        binding.postTime.text = utils.getTimeAgo(post.createdAt)

        //itemView.post3.movementMethod = ScrollingMovementMethod()

        if (post.postType == 2 ){
             binding.postImage.height.equals(500)
        }
        if (post.postType == 1) {
             binding.postPost.height = 550
        }
        binding.postPost.setMovementMethod(ScrollingMovementMethod())
        binding.postPost.text = post.text



        if (post.postType == 1) {
             binding.postImage.visibility = View.INVISIBLE
        }

        Glide.with(this).load(post.postimg)
                    .placeholder(R.drawable.background4)
                    .into(binding.postImage)

    }
}