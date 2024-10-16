package com.example.ateal_mybookwishlist;

import androidx.lifecycle.ViewModel;

/**
 * WishListViewModel is responsible for creating a Wishlist object that will exist between
 * fragment lifecycles. WishlistFragment calls WishlistViewModel using the getWishlist() method
 * to obtain the wishlist data everytime it is re-created.
 */
// The following statement from OpenAI, chatGPT, "How can I keep data alive between fragment lifecycles in android studio?"
// 2024-09-29. Returned: Create a custom viewModel -  Example: CustomViewModel extends ViewModel, and add the data to the view model
public class WishListViewModel extends ViewModel {
    private Wishlist wishlist = new Wishlist();

    public Wishlist getWishlist() {
        return this.wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }
}
