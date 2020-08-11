package tw.dworker.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText ed_Userid;
    private EditText ed_Passwd;
    private CheckBox cbRemId;
    private boolean checked;
    private CheckBox cbRemPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
/*        // Default 存入資料＝＝＝＝＝＝＝
        getSharedPreferences("ATM",MODE_PRIVATE)
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","joe")
                .commit();
        // 存入資料＝＝＝＝＝＝＝
        // 寫入資料＝＝＝＝＝＝＝
        int level = getSharedPreferences("ATM",MODE_PRIVATE)
                .getInt("LEVEL",0);
        // 寫入資料＝＝＝＝＝＝＝*/

        ed_Userid = findViewById(R.id.userid);
        ed_Passwd = findViewById(R.id.passwd);
        cbRemId = findViewById(R.id.cb_remember_userid);
        cbRemPw = findViewById(R.id.cb_remember_pw);

        //read userid=====
        String userid = getSharedPreferences("ATM",MODE_PRIVATE)
                .getString("USERID","");
        ed_Userid.setText(userid);
        //read userid=====

        boolean rem_Checked = getSharedPreferences("ATM", MODE_PRIVATE)
                .getBoolean("REMEMBER_ID", true);
        cbRemId.setChecked(rem_Checked);
        Log.d(TAG, "getSharedPreferences get " + rem_Checked);


        cbRemId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = b;
                Log.d(TAG, "onCheckedChanged: " + "\t" + checked);
            }
        });
        cbRemPw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbRemPw.isChecked()){
                    ed_Passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    ed_Passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
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
                    String uid = (checked) ? userid: "";

                    Log.d(TAG, "onDataChange: " + checked + "\t" + userid);
                    getSharedPreferences("ATM",MODE_PRIVATE)
                            .edit()
                            .putString("USERID",uid)
                            .putBoolean("REMEMBER_ID", checked) //checked是在onCreate方法中傾聽核取方塊勾選的boolean值
                            .apply();
                    if (checked){
                        getSharedPreferences("ATM", MODE_PRIVATE)
                                .edit()
                                .putString("USERID",userid)   //checked為true時,將userid儲存在ATM.xml中
                                .apply();
                    }else {
                        getSharedPreferences("ATM",MODE_PRIVATE)
                                .edit()
                                .putString("USERID","") //checked為false時,將空字串儲存在ATM.xml中
                                .apply();
                    }
                    Toast.makeText(LoginActivity.this,"Login successfully",Toast.LENGTH_LONG).show();

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