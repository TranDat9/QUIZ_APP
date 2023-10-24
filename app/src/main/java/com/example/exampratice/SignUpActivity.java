package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText name , email,pass,comfirmPass;
    private Button signUpB;
    private ImageView backB;
    FirebaseAuth mAuth;
    private Dialog prgressDialog;
    TextView dialogText;

     private String emailStr , passStr , confirmPassStr , nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.password);
        comfirmPass = findViewById(R.id.confirm_pass);
           signUpB = findViewById(R.id.signupB);
           backB = findViewById(R.id.backB);

           prgressDialog = new Dialog(SignUpActivity.this);
           prgressDialog.setContentView(R.layout.dialog_layout);
           prgressDialog.setCancelable(false);
           prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = prgressDialog.findViewById(R.id.dialog_text);
           dialogText.setText("Registering user ....");


           mAuth= FirebaseAuth.getInstance();

           backB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   finish();
               }
           });

           signUpB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(validate())
                   {
                       signupNewuser();
                   }

               }
           });

    }

    private boolean validate() {
        nameStr = name.getText().toString();
        passStr = pass.getText().toString();
        emailStr = email.getText().toString();
        confirmPassStr= comfirmPass.getText().toString();
        if(nameStr.isEmpty())
        {
             name.setError("Enter Your Name");
             return false;
        }

        if(passStr.isEmpty())
        {
            pass.setError("Enter PassWord");
            return false;
        }

        if(emailStr.isEmpty())
        {
            email.setError("Enter Email");
            return false;
        }
        if(confirmPassStr.isEmpty())
        {
            comfirmPass.setError("Enter PassWord");
            return false;
        }

        if (passStr.compareTo(confirmPassStr)!=0)
        {
            Toast.makeText(SignUpActivity.this,"khong trung ",Toast.LENGTH_LONG).show();
            return  false;
        }



        return true;
    }

    private void signupNewuser() {
      prgressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,"Sign up Succesfull",Toast.LENGTH_LONG).show();
                            DataBase.createUserData(emailStr,nameStr, new MyCompleteListener() {
                                @Override
                                public void onSucces() {
                                    DataBase.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSucces() {
                                            prgressDialog.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this,"Something wrong",Toast.LENGTH_LONG).show();
                                            prgressDialog.dismiss();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                     Toast.makeText(SignUpActivity.this,"Something wrong",Toast.LENGTH_LONG).show();
                                    prgressDialog.dismiss();
                                }
                            });


                        } else {
                           prgressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}