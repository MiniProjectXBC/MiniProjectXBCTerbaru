
package xbc.miniproject.com.xbcapplication.model.trainer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTrainer {

    @SerializedName("data")
    @Expose
    private Trainer data;
    @SerializedName("dataList")
    @Expose
    private List<DataListTrainer> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataListTrainer> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListTrainer> dataList) {
        this.dataList = dataList;
    }

    public Trainer getData() {
        return data;
    }

    public void setData(Trainer data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
