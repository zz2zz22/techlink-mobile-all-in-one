package com.example.techlinkmobileallinone.BigHoseCountDown;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techlinkmobileallinone.HomeActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.utils.Department;
import com.example.techlinkmobileallinone.utils.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class BHCDChooseDeptFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    DatabaseConnector databaseConnector = new DatabaseConnector();
    String empCode, empName, stationUUID, stationName, deptUUID, deptName;
    TextView employeeName, employeeCode;
    Spinner informationDept, informationStation;
    Button acceptInfoButton;
    SubMethods subMethods = new SubMethods();

    public BHCDChooseDeptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();

        Bundle results = activity.getUserData();
        empCode = results.getString("empCode");
        empName = results.getString("empName");
        View view = inflater.inflate(R.layout.fragment_b_h_c_d_choose_dept, container, false);
        employeeName = (TextView) view.findViewById(R.id.employeeName);
        employeeCode = (TextView) view.findViewById(R.id.employeeCode);
        informationDept = (Spinner) view.findViewById(R.id.informationDept);
        informationStation = (Spinner) view.findViewById(R.id.informationStation);
        acceptInfoButton = (Button) view.findViewById(R.id.acceptInfoButton);

        employeeName.setText("Tên nhân viên 员工姓名:  " + empName);
        employeeCode.setText("Mã số nhân viên 员工编号:  " + empCode);

        setDepartmentSpinnerData();

        informationDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Department department = (Department) adapterView.getSelectedItem();
                deptUUID = department.getDeptID();
                deptName = department.getDeptName();
                setStationSpinnerData(deptUUID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        informationStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (informationStation.getAdapter().getCount() > 0) {
                    Station station = (Station) adapterView.getSelectedItem();
                    stationName = station.getName();
                    stationUUID = station.getID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stationUUID = null;
            }
        });

        acceptInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stationUUID == null || stationUUID.isEmpty())
                    subMethods.showInformationDialog(getString(R.string.informationTitle), "Vui lòng chọn trạm sản xuất\n请选择生产站", getActivity());
                else {
                    Fragment fragment = new BHCDChooseProductFragment();
                    Bundle bundleArgs = new Bundle();
                    //empCode, empName, stationUUID, stationName, deptUUID, deptName;
                    bundleArgs.putString("empCode", empCode);
                    bundleArgs.putString("empName", empName);
                    bundleArgs.putString("stationUUID", stationUUID);
                    bundleArgs.putString("stationName", stationName);
                    bundleArgs.putString("deptUUID", deptUUID);
                    bundleArgs.putString("deptName", deptName);
                    fragment.setArguments(bundleArgs);
                    activity.enableViews(true);
                    activity.replaceFragment(fragment, true);
                }
            }
        });

        return view;
    }

    public void setDepartmentSpinnerData() {
        try {
            connect = databaseConnector.sqlProgramsDatabaseCon();
            String query = "select uuid, department_name from department_info";
            PreparedStatement stat = connect.prepareStatement(query);
            ResultSet rs = stat.executeQuery();

            ArrayList<Department> data = new ArrayList<>();
            while (rs.next()) {
                String dept_uuid = rs.getString("uuid");
                String dept_name = rs.getString("department_name");
                data.add(new Department(dept_uuid, dept_name));
            }
            ArrayAdapter<Department> array = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
            informationDept.setAdapter(array);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setStationSpinnerData(String departmentID) {
        try {
            connect = databaseConnector.sqlProgramsDatabaseCon();
            String query = "select uuid, station_name from station_info where uuid not like '740NR56W54WFVO' and department_uuid = '" + departmentID + "'";
            PreparedStatement stat = connect.prepareStatement(query);
            ResultSet rs = stat.executeQuery();

            ArrayList<Station> data = new ArrayList<>();
            while (rs.next()) {
                String station_uuid = rs.getString("uuid");
                String station_name = rs.getString("station_name");
                data.add(new Station(station_uuid, station_name));
            }
            ArrayAdapter<Station> array = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
            informationStation.setAdapter(array);
            informationStation.setSelection(-1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}