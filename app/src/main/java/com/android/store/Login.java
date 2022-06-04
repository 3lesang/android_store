package com.android.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button btn_ok, btn_cancel;
    TextView btn_forgot;
    EditText txt_uname, txt_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_forgot = findViewById(R.id.btn_forgot);
        txt_uname=(EditText)findViewById(R.id.txt_uname);
        txt_pwd=(EditText)findViewById(R.id.txt_pwd);

    }

    public void onClick_Ok(View view) {
        if(view.getId() == R.id.btn_ok) {
            String str1 = txt_uname.getText().toString();
            String str2 = txt_pwd.getText().toString();

            if (str1.length() == 0 || str2.length() == 0) {
                Toast.makeText(Login.this, "Vui lòng nhập Username và Password", Toast.LENGTH_SHORT).show();
            }
            else{
               if(txt_uname.getText().toString().equals("admin") && txt_pwd.getText().toString().equals("admin")) {
                    Intent intent = new Intent(Login.this, Admin.class);
                    startActivity(intent);
                }
               else{
                    Toast.makeText(Login.this, "Nhập sai Username và Password", Toast.LENGTH_SHORT).show();
               }

            }
        }
    }
    public void onClick_Cancel(View view) {
        if(view.getId() == R.id.btn_cancel) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void onClick_Forgot(View view) {
        if(view.getId() == R.id.btn_forgot) {
            Toast.makeText(Login.this, "Xin lỗi tính năng này hiện không hỗ trợ", Toast.LENGTH_SHORT).show();
        }
    }
}