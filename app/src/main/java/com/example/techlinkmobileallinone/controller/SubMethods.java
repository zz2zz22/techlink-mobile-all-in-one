package com.example.techlinkmobileallinone.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.techlinkmobileallinone.R;

public class SubMethods {
    public void showCustomAlertDialog(String Title, String Message, Activity ActivityToOpen, int imageID, int buttonColor, Boolean isNotUpdated) {
        ConstraintLayout alertConstraintLayout = ActivityToOpen.findViewById(R.id.alertConstraintLayout);
        View view = LayoutInflater.from(ActivityToOpen).inflate(R.layout.alert_dialog, alertConstraintLayout);
        Button alertDone = view.findViewById(R.id.alertDone);
        TextView alertTitle = view.findViewById(R.id.alertTitle);
        TextView alertDesc = view.findViewById(R.id.alertDesc);
        ImageView alertImage = view.findViewById(R.id.alertImage);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityToOpen);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        alertTitle.setText(Title);
        alertDesc.setText(Message);
        alertImage.setImageResource(imageID);
        alertDone.setBackgroundColor(alertDone.getContext().getResources().getColor(buttonColor));
        alertDone.findViewById(R.id.alertDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
                if(isNotUpdated)
                {
                    System.exit(0);
                }
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }
    public void showSuccessDialog(String Title, String Message, Activity ActivityToOpen) {
        showCustomAlertDialog(Title, Message, ActivityToOpen, R.drawable.alert_success_icon, R.color.green, false);
    }

    public void showErrorDialog(String Title, String Message, Activity ActivityToOpen) {
        showCustomAlertDialog(Title, Message, ActivityToOpen, R.drawable.alert_error_icon, R.color.fireBrick, false);
    }

    public void showInformationDialog(String Title, String Message, Activity ActivityToOpen) {
        showCustomAlertDialog(Title, Message, ActivityToOpen, R.drawable.alert_information_icon, R.color.lavender, false);
    }
    public void showInformationUpdateDialog(String Title, String Message, Activity ActivityToOpen) {
        showCustomAlertDialog(Title, Message, ActivityToOpen, R.drawable.alert_information_icon, R.color.lavender, true);
    }

    public void showAdminCheckDialog(Activity ActivityToOpen, Class NextActivity) {
        ConstraintLayout passwordConfirmConstraintLayout = ActivityToOpen.findViewById(R.id.passwordConfirmConstraintLayout);
        View view = LayoutInflater.from(ActivityToOpen).inflate(R.layout.password_confirm_dialog, passwordConfirmConstraintLayout);
        Button passwordConfirm = view.findViewById(R.id.passwordConfirm);
        TextView passwordTitle = view.findViewById(R.id.passwordTitle);
        EditText passwordInput = view.findViewById(R.id.passwordInput);
        ImageView passwordImage = view.findViewById(R.id.passwordImage);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityToOpen);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        passwordTitle.setText(R.string.passwordTitle);
        passwordImage.setImageResource(R.drawable.admin_icon);
        passwordConfirm.setBackgroundColor(passwordConfirm.getContext().getResources().getColor(R.color.rebeccaPurple));
        passwordConfirm.findViewById(R.id.passwordConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordInput.getText().toString().trim();
                if(password.isEmpty())
                {
                    showInformationDialog(ActivityToOpen.getString(R.string.errorTitle), ActivityToOpen.getString(R.string.inputDataEmpty), ActivityToOpen);
                    alertDialog.dismiss();
                }
                else{
                    if (password.equals("techlink@123"))
                    {
                        Intent i = new Intent(ActivityToOpen, NextActivity);
                        ActivityToOpen.startActivity(i);
                        ActivityToOpen.overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
                        alertDialog.dismiss();
                    }
                    else{
                        showInformationDialog(ActivityToOpen.getString(R.string.errorTitle), ActivityToOpen.getString(R.string.adminPasswordWrong), ActivityToOpen);
                        alertDialog.dismiss();
                    }
                }

            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }
}