package com.example.testcloudfirebase

import android.R
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {

    lateinit var list_user: ArrayList<UserModel>
    lateinit var adapter: FirestoreRecyclerAdapter<UserModel, ViewHolder>
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.testcloudfirebase.R.layout.activity_list)

        db = FirebaseFirestore.getInstance()

        set_recyclerView_user()

        loadData()

        textView_add.setOnClickListener {
            var firstName = editText_firstName.text.toString()
            var lastName = editText_lastName.text.toString()

            var map = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName
            )

            // add item with generate id
            db.collection("users").add(map)

        }

        textView_click.setOnClickListener {
            var docRef = db.collection("users").document("g3BNQJKu8K4fLmYPfdA2")
            docRef.get().addOnSuccessListener { document ->
                var firstName = document.get("firstName")
                textView_test.text = firstName.toString()
            }
        }

        textView_delete.setOnClickListener {
            var docRef = db.collection("users").document(editText_id.text.toString())
            docRef.delete().addOnSuccessListener { Toast.makeText(this@ListActivity, "deleted", Toast.LENGTH_SHORT).show() }
        }

        textView_update.setOnClickListener {
            var docRef = db.collection("users").document(editText_id.text.toString())
            docRef.update(mapOf(
                "firstName" to editText_firstName.text.toString(),
                "lastName" to editText_lastName.text.toString()
            )).addOnSuccessListener { Toast.makeText(this@ListActivity, "updated", Toast.LENGTH_SHORT).show() }
        }

    }

    private fun loadData() {
        /*if (list_user.size == 0) {
            db.collection("users").get().addOnSuccessListener { documents ->
                for (doc in documents) {
                    var firstName = doc.get("firstName").toString()
                    var lastName = doc.get("lastName").toString()
                    list_user.add(UserModel(doc.id, firstName, lastName))
                    adapter.notifyDataSetChanged()
                }
            }
        }*/

        var query = db.collection("users")
        val options: FirestoreRecyclerOptions<UserModel> =
            FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, object : SnapshotParser<UserModel> {
                    override fun parseSnapshot(snapshot: DocumentSnapshot): UserModel {
                        return UserModel(snapshot.id, snapshot.get("firstName").toString(), snapshot.get("lastName").toString())
                    }

                })
                .build()

        adapter = object : FirestoreRecyclerAdapter<UserModel, ViewHolder>(options) {

            override fun onCreateViewHolder(group: ViewGroup, i: Int): ViewHolder {
                val view: View = LayoutInflater.from(group.context)
                    .inflate(com.example.testcloudfirebase.R.layout.row_user, group, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: UserModel) {
                holder.textView_firstName.text = model.firstName
                holder.textView_lastName.text = model.lastName
                holder.itemView.setOnClickListener {
                    editText_id.text = Editable.Factory.getInstance().newEditable(model.id)
                    editText_firstName.text = Editable.Factory.getInstance().newEditable(model.firstName)
                    editText_lastName.text = Editable.Factory.getInstance().newEditable(model.lastName)
                }
            }


        }
        adapter.startListening()
        recyclerView_info.adapter = adapter

    }

    private fun set_recyclerView_user() {
        recyclerView_info.setHasFixedSize(true)
        var layoutManager =
            LinearLayoutManager(
                this@ListActivity,
                LinearLayoutManager.VERTICAL, false
            )
        recyclerView_info.layoutManager = layoutManager

        /*list_user = ArrayList()
        adapter = UserAdapter(this@ListActivity, list_user)
        recyclerView_info.adapter = adapter

        setClickAdapter()*/
    }

    private fun setClickAdapter() {
       /* adapter.setOnItemClickListener(object : UserAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View) {
                var item = list_user.get(position)

                editText_id.text = Editable.Factory.getInstance().newEditable(item.id)
                editText_firstName.text = Editable.Factory.getInstance().newEditable(item.firstName)
                editText_lastName.text = Editable.Factory.getInstance().newEditable(item.lastName)
            }

            override fun onItemLongClick(position: Int, v: View) {

            }
        })*/
    }

}