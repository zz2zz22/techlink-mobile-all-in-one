package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techlinkmobileallinone.R;

public class FSEHomeFragment extends Fragment {
    CardView maintenanceCard, checkCard, changeLocationCard, addNewCard, infoCard, insightCard;
    FireSafetyEquipmentMainActivity activity;
    public FSEHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        View view =inflater.inflate(R.layout.fragment_f_s_e_home, container, false);
        maintenanceCard = (CardView) view.findViewById(R.id.maintenanceCard);
        checkCard = (CardView) view.findViewById(R.id.checkCard);
        changeLocationCard = (CardView) view.findViewById(R.id.changeLocationCard);
        addNewCard = (CardView) view.findViewById(R.id.addNewCard);
        infoCard = (CardView) view.findViewById(R.id.infoCard);
        insightCard = (CardView) view.findViewById(R.id.insightCard);

        maintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "maintenance", new Bundle());
            }
        });

        checkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "check", new Bundle());
            }
        });

        changeLocationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "changeLocation", new Bundle());
            }
        });

        addNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "add", new Bundle());
            }
        });

        infoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "information", new Bundle());
            }
        });

        insightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEQRScanFragment(), true, "insight", new Bundle());
            }
        });

        return view;
    }
}