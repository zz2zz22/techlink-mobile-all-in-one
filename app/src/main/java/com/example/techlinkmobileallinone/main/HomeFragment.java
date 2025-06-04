package com.example.techlinkmobileallinone.main;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techlinkmobileallinone.BigHoseCountDown.BigHoseCountDownActivity;
import com.example.techlinkmobileallinone.FireSafetyEquipment.FireSafetyEquipmentMainActivity;
import com.example.techlinkmobileallinone.HomeActivity;
import com.example.techlinkmobileallinone.LoginActivity;
import com.example.techlinkmobileallinone.MachineCheck.MachineCheckMainActivity;
import com.example.techlinkmobileallinone.R;
import com.example.techlinkmobileallinone.controller.ButtonAdapter;
import com.example.techlinkmobileallinone.utils.ButtonDataClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<ButtonDataClass> dataList;
    ButtonAdapter adapter;
    ButtonDataClass androidData;
    SearchView searchView;
    String empCode, empName, empDeptCode;

    public HomeFragment(){
        // Required empty public constructor
    }

    private void searchList(String text){
        List<ButtonDataClass> dataSearchList = new ArrayList<>();
        for (ButtonDataClass data : dataList){
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase()) || data.getDataDept().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            //Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity activity = (HomeActivity)getActivity();

        Bundle results = activity.getUserData();
        empDeptCode = results.getString("empDeptCode");
        empCode = results.getString("empCode");
        empName = results.getString("empName");
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        searchView = (SearchView) view.findViewById(R.id.search);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        //recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();

        //Khu vực thêm nút
        androidData = new ButtonDataClass(getString(R.string.big_hose_countdown_title), CheckPermission(getString(R.string.big_hose_countdown_permission)), "Ống Lớn\n大管", R.drawable.button_bighose_countdown, BigHoseCountDownActivity.class, empCode, empName, false);
        dataList.add(androidData);
        androidData = new ButtonDataClass(getString(R.string.fire_equipment_title), CheckPermission(getString(R.string.fire_equipment_permission)), "HSE", R.drawable.button_fire_equipment, FireSafetyEquipmentMainActivity.class, empCode, empName, false);
        dataList.add(androidData);
        androidData = new ButtonDataClass(getString(R.string.machine_check_title), CheckPermission(getString(R.string.machine_check_permission)), "Bộ phận quản lý 管理处", R.drawable.button_machine_check, MachineCheckMainActivity.class, empCode, empName, false);
        dataList.add(androidData);

        adapter = new ButtonAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private boolean CheckPermission(String permissionArray)
    {
        if (permissionArray.isEmpty())
        {
            return true;
        }else {
            String[] permissions = permissionArray.split(";");
            if (Arrays.asList(permissions).contains(empDeptCode)) {
                return true;
            } else {
                return false;
            }
        }
    }
}