
package xbc.miniproject.com.xbcapplication.model.assignment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignmentList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("biodata")
    @Expose
    private Biodata biodata;
    @SerializedName("title")
    @Expose
    private Object title;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("realizationDate")
    @Expose
    private Object realizationDate;
    @SerializedName("notes")
    @Expose
    private Object notes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Biodata getBiodata() {
        return biodata;
    }

    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(Object realizationDate) {
        this.realizationDate = realizationDate;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(Object notes) {
        this.notes = notes;
    }

}
