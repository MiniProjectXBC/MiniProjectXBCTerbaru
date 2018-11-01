package xbc.miniproject.com.xbcapplication.fragment;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.AddTechnologyActivity;
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.TechnologyListAdapter;
import xbc.miniproject.com.xbcapplication.model.technology.DataList;
import xbc.miniproject.com.xbcapplication.model.technology.ModelTechnology;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class TechnologyFragment extends Fragment {
   // private Context context;
    private EditText technologyEditTextSearch;
    private ImageView technologyButtonInsert;
    private RecyclerView technologyRecyclerViewList;
    private List<DataList> technologyModelList =  new ArrayList<>();
    private TechnologyListAdapter technologyListAdapter;
    private RequestAPIServices apiServices;
    public TechnologyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     final View view = inflater.inflate(R.layout.fragment_technology, container, false);

     technologyRecyclerViewList = (RecyclerView)view.findViewById(R.id.technologyRecyclerViewList);
     RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
             LinearLayout.VERTICAL,
             false);
     technologyRecyclerViewList.setLayoutManager(layoutManager);
     technologyEditTextSearch =(EditText) view.findViewById(R.id.technologyEditTextSearch);
     technologyRecyclerViewList.setVisibility(view.INVISIBLE);
     technologyEditTextSearch.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {

         }

         @Override
         public void afterTextChanged(Editable s) {
             if(technologyEditTextSearch.getText().toString().trim().length()==0){
                 //Toast.makeText(getContext(), "Empty Keyword !", Toast.LENGTH_SHORT).show();
                 technologyRecyclerViewList.setVisibility(View.INVISIBLE);
             }else{
                 getDataFromApi(technologyEditTextSearch.getText().toString().trim());
             }
         }
     });

     technologyButtonInsert = (ImageView) view.findViewById(R.id.technologyButtonInsert);
     technologyButtonInsert.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(getContext(), AddTechnologyActivity.class);
             startActivity(intent);
         }
     });
        return  view;
    }
    private void getDataFromApi(String keyword){
        String contentType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(getContext());

        apiServices = APIUtilities.getAPIServices();
        apiServices.getListTechnology(contentType, token, keyword).enqueue(new Callback<ModelTechnology>() {
            @Override
            public void onResponse(Call<ModelTechnology> call, Response<ModelTechnology> response) {
                if(response.code()==200){
                    if(response.body().getDataList().size()>0){
                        technologyRecyclerViewList.setVisibility(View.VISIBLE);
                        tampilkanListTechnology(response.body().getDataList());
                    }
                }else{
                    Toast.makeText(getContext(), "Gagal Mendapatkan List Technology :"+response.code()+"msg: "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTechnology> call, Throwable t) {
                Toast.makeText(getContext(), "List User onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
//    public void filter(String text){
//        ArrayList<DataList> filteredList = new ArrayList<>();
//
//        for(DataList item: technologyModelList){
//            if(item.getName().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(item);
//            }
//
//        }
//        technologyListAdapter.filterList(filteredList);
//    }
    public void tampilkanListTechnology(List<DataList> dataLists){

            technologyListAdapter = new TechnologyListAdapter(getContext(), dataLists);
            technologyRecyclerViewList.setAdapter(technologyListAdapter);
            technologyListAdapter.notifyDataSetChanged();

    }
//    public void addDummyList(){
//        int index =1;
//        for(int i=0; i<5; i++){
//            TechnologyModel data =  new TechnologyModel();
//            data.setName("Dummy Name"+index);
//            technologyModelList.add(data);
//            index++;
//        }
//
//    }
}