package xbc.miniproject.com.xbcapplication.model.idleNews.shareIdleNews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelShareIdleNews {
    @SerializedName("email")
    @Expose
    private Object email;

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }
}
