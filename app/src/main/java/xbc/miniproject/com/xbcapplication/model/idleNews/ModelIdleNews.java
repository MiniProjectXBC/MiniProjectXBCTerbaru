
package xbc.miniproject.com.xbcapplication.model.idleNews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelIdleNews {

    @SerializedName("dataList")
    @Expose
    private List<IdleNewsList> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<IdleNewsList> getDataList() {
        return dataList;
    }

    public void setDataList(List<IdleNewsList> idleNewsList) {
        this.dataList = idleNewsList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
