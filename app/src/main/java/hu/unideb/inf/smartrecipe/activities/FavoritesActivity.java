package hu.unideb.inf.smartrecipe.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.adapter.FavoriteRecipeAdapter;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;
import hu.unideb.inf.smartrecipe.viewModel.FavoritesViewModel;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteRecipeAdapter adapter;
    private FavoritesViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FavoriteRecipeAdapter(recipe -> viewModel.deleteFavorite(recipe));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        viewModel.getFavoriteRecipes().observe(this, favoriteRecipes -> {
            adapter.setFavoriteRecipes(favoriteRecipes);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                Toast.makeText(FavoritesActivity.this, "Removed: " + swipedRecipe.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
