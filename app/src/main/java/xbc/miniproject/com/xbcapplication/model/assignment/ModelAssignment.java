
package xbc.miniproject.com.xbcapplication.model.assignment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelAssignment {

    @SerializedName("dataList")
    @Expose
    private List<AssignmentList> assignmentList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<AssignmentList> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<AssignmentList> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
