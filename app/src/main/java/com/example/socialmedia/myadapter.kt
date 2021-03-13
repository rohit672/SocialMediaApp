package com.example.socialmedia

import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmedia.models.LikeImages
import com.example.socialmedia.models.Post
import com.example.socialmedia.models.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.both_post.view.*
import kotlinx.android.synthetic.main.only_image.view.*
import kotlinx.android.synthetic.main.only_text.view.*


class myadapter(var data : ArrayList<Post> , val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    class imageViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post : Post) {

            itemView.context

            var profile : String = ""
            var name : String = ""
            val database = FirebaseFirestore.getInstance()
            val userCollection = post.creator.let { it1 -> database.collection("users").document(it1) }

            val userData = userCollection?.get()?.addOnSuccessListener {

                Log.i("usename" , it.data?.get("userName") as String)
                profile = it.data!!.get("imageUrl") as String
                name  = it.data!!.get("userName") as String

                itemView.userName2.text = name
                Glide.with(itemView.context).load(profile)
                        .placeholder(R.drawable.background4)
                        .into(itemView.progileImage2)

            }
            itemView.totalLikes2.text = post.likedby.size.toString() + " Likes"

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (post.likedby.containsKey(userId)) {

                Glide.with(itemView.context).load(LikeImages.liked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot2)
            }
            else {

                Glide.with(itemView.context).load(LikeImages.unliked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot2)
            }

            itemView.likedOrNot2.setOnClickListener {

                if (post.likedby.containsKey(userId)) {

                    post.likedby.remove(userId)
                    FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                    itemView.totalLikes2.text = post.likedby.size.toString() + " Likes"

                    Glide.with(itemView.context).load(LikeImages.unliked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot2)
                }
                else {

                    if (userId != null) {
                        post.likedby.put(userId , 1)
                    }
                    FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                    itemView.totalLikes2.text = post.likedby.size.toString() + " Likes"

                    Glide.with(itemView.context).load(LikeImages.liked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot2)
                }
            }

            itemView.timeStamp2.text = utils.getTimeAgo(post.createdAt)
            Glide.with(itemView.context).load(post.postimg)
                    .placeholder(R.drawable.background4)
                    .into(itemView.imagePost)

            itemView.imagePost.setOnClickListener {

                val intent = Intent(itemView.context , PostDisplay::class.java)
                intent.putExtra("post_object" , post)
                itemView.context.startActivity(intent)
            }
        }
    }

    class textViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(post : Post) {

            var profile : String = ""
            var name : String = ""
            val database = FirebaseFirestore.getInstance()
            val userCollection = post.creator.let { it1 -> database.collection("users").document(it1) }

            val userData = userCollection?.get()?.addOnSuccessListener {

               // Log.i("usename" , it.data?.get("userName") as String)
                 profile = it.data!!.get("imageUrl") as String
                 name  = it.data!!.get("userName") as String

                itemView.userName1.text = name
                Glide.with(itemView.context).load(profile)
                        .placeholder(R.drawable.background4)
                        .into(itemView.progileImage1)

            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            //itemView.textPost.movementMethod = ScrollingMovementMethod()
            itemView.timeStamp1.text = utils.getTimeAgo(post.createdAt)
            itemView.totalLikes1.text = post.likedby.size.toString() + " Likes"

            if (post.likedby.containsKey(userId)) {

                Glide.with(itemView.context).load(LikeImages.liked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot1)
            }
            else {

                Glide.with(itemView.context).load(LikeImages.unliked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot1)
            }


            var toupload : String = post.text
            if (post.text.length > 140) {
                 toupload = post.text.substring(0,140) + ".... "
                 itemView.textPost.setText(Html.fromHtml(toupload +"<font color='blue'> <u>View More</u></font>"))
            }
            else itemView.textPost.text = toupload

            itemView.textPost.setOnClickListener {

                    val intent = Intent(itemView.context , PostDisplay::class.java)
                    intent.putExtra("post_object" , post)
                    itemView.context.startActivity(intent)
            }

            itemView.likedOrNot1.setOnClickListener {

                if (post.likedby.containsKey(userId)) {

                    post.likedby.remove(userId)
                    FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                    itemView.totalLikes1.text = post.likedby.size.toString() + " Likes"

                    Glide.with(itemView.context).load(LikeImages.unliked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot1)
                }
                else {

                    if (userId != null) {
                        post.likedby.put(userId , 1)
                    }
                      FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                      itemView.totalLikes1.text = post.likedby.size.toString() + " Likes"

                      Glide.with(itemView.context).load(LikeImages.liked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot1)
                }
            }
        }
    }

    class bothViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post : Post) {

            var profile : String = ""
            var name : String = ""
            val database = FirebaseFirestore.getInstance()
            val userCollection = post.creator.let { it1 -> database.collection("users").document(it1) }

            val userData = userCollection?.get()?.addOnSuccessListener {

                Log.i("usename" , it.data?.get("userName") as String)
                profile = it.data!!.get("imageUrl") as String
                name  = it.data!!.get("userName") as String

                itemView.postUser.text = name

                Glide.with(itemView.context).load(profile)
                        .placeholder(R.drawable.background4)
                        .into(itemView.postprofile)

            }

            Log.i("third" , "created")
            //itemView.post3.movementMethod = ScrollingMovementMethod()

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            var toupload : String = post.text
            if (post.text.length > 140) {
                toupload = post.text.substring(0,140) + ".... "
                itemView.postPost.setText(Html.fromHtml(toupload +"<font color='blue'> <u>View More</u></font>"))
            }
            else itemView.postPost.text = toupload

            itemView.postTime.text = utils.getTimeAgo(post.createdAt)

            Glide.with(itemView.context).load(post.postimg)
                    .placeholder(R.drawable.background4)
                    .into(itemView.postImage)

            itemView.totalLikes3.text = post.likedby.size.toString() + " Likes"

            if (post.likedby.containsKey(userId)) {

                Glide.with(itemView.context).load(LikeImages.liked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot3)
            }
            else {

                Glide.with(itemView.context).load(LikeImages.unliked)
                        .placeholder(R.drawable.background4)
                        .into(itemView.likedOrNot3)
            }

            itemView.likedOrNot3.setOnClickListener {

                if (post.likedby.containsKey(userId)) {

                    post.likedby.remove(userId)
                    FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                    itemView.totalLikes3.text = post.likedby.size.toString() + " Likes"

                    Glide.with(itemView.context).load(LikeImages.unliked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot3)
                }
                else {

                    if (userId != null) {
                        post.likedby.put(userId, 1)
                    }
                    FirebaseDatabase.getInstance().reference.child("posts").child(post.location).child("likedby").setValue(post.likedby)
                    itemView.totalLikes3.text = post.likedby.size.toString() + " Likes"

                    Glide.with(itemView.context).load(LikeImages.liked)
                            .placeholder(R.drawable.background4)
                            .into(itemView.likedOrNot3)
                }
            }

            itemView.postPost.setOnClickListener {

                val intent = Intent(itemView.context , PostDisplay::class.java)
                intent.putExtra("post_object" , post)
                itemView.context.startActivity(intent)
            }

            itemView.postImage.setOnClickListener {

                val intent = Intent(itemView.context , PostDisplay::class.java)
                intent.putExtra("post_object" , post)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].postType
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            if (viewType == 1) {

                 val view  = LayoutInflater.from(parent.context).inflate(R.layout.only_text , parent , false)
                 return textViewholder(view)
            }
            else if (viewType == 2){

                val view  = LayoutInflater.from(parent.context).inflate(R.layout.only_image , parent , false)
                return imageViewholder(view)
            }
            else {
                val view  = LayoutInflater.from(parent.context).inflate(R.layout.both_post , parent , false)
                return bothViewholder(view)
            }
    }

    override fun getItemCount(): Int {

          return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            if (getItemViewType(position) == 1) {
                (holder as textViewholder).bind(data[position])
            }else if (getItemViewType(position) == 2){
                (holder as imageViewholder).bind(data[position])
            }else{
                (holder as bothViewholder).bind(data[position])
            }
    }

}
//interface IpostAdapter {
//
//      fun onLikeclicked(post : Post)
//}