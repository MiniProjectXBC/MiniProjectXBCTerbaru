
package xbc.miniproject.com.xbcapplication.model.testimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTestimony {

    @SerializedName("dataList")
    @Expose
    private List<DataListTestimony> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;
    private Testimony data;

    public List<DataListTestimony> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListTestimony> dataList) {
        this.dataList = dataList;
    }

    public Testimony getData() {
        return data;
    }

    public void setData(Testimony data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
