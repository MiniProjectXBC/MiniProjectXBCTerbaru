package xbc.miniproject.com.xbcapplication.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

public class APIUtilities {
    public static String BASE_URL = "http://139.162.5.173:8080/";

    public static RequestAPIServices getAPIServices() {
        return RetrofitClient.getClient(BASE_URL).create(RequestAPIServices.class);
    }

    public static MediaType mediaType() {
        return okhttp3.MediaType.parse("application/json; charset=utf-8");
    }

    public static String generateLoginMap(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (username != null) map.put("username", username);
        if (password != null) map.put("password", password);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //generate get idleNews MAP params
    public static String generateIdleNewsMap(String id, String title, String category, String content) {
        Map<String, Object> map = new HashMap<>();
        if (title != null) map.put("title", title);
        if (content != null) map.put("content", content);

        if (category != null) {
            Map<String, String> unitObj = new HashMap<>();
            unitObj.put("id", id);
            map.put("category", unitObj);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //generated get assignment MAP param
    public static String generateAssignmentMap (String name, String title, String startDate, String endDate, String description){
        Map<String, Object> map = new HashMap<>();
        if (title != null) map.put("title", title);
        if (description != null) map.put("content", description);
        if (startDate != null) map.put("startDate", startDate);
        if (endDate != null) map.put("endDate", endDate);

        if (name != null) {
            Map<String, String> unitObj = new HashMap<>();
            unitObj.put("name", name);
            map.put("name", unitObj);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //Generate Monitoring Request Body
    public static String generateMonitoring(String id, String idleDate, String lastProject, String idleReason){
        Map<String,Object> map = new HashMap<>();
        if(idleDate != null) map.put("idleDate", idleDate);
        if(lastProject != null) map.put("lastProject", lastProject);
        if(idleReason != null) map.put("idleReason", idleReason);

        if(id != null){
            Map<String,String> idObj = new HashMap<>();
            idObj.put("id",id);

            map.put("biodata",idObj);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //Generate Monitoring Request Body
    public static String generateEditMonitoring(String idMonitoring,
                                                String idBiodata,
                                                String idleDate,
                                                String lastProject,
                                                String idleReason){
        Map<String,Object> map = new HashMap<>();
        if(idMonitoring !=null) map.put("id", idMonitoring);
        if(idleDate != null) map.put("idleDate", idleDate);
        if(lastProject != null) map.put("lastProject", lastProject);
        if(idleReason != null) map.put("idleReason", idleReason);

        if(idBiodata != null){
            Map<String,String> idObj = new HashMap<>();
            idObj.put("id",idBiodata);

            map.put("biodata",idObj);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //Generate Placement Request Body
    public static String generatedPlacementBody(String idMonitoring,
                                                String placementDate,
                                                String placementAt,
                                                String notes){

        Map<String,String> map = new HashMap<>();
        if(idMonitoring !=null) map.put("id", idMonitoring);
        if(placementDate != null) map.put("placementDate", placementDate);
        if(placementAt != null) map.put("placementAt", placementAt);
        if(notes != null) map.put("notes", notes);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    //generate Create Batch
    public static String generateBatch (String technology, String trainer, String
            name, String periodFrom, String periodTo, String room, String bootcampType, String note ){
        Map<String, Object> map = new HashMap<>();
        if(technology!=null){
            Map <String, String> unitObj =  new HashMap<>();
            unitObj.put("id", technology);

            map.put("technology", unitObj);
        }
        if(trainer!=null){
            Map<String, String> unitObj =  new HashMap<>();
            unitObj.put("id", trainer);

            map.put("trainer", unitObj);
        }
        if (name != null) map.put("name", name);
        if (periodFrom != null) map.put("periodFrom", periodFrom);
        if (periodTo != null) map.put("periodTo", periodTo);
        if (room != null) map.put("room", room);
        if (bootcampType != null) map.put("bootcampType", bootcampType);
        if (note != null) map.put("note", note);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }



    //generate Create Batch AddParticipant
    public static String generateBatchAddParticipant (String Batch, String Biodata){
        Map<String, Object> map = new HashMap<>();
        if(Batch!=null){
            Map <String, String> unitObj =  new HashMap<>();
            unitObj.put("id", Batch);

            map.put("batch", unitObj);
        }
        if(Biodata!=null){
            Map <String, String> unitObj =  new HashMap<>();
            unitObj.put("id", Biodata);

            map.put("biodata", unitObj);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }

    public static String generateCreateFeedback(String id, String[] questionId, String[] answer){
        Map<String, Object> map = new HashMap<>();

        //isi objek test
        if(id != null){
            Map<String, String> unitObj = new HashMap<>();
            unitObj.put("id", id);

            map.put("test", unitObj);
        }

        //isi objek feedback
        if(questionId.length > 0){
            List<Object> arrayFeedback = new ArrayList<>();
            for(int n=0; n<questionId.length; n++) {
                Map<String, String> feedbackItem = new HashMap<>();
                feedbackItem.put("questionId", questionId[n]);
                feedbackItem.put("answer", answer[n]);
                arrayFeedback.add(feedbackItem);
            }
            map.put("feedback", arrayFeedback);
        }
        else{
            List<Object> arrayFeedback = new ArrayList<>();
            map.put("feedback", arrayFeedback);
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String json = gson.toJson(map);

        return json;
    }
}



