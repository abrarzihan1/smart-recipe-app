package hu.unideb.inf.smartrecipe.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hu.unideb.inf.smartrecipe.model.GroceryItem;

@Dao
public interface GroceryDao {
    @Insert
    void insert(GroceryItem item);

    @Delete
    void delete(GroceryItem item);

    @Query("SELECT * FROM grocery_items")
    LiveData<List<GroceryItem>> getAllGroceryItems();
}

