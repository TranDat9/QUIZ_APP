package com.example.exampratice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends AppCompatActivity {
      private EditText name , email , phone;
      private LinearLayout editB;
      private Button cancelB ,saveB;
      private TextView profileText;

      private String nameStr , phoneStr;

      private LinearLayout button_layout;

    private Dialog prgressDialog;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.mp_name);
        email = findViewById(R.id.mp_email);
        phone = findViewById(R.id.mp_phone);
        profileText = findViewById(R.id.profile_text);

        cancelB = findViewById(R.id.cancelB);
        saveB = findViewById(R.id.saveB);
        editB = findViewById(R.id.editB);

        button_layout= findViewById(R.id.button_layout);

        prgressDialog = new Dialog(MyProfileActivity.this);
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("UPDATING DATA....");

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    saveDate();
                }
            }
        });
    }

    private boolean validate() {
        nameStr = name.getText().toString();

        phoneStr = phone.getText().toString();
        if(nameStr.isEmpty())
        {
            name.setError("Name can not be empty");
            return false;
        }

        if(!phoneStr.isEmpty())
        {
            if(!(phoneStr.length()==10) && (TextUtils.isDigitsOnly(phoneStr)) )
            {
                phone.setError("Enter valid phone number");
                return  false;
            }
        }
        return true;
    }

    private void saveDate() {


        prgressDialog.show();

        if(phoneStr.isEmpty())
        {
            phoneStr=null;
        }

        DataBase.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSucces() {
                Toast.makeText(MyProfileActivity.this, "Profile Updated Successfully.",Toast.LENGTH_LONG);
                disableEditing();
                prgressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong ! Please try again later",Toast.LENGTH_LONG);
                prgressDialog.dismiss();
            }
        });

    }

    private void enableEditing() {
        name.setEnabled(true);
        phone.setEnabled(true);
        //email.setEnabled(false);
        button_layout.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        button_layout.setVisibility(View.GONE);

        name.setText(DataBase.myProfile.getName());
        email.setText(DataBase.myProfile.getEmail());

        if( (DataBase.myProfile.getPhone()) != null)
        {
            phone.setText(DataBase.myProfile.getPhone());
        }

        String profileName = DataBase.myProfile.getName();
        profileText.setText(profileName.toUpperCase().substring(0,1));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}