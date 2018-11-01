
package xbc.miniproject.com.xbcapplication.model.biodata.autoComplete;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelBiodataAutoComplete {

    @SerializedName("dataList")
    @Expose
    private List<DataList> dataList = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
