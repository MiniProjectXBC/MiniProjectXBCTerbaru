package xbc.miniproject.com.xbcapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.AddBiodataActivity;
import xbc.miniproject.com.xbcapplication.HomeActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.FeedbackListAdapter;
import xbc.miniproject.com.xbcapplication.model.feedback.autoComplete.DataListAutocompleteFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.autoComplete.ModelAutocompleteFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.getQuestion.DataListQuestionFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.getQuestion.ModelQuestionFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.postCreate.Example;
import xbc.miniproject.com.xbcapplication.model.feedback.postCreate.Feedback;
import xbc.miniproject.com.xbcapplication.model.feedback.postCreate.ModelCreateFeedback;
import xbc.miniproject.com.xbcapplication.model.idleNews.IdleNewsList;
import xbc.miniproject.com.xbcapplication.model.idleNews.ModelIdleNews;
import xbc.miniproject.com.xbcapplication.model.kelas.DataList;
import xbc.miniproject.com.xbcapplication.model.kelas.ModelClass;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.KArrayAdapter;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;
import xbc.miniproject.com.xbcapplication.viewHolder.FeedbackViewHolder;

public class FeedbackFragment extends Fragment  {
    private RecyclerView feedbackRecyclerView;
    private AutoCompleteTextView feedbackTextName;

    Button feedbackButtonSave, feedbackButtonCancel;
    private FeedbackListAdapter feedbackListAdapter;


    private RequestAPIServices apiServices;
    KArrayAdapter<DataListAutocompleteFeedback> adapter;
    int idAutoComplete;

    private List<DataListAutocompleteFeedback> dataListAutocompleteFeedbacks= new ArrayList<>();

    private List<DataListQuestionFeedback> dataListQuestionFeedbacks = new ArrayList<>();
    private List<Feedback> dataListAnswer = new ArrayList<>();





    private RequestAPIServices requestAPIServices;

    private List<DataListAutocompleteFeedback> feedbackModelList = new ArrayList<>();

    private boolean isTestSelected;
    private String[] test = {"Android", "Java"
    };


    public FeedbackFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        feedbackRecyclerView = (RecyclerView) view.findViewById(R.id.feedbackRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayout.VERTICAL,
                false);
        feedbackRecyclerView.setLayoutManager(layoutManager);
        feedbackRecyclerView.setVisibility(View.GONE);




        feedbackButtonSave = (Button) view.findViewById(R.id.feedbackButtonSave);
        feedbackButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });

        feedbackButtonCancel = (Button) view.findViewById(R.id.feedbackButtonCancel);
        feedbackButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();

                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
//                feedbackRecyclerView.setVisibility(View.GONE);
//                feedbackTextName.setText("");
//                feedbackTextName.setError(null);
            }
        });




        feedbackTextName = (AutoCompleteTextView) view.findViewById(R.id.feedbackTextName);

        feedbackTextName.setThreshold(1);



        feedbackTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackTextName.getText().toString().trim().length() == 0) {
                    //feedbackTextName.showDropDown();
                }
            }
        });

        feedbackTextName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isTestSelected = true;
                feedbackTextName.setError(null);
                //filter(feedbackTextName.getText().toString());
                feedbackRecyclerView.setVisibility(View.VISIBLE);
                tampilkanListQuestion(feedbackTextName.getText().toString());



                DataListAutocompleteFeedback selected = (DataListAutocompleteFeedback) parent.getAdapter().getItem(position);
                int aidi = selected.getId();
                idAutoComplete = selected.getId();
                Toast.makeText(getContext(),"idnya ini: "+aidi,Toast.LENGTH_LONG).show();




            }
        });




        feedbackTextName.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isTestSelected = false;
                feedbackTextName.setError("Test must from the list!");
                dataListQuestionFeedbacks = new ArrayList<>();
                feedbackRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (feedbackTextName.getText().toString().trim().length() != 0) {
                    feedbackRecyclerView.setVisibility(View.GONE);
                    String keyword = feedbackTextName.getText().toString().trim();
                    tampil_auto_complete(keyword);

                }
            }
        });




        //tampilkanListQuestion();
        return view;

    }

    public void tampil_auto_complete(String keyword){
        apiServices = APIUtilities.getAPIServices();
        apiServices.roleautocomplete(SessionManager.getToken(getContext()), keyword).enqueue(new Callback<ModelAutocompleteFeedback>() {
            @Override
            public void onResponse(Call<ModelAutocompleteFeedback> call, Response<ModelAutocompleteFeedback> response) {
                if (response.code() == 200){

                    dataListAutocompleteFeedbacks = response.body().getDataList();
                    System.out.println(Arrays.toString(dataListAutocompleteFeedbacks.toArray()));
                    getAutoCompletAdapter();
//                    if (response.body().getMessage() != null){
//                        List<String> str = new ArrayList<String>();
//                        for (DataListAutocompleteFeedback s : response.body().getDataList()){
//                            System.out.println(Arrays.toString(response.body().getDataList().toArray()));
//                            str.add(s.getId().toString());
//                        }
//                       ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.select_dialog_item, str.toArray(new String[0]));
//                        feedbackTextName.setThreshold(1);
//                        feedbackTextName.setAdapter(adapter);
//
//
//                    }
                }

            }

            @Override
            public void onFailure(Call<ModelAutocompleteFeedback> call, Throwable t) {

            }
        });

    }

    private void getAutoCompletAdapter() {
        adapter = new KArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, dataListAutocompleteFeedbacks);
        feedbackTextName.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void inputValidation() {
        if (feedbackTextName.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Test Field still empty!", Toast.LENGTH_SHORT).show();
        }
        else if (FeedbackViewHolder.question.getText().toString().trim().length() == 0){
            Toast.makeText(getActivity(), "Question Field still empty!", Toast.LENGTH_SHORT).show();
        }

//        else if (FeedbackViewHolder.customListFeedback.getText().toString().trim().length() == 0){
//            Toast.makeText(getActivity(), "Notes Field still empty!", Toast.LENGTH_SHORT).show();
//        }
        else {
            String id = feedbackTextName.getText().toString();
            String[] questionId = Constanta.ARRAY_ID;
            String[] answer = Constanta.ARRAY_FEEDBACK;
            saveFeedback(idAutoComplete+"", questionId, answer);

            //saveSuccesNotification();

        }
    }
    private void saveFeedback(String id, String[] questionId, String[] answer){
        String contentType = "application/json";
        String json = APIUtilities.generateCreateFeedback(id, questionId, answer);
        final String  tokenAuthorization = SessionManager.getToken(getContext());
        RequestBody bodyRequest = RequestBody.create(APIUtilities.mediaType(), json);

        apiServices = APIUtilities.getAPIServices();
        apiServices.createFeedback(contentType, tokenAuthorization, bodyRequest).enqueue(new Callback<ModelCreateFeedback>() {
            @Override
            public void onResponse(Call<ModelCreateFeedback> call, Response<ModelCreateFeedback> response) {

                if (response.code() == 201){
                    String message = response.body().getMessage();
                    if (message != null){
                        saveSuccesNotification(message);
                    } else{
                        saveSuccesNotification("Data Gagal ditambahkan");
                    }
                }
                else {
                    Toast.makeText(getContext(), "Create Feedback Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelCreateFeedback> call, Throwable t) {
                Toast.makeText(getContext(), "Error onFailure : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void saveSuccesNotification(String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("NOTIFICATION !")
                .setMessage("Data Successfully Submitted!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
        listKosong();
    }


    public void tampilkanListQuestion(String keyword) {
        //addDummyList();


        apiServices = APIUtilities.getAPIServices();
        apiServices.getListQuestionFeedback().enqueue(new Callback<ModelQuestionFeedback>() {
            @Override
            public void onResponse(Call<ModelQuestionFeedback> call, Response<ModelQuestionFeedback> response) {
                if(response.code() == 200){
                    List<DataListQuestionFeedback> tmp = response.body().getDataList();
                    for (int i = 0; i<tmp.size(); i++){
                        DataListQuestionFeedback data = tmp.get(i);
                        dataListQuestionFeedbacks.add(data);
                    }


                    for (int i =0 ; i<tmp.size(); i++){
                        Feedback feedback = new Feedback();
                        feedback.setQuestionId(""+i);
                        feedback.setAnswer("");
                        dataListAnswer.add(feedback);
                    }



                    showAdapter();
                } else {
                    Toast.makeText(getContext(),"Gagal Mendapatkan List Question: " + response.code() + " msg: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelQuestionFeedback> call, Throwable t) {
                Toast.makeText(getContext(), "List Question onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showAdapter() {
        if (feedbackListAdapter == null) {
            feedbackListAdapter = new FeedbackListAdapter(getContext(), dataListQuestionFeedbacks, dataListAnswer);
            feedbackRecyclerView.setAdapter(feedbackListAdapter);
        }
    }

    private void listKosong(){
        feedbackTextName.setText("");
        feedbackListAdapter.clearAdapter();
    }

    public void filter(String text) {
        ArrayList<DataListQuestionFeedback> filteredList = new ArrayList<>();

        for (DataListQuestionFeedback item : dataListQuestionFeedbacks) {
            if (item.getId().toString().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        }
        feedbackListAdapter.filterList(filteredList);
    }













}
