package hu.unideb.inf.smartrecipe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import hu.unideb.inf.smartrecipe.dao.GroceryDao;
import hu.unideb.inf.smartrecipe.dao.RecipeDao;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;

import hu.unideb.inf.smartrecipe.model.GroceryItem; // make sure this import is added

@Database(entities = {FavoriteRecipe.class, GroceryItem.class}, version = 2)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();
    public abstract GroceryDao groceryDao();

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "recipe_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
