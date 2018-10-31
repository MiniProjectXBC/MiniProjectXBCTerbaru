
package xbc.miniproject.com.xbcapplication.model.assignment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Biodata {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastEducation")
    @Expose
    private Object lastEducation;
    @SerializedName("graduationYear")
    @Expose
    private Object graduationYear;
    @SerializedName("educationalLevel")
    @Expose
    private Object educationalLevel;
    @SerializedName("majors")
    @Expose
    private Object majors;
    @SerializedName("gpa")
    @Expose
    private Object gpa;
    @SerializedName("active")
    @Expose
    private Object active;

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

    public Object getLastEducation() {
        return lastEducation;
    }

    public void setLastEducation(Object lastEducation) {
        this.lastEducation = lastEducation;
    }

    public Object getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Object graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Object getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(Object educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public Object getMajors() {
        return majors;
    }

    public void setMajors(Object majors) {
        this.majors = majors;
    }

    public Object getGpa() {
        return gpa;
    }

    public void setGpa(Object gpa) {
        this.gpa = gpa;
    }

    public Object getActive() {
        return active;
    }

    public void setActive(Object active) {
        this.active = active;
    }

}
