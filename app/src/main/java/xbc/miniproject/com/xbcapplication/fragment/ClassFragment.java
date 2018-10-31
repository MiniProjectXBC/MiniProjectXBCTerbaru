package xbc.miniproject.com.xbcapplication.fragment;

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
import xbc.miniproject.com.xbcapplication.R;
import xbc.miniproject.com.xbcapplication.adapter.ClassListAdapter;
import xbc.miniproject.com.xbcapplication.model.kelas.DataList;
import xbc.miniproject.com.xbcapplication.model.kelas.ModelClass;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class ClassFragment extends Fragment {
    private EditText classEditTextSearch;
    private ImageView classButtonSearch;
    private RecyclerView classRecyclerViewList;
    private List<DataList> listClass = new ArrayList<>();
    private ClassListAdapter classListAdapter;
    private RequestAPIServices apiServices;
    public ClassFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        classRecyclerViewList = (RecyclerView) view.findViewById(R.id.classRecyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        classRecyclerViewList.setLayoutManager(layoutManager);

        classEditTextSearch = (EditText) view.findViewById(R.id.classEditTextSearch);
        classRecyclerViewList.setVisibility(View.INVISIBLE);
        classEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (classEditTextSearch.getText().toString().trim().length() == 0){
                    classRecyclerViewList.setVisibility(View.INVISIBLE);
                }
//
//                else {
//                    classRecyclerViewList.setVisibility(View.VISIBLE);
//                    filter(editable.toString());
//                }
            }
        });
//        tampilkanListClass();
//
//        return view;
        classButtonSearch = (ImageView) view.findViewById(R.id.classButtonSearch);
        classButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classEditTextSearch.getText().toString().trim().length() == 0){
                    Toast.makeText(getContext(), "Empty Keyword !", Toast.LENGTH_SHORT).show();
                }else{
                    getDataFromAPI(classEditTextSearch.getText().toString().trim());
                }
            }
        });
        return view;
    }

    private void getDataFromAPI(String keyword){
        String contentType = Constanta.CONTENT_TYPE_API;
        String token = SessionManager.getToken(getContext());

        apiServices = APIUtilities.getAPIServices();
        apiServices.getListClass(contentType, token, keyword).enqueue(new Callback<ModelClass>() {
            @Override
            public void onResponse(Call<ModelClass> call, Response<ModelClass> response) {
                if(response.code() == 200){
                    if(response.body().getDataList().size()>0){
                        classRecyclerViewList.setVisibility(View.VISIBLE);
                        tampilkanListClass(response.body().getDataList());
                    }
                } else {
                    Toast.makeText(getContext(),"Gagal Mendapatkan List Class: " + response.code() + " msg: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelClass> call, Throwable t) {
                Toast.makeText(getContext(), "List Class onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void filter(String text) {
        ArrayList<DataList> filteredList = new ArrayList<>();

        for (DataList item : listClass) {
            if (item.getClass().toString().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        classListAdapter.filterList(filteredList);
    }

    private void tampilkanListClass(List<DataList> dataLists){
        classListAdapter = new ClassListAdapter(getContext(), dataLists);
        classRecyclerViewList.setAdapter(classListAdapter);
        classListAdapter.notifyDataSetChanged();
    }

//    private void addDummyList() {
//        int index = 1;
//        for (int i = 0; i < 5; i++) {
//            ClassModel data = new ClassModel();
//            data.setBatch("Dummy Batch " + index);
//            data.setName("Dummy Name " + index);
//            listClass.add(data);
//            index++;
//        }
//    }
}
