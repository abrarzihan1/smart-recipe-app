package hu.unideb.inf.smartrecipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.adapter.RecipeAdapter;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientSearchActivity extends AppCompatActivity {
    Button searchButton;
    Button addIngredientButton;
    RecipeApi recipeApi;
    EditText ingredientInput;
    ChipGroup ingredientChipGroup;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    TextView recipesTitle;
    TextView emptyStateTextView;
    List<String> ingredients = new ArrayList<>();
    String API_KEY = "6e46921f7d51428e912f8c4e4f7f0bb7";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredient_search);

        // Find views
        searchButton = findViewById(R.id.searchByIngredientsButton);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        ingredientInput = findViewById(R.id.ingredientInput);
        ingredientChipGroup = findViewById(R.id.ingredientChipGroup);
        recyclerView = findViewById(R.id.recipesRecyclerView);
        recipesTitle = findViewById(R.id.recipesTitle);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);

        // Set up RecyclerView
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>(), recipe -> {
            Intent intent = new Intent(IngredientSearchActivity.this, RecipeDetailActivity.class);
            intent.putExtra("id", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("image", recipe.getImage());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

        // Set up API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recipeApi = retrofit.create(RecipeApi.class);

        // Add ingredient on keyboard done action
        ingredientInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addIngredient();
                return true;
            }
            return false;
        });

        // Add ingredient button click
        addIngredientButton.setOnClickListener(v -> addIngredient());

        // Search button click
        searchButton.setOnClickListener(v -> {
            if (ingredients.isEmpty()) {
                Toast.makeText(this, "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
                return;
            }
            String ingredientString = TextUtils.join(",", ingredients);
            searchByIngredients(ingredientString);
        });
    }

    private void addIngredient() {
        String ingredient = ingredientInput.getText().toString().trim();
        if (!ingredient.isEmpty() && !ingredients.contains(ingredient)) {
            ingredients.add(ingredient);
            addChip(ingredient);
            ingredientInput.setText("");

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ingredientInput.getWindowToken(), 0);
        }
    }

    private void addChip(final String ingredient) {
        Chip chip = new Chip(this);
        chip.setText(ingredient);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);

        // Use MaterialComponents theme for proper styling
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
                this,
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

    public void searchByIngredients(String ingredients) {
        // Show loading state
        emptyStateTextView.setText("Searching for recipes...");
        emptyStateTextView.setVisibility(View.VISIBLE);
        recipesTitle.setVisibility(View.GONE);

        recipeApi.searchByIngredients(ingredients, 10, API_KEY).enqueue(new Callback<List<Recipe>>() {
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
