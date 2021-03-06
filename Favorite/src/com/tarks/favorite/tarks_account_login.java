//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package com.tarks.favorite;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.tarks.favorite.connect.AsyncHttpTask;
import com.tarks.favorite.global.Global;
import com.tarks.favorite.global.Globalvariable;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class tarks_account_login extends SherlockActivity {
    Button forgot_id_password_button;
    Button register_id_button;
    String myId, myPWord, myTitle, mySubject;
    String myResult = null;
    EditText input_id_edittext, input_password_edittext;
    String s1, s2;
//	boolean okbutton = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Can use progress
        AvLog.i("");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.tarks_account);
        setSupportProgressBarIndeterminateVisibility(false);

        // 액션바백버튼가져오기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        input_id_edittext = (EditText) findViewById(R.id.input_id_edittext);
        input_password_edittext = (EditText) findViewById(R.id.input_password_edittext);

        forgot_id_password_button = (Button) findViewById(R.id.forgot_id_password_button);
        forgot_id_password_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://tarks.net/index.php?mid=main&act=dispMemberFindAccount");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        register_id_button = (Button) findViewById(R.id.register_id_button);
        register_id_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://tarks.net/index.php?mid=main&act=dispMemberSignUpForm");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void ConnectionError() {
        // If No Network Connection
        // Check Internet Connection

        // Check Network Connection
        if (Global.InternetConnection(1) == true
                || Global.InternetConnection(0) == true) {

            Intent intent = new Intent(tarks_account_login.this, webview.class);
            intent.putExtra("url", getString(R.string.server_path));
            startActivity(intent);

            finish();


        } else {


            // 로딩 화면은 종료하라.
            Toast.makeText(tarks_account_login.this, getString(R.string.networkerrord), Toast.LENGTH_SHORT).show();



        }
    }

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            AvLog.i("msg.what state = "+msg.what);
            //Stop progressbar
            setSupportProgressBarIndeterminateVisibility(false);

            if (msg.what == -1) {
                ConnectionError();

            }

            if (msg.what == 1) {
                myResult = msg.obj.toString();
                if (myResult.matches("")) {
                    // Error Login
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(tarks_account_login.this);
                    builder1.setMessage(getString(R.string.error_login))
                            .setPositiveButton(getString(R.string.yes), null)
                            .setTitle(getString(R.string.error));
                    builder1.show();
                } else {
                    // Save auth key to temp

                    // Intent 생성
                    Intent intent = new Intent();
                    // 생성한 Intent에 데이터 입력
                    intent.putExtra("id", input_id_edittext.getText().toString());
                    intent.putExtra("auth_code", myResult);
                    // 결과값 설정(결과 코드, 인텐트)
                    tarks_account_login.this.setResult(RESULT_OK, intent);
                    // 본 Activity 종료
                    finish();
                }

            }

        }
    };

    public void TarksAccountLogin() throws NoSuchAlgorithmException {
        // Set Progress
        setSupportProgressBarIndeterminateVisibility(true);

        // import EditText string

        String s1 = input_id_edittext.getText().toString();
        String s2 = input_password_edittext.getText().toString();

        // md5 password value
        String src = s2;
        String enc = Global.getMD5Hash(src);
        AvLog.i("password before encription = "+s2);
        AvLog.i("make md5 hash, result = "+enc);

        //	Log.i("password", enc);

        ArrayList<String> Paramname = new ArrayList<String>();
        Paramname.add("authcode");
        Paramname.add("id");
        Paramname.add("password");

        ArrayList<String> Paramvalue = new ArrayList<String>();
        Paramvalue.add("642979");
        Paramvalue.add(s1);
        Paramvalue.add(enc);

        new AsyncHttpTask(this, getString(R.string.server_path) + "member/tarks_account_check.php", mHandler, Paramname, Paramvalue, null, 1,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // 메뉴 버튼 구현부분
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.accept, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.yes:
                // Check okbutton
                if (Globalvariable.okbutton == true) {
                    input_id_edittext = (EditText) findViewById(R.id.input_id_edittext);
                    s1 = input_id_edittext.getText().toString();

                    // no err
                    try {
                        // import EditText

                        // edit2 = (EditText) findViewById(R.id.editText2);
                        // String s2 = edit2.getText().toString();

                        if (s1.matches("")) {
                            // Show type id noti
                            Global.Infoalert(this, getString(R.string.notification), getString(R.string.type_id), getString(R.string.yes));
                        } else {
                            // TODO Auto-generated method stub
                            TarksAccountLogin();
                        }
                    } catch (Exception e) {
                        // Log.i("ERROR", "App has been error");
                        // System.out.println();
                        // Not Connected To Internet
                        ConnectionError();

                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
