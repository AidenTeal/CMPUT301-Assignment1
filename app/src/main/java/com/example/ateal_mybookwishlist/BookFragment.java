package com.example.ateal_mybookwishlist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ateal_mybookwishlist.databinding.FragmentSecondBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * BookFragment is responsible for displaying the details of a book a user selects and allowing a
 * user to edit or delete a book if they wish. BookFragment interacts with a Book and Wishlist object
 * which allows it to update or delete the book from the wishlist if the user chooses to do so.
 */
public class BookFragment extends Fragment {
    // BookFragment class is responsible for showing the book details for a specific book on a new android page
    // It also should allow a user to edit and delete the book
    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get book from fragment arguments
        Book viewedBook = BookFragmentArgs.fromBundle(getArguments()).getBook();
        Wishlist wishlist = BookFragmentArgs.fromBundle(getArguments()).getWishlist();

        // set the text views with the book information
        setViews(viewedBook);

        view.findViewById(R.id.button_to_wishlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BookFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.button_to_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook(viewedBook, wishlist);
            }
        });

        view.findViewById(R.id.button_to_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditBookDialog(viewedBook);
            }
        });
    }

    /*
      Description: Deletes a Book object from the Wishlist

      Arguments: Book viewedBook: Book object to be deleted
                 Wishlist wishlist: Wishlist the Book object is located within

      Returns: Nothing
    */
    public void deleteBook(Book viewedBook, Wishlist wishlist) {
        // Delete book from wishlist
        wishlist.removeBook(viewedBook);

        // Navigate back to wishlist page
        NavHostFragment.findNavController(BookFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    /*
       Description: Show an edit dialog that will allow the users to update book information

       Arguments: Book viewedBook: Book object user wants to edit

       Returns: Nothing
    */
    public void showEditBookDialog(Book viewedBook) {
        // bring up pop up dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.book_dialog_edit);
        dialog.show();

        // Get UI elements
        TextInputEditText bookTitle = dialog.findViewById(R.id.edit_book_title_input);
        TextInputEditText bookAuthor = dialog.findViewById(R.id.edit_author_input);
        TextInputEditText bookGenre = dialog.findViewById(R.id.edit_genre_input);
        TextInputEditText bookPublicationYear = dialog.findViewById(R.id.edit_publication_input);
        RadioButton radio_button_unread = dialog.findViewById(R.id.edit_radio_button_unread);
        RadioButton radio_button_read = dialog.findViewById(R.id.edit_radio_button_read);

        // Set UI Elements
        bookTitle.setText(viewedBook.getBookTitle());
        bookAuthor.setText(viewedBook.getAuthorName());
        if (viewedBook.getGenre().equals("None")) {
            bookGenre.setText("");
        } else {
            bookGenre.setText(viewedBook.getGenre());
        }
        bookPublicationYear.setText(String.valueOf(viewedBook.getPublicationYear()));
        if (viewedBook.getStatus().equals("Read")) {
            radio_button_read.setChecked(Boolean.TRUE);
        } else {
            radio_button_unread.setChecked(Boolean.TRUE);
        }

        dialog.findViewById(R.id.confirmBook_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBook(dialog, viewedBook);
            }
        });
        dialog.findViewById(R.id.cancelBook_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /*
      Description: Updates the Book object with the new data the user entered

      Arguments: Dialog dialog: Dialog containing the interface for editing the Book data
                 Book bookObj: The Book object that is being edited

      Returns: Nothing
    */
    public void editBook(Dialog dialog, Book bookObj) {
        // Obtain the text layout and text blocks in the edit dialog
        TextInputLayout bookTitleLayout = dialog.findViewById(R.id.titleInputLayout);
        TextInputEditText bookTitle = dialog.findViewById(R.id.edit_book_title_input);
        TextInputLayout bookAuthorLayout = dialog.findViewById(R.id.authorInputLayout);
        TextInputEditText bookAuthor = dialog.findViewById(R.id.edit_author_input);
        TextInputEditText bookGenre = dialog.findViewById(R.id.edit_genre_input);
        TextInputLayout bookPublicationLayout = dialog.findViewById(R.id.publicationInputLayout);
        TextInputEditText bookPublicationYear = dialog.findViewById(R.id.edit_publication_input);

        // Obtain the "Read" radio button
        RadioButton radio_button_read = dialog.findViewById(R.id.edit_radio_button_read);

        Boolean hasError = Boolean.FALSE;

        // Ensure text blocks are not null or empty
        if (!checkInput(bookTitle) || !checkInput(bookAuthor) || !checkInput(bookPublicationYear)) {
            hasError = Boolean.TRUE;
        }

        // setErrorMessage in the text layout if any of the user inputs don't satisfy requirements
        setErrorMessage(bookTitleLayout, bookTitle, "Title field cannot be empty");
        setErrorMessage(bookAuthorLayout, bookAuthor, "Author field cannot be empty");
        setErrorMessage(bookPublicationLayout, bookPublicationYear, "Publication year cannot be empty");

        // Ensure publication year is four digits
        if (bookPublicationYear.getText().length() != 4) {
            bookPublicationLayout.setError("Publication year must be 4 digits");
            hasError = Boolean.TRUE;
        } else {
            bookPublicationLayout.setError(null);
        }

        // no errors, update Book object with new information
        if (hasError == Boolean.FALSE) {
            String newTitle = bookTitle.getText().toString();
            bookObj.setBookTitle(newTitle);
            String newAuthor = bookAuthor.getText().toString();
            bookObj.setAuthorName(newAuthor);
            if (checkInput(bookGenre)) {
                String newGenre = bookGenre.getText().toString();
                bookObj.setGenre(newGenre);
            } else {
                bookObj.setGenre("None");
            }
            String newPublicationYear = bookPublicationYear.getText().toString();
            bookObj.setPublicationYear(Integer.parseInt(newPublicationYear));

            bookObj.setStatus(radio_button_read.isChecked());

            // Reload text views with updated information
            setViews(bookObj);

            dialog.dismiss();
        }
    }

    /*
      Description: Checks input for null or empty values

      Arguments: TextInputEditText text: An android text block containing the input a user enters

      Returns: Boolean - True if input passes checks, False if input fails checks
    */
    public Boolean checkInput(TextInputEditText text) {
        if (text.getText() == null) {
            return Boolean.FALSE;
        }
        if (text.getText().length() == 0) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /*
      Description: Sets an error message for the TextInputLayout xml tag

      Arguments: TextInputLayout textLayout: Xml tag wrapping the edittable input
                 TextInputEditText: An android text block containing the input a user enters
                 String errorMessage: The error message that should be shown

      Returns: Nothing
    */
    public void setErrorMessage(TextInputLayout textLayout, TextInputEditText textString, String errorMessage) {
        // If the input fails to pass the requirements, set an error message
        if (!checkInput(textString)) {
            textLayout.setError(errorMessage);
        } else {
            textLayout.setError(null);
        }
    }

    /*
      Description: Sets up the text views on the BookFragment to contain the updated book data

      Arguments: Book viewedBook: Book object containing the information for the book being displayed

      Returns: Nothing
    */
    public void setViews(Book viewedBook) {
        String authorText = getString(R.string.book_author_string, viewedBook.getAuthorName());
        String titleText = getString(R.string.book_title_string, viewedBook.getBookTitle());
        String genreText = getString(R.string.book_genre_string, viewedBook.getGenre());
        String publicationText = getString(R.string.book_publication_year_string, viewedBook.getPublicationYear());
        String statusText = getString(R.string.book_status_string, viewedBook.getStatus());

        // get the xml text blocks
        TextView titleTextView = getView().findViewById(R.id.second_title);
        TextView authorTextView = getView().findViewById(R.id.second_author);
        TextView genreTextView = getView().findViewById(R.id.second_genre);
        TextView publicationTextView = getView().findViewById(R.id.second_publicationYear);
        TextView statusTextView = getView().findViewById(R.id.second_read_status);

        // set the text in the xml
        titleTextView.setText(authorText);
        authorTextView.setText(titleText);
        genreTextView.setText(genreText);
        publicationTextView.setText(publicationText);
        statusTextView.setText(statusText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}