package com.hardbug.productos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_solicitudes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_solicitudes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialToolbar toolbar;
    FloatingActionButton add;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_solicitudes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_solicitudes.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_solicitudes newInstance(String param1, String param2) {
        fragment_solicitudes fragment = new fragment_solicitudes();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        toolbar = root.findViewById(R.id.toolbarsolicitudes);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        add = root.findViewById(R.id.fabsolicitudes);
        add.setOnClickListener(View -> {
            fragment_crear_solicitudes solicitudes = new fragment_crear_solicitudes();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, solicitudes, "");
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return root;
    }
}