package com.example.exampratice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import static com.example.exampratice.DataBase.g_usersCount;
import static com.example.exampratice.DataBase.g_usersList;
import static com.example.exampratice.DataBase.myperformance;


public class AccountFragment extends Fragment {
    private LinearLayout logoutB;
    private TextView profile_img_text , name , score , rank;
    private LinearLayout laederB , profleB , bookmarkB;

    BottomNavigationView bottomNavigationView;
    private Dialog prgressDialog;
    private TextView dialogText;



    public AccountFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);

        prgressDialog = new Dialog(getContext());
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("LOADING....");



        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");
       String userName = DataBase.myProfile.getName();
       profile_img_text.setText(userName.toUpperCase().substring(0,1));

       name.setText(userName);

       score.setText(String.valueOf(DataBase.myperformance.getScore()));

       if(DataBase.g_usersList.size()==0 )
       {
           prgressDialog.show();
           DataBase.getTopUsers(new MyCompleteListener() {
               @Override
               public void onSucces() {

                   if(DataBase.myperformance.getScore()!=0)
                   {
                       if(!DataBase.isMeOnTopList)
                       {
                           calculateRank();
                       }
                       score.setText("Score : " +DataBase.myperformance.getScore());
                       rank.setText("Rank - "+DataBase.myperformance.getRank());
                   }

                   prgressDialog.dismiss();
               }

               @Override
               public void onFailure() {
                   Toast.makeText(getContext(),"wrong!",Toast.LENGTH_LONG).show();
                   prgressDialog.dismiss();
               }
           });

       }
       else {
           score.setText("Score : " +DataBase.myperformance.getScore());
           if(myperformance.getScore()!=0) rank.setText("Rank - " + DataBase.myperformance.getRank());

       }

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // dang xuat khi tk save la firebase

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();
                GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getContext(),gso);
                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().finish();
                    }
                });



            }
        });
        bookmarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),MyProfileActivity.class);
                startActivity(intent);

            }
        });
        laederB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             bottomNavigationView.setSelectedItemId(R.id.bottomnavigation_leader);
            }
        });
        return view;
    }

    private void calculateRank() {
        int lowTopScore = g_usersList.get(g_usersList.size()-1).getScore();
        int remaining_slots = g_usersCount -20;
        int myslot = (myperformance.getScore()*remaining_slots) / lowTopScore;
        int rank;
        if(lowTopScore != myperformance.getScore())
        {
            rank = g_usersCount -myslot;
        }else {
            rank=21;
        }
        myperformance.setRank(rank);
    }


    private void initViews(View view) {
        logoutB = view.findViewById(R.id.logoutB);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        laederB = view.findViewById(R.id.leaderB);
        bookmarkB = view.findViewById(R.id.bookmarkB);
        profleB = view.findViewById(R.id.profileB);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
    }
}