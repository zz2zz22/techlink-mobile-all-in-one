package com.example.techlinkmobileallinone.MachineCheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techlinkmobileallinone.FireSafetyEquipment.FireSafetyEquipmentMainActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.controller.UUIDGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MCCheckFragment extends Fragment {
    LinearLayout container;
    List<CheckBox> checkBoxList = new ArrayList<>();
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

        checkDeviceCode = (TextView) view.findViewById(R.id.checkDeviceCode);
        checkDeviceName = (TextView) view.findViewById(R.id.checkDeviceName);
        checkDeviceDetail = (TextView) view.findViewById(R.id.checkDeviceDetail);

        Button checkButton = (Button) view.findViewById(R.id.scanCheckButton);

        try{
            DatabaseConnector databaseConnector = new DatabaseConnector();
            Connection connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                Statement stGetData = connect.createStatement();
                ResultSet rsGetData = stGetData.executeQuery("select * from property_info where uuid = '"+ deviceUUID.trim() +"'");

                while(rsGetData.next())
                {
                    checkDeviceCode.setText("Mã 代码:\n" + rsGetData.getString("code"));
                    checkDeviceName.setText("Tên TB 设备名称:\n" + rsGetData.getString("name"));
                    checkDeviceDetail.setText("Mô tả 描述:\n" + rsGetData.getString("detail"));

                    connect.close();

                    loadCheckBoxes(rsGetData.getString("check_type_id"));

                    stGetData.close();
                    rsGetData.close();
                }
            }else{
                activity.returnToHome();
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception e) {
            activity.returnToHome();
            subMethods.showErrorDialog(getString(R.string.errorTitle), e.getMessage(), activity);
        }

        checkButton.setOnClickListener(v -> {
            if (areAllChecked()) {
                //Toast.makeText(getContext(), "Thiết bị đã được kiểm tra hoàn tất!该设备已进行了测试！", Toast.LENGTH_SHORT).show();
                //Lưu kết quả vào database
                executeSaveNewCheckInfo(deviceUUID, "OK", "");
            } else {
                //Toast.makeText(getContext(), "Chưa hoàn tất kiểm tra thiết bị! 尚未完成设备测试！", Toast.LENGTH_SHORT).show();
                List<String> uncheckedItems = new ArrayList<>();

                for (CheckBox cb : checkBoxList) {
                    if (!cb.isChecked()) {
                        uncheckedItems.add(cb.getText().toString());
                    }
                }
                String result = TextUtils.join(";", uncheckedItems);
                executeSaveNewCheckInfo(deviceUUID, "NG", result);
            }
        });

        return view;
    }

    private void loadCheckBoxes(String checkTypeID) {
        new Thread(() -> {
            try {
                DatabaseConnector db = new DatabaseConnector();
                Connection con = db.sqlDeviceMaintenanceCon();
                if (con != null) {
                    String query = "SELECT check_data FROM property_check_type where uuid = '"+ checkTypeID+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        String checkData = rs.getString("check_data");
                        String[] items = checkData.split(";");

                        requireActivity().runOnUiThread(() -> {
                            for (String item : items) {
                                String trimmed = item.trim(); // Clean spaces
                                CheckBox cb = new CheckBox(getContext());
                                cb.setText(trimmed);
                                cb.setPadding(16, 16, 16, 16);
                                checkBoxList.add(cb);
                                container.addView(cb);
                            }
                        });
                    }

                    rs.close();
                    stmt.close();
                    con.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private boolean areAllChecked() {
        for (CheckBox cb : checkBoxList) {
            if (!cb.isChecked()) return false;
        }
        return true;
    }

    public void executeSaveNewCheckInfo(String deviceUUID, String result, String defect) {
        MachineCheckMainActivity activity = (MachineCheckMainActivity)getActivity();
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            Connection connect = databaseConnector.sqlDeviceMaintenanceCon();
            if (connect != null) {
                String uuid = UUIDGenerator.getAscId();
                Statement st = connect.createStatement();
                String insertCheckRecord = "Insert into property_check_result (uuid, property_id, check_result, defect_data, check_employee, check_date) values ('" + uuid + "', '" + deviceUUID + "', '"+result+"', N'" + defect + "', N'" + empCode + " - " + empName + "', GETDATE())";
                st.execute(insertCheckRecord);

                String updateDeviceInfo = "Update property_info set check_history = '" + uuid + "', check_date = GETDATE(), update_date = GETDATE() where device_uuid = '" + deviceUUID + "'";
                st.executeUpdate(updateDeviceInfo);

                st.close();
                connect.close();

                activity.returnToHome();
                subMethods.showSuccessDialog(getString(R.string.successTitle), "Hoàn tất kiểm tra thiết bị\n完成设备检查", activity);
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
            }
        } catch (Exception ex) {
            subMethods.showInformationDialog(getString(R.string.errorTitle), "Lỗi hệ thống!\n系统错误！\n" + ex.getMessage(), activity);
        }
    }
}