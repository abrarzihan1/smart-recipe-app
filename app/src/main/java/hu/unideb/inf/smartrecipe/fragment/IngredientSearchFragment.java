package hu.unideb.inf.smartrecipe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.unideb.inf.smartrecipe.BuildConfig;
import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.activities.RecipeDetailActivity;
import hu.unideb.inf.smartrecipe.adapter.RecipeAdapter;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientSearchFragment extends Fragment {

    Button searchButton, addIngredientButton;
    EditText ingredientInput;
    ChipGroup ingredientChipGroup;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    TextView recipesTitle, emptyStateTextView;
    List<String> ingredients = new ArrayList<>();
    RecipeApi recipeApi;
    private List<String> ingredientsList;

    String API_KEY = BuildConfig.API_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredient_search, container, false);

        // Initialize views
        searchButton = view.findViewById(R.id.searchByIngredientsButton);
        addIngredientButton = view.findViewById(R.id.addIngredientButton);
        ingredientInput = view.findViewById(R.id.ingredientInput);
        ingredientChipGroup = view.findViewById(R.id.ingredientChipGroup);
        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        recipesTitle = view.findViewById(R.id.recipesTitle);
        emptyStateTextView = view.findViewById(R.id.emptyStateTextView);

        // Set up RecyclerView
        recipeAdapter = new RecipeAdapter(getContext(), new ArrayList<>(), recipe -> {
            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
            intent.putExtra("id", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("image", recipe.getImage());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recipeAdapter);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recipeApi = retrofit.create(RecipeApi.class);

        // Add ingredient via keyboard action
        ingredientInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                addIngredient();
                return true;
            }
            return false;
        });

        // Button listeners
        addIngredientButton.setOnClickListener(v -> addIngredient());
        searchButton.setOnClickListener(v -> {
            if (ingredients.isEmpty()) {
                Toast.makeText(getContext(), "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
                return;
            }
            String ingredientString = TextUtils.join(",", ingredients);
            searchByIngredients(ingredientString);
        });

        // Get ingredients from GroceryFragment
        Bundle args = getArguments();
        if (args != null) {
            ingredientsList = args.getStringArrayList("ingredientsList");
        }

        // Example: use it in an API request
        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            String ingredientsParam = TextUtils.join(",", ingredientsList); // comma-separated
            searchByIngredients(ingredientsParam);
        }

        return view;
    }


    private void addIngredient() {
        String ingredient = ingredientInput.getText().toString().trim();
        if (!ingredient.isEmpty() && !ingredients.contains(ingredient)) {
            ingredients.add(ingredient);
            addChip(ingredient);
            ingredientInput.setText("");

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(ingredientInput.getWindowToken(), 0);
            }
        }
    }

    private void addChip(final String ingredient) {
        Chip chip = new Chip(getContext());
        chip.setText(ingredient);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);

        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry);
        chip.setChipDrawable(chipDrawable);

        chip.setOnCloseIconClickListener(v -> {
            ingredientChipGroup.removeView(chip);
            ingredients.remove(ingredient);
        });

        ingredientChipGroup.addView(chip);
    }

    private void searchByIngredients(String ingredientsStr) {
        emptyStateTextView.setText("Searching for recipes...");
        emptyStateTextView.setVisibility(View.VISIBLE);
        recipesTitle.setVisibility(View.GONE);

        recipeApi.searchByIngredients(ingredientsStr, 10, API_KEY).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Code: " + response.code());
                    emptyStateTextView.setText("Error loading recipes. Please try again.");
                    return;
                }

                List<Recipe> recipes = response.body();
                if (recipes != null && !recipes.isEmpty()) {
                    recipeAdapter.updateData(recipes);
                    recipesTitle.setVisibility(View.VISIBLE);
                    emptyStateTextView.setVisibility(View.GONE);
                } else {
                    emptyStateTextView.setText("No recipes found with these ingredients");
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    recipesTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("API_FAIL", Objects.requireNonNull(t.getMessage()));
                emptyStateTextView.setText("Network error. Please check your connection.");
                emptyStateTextView.setVisibility(View.VISIBLE);
                recipesTitle.setVisibility(View.GONE);
            }
        });
    }
}
