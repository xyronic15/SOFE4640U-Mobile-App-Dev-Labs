package com.uoit.noteme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    Context context;
    ArrayList<NotesModel> notesArrayList;
    OnNoteListener noteOnNoteListener;

    public ListAdapter(Context context, ArrayList<NotesModel> notesArrayList, OnNoteListener onNoteListener) {
        this.context = context;
        this.notesArrayList = notesArrayList;
        this.noteOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list, parent, false);
        return new MyViewHolder(view, noteOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        NotesModel note = notesArrayList.get(position);
        holder.title.setText(note.getTitle());
        holder.subtitle.setText(note.getSubtitle());
        holder.setBackgroundColor(note.getColour());
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView subtitle;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            title = itemView.findViewById(R.id.listNoteTitle);
            subtitle = itemView.findViewById(R.id.listNoteSubtitle);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }
        public void setBackgroundColor(String colour) {
            itemView.setBackgroundColor(Color.parseColor(colour));
        }

        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public void updateAdapter(ArrayList<NotesModel> notes){
        this.notesArrayList = notes;
        this.notifyDataSetChanged();
    }
}
