package com.example.ateal_mybookwishlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Book is responsible for storing a book's data, including title, author, genre, publication year,
 * and status. It can be instantiated with or without a genre, but all other fields are mandatory.
 * Since user's are able to edit a book, there are getters and setters to update and fetch book
 * information.
 */
public class Book implements Parcelable {
    private String bookTitle;
    private String authorName;
    private String genre;
    private Integer publicationYear;
    private String status;

    public Book(String bookTitle, String authorName, String genre, Integer publicationYear, Boolean isRead) {
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.genre = genre;
        this.publicationYear = publicationYear;
        if(isRead) {
            this.status = "Read";
        } else {
            this.status = "Unread";
        }
    }

    public Book(String bookTitle, String authorName, Integer publicationYear, Boolean isRead) {
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.genre = "None";
        this.publicationYear = publicationYear;
        if(isRead) {
            this.status = "Read";
        } else {
            this.status = "Unread";
        }
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getGenre() {
        return this.genre;
    }

    public Integer getPublicationYear() {
        return this.publicationYear;
    }

    public String getStatus() {
        return this.status;
    }

    public void setBookTitle(String title) {
        this.bookTitle = title;
    }

    public void setAuthorName(String author) {
        this.authorName = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setStatus(Boolean isRead) {
        if (isRead) {
            this.status = "Read";
        } else {
            this.status = "Unread";
        }
    }

    protected Book(Parcel in) {
        bookTitle = in.readString();
        authorName = in.readString();
        genre = in.readString();
        if (in.readByte() == 0) {
            publicationYear = null;
        } else {
            publicationYear = in.readInt();
        }
        status = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(bookTitle);
        parcel.writeString(authorName);
        parcel.writeString(genre);
        if (publicationYear == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(publicationYear);
        }
        parcel.writeString(status);
    }
}