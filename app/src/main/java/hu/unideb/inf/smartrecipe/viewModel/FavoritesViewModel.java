package hu.unideb.inf.smartrecipe.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.unideb.inf.smartrecipe.dao.RecipeDao;
import hu.unideb.inf.smartrecipe.database.RecipeDatabase;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;

public class FavoritesViewModel extends AndroidViewModel {

    private final RecipeDao recipeDao;
    private final LiveData<List<FavoriteRecipe>> favoriteRecipes;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        RecipeDatabase db = RecipeDatabase.getInstance(application);
        recipeDao = db.recipeDao();
        favoriteRecipes = recipeDao.getAllFavorites();
    }

    public LiveData<List<FavoriteRecipe>> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void deleteFavorite(FavoriteRecipe recipe) {
        new Thread(() -> recipeDao.delete(recipe)).start();
    }

    public void insertFavorite(FavoriteRecipe recipe) {
        new Thread(() -> recipeDao.insert(recipe)).start();
    }
}
