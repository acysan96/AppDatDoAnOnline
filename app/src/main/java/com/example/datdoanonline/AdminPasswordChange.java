package com.example.datdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.datdoanonline.R;

public class AdminPasswordChange extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Button btnPasswordSave, btnBack;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Intent intent;
    private String sOldPassword, sNewPassword, sConfirmPassword, sUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.admin_password_change_layout);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnPasswordSave = findViewById(R.id.btnPasswordSave);

        intent = getIntent();


        sUserName = intent.getExtras().getString("UserName");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnPasswordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sOldPassword = edtOldPassword.getText().toString();
                sNewPassword = edtNewPassword.getText().toString();
                sConfirmPassword = edtConfirmPassword.getText().toString();
                if(sOldPassword.isEmpty()){
                    edtOldPassword.setError("B???n ch??a nh???p m???t kh???u c??!");
                }
                else if (sNewPassword.isEmpty()) {
                    edtNewPassword.setError("B???n ch??a nh???p m???t kh???u m???i!");
                }
                else if(sNewPassword.length() < 6) {
                    edtNewPassword.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!");
                }
                else if(sNewPassword.equals(sOldPassword)) {
                    edtNewPassword.setError("M???t kh???u m???i ph???i kh??c m???t kh???u c??!");
                }
                else if (sConfirmPassword.isEmpty()) {
                    edtConfirmPassword.setError("B???n ch??a x??c nh???n m???t kh???u!");
                }
                else if(sConfirmPassword.length() < 6) {
                    edtConfirmPassword.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!");
                }
                else if(!sNewPassword.equals(sConfirmPassword)) {
                    edtConfirmPassword.setError("M???t kh???u x??c nh???n ch??a ch??nh x??c!");
                }
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getsPassword().equals(sOldPassword)){
                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsFullName(),
                                        snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                        edtNewPassword.getText().toString(),0);
                                databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                intent = new Intent(AdminPasswordChange.this,LoginActivity.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Toast.makeText(AdminPasswordChange.this, "C???p nh???t m???t kh???u th??nh c??ng!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        edtNewPassword.setText("");
        edtOldPassword.setText("");
        edtConfirmPassword.setText("");
    }
}