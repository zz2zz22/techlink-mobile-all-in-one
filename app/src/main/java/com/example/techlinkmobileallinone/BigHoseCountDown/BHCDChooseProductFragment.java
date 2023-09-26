package com.example.techlinkmobileallinone.BigHoseCountDown;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.DatabaseConnector;
import com.example.techlinkmobileallinone.controller.SubMethods;
import com.example.techlinkmobileallinone.utils.ProductResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BHCDChooseProductFragment extends Fragment {
    Connection connect;
    String ConnectionResult = "";
    DatabaseConnector databaseConnector = new DatabaseConnector();
    String empCode, empName, stationUUID, stationName, deptUUID, deptName, productResultUUID, productResultNo;
    TextView employeeName, employeeCode, employeeDept, employeeStation;
    SearchView searchProduct;
    Spinner productCodeChoose;
    Button acceptProductButton;
    SubMethods subMethods = new SubMethods();
    public BHCDChooseProductFragment() {
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

        Bundle results = getArguments();
        //empCode, empName, stationUUID, stationName, deptUUID, deptName;
        empCode = results.getString("empCode");
        empName = results.getString("empName");
        stationUUID = results.getString("stationUUID");
        stationName = results.getString("stationName");
        deptUUID = results.getString("deptUUID");
        deptName = results.getString("deptName");

        View view = inflater.inflate(R.layout.fragment_b_h_c_d_choose_product, container, false);
        employeeName = (TextView) view.findViewById(R.id.employeeName);
        employeeCode = (TextView) view.findViewById(R.id.employeeCode);
        employeeDept = (TextView) view.findViewById(R.id.employeeDept);
        employeeStation = (TextView) view.findViewById(R.id.employeeStation);
        searchProduct = (SearchView) view.findViewById(R.id.searchProduct);
        productCodeChoose = (Spinner) view.findViewById(R.id.productCodeChoose);
        acceptProductButton = (Button) view.findViewById(R.id.acceptProductButton);
        searchProduct.clearFocus();

        employeeName.setText("Tên nhân viên 员工姓名:  " + empName);
        employeeCode.setText("Mã số nhân viên 员工编号:  " + empCode);
        employeeDept.setText("Bộ phận 工区:  " + deptName);
        employeeStation.setText("Trạm 局:  " + stationName);
        fillProdSpinner("", true);
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                fillProdSpinner(newText, false);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        productCodeChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProductResult productResult = (ProductResult) adapterView.getSelectedItem();
                productResultUUID = productResult.getID();
                productResultNo = productResult.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        acceptProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productResultUUID == null || productResultUUID.isEmpty())
                    subMethods.showInformationDialog(getString(R.string.informationTitle), "Vui lòng tìm và chọn mã hàng\n请搜索并选择产品代码", getActivity());
                else {
                    Fragment fragment = new BHCDCountDownFragment();
                    Bundle bundleArgs = new Bundle();
                    bundleArgs.putString("empCode", empCode);
                    bundleArgs.putString("empName", empName);
                    bundleArgs.putString("stationUUID", stationUUID);
                    bundleArgs.putString("stationName", stationName);
                    bundleArgs.putString("deptUUID", deptUUID);
                    bundleArgs.putString("deptName", deptName);
                    bundleArgs.putString("productResultUUID", productResultUUID);
                    bundleArgs.putString("productResultNo", productResultNo);
                    fragment.setArguments(bundleArgs);
                    activity.enableViews(true);
                    activity.replaceFragment(fragment, true);
                }
            }
        });

        return view;
    }

    public void fillProdSpinner(String search, boolean isFirst) {
        BigHoseCountDownActivity activity = (BigHoseCountDownActivity) getActivity();
        try {
            productResultUUID = null;
            connect = databaseConnector.sqlProgramsDatabaseCon();
            if (connect != null) {
                ArrayList<ProductResult> data = new ArrayList<ProductResult>();
                Statement st = connect.createStatement();
                String query;
                if (search == null || search.isEmpty())
                {
                    query = "select uuid, product_no from big_hose_countdown_result where isFinished = 0 and plan_start < GETDATE() and plan_end > GETDATE() and station_uuid = '"+stationUUID+"'";
                }else{
                    query = "select uuid, product_no from big_hose_countdown_result where product_no like '%"+search+"%' and isFinished = 0 and plan_start < GETDATE() and plan_end > GETDATE() and station_uuid = '"+stationUUID+"'";
                }
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String prodRUUID = rs.getString("uuid");
                    String prodNo = rs.getString("product_no");
                    data.add(new ProductResult(prodRUUID, prodNo));
                }
                if(data.size() > 0)
                {
                    subMethods.showInformationDialog(getString(R.string.informationTitle), "Đã tìm được " + data.size() + " mã hàng!\n找到 " + data.size() + " 件商品！", activity);
                }else{
                    if(isFirst)
                    {
                        subMethods.showInformationDialog(getString(R.string.informationTitle), "Không tìm thấy mã hàng nào có có sản lượng hôm nay!\n今天在生产中找不到产品代码！", activity);
                        activity.returnToHome();
                    }else{
                        subMethods.showInformationDialog(getString(R.string.informationTitle), "Không tìm thấy! Mã hàng có thể chưa có sản lượng hôm nay!\n未找到！产品代码今天可能不可用！", activity);
                    }
                }
                ArrayAdapter<ProductResult> adapter = new ArrayAdapter<ProductResult>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
                productCodeChoose.setAdapter(adapter);
            } else {
                subMethods.showErrorDialog(getString(R.string.errorTitle), getString(R.string.sqlNotConnect), activity);
                activity.returnToHome();
            }
        } catch (Exception ex) {
            subMethods.showErrorDialog(getString(R.string.errorTitle), ex.getMessage(), activity);
            activity.returnToHome();
        }
    }
}