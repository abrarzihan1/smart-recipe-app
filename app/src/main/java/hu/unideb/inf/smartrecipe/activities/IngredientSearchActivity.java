package hu.unideb.inf.smartrecipe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.adapter.RecipeAdapter;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import hu.unideb.inf.smartrecipe.viewModel.IngredientSearchViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientSearchActivity extends AppCompatActivity {
    Button searchButton;
    RecipeApi recipeApi;
    EditText ingredientInput;
    IngredientSearchViewModel viewModel;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;

    String API_KEY = "6e46921f7d51428e912f8c4e4f7f0bb7";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_search);

        searchButton = findViewById(R.id.searchByIngredientsButton);
        ingredientInput = findViewById(R.id.ingredientInput);
        recyclerView = findViewById(R.id.recipesRecyclerView);

        recipeAdapter = new RecipeAdapter(this, new ArrayList<>(), recipe -> {
            Intent intent = new Intent(IngredientSearchActivity.this, RecipeDetailActivity.class);
            intent.putExtra("id", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("image", recipe.getImage());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeApi = retrofit.create(RecipeApi.class);

        searchButton.setOnClickListener(v -> {
            String ingredients = ingredientInput.getText().toString();
            searchByIngredients(ingredients);
        });
    }

    public void searchByIngredients(String ingredients) {
        recipeApi.searchByIngredients(ingredients, 5, API_KEY).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Code: " + response.code());
                    return;
                }

                List<Recipe> recipes = response.body();
                if (recipes != null) {
                    recipeAdapter.updateData(recipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("API_FAIL", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
