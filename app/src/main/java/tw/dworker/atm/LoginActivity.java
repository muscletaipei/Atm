package tw.dworker.atm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText ed_Userid;
    private EditText ed_Passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_Userid = findViewById(R.id.userid);
        ed_Passwd = findViewById(R.id.passwd);


    }

    public void login(View view){
        Log.d(TAG, "login:");

        String userid = ed_Userid.getText().toString();
        final String passwd = ed_Passwd.getText().toString();
        Log.d(TAG, "userid : " + userid + "\t" +"passwd : "+ passwd);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(userid).child("password");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pw = (String) snapshot.getValue();
                Log.d(TAG, "Firebase Connected: ");

                if (pw.equals(passwd)){
                            setResult(RESULT_OK);
                            finish();
                        }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


/*        if ("jack".equals(userid) && "123456".equals(passwd)){
            setResult(RESULT_OK);
            finish();

        }*/

    }

    public void quit(View view){
        finish();

    }

}