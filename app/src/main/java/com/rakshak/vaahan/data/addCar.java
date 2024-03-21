package com.rakshak.vaahan.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakshak.vaahan.MainActivity;
import com.rakshak.vaahan.R;

import java.util.List;

public class addCar extends AppCompatActivity {
    DatabaseReference database;
    @Nullable
    private RecaptchaTasksClient recaptchaTasksClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeRecaptchaClient();
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance().getReference("user");
        setContentView(R.layout.add_car);
    }
    public void initializeRecaptchaClient() {
        Recaptcha
            .getTasksClient(getApplication(), "6Lfkt5UpAAAAAJC_QDX2Hh24qaWAq08xz9NvONFf")
            .addOnSuccessListener(
                this,
                new OnSuccessListener<RecaptchaTasksClient>() {
                    @Override
                    public void onSuccess(RecaptchaTasksClient client) {
                        Toast.makeText(addCar.this, "Connected", Toast.LENGTH_SHORT).show();
                        addCar.this.recaptchaTasksClient = client;
                    }
                })
            .addOnFailureListener(
                this,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addCar.this, "Some Error", Toast.LENGTH_SHORT).show();
                        // Handle communication errors ...
                        // See "Handle communication errors" section
                    }
                });
    }

    public void executeLoginAction(View v) {
        Toast.makeText(this, "work", Toast.LENGTH_SHORT).show();
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
            .executeTask(RecaptchaAction.LOGIN)
            .addOnSuccessListener(
                this,
                new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        String[] userDet = new String[]{"3456543","Amit","7458963254","745841257563","DL96587412","Gurgaon"};
                        addUser(userDet);
                    }
                })
            .addOnFailureListener(
                this,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle communication errors ...
                        // See "Handle communication errors" section
                    }
                });
    }

    public void addUser(String[] userDet){
        String userID = userDet[0];
        String name = userDet[1];
        String mob = userDet[2];
        String aadhar = userDet[3];
        String drLisc = userDet[4];
        String addr = userDet[5];
        AccDetails user = new AccDetails(userID,name,mob,aadhar,drLisc,addr);
        database.child(userID +"/userDet").setValue(user);
    }

    public void addUserCar(String[] carDet){
        String userID = carDet[0];
        String carN = carDet[1];
        String regNo = carDet[2];
        String brand = carDet[3];
        String model = carDet[4];
        AddUserCar car = new AddUserCar(regNo,brand,model);
        database.child(userID+"/cars/"+carN).setValue(car);
    }




}