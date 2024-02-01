package com.example.exampratice;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.exampratice.DataBase.g_usersCount;
import static com.example.exampratice.DataBase.g_usersList;
import static com.example.exampratice.DataBase.myperformance;

public class LeaderBoardFragment extends Fragment {
    private TextView totalUsersTV , myImgTextTV, myScoreTV , myRankTV ;
    private RecyclerView usersView;
    private RankAdapter adapter;

    private Dialog prgressDialog;
    private TextView dialogText;



    public LeaderBoardFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        initViews(view);

        prgressDialog = new Dialog(getContext());
        prgressDialog.setContentView(R.layout.dialog_layout);
        prgressDialog.setCancelable(false);
        prgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = prgressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("LOADING....");
        prgressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(g_usersList);

        usersView.setAdapter(adapter);
        DataBase.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSucces() {

                adapter.notifyDataSetChanged();
                if(DataBase.myperformance.getScore()!=0)
                {
                    if(!DataBase.isMeOnTopList)
                    {
                        calculateRank();
                    }
                    myScoreTV.setText("Score : " +DataBase.myperformance.getScore());
                    myRankTV.setText("Rank : " +DataBase.myperformance.getRank());
                }

                prgressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(),"wrong!",Toast.LENGTH_LONG).show();
             prgressDialog.dismiss();
            }
        });

        totalUsersTV.setText("Total Users : "+DataBase.g_usersCount);
        myImgTextTV.setText(myperformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void calculateRank() {
           int lowTopScore = g_usersList.get(g_usersList.size()-1).getScore();
           int remaining_slots = g_usersCount -20;
           int myslot = (myperformance.getScore()*remaining_slots) / lowTopScore;
           int rank;
           if(lowTopScore != myperformance.getScore())
           {
               rank = g_usersCount - myslot;
           }else {
               rank=21;
           }
           myperformance.setRank(rank);
    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.total_users);
        myImgTextTV = view.findViewById(R.id.img_text);
        myScoreTV = view.findViewById(R.id.total_score);
        myRankTV = view.findViewById(R.id.rank);
        usersView = view.findViewById(R.id.users_view);

    }
}