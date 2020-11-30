package com.app.biketour.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.biketour.HomeActivity;
import com.app.biketour.R;
import com.app.biketour.Utillity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UserSavedFragment extends Fragment {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 1;

    private static String TAG = "BikeTour App - User Saved";
    ImageButton ibtnTopPopupMenu;
    SharedPreferences pref;

    private UserSavedNoteViewModel userSavedNoteViewModel;

    public static UserSavedFragment newInstance() {
        return new UserSavedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_saved, container, false);

        // Visible top popup menu icon
        ibtnTopPopupMenu = (ImageButton) getActivity().findViewById(R.id.ibtnMenu);
        ibtnTopPopupMenu.setVisibility(View.VISIBLE);

        // Check login state
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        if(!pref.contains("email") && !pref.contains("firstname")){
            // Redirect login fragment
            ((HomeActivity)getActivity()).replaceFragment(UserLoginFragment.newInstance("saved"));
        }

        FloatingActionButton buttonAddNote = root.findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(UserSavedAddEditNote.newInstance(0,"","",""));
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final UserSavedNoteAdapter adapter = new UserSavedNoteAdapter();
        recyclerView.setAdapter(adapter);

        userSavedNoteViewModel = new ViewModelProvider(this).get(UserSavedNoteViewModel.class);
        userSavedNoteViewModel.getAllNotes().observe(getActivity(), new Observer<List<UserSavedNote>>() {
            @Override
            public void onChanged(@Nullable List<UserSavedNote> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                userSavedNoteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Utillity.showMessage(getActivity(),"INFO", "Note deleted");
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new UserSavedNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserSavedNote note) {
                ((HomeActivity)getActivity()).replaceFragment(UserSavedAddEditNote.newInstance(note.getId(), note.getTitle(), note.getDescription(), note.getRegisterDate()));
                //Utillity.showMessage(getActivity(),"INFO", String.valueOf(note.getId()));
            }
        });

        return root;
    }
}
