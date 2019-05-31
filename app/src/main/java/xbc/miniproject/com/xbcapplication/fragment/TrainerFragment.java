package xbc.miniproject.com.xbcapplication.fragment;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.AddTrainerActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.TrainerListAdapter;
import xbc.miniproject.com.xbcapplication.model.trainer.DataListTrainer;
import xbc.miniproject.com.xbcapplication.model.trainer.ModelTrainer;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class TrainerFragment extends Fragment {

    private EditText trainerEditTextSearch;
    private Button trainerButtonSearch;
    private ImageView trainerButtonInsert;
    private RecyclerView trainerRecyclerViewList;
    private List<DataListTrainer> trainerModelList =  new ArrayList<>();
    private TrainerListAdapter trainerListAdapter;
    private RequestAPIServices apiServices;
    public TrainerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trainer_layout, container, false);
        trainerButtonInsert = (ImageView) view.findViewById(R.id.trainerButtonInsert);
        trainerButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddTrainerActivity.class);
                startActivity(intent);
            }
        });




        trainerRecyclerViewList = (RecyclerView) view.findViewById(R.id.trainerRecyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        trainerRecyclerViewList.setLayoutManager(layoutManager);

        trainerEditTextSearch = (EditText) view.findViewById(R.id.trainerEditTextSearch);
        trainerEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (trainerEditTextSearch.getText().toString().trim().length() == 0){

                } else{
                    String keyword = trainerEditTextSearch.getText().toString().trim();
                    trainerModelList = new ArrayList<>();
                    getDataFromAPI(keyword);
                }

            }
        });


        return  view;
    }

    private void getDataFromAPI(String keyword) {
        final ProgressDialog loading = LoadingClass.loadingAnimationAndText(getContext(),
                "Sedang Memuat Data . . .");
        loading.show();


        String contentTypes = "application/json";
        apiServices = APIUtilities.getAPIServices();
        apiServices.getListTrainer(SessionManager.getToken(getContext()),keyword).enqueue(new Callback<ModelTrainer>() {
            @Override
            public void onResponse(Call<ModelTrainer> call, Response<ModelTrainer> response) {
                loading.dismiss();
                if (response.code() == 200){
                    List<DataListTrainer> tmp = response.body().getDataList();
                    for (int i = 0; i<tmp.size();i++){
                        DataListTrainer data = tmp.get(i);
                        trainerModelList.add(data);
                    }
                    trainerRecyclerViewList.setVisibility(View.VISIBLE);
                    tampilkanListTrainer();
                } else{
                    Toast.makeText(getContext(), "Gagal Mendapatkan List Trainer: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTrainer> call, Throwable t) {
                loading.dismiss();
                 }
        });
    }

    public void filter(String text){
        ArrayList<DataListTrainer> filteredList = new ArrayList<>();

        for(DataListTrainer item: trainerModelList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }

        }
        trainerListAdapter.filterList(filteredList);
    }
    public void tampilkanListTrainer(){

        if(trainerListAdapter==null){
            trainerListAdapter = new TrainerListAdapter(getContext(), trainerModelList);
            trainerRecyclerViewList.setAdapter(trainerListAdapter);
        }
    }

    @Override
    public void onResume() {
        clearSearch();
        super.onResume();
    }

    public void clearSearch(){
        trainerEditTextSearch.setText("");
        trainerRecyclerViewList.setVisibility(View.INVISIBLE);
    }


}

