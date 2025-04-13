package hu.unideb.inf.smartrecipe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import hu.unideb.inf.smartrecipe.dao.RecipeDao;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;

@Database(entities = {FavoriteRecipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "recipe_database")
                    .fallbackToDestructiveMigration(true)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
