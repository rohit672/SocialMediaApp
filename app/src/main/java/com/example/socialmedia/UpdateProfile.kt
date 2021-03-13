package com.example.socialmedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.databinding.ActivityUpdateProfileBinding
import com.example.socialmedia.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.only_image.view.*

lateinit var binding : ActivityUpdateProfileBinding

var  postUri : Uri? = null
lateinit var storageref : StorageReference
var tosaveUrl : String = ""

class UpdateProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var dialog = com.example.socialmedia.ProgressDialog.progressDialog(this)
        var db = FirebaseFirestore.getInstance()
        var uid = FirebaseAuth.getInstance().currentUser?.uid


        var crrImg : Task<DocumentSnapshot> = uid?.let {
            db.collection("users").document(it).get().addOnSuccessListener {

                var cimg = it.data?.get("imageUrl").toString()
                Glide.with(this).load(cimg)
                        .placeholder(R.drawable.background4)
                        .into(binding.updatedImage)

            }
        } as Task<DocumentSnapshot>

        storageref = FirebaseStorage.getInstance().reference.child("profileImages")


        binding.updatedImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        }

        binding.save.setOnClickListener {

            if (postUri == null) {

                 Toast.makeText(this , "Please Select an Image.....", Toast.LENGTH_LONG).show()
            }
            else if (postUri != null) {

                dialog.show()
                val fileref = storageref.child(System.currentTimeMillis().toString() + ".jpg")
                //   val uploadTask : StorageTask<*>
//                   val uploadTask = fileref?.putFile(postUri)
//                   val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//                   }
                fileref.putFile(postUri!!)
                        .addOnSuccessListener {

                            fileref.downloadUrl.addOnSuccessListener {

                                tosaveUrl = it.toString()

                                if (uid != null) {
                                    db.collection("users").document(uid).update("imageUrl", tosaveUrl)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                postUri = data.data!!

                Glide.with(this).load(postUri)
                        .placeholder(R.drawable.background4)
                        .into(binding.updatedImage)
            }
        }
    }
}