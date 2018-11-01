package xbc.miniproject.com.xbcapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.AddBiodataActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.BiodataListAdapter;
import xbc.miniproject.com.xbcapplication.dummyModel.BiodataModel;
import xbc.miniproject.com.xbcapplication.model.biodata.BiodataList;
import xbc.miniproject.com.xbcapplication.model.biodata.ModelBiodata;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.LoadingClass;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class BiodataFragment extends Fragment {
    public EditText biodataEditTextSearch;
    private ImageView biodataButtonInsert, biodataButtonSearch;
    private RecyclerView biodataRecyclerViewList;

    private List<BiodataList> listBiodata = new ArrayList<>();
    private BiodataListAdapter biodataListAdapter;

    private RequestAPIServices apiServices;
    private ProgressDialog loading;

    public BiodataFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biodata, container, false);

        biodataRecyclerViewList = (RecyclerView) view.findViewById(R.id.biodataRecyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayout.VERTICAL, false);
        biodataRecyclerViewList.setLayoutManager(layoutManager);

        loading = LoadingClass.loadingAnimationAndText(getContext(),
                "Sedang Memuat Data . . .");

        biodataEditTextSearch = (EditText) view.findViewById(R.id.biodataEditTextSearch);
        biodataEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (biodataEditTextSearch.getText().toString().trim().length() == 0) {
                    biodataRecyclerViewList.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0){
                    if (s.toString().length()==0) {
                        biodataRecyclerViewList.setVisibility(View.INVISIBLE);
                    }
                } else {
                    biodataRecyclerViewList.setVisibility(View.VISIBLE);
                    //String keyword = biodataEditTextSearch.getText().toString().trim();
                    getDataFromAPI(s.toString());
                }
            }
        });

        biodataButtonInsert = (ImageView) view.findViewById(R.id.biodataButtonInsert);
        biodataButtonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddBiodataActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getDataFromAPI(String keyword) {
        loading.show();
        apiServices = APIUtilities.getAPIServices();
        apiServices.getListBiodata(SessionManager.getToken(getContext()),keyword).enqueue(new Callback<ModelBiodata>() {
            @Override
            public void onResponse(Call<ModelBiodata> call, Response<ModelBiodata> response) {
                loading.dismiss();
                if (response.code() == 200){
                    listBiodata = new ArrayList<>();
                    List<BiodataList> tmp = response.body().getDataList();
                    for (int i = 0; i<tmp.size();i++){
                        BiodataList data = tmp.get(i);
                        listBiodata.add(data);
                    }
                    if (biodataEditTextSearch.getText().toString().trim().length() == 0) {
                        biodataRecyclerViewList.setVisibility(View.INVISIBLE);
                    } else{
                        biodataRecyclerViewList.setVisibility(View.VISIBLE);
                    }

                    tampilkanListBiodata();
                } else{
                    Toast.makeText(getContext(), "Gagal Mendapatkan List Biodata: " + response.code() + " msg: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelBiodata> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), "List Biodata onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filter(String text) {
        ArrayList<BiodataList> filteredList = new ArrayList<>();

        for (BiodataList item : listBiodata) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        biodataListAdapter.filterList(filteredList);
    }

    private void tampilkanListBiodata() {
        if (biodataListAdapter == null) {
            biodataListAdapter = new BiodataListAdapter(getContext(), listBiodata);
            biodataRecyclerViewList.setAdapter(biodataListAdapter);
        }
    }

    @Override
    public void onResume() {
        clearSearch();
        super.onResume();
    }

    public void clearSearch(){
        biodataEditTextSearch.setText("");
        biodataRecyclerViewList.setVisibility(View.INVISIBLE);

    }
}
