package com.example.tokopari;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private EditText editEmail, editpassword;
    private Button btnlogin, btnregister;
    private FirebaseAuth mAUth;
    private ProgressDialog ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.email);
        editpassword = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btn_login);
        btnregister = findViewById(R.id.btn_register);

        mAUth = FirebaseAuth.getInstance();
        ProgressDialog = new ProgressDialog(LoginActivity.this);
        ProgressDialog.setTitle("Loading");
        ProgressDialog.setMessage("Please Wait");
        ProgressDialog.setCancelable(false);

        btnregister.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
        btnlogin.setOnClickListener(view -> {
            if(editEmail.getText().length()>0 && editpassword.getText().length()>0){
                login(editEmail.getText().toString(), editpassword.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Please Input the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String email, String password){
        mAUth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    if (task.getResult().getUser()!=null){
                        reload();
                    }else{
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
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
