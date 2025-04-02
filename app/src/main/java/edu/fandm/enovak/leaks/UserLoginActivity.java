package edu.fandm.enovak.leaks;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class UserLoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        FirebaseAuth fbAuth = FirebaseAuth.getInstance();

        Button reg = (Button) findViewById(R.id.fb_login_reg_button);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) findViewById(R.id.fb_email_et)).getText().toString();
                String password = ((EditText) findViewById(R.id.fb_password_et)).getText().toString();


                Task s = fbAuth.createUserWithEmailAndPassword(email, password);
                s.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fbAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "New User Created!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to create new user :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        Button log = (Button)findViewById(R.id.fb_login_login_button);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) findViewById(R.id.fb_email_et)).getText().toString();
                String password = ((EditText) findViewById(R.id.fb_password_et)).getText().toString();


                Task s = fbAuth.signInWithEmailAndPassword(email, password);
                s.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fbAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                            TextView tv = (TextView) findViewById(R.id.fb_login_uid_tv);
                            tv.setText("Login Successful!\nUID: " + user.getUid());
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to login :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}