package com.example.tokopari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText editname, editemail, editpassword, editpasswordconf;
    private Button btn_register, btn_login;
    private ProgressDialog ProgressDialog;
    private FirebaseAuth mAUth;
    private String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editname = findViewById(R.id.name);
        editname = findViewById(R.id.email);
        editname = findViewById(R.id.password);
        editname = findViewById(R.id.password_conf);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);


        mAUth = FirebaseAuth.getInstance();
        ProgressDialog = new ProgressDialog(RegisterActivity.this);
        ProgressDialog.setTitle("Loading");
        ProgressDialog.setMessage("Please Wait");
        ProgressDialog.setCancelable(false);

        btn_login.setOnClickListener(view -> {
            finish();
        });
        btn_register.setOnClickListener(view -> {
            if (editname.getText().length()>0 && editemail.getText().length()>0 && editpassword.getText().length()>0 && editpasswordconf.getText().length()>0){
                if(editpassword.getText().toString().equals(editpasswordconf.getText().toString())){
                    register(editname.getText().toString(), editemail.getText().toString(), editpassword.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Input the Same Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please Input the Data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String name, String email, String password){
        ProgressDialog.show();
        mAUth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null) {
                        UserProfileChangeRequest profilUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseUser.updatePassword(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    } else{
                        Toast.makeText(getApplicationContext(), "Failed to Regist", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAUth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}
