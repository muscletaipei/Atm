package tw.dworker.atm;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
 /*       // Default 存入資料＝＝＝＝＝＝＝
        getSharedPreferences("ATM",MODE_PRIVATE)
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","joe")
                .commit();
        // 存入資料＝＝＝＝＝＝＝
        // 寫入資料＝＝＝＝＝＝＝
        int level = getSharedPreferences("ATM",MODE_PRIVATE)
                .getInt("LEVEL",0);
        // 寫入資料＝＝＝＝＝＝＝
        Log.d(TAG, "getSharedPreferences write " + level);*/

        ed_Userid = findViewById(R.id.userid);
        ed_Passwd = findViewById(R.id.passwd);

        //read userid=====
        String userid = getSharedPreferences("ATM",MODE_PRIVATE)
                .getString("USERID","");
        ed_Userid.setText(userid);
        //read userid=====
    }

    public void login(View view){
        Log.d(TAG, "login:");

        final String userid = ed_Userid.getText().toString();
        final String passwd = ed_Passwd.getText().toString();
        Log.d(TAG, "userid : " + userid + "\t" +"passwd : "+ passwd);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(userid).child("password");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String pw = (String) snapshot.getValue();
                String user = (String) snapshot.getValue();
                Log.d(TAG, "Firebase Connected: ");
                if (snapshot.getValue() == null){
                    Log.d(TAG, "Get Value "+ snapshot.getValue());
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("訊息")
                            .setMessage("登入失敗")
                            .setPositiveButton("確認",null)
                            .show();
                }else if (pw.equals(passwd) ){

                    // save userid===
                    getSharedPreferences("ATM",MODE_PRIVATE)
                            .edit()
                            .putString("USERID", userid)
                            .apply();
                    // save userid===

                    setResult(RESULT_OK);
                    finish();
                    Log.d(TAG, "Login success:");
                }else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("訊息")
                            .setMessage("登入失敗")
                            .setPositiveButton("確認",null)
                            .show();
                    Log.d(TAG, "Login failed: ");
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