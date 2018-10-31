
package xbc.miniproject.com.xbcapplication.model.technology;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTechnology {

    @SerializedName("data")
    @Expose
    private Technology data;
    @SerializedName("dataList")
    @Expose
    private List<DataList> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Technology getData(){
        return data;
    }
    public void setDataList(Technology data) {
        this.data = data;
    }

    public List<DataList> getDataList() {
        return dataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
