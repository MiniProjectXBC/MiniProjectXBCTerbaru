
package xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelMonitoringAutoComplete {

    @SerializedName("dataList")
    @Expose
    private List<DataList> dataList = null;
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
