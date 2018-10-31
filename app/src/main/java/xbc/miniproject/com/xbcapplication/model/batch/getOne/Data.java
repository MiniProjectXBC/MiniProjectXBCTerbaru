
package xbc.miniproject.com.xbcapplication.model.batch.getOne;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("technology")
    @Expose
    private Technology technology;
    @SerializedName("trainer")
    @Expose
    private Trainer trainer;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("periodFrom")
    @Expose
    private String periodFrom;
    @SerializedName("periodTo")
    @Expose
    private String periodTo;
    @SerializedName("room")
    @Expose
    private String room;
    @SerializedName("bootcampType")
    @Expose
    private String bootcampType;
    @SerializedName("notes")
    @Expose
    private String notes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBootcampType() {
        return bootcampType;
    }

    public void setBootcampType(String bootcampType) {
        this.bootcampType = bootcampType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
