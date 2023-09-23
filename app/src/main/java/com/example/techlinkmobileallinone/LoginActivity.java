package com.example.techlinkmobileallinone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

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
        // https://github.com/javiersantos/AppUpdater
        AppUpdater appUpdater = new AppUpdater(this)
                .setTitleOnUpdateAvailable("Có bản cập nhật mới! 有新更新！")
                .setContentOnUpdateAvailable("Vui lòng cập nhật để có thể sử dụng!\n请更新后才能使用！")
                .setButtonUpdate("Cập nhật 更新")
                .setButtonDismiss("Hủy bỏ 撤消")
                .setButtonDismissClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                })
                .setButtonDoNotShowAgain(null)
                .setCancelable(false)
                .setIcon(R.drawable.small_logo)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://raw.githubusercontent.com/zz2zz22/techlink-mobile-all-in-one/master/app/update-changelog.json");
        appUpdater.start();
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("https://raw.githubusercontent.com/zz2zz22/techlink-mobile-all-in-one/master/app/update-changelog.json")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", String.valueOf(update.getUrlToDownload()));
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();

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
                            appUpdater.dismiss();
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