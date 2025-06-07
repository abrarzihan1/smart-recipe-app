package hu.unideb.inf.smartrecipe.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grocery_items")
public class GroceryItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public GroceryItem(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
