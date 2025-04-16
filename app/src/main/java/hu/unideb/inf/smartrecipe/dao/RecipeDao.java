package hu.unideb.inf.smartrecipe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;

@Dao
public interface RecipeDao {
    @Insert
    void insert(FavoriteRecipe recipe);

    @Delete
    void delete(FavoriteRecipe recipe);

    @Query("SELECT * FROM favorite_recipes")
    LiveData<List<FavoriteRecipe>> getAllFavorites();

    @Query("SELECT * FROM favorite_recipes WHERE id = :id LIMIT 1")
    FavoriteRecipe getById(int id);
}
