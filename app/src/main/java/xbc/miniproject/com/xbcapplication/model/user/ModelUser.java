
package xbc.miniproject.com.xbcapplication.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelUser {
    @SerializedName("role")
    @Expose
    private Role role;
    @SerializedName("data")
    @Expose
    private User data;
    @SerializedName("dataList")
    @Expose
    private List<DataList> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Role getRole(){
        return role;
    }

    public void setDataListRole(Role role) {
        this.role = role;
    }
    public User getData(){
        return data;
    }





    public void setDataList(User data) {
        this.data = data;
    }
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
