
package xbc.miniproject.com.xbcapplication.model.feedback.getQuestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataListQuestionFeedback {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notes")
    @Expose
    private Object notes;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(Object notes) {
        this.notes = notes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
