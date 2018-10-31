
package xbc.miniproject.com.xbcapplication.model.feedback.getQuestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelQuestionFeedback {

    @SerializedName("dataList")
    @Expose
    private List<DataListQuestionFeedback> dataList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DataListQuestionFeedback> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListQuestionFeedback> dataList) {
        this.dataList = dataList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
