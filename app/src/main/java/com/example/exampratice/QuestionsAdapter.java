package com.example.exampratice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.exampratice.DataBase.ANSWERED;
import static com.example.exampratice.DataBase.REVIEW;
import static com.example.exampratice.DataBase.UNANSWERED;
import static com.example.exampratice.DataBase.g_quesList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questionsList;

    public QuestionsAdapter(List<QuestionModel> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ques ;
        private Button optionA ,  optionB , optionC , optionD , prevSelectedB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques =itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB= itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;
        }
        private void setData(final int pos)
        {
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOption(optionA,1,pos);
            setOption(optionB,2,pos);
            setOption(optionC,3,pos);
            setOption(optionD,4,pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionA,1,pos);
                }


            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB,2,pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC,3,pos);
                }
            });
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD,4,pos);
                }
            });

        }

        private void selectOption(Button btn, int option_num, int quesID) {
               if(prevSelectedB == null)
               {
                   btn.setBackgroundResource(R.drawable.select_btn);
                   DataBase.g_quesList.get(quesID).setSelectedAns(option_num);
                   changeStatus(quesID,ANSWERED);
                   prevSelectedB =btn;
               }
               else {
                   if (prevSelectedB.getId() == btn.getId())
                   {
                       btn.setBackgroundResource(R.drawable.unselected_btn);
                       DataBase.g_quesList.get(quesID).setSelectedAns(-1);
                       changeStatus(quesID,UNANSWERED);
                       prevSelectedB = null;
                   }
                  else {
                      prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                      btn.setBackgroundResource(R.drawable.select_btn);
                      DataBase.g_quesList.get(quesID).setSelectedAns(option_num);
                       changeStatus(quesID,ANSWERED);
                      prevSelectedB = btn;
                   }
               }
        }

        private void changeStatus(int id, int status) {
              if(g_quesList.get(id).getStatus()!=REVIEW)
              {
                  g_quesList.get(id).setStatus(status);
              }
        }

        private void setOption(Button btn, int option_num, int quesID) {
              if(DataBase.g_quesList.get(quesID).getSelectedAns() == option_num)
              {
                  btn.setBackgroundResource(R.drawable.select_btn);
              }
              else {
                  btn.setBackgroundResource(R.drawable.unselected_btn);
              }
        }
    }




}
