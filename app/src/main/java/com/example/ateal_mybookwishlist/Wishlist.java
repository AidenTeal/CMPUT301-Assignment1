package com.example.ateal_mybookwishlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Wishlist is solely responsible for holding a list of Books that represent a users wishlist. The wishlist
 * is able to get books from its list, add books to its list, and delete books. Furthermore, Wishlist
 * can also obtain the total number of books and books read using their respective methods.
 */
public class Wishlist implements Parcelable {
    private ArrayList<Book> wishlist;

    public Wishlist (ArrayList<Book> books) {
        this.wishlist = new ArrayList<Book>();
        wishlist.addAll(books);
    }

    public Wishlist () {
        this.wishlist = new ArrayList<Book>();
    }

    protected Wishlist(Parcel in) {
        wishlist = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<Wishlist> CREATOR = new Creator<Wishlist>() {
        @Override
        public Wishlist createFromParcel(Parcel in) {
            return new Wishlist(in);
        }

        @Override
        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };

    public ArrayList<Book> getWishlist() {
        return wishlist;
    }

    public Book getBook(Integer index) {
        return wishlist.get(index);
    }

    public void addBooks (ArrayList<Book> books) {
        wishlist.addAll(books);
    }

    public void addBook (Book book) {
        wishlist.add(book);
    }

    public void removeBook (Book book) {
        wishlist.remove(book);
    }

    public Integer getNumberOfBooks () {
        return wishlist.size();
    }

    public Integer getNumberOfBooksRead () {
        int booksRead = 0;
        for (int i = 0; i < wishlist.size(); i++) {
            if (wishlist.get(i).getStatus().equals("Read")) {
                booksRead++;
            }
        }
        return booksRead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeTypedList(wishlist);
    }
}

