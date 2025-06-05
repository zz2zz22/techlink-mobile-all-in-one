package com.example.techlinkmobileallinone.MachineCheck;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.techlinkmobileallinone.FireSafetyEquipment.FireSafetyEquipmentMainActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MCCheckFragment extends Fragment {
    LinearLayout container;
    TextView checkDeviceCode, checkDeviceName, checkDeviceDetail;
    private String deviceUUID, empCode, empName;
    SubMethods subMethods;
    public MCCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MachineCheckMainActivity activity = (MachineCheckMainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_m_c_check, container, false);
        Bundle bundleArgs = getArguments();
        deviceUUID = bundleArgs.getString("uuid");
        Bundle bundle = activity.getUserData();
        empCode = bundle.getString("empCode");
        empName = bundle.getString("empName");

        try{
            DatabaseConnector databaseConnector = new DatabaseConnector();
            Connection connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                Statement stGetData = connect.createStatement();
                ResultSet rsGetData = stGetData.executeQuery("select * from property_info where uuid = '"+ deviceUUID.trim() +"'");

                while(rsGetData.next())
                {
                    checkDeviceCode = (TextView) view.findViewById(R.id.checkDeviceCode);
                    checkDeviceName = (TextView) view.findViewById(R.id.checkDeviceName);
                    checkDeviceDetail = (TextView) view.findViewById(R.id.checkDeviceDetail);

                    checkDeviceCode.setText("Mã 代码:\n" + rsGetData.getString("code"));
                    checkDeviceName.setText("Tên TB 设备名称:\n" + rsGetData.getString("name"));
                    checkDeviceDetail.setText("Mô tả 描述:\n" + rsGetData.getString("detail"));


                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery("SELECT description FROM property_check_type_detail where check_type_id = '"+ rsGetData.getString("check_type_id") +"'");
                    new Thread(() -> {
                        try{
                            while (rs.next()) {
                                String description = rs.getString("description");

                                // Switch to UI thread to update views
                                activity.runOnUiThread(() -> addCheckbox(description));
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).start();

                    connect.close();
                    stGetData.close();
                    st.close();
                    rsGetData.close();
                    rs.close();
                }

            }else{
                activity.returnToHome();
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception e) {
            activity.returnToHome();
            subMethods.showErrorDialog(getString(R.string.errorTitle), e.getMessage(), activity);
        }

        return view;
    }

    private void addCheckbox(String label) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setText(label);
        checkBox.setTextSize(16);
        checkBox.setPadding(16, 16, 16, 16);

        container.addView(checkBox);
    }
}