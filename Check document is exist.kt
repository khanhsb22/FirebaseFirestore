var db = Firebase.firestore

button_submit_register.setOnClickListener {
            progressBar_register.visibility = View.VISIBLE
            var username = editText_username_register.text.toString()
            var password = editText_password_register.text.toString()
            var retype_password = editText_retype_password_register.text.toString()

            db.collection("users").document(username).get().addOnCompleteListener(object :
                OnCompleteListener<DocumentSnapshot>{
                override fun onComplete(p0: Task<DocumentSnapshot>) {
                    if (p0.isSuccessful) {
                        var document: DocumentSnapshot = p0.result!!
                        if (document.exists()) { // Not valid
                            
                        } else { // Valid
                            
                        }
                    } else {
                        Log.d("Register", "Failed " + p0.exception)
                    }
                }
            })
        }
