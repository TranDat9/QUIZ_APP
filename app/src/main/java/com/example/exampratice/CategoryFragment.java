package com.example.exampratice;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class CategoryFragment extends Fragment {
    GridView catView;


    CategoryAdapter adapter;
    public CategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category, container, false);
        catView= view.findViewById(R.id.cat_Grid);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Categories");

       // loadCategories();
         adapter = new CategoryAdapter(DataBase.g_catList);
          catView.setAdapter(adapter);

        return view;
    }

}