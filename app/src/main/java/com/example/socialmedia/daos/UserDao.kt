package com.example.socialmedia.daos

import com.example.socialmedia.models.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserDao {

      private val database = FirebaseFirestore.getInstance()
      private val firebaseStorage = FirebaseStorage.getInstance()
      private val userCollection = database.collection("users")

      fun addUser(user : User?) {

          user?.let {

              GlobalScope.launch (Dispatchers.IO) {

                  var userRef = userCollection.document(user.uid)

                  userRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
                      override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {

                          if (value != null) {
                              if (!(value.exists())) {

                                      userRef.set(user)
                              }

                          }
                      }
                  })
                  //Log.i("data" , "updated") ;
              }
          }
      }

}