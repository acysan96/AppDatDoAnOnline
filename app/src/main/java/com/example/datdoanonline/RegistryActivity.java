package com.example.datdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistryActivity extends AppCompatActivity {
    private TextView txtRegistryReturn;
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi, edtPass, edtPassConfirm;
    private Spinner spnGender;
    private Button btnRegistry;
    Intent intent = LoginActivity.intent;
    private String UserName = "", FullName = "", SDT = "", DiaChi = "", Pass = "", PassConfirm = "", Gender = "";
    private int iPermission = 0;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ArrayList<UserData> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.registry_layout);

        txtRegistryReturn = findViewById(R.id.txtRegistryReturn);
        spnGender = findViewById(R.id.spnGender);
        edtUserName = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtPass = findViewById(R.id.edtPassword);
        edtPassConfirm = findViewById(R.id.edtPasswordConfirm);
        btnRegistry = findViewById(R.id.btnRegistry);

        userList = new ArrayList<UserData>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getValue(UserData.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRegistry.setOnClickListener(OnClick);

        txtRegistryReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Boolean UserNameCheck(ArrayList<UserData> userList, String sUserName){
        for(UserData user : userList){
            if(user.getsUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserName = edtUserName.getText().toString();
            FullName = edtFullName.getText().toString();
            SDT = edtSDT.getText().toString();
            DiaChi = edtDiaChi.getText().toString();
            Pass = edtPass.getText().toString();
            PassConfirm = edtPassConfirm.getText().toString();
            Gender = spnGender.getSelectedItem().toString();
            if(UserName.isEmpty()){
                edtUserName.setError("B???n ch??a nh???p user name!");
            }
            else if(UserNameCheck(userList,UserName) == false){
                edtUserName.setError("User name ???? t???n t???i!");
            }
            else if(FullName.isEmpty()){
                edtFullName.setError("B???n ch??a nh???p h??? t??n!");
            }
            else if(SDT.isEmpty()) {
                edtSDT.setError("B???n ch??a nh???p s??? ??i???n tho???i!");
            }
            else if(SDT.length() > 11 || SDT.length() < 10){
                edtSDT.setError("B???n nh???p sai s??? ??i???n tho???i!");
            }
            else if(DiaChi.isEmpty()){
                edtDiaChi.setError("B???n ch??a nh???p ?????a ch???!");
            }
            else if(Pass.isEmpty()){
                edtPass.setError("B???n ch??a nh???p m???t kh???u!");
            }
            else if(Pass.length() < 6){
                edtPass.setError("M???t kh???u qu?? ng???n!");
            }
            else if(PassConfirm.isEmpty()){
                edtPassConfirm.setError("B???n ch??a x??c nh???n m???t kh???u!");
            }
            else if(!Pass.equals(PassConfirm)){
                edtPassConfirm.setError("M???t kh??u x??c nh???n kh??ng ch??nh x??c!");
            }
            else{
                Toast.makeText(RegistryActivity.this, "T???o t??i kho???n th??nh c??ng!", Toast.LENGTH_SHORT).show();
                UserData user = new UserData();
                CreateUser(UserName, FullName, SDT, Gender, DiaChi, Pass, iPermission);
                intent = new Intent(RegistryActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
    private void CreateUser(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, int iPermission){
        UserData user = new UserData(sUserName,sFullName,sSdt,sGioiTinh,sDiaChi,sPassword, iPermission);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String sKey = mDatabase.push().getKey();
        mDatabase.child("User").child(sKey).setValue(user);
    }
}