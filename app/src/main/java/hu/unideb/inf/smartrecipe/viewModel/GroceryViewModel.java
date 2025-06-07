package hu.unideb.inf.smartrecipe.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.unideb.inf.smartrecipe.model.GroceryItem;
import hu.unideb.inf.smartrecipe.repository.GroceryRepository;

public class GroceryViewModel extends AndroidViewModel {
    private final GroceryRepository repository;
    private final LiveData<List<GroceryItem>> allItems;

    public GroceryViewModel(@NonNull Application application) {
        super(application);
        repository = new GroceryRepository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<GroceryItem>> getAllItems() { return allItems; }

    public void insert(GroceryItem item) { repository.insert(item); }

    public void delete(GroceryItem item) { repository.delete(item); }
}
