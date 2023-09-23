package com.example.techlinkmobileallinone.FireSafetyEquipment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techlinkmobileallinone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FSEHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FSEHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView maintenanceCard, checkCard, changeLocationCard, addNewCard, infoCard, insightCard;
    FireSafetyEquipmentMainActivity activity;
    public FSEHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FSEHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FSEHomeFragment newInstance(String param1, String param2) {
        FSEHomeFragment fragment = new FSEHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireSafetyEquipmentMainActivity activity = (FireSafetyEquipmentMainActivity)getActivity();
        View view =inflater.inflate(R.layout.fragment_f_s_e_home, container, false);
        maintenanceCard = (CardView) view.findViewById(R.id.maintenanceCard);
        checkCard = (CardView) view.findViewById(R.id.checkCard);

        maintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSEMaintenanceFragment(), true);
            }
        });

        checkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.enableViews(true);
                activity.replaceFragment(new FSECheckFragment(), true);
            }
        });

        return view;
    }
}