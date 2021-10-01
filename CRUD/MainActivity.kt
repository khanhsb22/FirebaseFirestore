package com.example.testcloudfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()

        // Add new document with generate id
        button_add.setOnClickListener {

            val user = hashMapOf(
                "firts" to "Bra",
                "last" to "Andy",
                "born" to 1888
            )

            db.collection("users").add(user)
                .addOnSuccessListener { documentRef ->
                    Log.d("Insert", "added with id: ${documentRef.id}")
                }
                .addOnFailureListener { e ->
                    Log.d("Error", "Error adding document", e)
                }
        }

        // Delete document (generate id) with query have condition
        button_delete.setOnClickListener {
            var itemRef = db.collection("users")
            var query = itemRef.whereEqualTo("firts", "Bra")
                .whereEqualTo("last", "Andy")
            query.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (documentItem in task.result!!) {
                        itemRef.document(documentItem.id).delete()
                    }
                } else {
                    Log.d("Error", "Error getting documents: ", task.exception);
                }
            }
        }

        // Read a document
        button_get.setOnClickListener {
            val docRef = db.collection("users")
                .document("tJj1YwdXqowc484yo9jV")
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d("Get", "data: ${document.data}")
                    var born = document.get("born")
                    Log.d("Get", "data: $born")
                }
            }
        }

        // Update a document
        button_update.setOnClickListener {
            var docRef = db.collection("users")
                .document("tJj1YwdXqowc484yo9jV")
            //docRef.update("born", "1899")
            docRef.update(mapOf(
                "born" to "1822",
                "first" to "Bjb"
            ))
        }

        button_get_2.setOnClickListener {
            var docRef = db.collection("users")
                .document("tJj1YwdXqowc484yo9jV").collection("fff")
                .document("fX48cZmP1U7CgAjVVFxD")
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var hhh = documentSnapshot.get("hhh")
                    Log.d("Get_2", "data: $hhh")
                }
            }
        }

        button_add_2.setOnClickListener {
            val city = hashMapOf(
                "name" to "Los Angeles",
                "state" to "CA",
                "country" to "USA"
            )
            db.collection("cities").document("LA")
                .set(city)

            db.collection("admin").document("xxx").update(
                mapOf("name" to "bbb")
            )
        }
    }
}
