package xbc.miniproject.com.xbcapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.model.feedback.getQuestion.DataListQuestionFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.postCreate.Feedback;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.viewHolder.FeedbackViewHolder;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackViewHolder> {

    private Context context;
    private List<DataListQuestionFeedback> dataListQuestionFeedback;
    private List<Feedback> dataListAnswers;

    public FeedbackListAdapter(Context context, List<DataListQuestionFeedback> dataListQuestionFeedback, List<Feedback> dataListAnswers) {
        this.context = context;
        this.dataListQuestionFeedback = dataListQuestionFeedback;
        this.dataListAnswers = dataListAnswers;

        System.out.println("test"+dataListAnswers.size());
        System.out.println("test"+dataListQuestionFeedback.size());
        if (getItemCount() >0){
            Constanta.ARRAY_FEEDBACK = new String[dataListAnswers.size()];
            for (int x = 0; x<Constanta.ARRAY_FEEDBACK.length; x++){
                Constanta.ARRAY_FEEDBACK[x]=dataListAnswers.get(x).getAnswer().toString();

            }
            Constanta.ARRAY_ID= new  String[dataListQuestionFeedback.size()];
            for (int x = 0; x<Constanta.ARRAY_FEEDBACK.length; x++){
                Constanta.ARRAY_ID[x]=dataListQuestionFeedback.get(x).getId().toString();

            }
        }
        else{
            Constanta.ARRAY_FEEDBACK = new String[0];
            Constanta.ARRAY_ID = new String[0];
        }

    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View customView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.custom_list_feedback,
                viewGroup,
                false
        );
        return new FeedbackViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, int i) {
        final DataListQuestionFeedback user= dataListQuestionFeedback.get(i);
        final Feedback user1 = dataListAnswers.get(i);
        feedbackViewHolder.setModel(user,user1, i, context);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull FeedbackViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.customListFeedback != null && holder.customListFeedback.getText().length() == 0){
            holder.customListFeedback.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        if(dataListQuestionFeedback!=null){
            return dataListQuestionFeedback.size();
        }else {
            return 0;
        }
    }

    public void clearAdapter(){
        dataListQuestionFeedback = new ArrayList<>();
        dataListAnswers = new ArrayList<>();

    }


    public  void filterList(List<DataListQuestionFeedback> filterList){
        dataListQuestionFeedback = filterList;
        notifyDataSetChanged();
    }

    public void filterList1(List<Feedback> filterList1){
        dataListAnswers = filterList1;
        notifyDataSetChanged();
    }




}
