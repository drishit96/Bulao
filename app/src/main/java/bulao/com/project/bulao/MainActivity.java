package bulao.com.project.bulao;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import im.delight.android.location.SimpleLocation;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;

    SimpleLocation location;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = new SimpleLocation(this, false, false, 5000);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.GOOGLE_PROVIDER
                                    )
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        SharedPreferences prefs = MainActivity.this.getSharedPreferences("Values", Context.MODE_PRIVATE);
        int count = prefs.getInt("count", 0);
        if (count < 1)
            createUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
        location.endUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        location.beginUpdates();
    }

    public void createUser() {
        SharedPreferences prefs = MainActivity.this.getSharedPreferences("Values", Context.MODE_PRIVATE);
        prefs.edit().putInt("count", 1).apply();
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void sendHelpMessage(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            if (!location.hasLocationEnabled()) {
                SimpleLocation.openSettings(this);
            }
            sendSMS();
            Log.d("Coordinates: ", String.valueOf(location.getLatitude()) +  String.valueOf(location.getLongitude()));
        }
    }

    @TargetApi(23)
    public void sendSMS() {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9769692910", null, "Id: " + user.getUid() + "\nName: " + user.getDisplayName() + "\nLatitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude(), null, null);
    }
}
