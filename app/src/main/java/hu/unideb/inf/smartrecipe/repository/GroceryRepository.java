package hu.unideb.inf.smartrecipe.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;

import hu.unideb.inf.smartrecipe.dao.GroceryDao;
import hu.unideb.inf.smartrecipe.database.RecipeDatabase;
import hu.unideb.inf.smartrecipe.model.GroceryItem;

public class GroceryRepository {
    private final GroceryDao groceryDao;
    private final LiveData<List<GroceryItem>> allItems;

    public GroceryRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getInstance(application);
        groceryDao = db.groceryDao();
        allItems = groceryDao.getAllGroceryItems();
    }

    public LiveData<List<GroceryItem>> getAllItems() { return allItems; }

    public void insert(GroceryItem item) {
        Executors.newSingleThreadExecutor().execute(() -> groceryDao.insert(item));
    }

    public void delete(GroceryItem item) {
        Executors.newSingleThreadExecutor().execute(() -> groceryDao.delete(item));
    }
}

