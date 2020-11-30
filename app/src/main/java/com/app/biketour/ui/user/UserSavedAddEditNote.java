package com.app.biketour.ui.user;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.app.biketour.ui.home.TourNewsDetailFragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSavedAddEditNote extends Fragment {

    public static final String EXTRA_ID =
            "com.codinginflow.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.codinginflow.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.codinginflow.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_REGISTERDATE =
            "com.codinginflow.architectureexample.EXTRA_REGISTERDATE";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView editTextRegisterDate;
    Button btnSave, btnCancel;
    private UserSavedNoteViewModel userSavedNoteViewModel;

    public static UserSavedAddEditNote newInstance(int id, String title, String des, String rDate) {
        UserSavedAddEditNote fragment = new UserSavedAddEditNote();
        Bundle args = new Bundle();
        args.putString(EXTRA_ID, String.valueOf(id));
        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_DESCRIPTION, des);
        args.putString(EXTRA_REGISTERDATE, rDate);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_saved_add_note, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editTextTitle = root.findViewById(R.id.edit_text_title);
        editTextDescription = root.findViewById(R.id.edit_text_description);
        editTextRegisterDate = root.findViewById(R.id.tv_note_registerdate);

        if (!getArguments().getString(EXTRA_ID).equals("0")) {
            editTextTitle.setText(getArguments().getString(EXTRA_TITLE));
            editTextDescription.setText(getArguments().getString(EXTRA_DESCRIPTION));
            editTextRegisterDate.setText(getArguments().getString(EXTRA_REGISTERDATE));
        }
        else {
            Date nowDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String registerDate = formatter.format(nowDate);

            editTextRegisterDate.setText(registerDate);
        }
        btnSave = (Button) root.findViewById(R.id.btn_save_note);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        btnCancel = (Button) root.findViewById(R.id.btn_save_note_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(UserSavedFragment.newInstance());
            }
        });

        userSavedNoteViewModel = new ViewModelProvider(this).get(UserSavedNoteViewModel.class);

        return root;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String registerDate = formatter.format(nowDate);

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Utillity.showMessage(getActivity(), "INFO", "Please insert a title and description");
            return;
        }

        int id = Integer.parseInt(getArguments().getString(EXTRA_ID));
        if (id > 0) {

            if (id == 0) {
                Utillity.showMessage(getActivity(),"INFO", "Note can't be updated");
                return;
            }

            UserSavedNote note = new UserSavedNote(title, description, registerDate);
            note.setId(id);
            userSavedNoteViewModel.update(note);

            Utillity.showMessage(getActivity(),"INFO", "Note updated");

            ((HomeActivity)getActivity()).replaceFragment(UserSavedFragment.newInstance());
        }
        else {
            UserSavedNote note = new UserSavedNote(title, description, registerDate);
            userSavedNoteViewModel.insert(note);
            Utillity.showMessage(getActivity(),"INFO", "Note Saved");

            ((HomeActivity)getActivity()).replaceFragment(UserSavedFragment.newInstance());
        }


    }
}
