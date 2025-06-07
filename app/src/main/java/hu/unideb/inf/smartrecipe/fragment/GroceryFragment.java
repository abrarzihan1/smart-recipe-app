package hu.unideb.inf.smartrecipe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.adapter.GroceryAdapter;
import hu.unideb.inf.smartrecipe.model.GroceryItem;
import hu.unideb.inf.smartrecipe.viewModel.GroceryViewModel;

public class GroceryFragment extends Fragment {

    private GroceryViewModel viewModel;
    private GroceryAdapter adapter;
    private List<GroceryItem> groceryItems = new ArrayList<>();

    private EditText editTextItem;
    private Button buttonAdd, buttonSearch;

    public GroceryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery, container, false);

        editTextItem = view.findViewById(R.id.editTextItem);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonSearch = view.findViewById(R.id.buttonSearch);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGrocery);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroceryAdapter(groceryItems, item -> viewModel.delete(item));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        viewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            groceryItems.clear();
            groceryItems.addAll(items);
            adapter.notifyDataSetChanged();
        });

        buttonAdd.setOnClickListener(v -> {
            String name = editTextItem.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.insert(new GroceryItem(name));
                editTextItem.setText("");
            }
        });

        buttonSearch.setOnClickListener(v -> {
            List<String> ingredients = new ArrayList<>();
            for (GroceryItem item : groceryItems) {
                ingredients.add(item.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("ingredientsList", new ArrayList<>(ingredients));

            IngredientSearchFragment fragment = new IngredientSearchFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
