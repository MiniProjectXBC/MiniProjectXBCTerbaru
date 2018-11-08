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
import xbc.miniproject.com.xbcapplication.AddTestimonyActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.TestimonyListAdapter;
import xbc.miniproject.com.xbcapplication.model.testimony.DataListTestimony;
import xbc.miniproject.com.xbcapplication.model.testimony.ModelTestimony;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class TestimonyFragment extends Fragment {
    private EditText testimonyEditTextSearch;
    private Button testimonyButtonSearch;
    private ImageView testimonyButtonInsert;
    private RecyclerView testimonyRecyclerViewList;
    private List<DataListTestimony> testimonyModelList =  new ArrayList<>();
    private TestimonyListAdapter testimonyListAdapter;
    private RequestAPIServices apiServices;
    public TestimonyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_testimony,container,false);
        testimonyButtonInsert = (ImageView) view.findViewById(R.id.testimonyButtonInsert);
        testimonyButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddTestimonyActivity.class);
                startActivity(intent);
            }
        });



        testimonyRecyclerViewList = (RecyclerView) view.findViewById(R.id.testimonyRecyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        testimonyRecyclerViewList.setLayoutManager(layoutManager);

        testimonyEditTextSearch = (EditText) view.findViewById(R.id.testimonyEditTextSearch);
        testimonyEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (testimonyEditTextSearch.getText().toString().trim().length() == 0){

                }
                else{
                    String keyword = testimonyEditTextSearch.getText().toString().trim();
                    testimonyModelList = new ArrayList<>();
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
        apiServices = APIUtilities.getAPIServices();
        apiServices.getListTestimony(SessionManager.getToken(getContext()),keyword).enqueue(new Callback<ModelTestimony>() {
            @Override
            public void onResponse(Call<ModelTestimony> call, Response<ModelTestimony> response) {
                loading.dismiss();
                if (response.code() == 200){
                    List<DataListTestimony> tmp = response.body().getDataList();
                    for (int i = 0; i<tmp.size();i++){
                        DataListTestimony data = tmp.get(i);
                        testimonyModelList.add(data);
                    }
                    testimonyRecyclerViewList.setVisibility(View.VISIBLE);
                    tampilkanListTestimony();
                } else{
                    Toast.makeText(getContext(), "Gagal Mendapatkan List Testimony: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTestimony> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    public  void filter(String text){
        ArrayList<DataListTestimony> filteredList = new ArrayList<>();
        for(DataListTestimony item : testimonyModelList){
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }

        }
        testimonyListAdapter.filterList(filteredList);
    }
    public void  tampilkanListTestimony(){

        if(testimonyListAdapter==null){
            testimonyListAdapter =  new TestimonyListAdapter(getContext(), testimonyModelList);
            testimonyRecyclerViewList.setAdapter(testimonyListAdapter);
        }
    }

    @Override
    public void onResume() {
        clearSearch();
        super.onResume();
    }

    public void clearSearch(){
        testimonyEditTextSearch.setText("");
        testimonyRecyclerViewList.setVisibility(View.INVISIBLE);
    }


}
