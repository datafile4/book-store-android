package com.example.datafile4.bookstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by datafile4 on 8/29/16.
 */
public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

    interface OnItemCheckListener{
        void onItemCheck(Genre genre);
        void onItemUncheck(Genre genre);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public CheckBox checkBox;
        //constructor that accepts the entire item row
        // and does the view lookups to find each subview

        public ViewHolder(View itemView) {
            super(itemView);
           // itemView.setOnClickListener(this);
            nameTextView = (TextView)itemView.findViewById(R.id.filter_row_text);
            checkBox = (CheckBox) itemView.findViewById(R.id.filter_row_checkbox);

        }

    }
    private List<Genre> mGenres;
    //Store context. It will be needed below
    private Context mContext;

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    //Constructor
    public GenreListAdapter(Context context, List<Genre> genres, @NonNull OnItemCheckListener onItemCheckListener){
        mGenres = genres;
        mContext = context;
        this.onItemCheckListener = onItemCheckListener;
    }



    public Context getContext(){
        return mContext;
    }

    //involves inflating a layout from XML and returning the holder

    @Override
    public GenreListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View contactView = inflater.inflate(R.layout.filter_row,parent,false);
        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the data model based on position
        final Genre genre = mGenres.get(position);

        // Setting item views based on my views and data model
        TextView textView = holder.nameTextView;
        textView.setText(genre.getGenreName());

        final CheckBox checkBox = holder.checkBox;
       // checkBox.setId(genre.getGenreID());


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    onItemCheckListener.onItemCheck(genre);
                    genre.setSelected(true);
                } else {
                    onItemCheckListener.onItemUncheck(genre);
                    genre.setSelected(false);
                }
            }
        });
        if(genre.isSelected()){
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        //checkBox.setChecked(false);
    }
    @Override
    public int getItemCount() {
        return mGenres.size();
    }

    public void updateList(List<Genre> genres){
        mGenres = genres;
        notifyDataSetChanged();
    }
}
