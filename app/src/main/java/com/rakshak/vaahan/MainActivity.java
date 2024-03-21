package com.rakshak.vaahan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakshak.vaahan.data.DatabaseHelper;
import com.rakshak.vaahan.data.addCar;
import com.rakshak.vaahan.databinding.ActivityMainBinding;
import com.rakshak.vaahan.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DatabaseHelper dbHelper;
    DatabaseReference database;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        dbHelper = new DatabaseHelper(this);
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeScreen);
        database = FirebaseDatabase.getInstance().getReference("user");


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS},
                PackageManager.PERMISSION_GRANTED);

        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    highAlert();
//                    Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, addCar.class));
                }
            });
        }



        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();


        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_reflow, R.id.nav_report)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        String userID = "3456543";
        loadCarList(userID);
        if (homeFragment != null) {
            homeFragment.updateCars();
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    public void inCarList(List<String> carData) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        cData = ["KIA","Carens","OD11K2343"];
        contentValues.put("Brand", carData.get(0));
        contentValues.put("Model", carData.get(1));
        contentValues.put("RegNo", carData.get(2));
        long result = db.insert("carList", null, contentValues);
        if (result == -1) {
            Log.d("Error", "Error");
        }
        db.close();
    }

    public void loadCarList(String userID) {
        database.child(userID + "/cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("values", String.valueOf(snapshot.getValue()));
                Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                List<String> carDet = new ArrayList<>();
                String[] vali = String.valueOf(snapshot.getValue()).split(", ");
                for (int i = 1; i < vali.length; i++) {
                    try {
                        vali[i] = vali[i].split("=")[1];
                        vali[i] = vali[i].replace("}", "");
                        vali[i] = vali[i].replace("]", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    carDet.add(vali[i]);
                    if (i % 3 == 0) {
                        Collections.reverse(carDet);
                        dbHelper.insertCarList(carDet);
                        carDet = new ArrayList<>();
                    }
                }
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.overflow, menu);
        return result;
    }


    public void highAlert() {
        createNotificationChannel();

        Notification n = new Notification.Builder(this, "Alert")
                .setSmallIcon(R.drawable.report)
                .setContentTitle(getString(R.string.alert))
                .setContentText(getString(R.string.alert_desc))
                .build();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Drive Slow \nAccident Prone Zone Ahead");
        builder.setTitle("Caution!!").setCancelable(false);
        builder.setPositiveButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibrationWaveFormDurationPattern = {1000,1000,1000};
        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(vibrationWaveFormDurationPattern, 0);
        vibrator.cancel();

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.POST_NOTIFICATIONS},
                    PackageManager.PERMISSION_GRANTED);
            return;
        }
        nmc.notify(101, n);
        alertDialog.show();
        vibrator.vibrate(vibrationEffect);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.cancel();
            }
        },11000);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = getString(R.string.alert);
        String description = getString(R.string.alert_desc);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Alert", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}