package hu.unideb.inf.smartrecipe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.activities.RecipeDetailActivity;
import hu.unideb.inf.smartrecipe.adapter.FavoriteRecipeAdapter;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;
import hu.unideb.inf.smartrecipe.viewModel.FavoritesViewModel;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteRecipeAdapter adapter;
    private FavoritesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Updated adapter constructor with both click listeners
        adapter = new FavoriteRecipeAdapter(
                // Click listener for opening recipe details
                recipe -> {
                    Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                    intent.putExtra("id", recipe.getId());
                    intent.putExtra("title", recipe.getTitle());
                    intent.putExtra("image", recipe.getImage());
                    startActivity(intent);
                },
                // Delete listener for removing favorites
                recipe -> viewModel.deleteFavorite(recipe)
        );

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        viewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), favoriteRecipes -> {
            adapter.setFavoriteRecipes(favoriteRecipes);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FavoriteRecipe swipedRecipe = adapter.getRecipeAt(position);

                viewModel.deleteFavorite(swipedRecipe);
                Snackbar.make(recyclerView, "Recipe removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> viewModel.insertFavorite(swipedRecipe))
                        .show();

                Toast.makeText(getContext(), "Removed: " + swipedRecipe.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }
}