package xbc.miniproject.com.xbcapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xbc.miniproject.com.xbcapplication.model.assignment.AssignmentList;
import xbc.miniproject.com.xbcapplication.model.assignment.ModelAssignment;
import xbc.miniproject.com.xbcapplication.retrofit.APIUtilities;
import xbc.miniproject.com.xbcapplication.retrofit.RequestAPIServices;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class DoneAssignmentActivity extends Activity {
    private Context context = this;

    private EditText doneAssignmentEditTextRealDate,
            doneAssignmentEditTextNote;

    private Button doneAssignmentButtonSave,
            doneAssignmentButtonCancel;
    private Calendar calendar;

    RequestAPIServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_assignment);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Mark as Done");

        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        doneAssignmentEditTextNote = (EditText) findViewById(R.id.doneAssignmentEditTextNote);

        doneAssignmentEditTextRealDate = (EditText) findViewById(R.id.doneAssignmentEditTextRealDate);
        doneAssignmentEditTextRealDate.setFocusable(false);
        doneAssignmentEditTextRealDate.setClickable(true);
        doneAssignmentEditTextRealDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        doneAssignmentButtonSave = (Button) findViewById(R.id.doneAssignmentButtonSave);
        doneAssignmentButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();

            }
        });

        doneAssignmentButtonCancel = (Button) findViewById(R.id.doneAssignmentButtonCancel);
        doneAssignmentButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        doneAssignmentEditTextRealDate.setText(sdf.format(calendar.getTime()));
    }

    private void inputValidation() {
        if (doneAssignmentEditTextRealDate.getText().toString().trim().length() == 0){
            Toast.makeText(context,"Realization Date Field still empty!",Toast.LENGTH_SHORT).show();
        } else{
            DoneQuestion();
        }
    }

    private void DoneQuestion(){
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning!")
                .setMessage("Apakah Anda Yakin Akan Update Data ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DoneAssignmentAPI();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false).show();
    }

    private void DoneAssignmentAPI(){
        apiServices = APIUtilities.getAPIServices();
        AssignmentList data = new AssignmentList();
        data.setRealizationDate(doneAssignmentEditTextRealDate.getText().toString());
        data.setNotes(doneAssignmentEditTextNote.getText().toString());

        apiServices.doneAssigment("application/json", SessionManager.getToken(context), data)
                .enqueue(new Callback<ModelAssignment>() {
                    @Override
                    public void onResponse(Call<ModelAssignment> call, Response<ModelAssignment> response) {
                        if (response.code()==200){
                            String message = response.body().getMessage();
                            if (message != null){
                                DoneSuccessNotification(context, message);
                            }
                            else {
                                DoneSuccessNotification(context, "Message Gagal Diambil");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAssignment> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void DoneSuccessNotification(final Context context, String message) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("NOTIFICATION !")
                .setMessage(message+"!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
