package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    /////Fields
    EditText username;
    Button loginButton;
    Connection connectHR;
    String empCode, empName, empDeptCode;
    DatabaseConnector databaseConnector = new DatabaseConnector();
    SubMethods subMethods = new SubMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViews();
        loginButton.setOnClickListener(view -> {
            try {
                if (!username.getText().toString().trim().isEmpty()) {
                    connectHR = databaseConnector.sqlHRCon();

                    if (connectHR != null) {Statement st = connectHR.createStatement();
                        ResultSet rs = st.executeQuery("exec VuSP_GetEmpInfo '" + username.getText() + "'");
                        while (rs.next()) {
                            empDeptCode = rs.getString(2);
                            empCode = rs.getString(3);
                            empName = rs.getString(4);
                        }
                        if (empCode == null || empCode.length() == 0) {
                            subMethods.showInformationDialog(getString(R.string.errorTitle), getString(R.string.employeeNotFound), LoginActivity.this);
                        } else {
                            String firstCharacters = empDeptCode. substring(0, 3);
                            if (!firstCharacters.equals("777") && !firstCharacters.equals("888"))
                                empDeptCode = firstCharacters;
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("empDeptCode", empDeptCode.trim());
                            i.putExtra("empCode", empCode.trim());
                            i.putExtra("empName", empName.trim());
                            LoginActivity.this.startActivity(i);
                            overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
                            finish();
                        }
                    } else {
                        subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), LoginActivity.this);
                    }
                }else{
                    subMethods.showInformationDialog(getString(R.string.errorTitle), getString(R.string.inputDataEmpty), LoginActivity.this);
                    username.requestFocus();
                }
            } catch (Exception ex) {
                subMethods.showErrorDialog(getString(R.string.errorTitle), ex.getMessage(), LoginActivity.this);
            }
        });
    }

    private void getViews() {
        username = findViewById(R.id.username);
        loginButton = findViewById(R.id.loginButton);
    }
}