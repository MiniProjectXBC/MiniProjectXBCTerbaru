
package xbc.miniproject.com.xbcapplication.model.feedback.autoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataListAutocompleteFeedback {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
