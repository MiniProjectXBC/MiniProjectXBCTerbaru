
package xbc.miniproject.com.xbcapplication.model.monitoring;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitoringDataList implements Comparable<MonitoringDataList> {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("biodata")
    @Expose
    private MonitoringBiodata monitoringBiodata;
    @SerializedName("idleDate")
    @Expose
    private String idleDate;
    @SerializedName("lastProject")
    @Expose
    private Object lastProject;
    @SerializedName("idleReason")
    @Expose
    private Object idleReason;
    @SerializedName("placementDate")
    @Expose
    private Object placementDate;
    @SerializedName("placementAt")
    @Expose
    private String placementAt;
    @SerializedName("notes")
    @Expose
    private Object notes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MonitoringBiodata getMonitoringBiodata() {
        return monitoringBiodata;
    }

    public void setMonitoringBiodata(MonitoringBiodata monitoringBiodata) {
        this.monitoringBiodata = monitoringBiodata;
    }

    public String getIdleDate() {
        return idleDate;
    }

    public void setIdleDate(String idleDate) {
        this.idleDate = idleDate;
    }

    public Object getLastProject() {
        return lastProject;
    }

    public void setLastProject(Object lastProject) {
        this.lastProject = lastProject;
    }

    public Object getIdleReason() {
        return idleReason;
    }

    public void setIdleReason(Object idleReason) {
        this.idleReason = idleReason;
    }

    public Object getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Object placementDate) {
        this.placementDate = placementDate;
    }

    public String getPlacementAt() {
        return placementAt;
    }

    public void setPlacementAt(String placementAt) {
        this.placementAt = placementAt;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(Object notes) {
        this.notes = notes;
    }

    @Override
    public int compareTo(@NonNull MonitoringDataList monitoringDataList) {
        Date date1 = null;
        Date date2 = null;

        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date1 = sourceFormat.parse(this.idleDate);
            date2 = sourceFormat.parse(monitoringDataList.idleDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1.compareTo(date2);
    }
}
