package bulao.com.project.bulao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bulao.com.project.bulao.Model.Person;

public class RegisterActivity extends AppCompatActivity {

    TextView number, friendNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        number = (TextView) findViewById(R.id.number);
        friendNumber = (TextView) findViewById(R.id.friend_number);
    }

    public void registerUser(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Person person = new Person();
        if (number != null && friendNumber != null) {
            person.setNumber(number.getText().toString());
            person.setAlernateNumber(friendNumber.getText().toString());
            person.setName(user.getDisplayName());

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid());
            databaseReference.setValue(person);
        }
    }
}
