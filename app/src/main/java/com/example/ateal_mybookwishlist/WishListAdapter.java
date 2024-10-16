package com.example.ateal_mybookwishlist;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * WishListAdapter is responsible for customizing an ArrayAdapter to allow for the storing of Book
 * Objects that can be displayed in a ListView
 */
public class WishListAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> books;
    public WishListAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content,
                    parent, false);
        } else {
            view = convertView;
        }
        Book book = books.get(position);
        TextView book_details_one = view.findViewById(R.id.book_details_one);
        TextView book_details_two = view.findViewById(R.id.book_details_two);

        String bookDetailsOne = getContext().getString(R.string.book_details_one_string, book.getBookTitle(), book.getAuthorName());
        String bookDetailsTwo = getContext().getString(R.string.book_details_two_string, book.getGenre(), book.getStatus());

        book_details_one.setText(bookDetailsOne);
        book_details_two.setText(bookDetailsTwo);
        return view;
    }
}

