package com.app.biketour.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.app.biketour.R;

public class UserContactFragment extends Fragment {
    private static String TAG = "BikeTour App - News List";
    ImageButton ibtnTopPopupMenu;

    public static UserContactFragment newInstance() {
        return new UserContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contact, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        return root;
    }

}
