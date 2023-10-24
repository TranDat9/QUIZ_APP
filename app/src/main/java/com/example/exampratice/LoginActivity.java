package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText email , pass;
    Button loginB;
    TextView forgotpassB , signupB;
  private FirebaseAuth mAuth;
  GoogleSignInClient googleSignInClient;
  FirebaseDatabase firebaseDatabase;
  private Dialog prgressDialog;
  TextView dialogText;
  private RelativeLayout ggSignB;
  int RC_SIGN_IN=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);

        loginB = findViewById(R.id.loginB);
        ggSignB = findViewById(R.id.relativegg);

        forgotpassB = findViewById(R.id.forgotpassB);
        signupB = findViewById(R.id.signupB);
        prgressDialog = new Dialog(LoginActivity.this);
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing in ....");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData())
                {
                    login();
                }
            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        ggSignB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignin();
            }
        });


    }

    private void googleSignin() {
       Intent intent = googleSignInClient.getSignInIntent();
       startActivityForResult(intent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                 GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (Exception e)
            {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null );
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this,"GG Sign In Success",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                DataBase.createUserData(user.getEmail().toString(), user.getDisplayName(), new MyCompleteListener() {
                                    @Override
                                    public void onSucces() {
                                        DataBase.loadData(new MyCompleteListener() {
                                            @Override
                                            public void onSucces() {
                                                // dua vao realtime data base
                                                HashMap<String,Object> map = new HashMap<>();
                                                map.put("id",user.getUid());
                                                map.put("name",user.getDisplayName());
                                                map.put("profile",user.getPhotoUrl().toString());
                                                firebaseDatabase.getReference().child("users").child(user.getUid()).setValue(map);
                                                //
                                                prgressDialog.dismiss();
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                prgressDialog.dismiss();
                                                Toast.makeText(LoginActivity.this,"wrong!",Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure() {
                                        prgressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this,"wrong!",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                        DataBase.loadData(new MyCompleteListener() {
                                            @Override
                                            public void onSucces() {
                                                prgressDialog.dismiss();
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                prgressDialog.dismiss();
                                                Toast.makeText(LoginActivity.this,"wrong!",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }

                        else {
                            prgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void login() {
        prgressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_LONG).show();
                            DataBase.loadData(new MyCompleteListener() {
                                @Override
                                public void onSucces() {
                                    prgressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    prgressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"wrong!",Toast.LENGTH_LONG).show();
                                }
                            });


                        } else {
                            prgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private boolean validateData() {

        if(email.getText().toString().isEmpty())
        {
            email.setError("Enter E-mail");
            return  false;
        }
        if(pass.getText().toString().isEmpty())
        {
            pass.setError("Enter Password");
            return  false;
        }

        return true;
    }
}