package com.uoit.noteme;

import android.content.Context;
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

    public ListAdapter(Context context, ArrayList<NotesModel> notesArrayList) {
        this.context = context;
        this.notesArrayList = notesArrayList;
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.note_list, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        NotesModel note = notesArrayList.get(position);
        holder.title.setText(note.getTitle());
        holder.subtitle.setText(note.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView subtitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listNoteTitle);
            subtitle = itemView.findViewById(R.id.listNoteSubtitle);
        }
    }

    public void updateAdapter(ArrayList<NotesModel> notes){
        this.notesArrayList = notes;
        this.notifyDataSetChanged();
    }
}
