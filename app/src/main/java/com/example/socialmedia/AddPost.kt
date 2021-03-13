package com.example.socialmedia

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.databinding.ActivityAddPostBinding
import com.example.socialmedia.models.Post
import com.example.socialmedia.models.utils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.only_text.*

    class AddPost : AppCompatActivity() {

    lateinit var binding: ActivityAddPostBinding
    var  postUri : Uri? = null
    lateinit var storageref : StorageReference
    var tosaveUrl : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    setContentView(R.layout.activity_add_post)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        storageref = FirebaseStorage.getInstance().reference.child("postImages")
        binding.clipBoard.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        }


        var dialog = com.example.socialmedia.ProgressDialog.progressDialog(this)
        binding.post.setOnClickListener {

               dialog.show()
               var postText : String ? = null
               postText = binding.editText.text.toString()

               var uid : String? = FirebaseAuth.getInstance().currentUser?.uid

//               val database = FirebaseFirestore.getInstance()
//               val userCollection = FirebaseAuth.getInstance().currentUser?.uid?.let { it1 -> database.collection("users").document(it1) }
//               val userData = userCollection?.get()?.addOnSuccessListener {
//
//                   Log.i("usename" , it.data?.get("userName") as String)
//
//               }

               var timeStamp =  System.currentTimeMillis()

               if (postUri == null) {


                   if (postText == "") {

                       Toast.makeText(this, "Cannot upload Empty post", Toast.LENGTH_LONG).show()
                       val intent = Intent(this , MainActivity::class.java)
                       startActivity(intent)
                       finish()
                   }
                   else {
                       var post = Post()
                       if (uid != null) {
                           post.creator = uid
                       }
                       if (postText != null)
                           post.text = postText

                       var pT = 1
                       pT = 1
                       post.postType = 1
                       post.createdAt = timeStamp

                       var location = FirebaseDatabase.getInstance().reference.child("posts")
                               .push().key

                       if (location != null) {
                           post.location = location
                       }

                       if (location != null) {
                           FirebaseDatabase.getInstance().reference.child("posts")
                                   .child(location)
                                   .setValue(post)
                       }

                       dialog.dismiss()
                       val intent = Intent(this, MainActivity::class.java)
                       startActivity(intent)
                       finish()
                   }
               }

               if (postUri != null) {

                    val fileref = storageref.child(System.currentTimeMillis().toString() + ".jpg")
                 //   val uploadTask : StorageTask<*>
//                   val uploadTask = fileref?.putFile(postUri)
//                   val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//                   }
                    fileref.putFile(postUri!!)
                            .addOnSuccessListener {

                                  fileref.downloadUrl.addOnSuccessListener {

                                      tosaveUrl = it.toString()
                                      var post = Post()

                                      var pT = 0
                                      if (uid != null) {
                                          post.creator = uid
                                      }
                                      if (postText == "") {
                                          pT = 2
                                      }
                                      else {
                                          pT = 3
                                          post.text = postText
                                      }
                                      post.createdAt = timeStamp
                                      post.postimg = tosaveUrl
                                      post.postType = pT

                                      var location = FirebaseDatabase.getInstance().reference.child("posts")
                                              .push().key

                                      if (location != null) {
                                          post.location = location
                                      }

                                      //Log.i("loaction " , location.toString())

                                      if (location != null) {
                                          FirebaseDatabase.getInstance().reference.child("posts")
                                                  .child(location)
                                                  .setValue(post)
                                      }

                                      dialog.dismiss()

                                      val intent = Intent(this , MainActivity::class.java)
                                      startActivity(intent)
                                      finish()

                                  }
                            }
                            .addOnFailureListener {
                                Log.i("Sorry" , "could not upload") ;
                            }
               }
        }

    }

    private fun updatePost() {

            Log.i("dn d " , binding.editText.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                  postUri = data.data!!
            }
        }
    }
}