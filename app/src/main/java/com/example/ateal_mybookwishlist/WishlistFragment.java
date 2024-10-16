package com.example.ateal_mybookwishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.RadioButton;
import android.app.Dialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.example.ateal_mybookwishlist.databinding.FragmentFirstBinding;
import com.google.android.material.textfield.TextInputLayout;

/**
 * WishlistFragment is responsible for displaying the list of books in the user's wishlist on a distinct page.
 * It also is responsible for displaying the total books, and total book read. Furthermore, this class is
 * responsible for allowing a user to add a book, by providing the user with a dialog to do so. If a user adds a book
 * the UI will be updated to reflect that.
 */
public class WishlistFragment extends Fragment {
    // declaring required objects
    private ListView bookList;
    private WishListViewModel wishlistViewModel;
    private ArrayAdapter<Book> wishlistAdapter;
    private TextView totalBooksView;
    private TextView readBooksView;

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // The following statement from OpenAI, chatGPT, "How do I use a custom viewModel from android studio
        // in my fragment class?", 2024-09-29
        // Initialize WishlistViewModel
        wishlistViewModel = new ViewModelProvider(requireActivity()).get(WishListViewModel.class);

        Wishlist wishlist = wishlistViewModel.getWishlist();

        bookList = view.findViewById(R.id.book_list);
        wishlistAdapter = new WishListAdapter(getContext(), wishlist.getWishlist());
        bookList.setAdapter(wishlistAdapter);

        totalBooksView = view.findViewById(R.id.total_books);
        readBooksView = view.findViewById(R.id.total_books_read);

        // Update total text views to display initial wishlist information
        totalBooksView.setText(String.format("Total Books: " + wishlist.getNumberOfBooks().toString()));
        readBooksView.setText(String.format("Total Books Read: " + wishlist.getNumberOfBooksRead()));

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the book object the user clicked on
                Book bookToView = wishlist.getBook(position);

                // Take user to Book Fragment
                WishlistFragmentDirections.ActionFirstFragmentToSecondFragment action =
                        WishlistFragmentDirections.actionFirstFragmentToSecondFragment(bookToView, wishlist);
                NavHostFragment.findNavController(WishlistFragment.this).navigate(action);
            }
        });

        view.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBookDialog(view);
            }
        });
    }

    /*
      Description: Displays a dialog allowing the user to add a book

      Arguments: View view: Current view of the application

      Returns: Nothing
    */
    public void showAddBookDialog(View view) {
        // bring up pop up dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.book_dialog_add);
        dialog.show();

        // Set read status to unread as default
        RadioButton buttonUnread = dialog.findViewById(R.id.add_radio_button_unread);
        buttonUnread.setChecked(Boolean.TRUE);

        dialog.findViewById(R.id.confirmBook_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook(dialog);
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
      Description: Adds a Book object to the wishlist

      Arguments: Dialog dialog: Dialog containing the book information the user entered

      Returns: Nothing
    */
    public void addBook(Dialog dialog) {
        // Obtain the text layout and text blocks in the add dialog
        TextInputLayout bookTitleLayout = dialog.findViewById(R.id.titleInputLayout);
        TextInputEditText bookTitle = dialog.findViewById(R.id.add_book_title_input);
        TextInputLayout bookAuthorLayout = dialog.findViewById(R.id.authorInputLayout);
        TextInputEditText bookAuthor = dialog.findViewById(R.id.add_author_input);
        TextInputEditText bookGenre = dialog.findViewById(R.id.add_genre_input);
        TextInputLayout bookPublicationLayout = dialog.findViewById(R.id.publicationInputLayout);
        TextInputEditText bookPublicationYear = dialog.findViewById(R.id.add_publication_input);

        // Obtain the "Read" radio button
        RadioButton isReadButton = dialog.findViewById(R.id.add_radio_button_read);
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

        // no errors, add Book object with new information
        if (hasError == Boolean.FALSE) {
            String newTitle = bookTitle.getText().toString();
            String newAuthor = bookAuthor.getText().toString();
            String newPublicationYear = bookPublicationYear.getText().toString();

            // instantiate and create a new book object with user's input
            Book newBookObj;
            if (checkInput(bookGenre)) {
                String newGenre = bookGenre.getText().toString();
                newBookObj = new Book(newTitle, newAuthor, newGenre, Integer.parseInt(newPublicationYear), isReadButton.isChecked());
            } else {
                newBookObj = new Book(newTitle, newAuthor, Integer.parseInt(newPublicationYear), isReadButton.isChecked());
            }

            // add city to list
            wishlistAdapter.add(newBookObj);

            // update total books and read books in library
            totalBooksView.setText(String.format("Total Books: " + wishlistViewModel.getWishlist().getNumberOfBooks().toString()));
            readBooksView.setText(String.format("Total Books Read: " + wishlistViewModel.getWishlist().getNumberOfBooksRead()));

            dialog.dismiss();
        }
    }

    /*
      Description: Checks input for null or empty values

      Arguments: TextInputEditText text: An android text block containing the input a user enters

      Returns: Boolean - True if input passes checks, False if input fails checks
    */
    private Boolean checkInput(TextInputEditText text) {
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
      Description: Checks input for null or empty values

      Arguments: TextInputEditText text: An android text block containing the input a user enters

      Returns: Boolean - True if input passes checks, False if input fails checks
    */
    public void setErrorMessage(TextInputLayout textLayout, TextInputEditText textString, String errorMessage) {
        if (!checkInput(textString)) {
            textLayout.setError(errorMessage);
        } else {
            textLayout.setError(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}