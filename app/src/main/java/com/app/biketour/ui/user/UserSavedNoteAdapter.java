package com.app.biketour.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.biketour.R;

import java.util.ArrayList;
import java.util.List;

public class UserSavedNoteAdapter extends ListAdapter<UserSavedNote, UserSavedNoteAdapter.NoteHolder> {
    private List<UserSavedNote> notes = new ArrayList<>();
    private OnItemClickListener listener;

    protected UserSavedNoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<UserSavedNote> DIFF_CALLBACK = new DiffUtil.ItemCallback<UserSavedNote>() {
        @Override
        public boolean areItemsTheSame(UserSavedNote oldItem, UserSavedNote newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(UserSavedNote oldItem, UserSavedNote newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getRegisterDate() == newItem.getRegisterDate();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_saved_note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        UserSavedNote currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
//        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewRegisterDate.setText(currentNote.getRegisterDate());
    }

    public UserSavedNote getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewRegisterDate;
        private TextView textViewDescription;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewRegisterDate = itemView.findViewById(R.id.text_view_registerdate);
           // textViewDescription = itemView.findViewById(R.id.edit_text_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(UserSavedNote note);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
