package hu.unideb.inf.smartrecipe.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.dao.RecipeDao;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder> {

    private List<FavoriteRecipe> favorites = new ArrayList<>();
    private RecipeDao recipeDao;
    private final OnDeleteClickListener deleteClickListener;
    private final OnRecipeClickListener recipeClickListener;

    public FavoriteRecipe getRecipeAt(int position) {
        return favorites.get(position);
    }

    public interface OnDeleteClickListener {
        void OnDeleteClick(FavoriteRecipe recipe);
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(FavoriteRecipe recipe);
    }

    // Updated constructor to accept both listeners
    public FavoriteRecipeAdapter(OnRecipeClickListener recipeClickListener, OnDeleteClickListener deleteClickListener) {
        this.recipeClickListener = recipeClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteRecipe recipe = favorites.get(position);
        holder.title.setText(recipe.getTitle());

        Glide.with(holder.image.getContext())
                .load(recipe.getImage())
                .into(holder.image);

        // Set click listener for the entire item to open recipe details
        holder.itemView.setOnClickListener(v -> {
            if (recipeClickListener != null) {
                recipeClickListener.onRecipeClick(recipe);
            }
        });

        // Set click listener for delete icon
        holder.deleteIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Remove Recipe")
                    .setMessage("Are you sure you want to remove this recipe from favorites?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteClickListener.OnDeleteClick(recipe);
                        Toast.makeText(holder.itemView.getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void setFavoriteRecipes(List<FavoriteRecipe> favoriteRecipes) {
        this.favorites = favoriteRecipes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public ImageView deleteIcon;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.recipeTitle);
            image = view.findViewById(R.id.recipeImage);
            deleteIcon = view.findViewById(R.id.deleteIcon);
        }
    }
}