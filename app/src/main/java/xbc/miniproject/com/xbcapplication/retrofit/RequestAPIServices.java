package xbc.miniproject.com.xbcapplication.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import xbc.miniproject.com.xbcapplication.model.assignment.AssignmentList;
import xbc.miniproject.com.xbcapplication.model.assignment.ModelAssignment;
import xbc.miniproject.com.xbcapplication.model.assignment.autoComplete.ModelAutoCompleteAssignment;
import xbc.miniproject.com.xbcapplication.model.batch.ModelBatch;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTechnology.ModelBatchTechnologyAutoComplete;
import xbc.miniproject.com.xbcapplication.model.batch.autoCompleteTrainer.ModelBatchTrainerAutoComplete;
import xbc.miniproject.com.xbcapplication.model.batch.getOne.ModelBatchAutoComplete;
import xbc.miniproject.com.xbcapplication.model.biodata.Biodata;
import xbc.miniproject.com.xbcapplication.model.biodata.BiodataList;
import xbc.miniproject.com.xbcapplication.model.biodata.ModelBiodata;

import xbc.miniproject.com.xbcapplication.model.biodata.autoComplete.ModelBiodataAutoComplete;
import xbc.miniproject.com.xbcapplication.model.feedback.autoComplete.ModelAutocompleteFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.getQuestion.ModelQuestionFeedback;
import xbc.miniproject.com.xbcapplication.model.feedback.postCreate.ModelCreateFeedback;

import xbc.miniproject.com.xbcapplication.model.idleNews.autoComplete.ModelAutoCompleteIdleNews;
import xbc.miniproject.com.xbcapplication.model.idleNews.shareIdleNews.ModelShareIdleNews;
import xbc.miniproject.com.xbcapplication.model.login.ModelLoginMessage;
import xbc.miniproject.com.xbcapplication.model.monitoring.ModelMonitoring;
import xbc.miniproject.com.xbcapplication.model.idleNews.ModelIdleNews;
import xbc.miniproject.com.xbcapplication.model.monitoring.autoComplete.ModelMonitoringAutoComplete;
import xbc.miniproject.com.xbcapplication.model.monitoring.getOne.ModelMonitoringGetOne;
import xbc.miniproject.com.xbcapplication.model.role.ModelRole;
import xbc.miniproject.com.xbcapplication.model.technology.ModelTechnology;
import xbc.miniproject.com.xbcapplication.model.technology.DataList;
import xbc.miniproject.com.xbcapplication.model.technology.Technology;
import xbc.miniproject.com.xbcapplication.model.testimony.DataListTestimony;
import xbc.miniproject.com.xbcapplication.model.kelas.ModelClass;
import xbc.miniproject.com.xbcapplication.model.testimony.ModelTestimony;
import xbc.miniproject.com.xbcapplication.model.testimony.Testimony;
import xbc.miniproject.com.xbcapplication.model.trainer.Trainer;
import xbc.miniproject.com.xbcapplication.model.user.ModelUser;
import xbc.miniproject.com.xbcapplication.model.trainer.DataListTrainer;
import xbc.miniproject.com.xbcapplication.model.trainer.ModelTrainer;
import xbc.miniproject.com.xbcapplication.model.user.User;
import xbc.miniproject.com.xbcapplication.model.user.UserGetOne.ModelUserGetOne;

public interface RequestAPIServices {

    //Koneksi API di Login Page
    @POST("/xbc-ws/api/login")
    Call<ModelLoginMessage> goLogin(@Header("Content-Type") String contentType,
                                    @Body RequestBody data);

    //Koneksi API di menu Biodata
    //GET Search
    @GET("xbc-ws/api/biodata/name/{keyword}")
    Call<ModelBiodata> getListBiodata(@Header("Authorization") String authorization,
                                      @Path("keyword") String keyword);

    //POST Create
    @POST("/xbc-ws/api/biodata/create")
    Call<ModelBiodata> createNewBiodata(@Header("Authorization") String auth,
                                        @Header("Content-Type") String contentType,
                                        @Body BiodataList data);

    //GET get_one
    @GET("/xbc-ws/api/biodata/id/{id}")
    Call<ModelBiodata> getOneBiodata(@Header("Authorization") String auth,
                                     @Path("id") int id);

    //Koneksi API Get Auto Complete add
    @GET("/xbc-ws/api/biodata/key/{keyword}")
    Call<ModelBiodataAutoComplete> getAutoCompleteBatchBiodata(@Header("Authorization") String authorization,
                                                               @Path("keyword") String keyword);

    //Koneksi API Get Auto Complete add
    @GET("/xbc-ws/api/technology/key/{keyword}")
    Call<ModelBatchTechnologyAutoComplete> getAutoCompleteBatchTechnologyList2(@Header("Authorization") String authorization,
                                                                               @Path("keyword") String keyword);

    //PUT edit
    @PUT("/xbc-ws/api/biodata/update")
    Call<ModelBiodata> editBiodata(@Header("Content-Type") String contentType,
                                   @Header("Authorization") String authorization,
                                   @Body Biodata data);

    //PUT deactivate
    @PUT("/xbc-ws/api/biodata/deactivate/{id}")
    Call<ModelBiodata> deactivateBiodata(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Path("id") int id);

    //Koneksi API ke menu Monitoring
    //Koneksi API Get Search
    @GET("/xbc-ws/api/monitoring/biodataname/{keyword}")
    Call<ModelMonitoring> getMonitoringList(@Header("Authorization") String authorization,
                                            @Path("keyword") String keyword);

    //Koneksi API Get Auto Complete Monitoring
    @GET("/xbc-ws/api/monitoring/key/{keyword}")
    Call<ModelMonitoringAutoComplete> getAutoCompleteMonitoringList(@Header("Authorization") String authorization,
                                                                    @Path("keyword") String keyword);

    //Koneksi API Create Monitoring
    @POST("/xbc-ws/api/monitoring/create")
    Call<ModelMonitoringGetOne> createMonitoring(@Header("Content-Type") String contentType,
                                                 @Header("Authorization") String authorization,
                                                 @Body RequestBody data);

    //Koneksi API get One Monitoring
    @GET("/xbc-ws/api/monitoring/id/{id}")
    Call<ModelMonitoringGetOne> getOneMonitoring(@Header("Authorization") String authorization,
                                                 @Path("id") String keyword);

    //Koneksi API Edit Monitoring
    @PUT("/xbc-ws/api/monitoring/update")
    Call<ModelMonitoringGetOne> editMonitoring(@Header("Content-Type") String contentType,
                                               @Header("Authorization") String authorization,
                                               @Body RequestBody data);

    //Koneksi API Edit Placement Monitoring
    @PUT("/xbc-ws/api/monitoring/placement")
    Call<ModelMonitoringGetOne> addPlacementMonitoring(@Header("Content-Type") String contentType,
                                                       @Header("Authorization") String authorization,
                                                       @Body RequestBody data);

    //Koneksi API delete monitoring
    @DELETE("/xbc-ws/api/monitoring/delete/{id}")
    Call<ModelMonitoringGetOne> deleteMonitoring(@Header("Content-Type") String contentType,
                                                 @Header("Authorization") String authorization,
                                                 @Path("id") String id);


    //koneksi API di menu Role
    //Get Role
    @GET("/xbc-ws/api/role/key/{keyword}")
    Call<ModelRole> getListRole(@Header("Authorization") String authorization,
                                @Path("keyword") String keyword);


    @GET("/xbc-ws/api/test/key/{keyword}")
    Call<ModelAutocompleteFeedback> testAutoCompleteFeedback(@Header("Authorization") String authorization,
                                                             @Path("keyword") String keyword);

    //Koneksi API di menu Assignmnet
    //GET Search
    @GET("xbc-ws/api/assignment/biodataname/{keyword}")
    Call<ModelAssignment> getListAssignment(@Header("Authorization") String authorization,
                                            @Path("keyword") String keyword);

    //POST Create
    @POST("/xbc-ws/api/assignment/create")
    Call<ModelAssignment> createNewAssignment(@Header("Authorization") String authorization,
                                              @Header("Content-Type") String contentType,
                                              @Body RequestBody data);

    //PUT edit
    @PUT("/xbc-ws/api/assignment/update")
    Call<ModelAssignment> editAssigment(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Body RequestBody data);

    //DEL delete assignment
    @DELETE("/xbc-ws/api/assignment/delete/{id}")
    Call<ModelAssignment> deleteAssignmnet(@Header("Content-Type") String contentType,
                                           @Header("Authorization") String authorization,
                                           @Path("id") int id);

    //PUT hold
    @PUT("/xbc-ws/api/assignment/hold/{id}")
    Call<ModelAssignment> holdAssigment(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Path("id") int id);

    //PUT done
    @PUT("/xbc-ws/api/assignment/done")
    Call<ModelAssignment> doneAssigment(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Body AssignmentList data);

    //GET AutoComplete
    @GET("/xbc-ws/api/assignment/key/{keyword}")
    Call<ModelAutoCompleteAssignment> assignmentAutoComplete(@Header("Content-Type") String contentType,
                                                             @Header("Authorization") String tokenAuthorization,
                                                             @Path("keyword") String keyword);

    //Koneksi API di menu Idle News
    //GET Search
    @GET("xbc-ws/api/idlenews/title/{keyword}")
    Call<ModelIdleNews> getListIdleNews(@Header("Authorization") String tokenAuthorization,
                                        @Path("keyword") String keyword);

    //POST Create
    @POST("xbc-ws/api/idlenews/create")
    Call<ModelIdleNews> createNewIdleNews(@Header("Authorization") String tokenAuthorization,
                                          @Header("Content-Type") String contentType,
                                          @Body RequestBody data);

    //PUT edit
    @PUT("/xbc-ws/api/idlenews/update")
    Call<ModelIdleNews> editIdleNews(@Header("Content-Type") String contentType,
                                     @Header("Authorization") String authorization,
                                     @Body RequestBody data);

    //GET AutoComplete
    @GET("/xbc-ws/api/idlenews/key/{keyword}")
    Call<ModelAutoCompleteIdleNews> idleNewsAutoComplete(@Header("Content-Type") String contentType,
                                                         @Header("Authorization") String tokenAuthorization,
                                                         @Path("keyword") String keyword);

    //Delete Idle News
    @DELETE("/xbc-ws/api/idlenews/delete/{id}")
    Call<ModelIdleNews> deleteIdleNews(@Header("Content-Type") String contentType,
                                       @Header("Authorization") String authorization,
                                       @Path("id") int id);

    //PUT Publish
    @PUT("/xbc-ws/api/idlenews/publish/{id}")
    Call<ModelIdleNews> publishIdleNews(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Path("id") int id);

    //POST Share to email
    @POST("/xbc-ws/api/idlenews/share-to-email")
    Call<ModelIdleNews> shareNewIdleNews(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Body ModelShareIdleNews data);

    //Koneksi API Get Auto Complete Trainer
    @GET("/xbc-ws/api/trainer/key/{keyword}")
    Call<ModelBatchTrainerAutoComplete> getAutoCompleteBatchTrainerList(@Header("Authorization") String authorization,
                                                                        @Path("keyword") String keyword);


    //konek API di menu Batch
    //GET Search
    @GET("xbc-ws/api/batch/name/{keyword}")
    Call<ModelBatch> getListBatch(@Header("Content-Type") String contentType,
                                  @Header("Authorization") String authorization,
                                  @Path("keyword") String keyword);

    //POST CREATE
    @POST("xbc-ws/api/batch/create")
    Call<ModelBatch> createNewBatch(@Header("Content-Type") String contentType,
                                    @Header("Authorization") String authorization,
                                    @Body RequestBody data);

    //POST CREATE
    @POST("/xbc-ws/api/class/participant/add/{id}")
    Call<ModelBatch> createNewBatchAddParticipant(@Header("Content-Type") String contentType,
                                                  @Header("Authorization") String authorization,
                                                  @Path("id") String id,
                                                  @Body RequestBody data);

    //Koneksi API get One Batch Edit
    @GET("/xbc-ws/api/batch/id/{id}")
    Call<ModelBatchAutoComplete> getOneBatch(@Header("Authorization") String authorization,
                                             @Path("id") String keyword);

    //PUT EDIT
    @PUT("xbc-ws/api/batch/update")
    Call<ModelBatch> editBatch(@Header("Content-Type") String contentType,
                               @Header("Authorization") String authorization,
                               @Body RequestBody data);

    //Koneksi API di menu Class
    //GET Search
    @GET("xbc-ws/api/class/batch/{keyword}")
    Call<ModelClass> getListClass(@Header("Content-Type") String contentType,
                                  @Header("Authorization") String authorization,
                                  @Path("keyword") String keyword);

    //Delete
    @DELETE("/xbc-ws/api/class/participant/delete/{id}")
    Call<ModelClass> deleteClass(@Header("Content-Type") String contentType,
                                 @Header("Authorization") String authorization,
                                 @Path("id") int id);

    //Koneksi API di menu Technology
    //Get DataListGetOne Technology

    @GET("xbc-ws/api/technology/name/{keyword}")
    Call<ModelTechnology> getListTechnology(@Header("Content-Type") String contentType,
                                            @Header("Authorization") String authorization,
                                            @Path("keyword") String keyword);

    //Create
    @POST("/xbc-ws/api/technology/create")
    Call<ModelTechnology> createNewTechnology(@Header("Content_Type") String contentType,
                                              @Header("Authorization") String authorization,
                                              @Body DataList data);

    //@Headers("Authorization: JCZXSHTUOIW5PAAGXIYZFTTX43KGRGJGFKL8DLMPJUMNFRIYOSTZUSL2157WV2MKY8CNNJDP8SAYN1KHHGBHV0B2W1UFPCR4APQKYEW6HJVFM98F4KY5T0QVWRGZXRTP")
    //GET one user
    @GET("/xbc-ws/api/technology/id/{id}")
    Call<ModelTechnology> getOneTechnology(@Header("Authorization") String authorization,
                                           @Path("id") int id);

    //PUT edit
    @PUT("/xbc-ws/api/technology/update")
    Call<ModelTechnology> editTechnology(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Body Technology data);

    @PUT("/xbc-ws/api/technology/deactivate/{id}")
    Call<ModelTechnology> deactiveTechnology(@Header("Content-Type") String contentType,
                                             @Header("Authorization") String authorization,
                                             @Path("id") int id);

    //Koneksi API di menu User
    //Get DataListGetOne User (Search)
    @GET("xbc-ws/api/user/name/{keyword}")
    Call<ModelUser> getListUsser(@Header("Content-Type") String contentType,
                                 @Header("Authorization") String authorization,
                                 @Path("keyword") String keyword);


    //Post DataListGetOne User(Create)
    @POST("/xbc-ws/api/user/create")
    Call<ModelUser> createNewUser( @Header("Authorization") String authorization,
                                   @Header("Content-Type") String contentTypeUser,
                                  @Body xbc.miniproject.com.xbcapplication.model.user.DataList datauser);

    @GET("/xbc-ws/api/user/id/{id}")
    Call<ModelUserGetOne> getOneUser(@Header("Authorization") String authorization,
                                     @Path("id") int id);

    @PUT("/xbc-ws/api/user/update")
    Call<ModelUser> editUser(@Header("Content-Type") String contentType,
                             @Header("Authorization") String authorization,
                             @Body User data);

    //PUT DataListGetOne User (Deactivate)
    @PUT("/xbc-ws/api/user/deactivate/{id}")
    Call<ModelTrainer> deactivateUser(@Header("Content-Type") String contentType,
                                      @Header("Authorization") String authorization,
                                      @Path("id") int id);


    //get question
    @GET("/xbc-ws/api/question/test/{id}")
    Call<ModelQuestionFeedback> getListQuestionFeedback(@Header("Authorization") String tokenAuthorization,
                                                        @Path("id") String id);

    //post create feedback
    @POST("xbc-ws/api/feedback/create")
    Call<ModelCreateFeedback> createFeedback(@Header("Content-Type") String contentType,
                                             @Header("Authorization") String tokenAuthorization,
                                             @Body RequestBody data);

    //Koneksi api di menu testimony
    //GET SEARCH
    @GET("xbc-ws/api/testimony/title/{keyword}")
    Call<ModelTestimony> getListTestimony(@Header("Authorization") String authorization,
                                          @Path("keyword") String keyword);

    //GET ONE-SEARCH
    @GET("xbc-ws/api/testimony/id/{id}")
    Call<ModelTestimony> getOneTestimony(@Header("Authorization") String authorization,
                                         @Path("id") int id);

    //POST CREATE
    @POST("/xbc-ws/api/testimony/create")
    Call<ModelTestimony> createNewTestimony(@Header("Authorization") String authorization,
                                            @Header("Content-Type") String contentType,
                                            @Body DataListTestimony data);

    //PUT EDIT
    @PUT("/xbc-ws/api/testimony/update")
    Call<ModelTestimony> editTestimony(@Header("Content-Type") String contentType,
                                       @Header("Authorization") String authorization,
                                       @Body Testimony data);

    //DEL delete
    @DELETE("xbc-ws/api/testimony/delete/{id}")
    Call<ModelTestimony> deleteTestimony(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Path("id") int id);

    //koneksi API di menu trainer
    //get search
    @GET("xbc-ws/api/trainer/name/{keyword}")
    Call<ModelTrainer> getListTrainer(@Header("Authorization") String authorization,
                                      @Path("keyword") String keyword);

    //Create data
    //post Create
    @POST("xbc-ws/api/trainer/create")
    Call<ModelTrainer> createNewTrainer(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Body DataListTrainer data);

    //GET get_one search
    @GET("xbc-ws/api/trainer/id/{id}")
    Call<ModelTrainer> getOneTrainer(@Header("Authorization") String authorization,
                                     @Path("id") int id);

    //PUT edit
    @PUT("/xbc-ws/api/trainer/update")
    Call<ModelTrainer> editTrainer(@Header("Content-Type") String contentType,
                                   @Header("Authorization") String authorization,
                                   @Body Trainer data);

    //PUT Deactivate
    @PUT("/xbc-ws/api/trainer/deactivate/{id}")
    Call<ModelTrainer> deactivateTrainer(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Path("id") int id);
}
