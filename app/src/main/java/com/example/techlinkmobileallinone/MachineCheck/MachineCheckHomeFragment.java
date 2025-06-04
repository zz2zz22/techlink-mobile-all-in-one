package com.example.techlinkmobileallinone.MachineCheck;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techlinkmobileallinone.R;

public class MachineCheckHomeFragment extends Fragment {
    CardView maintenanceCard, checkCard, updateCard, infoCard;
    MachineCheckMainActivity activity;
    public MachineCheckHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MachineCheckMainActivity activity = (MachineCheckMainActivity)getActivity();
        View view =inflater.inflate(R.layout.fragment_machine_check_home, container, false);
        maintenanceCard = (CardView) view.findViewById(R.id.maintenanceCard);
        checkCard = (CardView) view.findViewById(R.id.checkCard);
        updateCard = (CardView) view.findViewById(R.id.updateCard);
        infoCard = (CardView) view.findViewById(R.id.infoCard);

        maintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new MachineCheckQRScanFragment(), true, "maintenance", new Bundle());
            }
        });

        checkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new MachineCheckQRScanFragment(), true, "check", new Bundle());
            }
        });

        updateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new MachineCheckQRScanFragment(), true, "changeLocation", new Bundle());
            }
        });

        infoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new MachineCheckQRScanFragment(), true, "information", new Bundle());
            }
        });

        return view;
    }
}