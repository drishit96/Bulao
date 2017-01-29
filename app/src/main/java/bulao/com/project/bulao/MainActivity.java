package bulao.com.project.bulao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
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

        createUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void createUser() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void sendHelpMessage(View view) {
        Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
        startActivity(intent);
        /*
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "8080276120"));
            intent.putExtra("Id: " + user.getUid() + "\nName: " + user.getDisplayName(), message);
            startActivity(intent);
        }
        */
    }
}
