package com.example.exampratice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import static com.example.exampratice.DataBase.ANSWERED;
import static com.example.exampratice.DataBase.NOT_VISITED;
import static com.example.exampratice.DataBase.REVIEW;
import static com.example.exampratice.DataBase.UNANSWERED;

public class QuestionGridAdapter extends BaseAdapter {
    private Context conText ;
    private int numOfQues;


    public QuestionGridAdapter(Context conText, int numOfQues) {
        this.conText = conText;
        this.numOfQues = numOfQues;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;
        if(view == null)
        {
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);

        }
        else {
            myView = view;
        }
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(conText instanceof QuestionsActivity)
                  ((QuestionsActivity) conText).goToQuestion(i);
            }
        });
        TextView quesTV = myView.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(i+1));
        switch (DataBase.g_quesList.get(i).getStatus())
        {
            case NOT_VISITED :
                 quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.grey)));
                 break;
            case UNANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.red)));
                break;
            case ANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.green)));
                break;
            case REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.pink)));
                break;
            default:
                break;

        }
        return myView;
    }
}
