Firebase: 

- Get data and show it Firebase: (Get)
		
		Show it in setting account:
		
		var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(Prevalent.currentOnlineUser.phone)
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    if (p0.child("image").exists()) {
                        var image = p0.child("image").value.toString()
                        var name = p0.child("name").value.toString()
                        var phone = p0.child("phone").value.toString()
                        var address = p0.child("address").value.toString()

                        Picasso.get().load(image).into(imageviewProfile)
                        edittextPhoneSetting?.text = Editable.Factory.getInstance().newEditable(phone)
                        edittextFullnameSetting?.text = Editable.Factory.getInstance().newEditable(name)
                        edittextAddressSetting?.text = Editable.Factory.getInstance().newEditable(address)
                    }
                }
            }
        })	
		
- UpLoad image Firebase:

			// fileRef is save image to Storage
			val fileRef: StorageReference = storageReference.child(Prevalent.currentOnlineUser.phone + ".jpg")
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if (!p0.isSuccessful) {
                        throw p0.exception!!
                    }
                    return fileRef.downloadUrl
                }
            }).addOnCompleteListener(object : OnCompleteListener<Uri>{
                override fun onComplete(p0: Task<Uri>) {
                    if (p0.isSuccessful) {
                        var downloadUri = p0.result
                        myUrl = downloadUri.toString()

						// dbRef is save image to database with child ("Users")
                        var dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                        var userMap: HashMap<String, Any> = HashMap()
                        userMap.put("name", editText_fullname_setting.text.toString())
                        userMap.put("address", editText_address_setting.text.toString())
                        userMap.put("phoneOrder", editText_phone_setting.text.toString())
                        userMap.put("image", myUrl)
                        dbRef.child(Prevalent.currentOnlineUser.phone).updateChildren(userMap)

                        progressDialog.dismiss()

                        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
                        Toast.makeText(this@SettingActivity, "Profile Information was updated", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SettingActivity, "Error update !", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }
            })
			
- Put data into Firebase (Insert)

Cách 1:

var dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                // Nếu tên đăng nhập chưa ai sử dụng thì thêm thông tin đk vào db
                if (!(p0.child("Normal User").child(tenDangNhap).exists())) {
                    var userMap: HashMap<String, Any> = HashMap()

                    userMap.put("So Dien Thoai", soDienThoai)
                    userMap.put("Mat Khau", matKhau)
                    userMap.put("Email", "")
                    userMap.put("Image", "")

                    var idUser: String = getRandomString(9)
                    userMap.put("Id User", idUser)

                    dbRef.child("Normal User").child(tenDangNhap).child("Thong Tin Tai Khoan").updateChildren(userMap)
                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful) {
                                    // dismiss progressDialog and show alertDialog
                                   


                                } else {
                                    callProgressDialog.dismissProgress()
                                    Toast.makeText(
                                        this@DangKyActivity,
                                        "Đã xảy ra lỗi từ hệ thống: " + p0.exception.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        })
                } else {
                    // Đã tồn tại tên đăng nhập trong database
                    callProgressDialog.dismissProgress()
                    Toast.makeText(
                        this@DangKyActivity, "Tên đăng nhập này đã được sử dụng, bạn hãy chọn " +
                                "tên đăng nhập khác !", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
		
Cách 2:
	  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").push();
          Map<String, Object> map = new HashMap<>();
          map.put("id", databaseReference.getKey());
          map.put("title", editText.getText().toString());
          map.put("desc", etd.getText().toString());

          databaseReference.setValue(map);
	
- Remove value in Firebase (Delete)
dbRef.child("User View").child(Prevalent.currentOnlineUser.phone)
.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ConfirmFinalOrderActivity, "Your final order is successfully",
                                Toast.LENGTH_SHORT).show()
                            var intent = Intent(Intent(this@ConfirmFinalOrderActivity, HomeActivity::class.java))
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }
		
- Update value in Firebase
dbRef.addListenerForSingleValueEvent()... 
{
dbRef.child("password").setValue(editText_matkhaumoi.text.toString())
                    .addOnCompleteListener { task ->
}

- Get key in firebase:
newOrderRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (dataSnapshot in p0.children) {

                    var strKey = dataSnapshot.key

                }

            }

        })

- Firebase Recycler Adapter:

+ Java:

var baiHoc_adapter: FirebaseRecyclerAdapter<BaiHoc, BaiHocViewHolder>? = null
 private void fetch() {
        var layoutManager = LinearLayoutManager(this@BaiHocActivity, LinearLayoutManager.VERTICAL, false)
        recycler_baiHoc.layoutManager = layoutManager
        recycler_baiHoc.addItemDecoration(DividerItemDecoration(recycler_baiHoc.context, DividerItemDecoration.VERTICAL))
		
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() { // Dùng cách này khi muốn lấy tên biến 
						// trong Model class không giống với Firebase
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("id").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("desc").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Model model) {
                holder.setTxtTitle(model.getmTitle());
                holder.setTxtDesc(model.getmDesc());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
		adapter.startListening();
    }
	
@Override
protected void onStart() {
    super.onStart();
    adapter.startListening();
}

@Override
protected void onStop() {
    super.onStop();
    adapter.stopListening();
}

+ Kotlin:
Import gradle: implementation 'com.firebaseui:firebase-ui-database:3.2.2'

var baiHoc_adapter: FirebaseRecyclerAdapter<ModelClass, ViewHolder>? = null
private fun fetchData() {
        Paper.init(this@KiemTraBHActivity)
        var idBH = Paper.book().read("idBH", "null") // get from BaiHocActivity

	var layoutManager = LinearLayoutManager(this@CommentActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView_comments.layoutManager = layoutManager

        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("posts")

        val options: FirebaseRecyclerOptions<ModelClass> = FirebaseRecyclerOptions.Builder<ModelClass>()
            .setQuery(query, ModelClass::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                return ViewHolder(view)
            }

            protected override fun onBindViewHolder(
                holder: ViewHolder,
                position: Int,
                model: ModelClass
            ) {
                holder.setTxtTitle(model.getmTitle())
                holder.setTxtDesc(model.getmDesc())
                holder.root.setOnClickListener(object : OnClickListener() {
                    fun onClick(view: View?) {
                        Toast.makeText(this@MainActivity, position.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
		recyclerView.adapter = adapter;
		adapter.startListening();
    }

class BaiHocViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var imageView_baiHoc = itemView.imageView_baiHoc
    var textView_ten_baiHoc = itemView.textView_ten_baiHoc
    var textView_soTrang = itemView.textView_soTrang

}


- Get Key and count children:

FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference();

//You can use the single or the value.. depending if you want to keep track
thismyRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot snap: dataSnapshot.getChildren()) {
            Log.e(snap.getKey(),snap.getChildrenCount() + "");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

- Không lấy được các phần tử trong arraylist khi vào vòng lặp khi child:
-> Đặt điều kiện if trong vòng lặp for, trong hàm onDataChange()
for (i in 1.._soTrang) {
            var dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("Bai Hoc").child(idBH).child("Trang")
                .child(i.toString()).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var tieuDe = p0.child("tieuDe").value.toString()
                        var noiDung = p0.child("noiDung").value.toString()
						
                        list_CTBaiHoc.add(CTBaiHoc(i, tieuDe, noiDung))
						
                        if (i == _soTrang) {
                            for (i in 0 until list_CTBaiHoc.size) {
                                // Code here
                            }
                        }

                    }
                })


        }



// Kiểm tra xem sdt đã tồn tại trong db hay chưa, nếu chưa thì cho gửi mã xác thực
                    var dbRef = FirebaseDatabase.getInstance().reference
                    dbRef.child("Normal User").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            var _count_phone = 0
                            var _count_node = 0
                            for (p00 in p0.children) {
                                var node = p00.childrenCount.toInt()
                                _count_node++
                                var userName = p00.key
                                var dbRef2 = FirebaseDatabase.getInstance().reference
                                dbRef2.child("Normal User").child(userName.toString()).child("Thong Tin Tai Khoan")
                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            var phone = p0.child("So Dien Thoai").value.toString()
                                            if (phone == soDienThoai) {
                                                _count_phone++
                                            }
                                            if (_count_node == node) {
                                                Log.d("count_phone", _count_phone.toString())
                                            }
                                        }

                                    })
                            }

                        }
					


                    })
					

// Gửi mã OTP sử dụng Firebase Auth

private fun guiMaXacThuc(soDienThoai: String) {
        var mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                code = p0!!.smsCode.toString()
                progressBar_doiSdt.visibility = View.GONE
                var intent = Intent(this@DoiSdtActivity, XacNhanDoiSdtActivity::class.java)
                intent.putExtra("codeSended", code)
                intent.putExtra("soDienThoai", soDienThoai)
                startActivity(intent)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                progressBar_doiSdt.visibility = View.GONE
                Toast.makeText(
                    this@DoiSdtActivity,
                    "Không gửi được mã xác thực ! Số điện thoại này không tồn tại, hãy chọn số điện thoại khác !",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                if (p0 != null) {
                    progressBar_doiSdt.visibility = View.GONE
                    var intent = Intent(this@DoiSdtActivity, XacNhanDoiSdtActivity::class.java)
                    intent.putExtra("codeSended", code)
                    intent.putExtra("soDienThoai", soDienThoai)
                    startActivity(intent)
                }
            }
        }
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(soDienThoai, 1, TimeUnit.MINUTES, this@DoiSdtActivity, mCallbacks)

    }
	
- Update child value:

var dbRef_2 = FirebaseDatabase.getInstance().reference
                            dbRef_2.child("Article").child(id_article.toString()).child("Comments")
                                    .child((position + 1).toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            var numLikes = p0.child("numLikes").value.toString()
                                            var update_value = numLikes.toInt() + 1
                                            dbRef_2.child("Article").child(id_article.toString()).child("Comments")
                                                    .child((position + 1).toString()).child("numLikes").setValue(update_value.toString())

                                        }

                                    })
