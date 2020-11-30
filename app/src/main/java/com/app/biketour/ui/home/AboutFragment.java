package com.app.biketour.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.biketour.R;

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    ImageButton ibtnTopPopupMenu;
    TextView tvAboutTitle, tvAboutDetial;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) (getActivity()).findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        tvAboutTitle = (TextView) root.findViewById(R.id.tv_about_title);
        tvAboutTitle.setText(R.string.about_title);

        tvAboutDetial = (TextView) root.findViewById(R.id.tv_about_detail);
        tvAboutDetial.setText(R.string.about_detail);

        return root;
    }
}
