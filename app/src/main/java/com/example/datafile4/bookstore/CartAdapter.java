package com.example.datafile4.bookstore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by datafile4 on 8/19/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView authorTextView;
        public NetworkImageView bookImage;

        //constructor that accepts the entire item row
        // and does the view lookups to find each subview

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            nameTextView = (TextView) itemView.findViewById(R.id.cart_item_book_name);
            authorTextView = (TextView)itemView.findViewById(R.id.cart_item_book_author);
            bookImage = (NetworkImageView) itemView.findViewById(R.id.cart_item_book_image);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CartAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    //Store a member variable for the books
    private List<Book> mBooks;
    //Store context. It will be needed below
    private Context mContext;

    //Constructor
    public CartAdapter(Context context, List<Book> books) {
        mBooks = books;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    //involves inflating a layout from XML and returning the holder

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_cart, parent, false);
        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the data model based on position
        Book book = mBooks.get(position);

        // Setting item views based on my views and data model
        TextView textViewName = holder.nameTextView;
        textViewName.setText(book.getBookName());

        TextView textViewAuthor = holder.authorTextView;

        final NetworkImageView image = holder.bookImage;
        ImageLoader mImageLoader = MySingleton.getInstance(mContext).getImageLoader();
        image.setImageUrl(book.getBookImgUrl(), mImageLoader);
        image.setErrorImageResId(R.drawable.defaultcover);

    }

    public void updateGrid(List<Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public void removeGrid(){
        mBooks = null;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }
}
