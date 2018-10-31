package xbc.miniproject.com.xbcapplication.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.feedback.getQuestion.DataListQuestionFeedback;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;

public class FeedbackViewHolder extends RecyclerView.ViewHolder {
    private Context context;

     public static TextView question;
    public static EditText  customListFeedback;
    Button feedbackButtonSave, feedbackButtonCancel;
    RequestAPIServices apiServices;
    public static int id, position;

    public FeedbackViewHolder(@NonNull View itemView) {
        super(itemView);
        //context = itemView.getContext();
        question = (TextView) itemView.findViewById(R.id.question);
        customListFeedback = (EditText) itemView.findViewById(R.id.customListFeedback);

        customListFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setFeedbackText();

            }
        });
    }

    public void setModel(DataListQuestionFeedback dataListQuestionFeedback, final int position, final Context context) {
        this.position = position;
        question.setText(dataListQuestionFeedback.getName());
        //customListFeedback.setText(dataListQuestionFeedback);

    }

    public void  setFeedbackText(){
        String text = customListFeedback.getText().toString().trim();
        if (text.length() == 0){
            Constanta.ARRAY_FEEDBACK[position]="-";

        }
        else{
            Constanta.ARRAY_FEEDBACK[position]=text;

        }

    }
    }

