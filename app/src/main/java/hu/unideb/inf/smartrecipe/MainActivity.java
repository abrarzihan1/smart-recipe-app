package hu.unideb.inf.smartrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.unideb.inf.smartrecipe.activities.FavoritesActivity;
import hu.unideb.inf.smartrecipe.activities.IngredientSearchActivity;
import hu.unideb.inf.smartrecipe.activities.RecipeDetailActivity;
import hu.unideb.inf.smartrecipe.adapter.FavoriteRecipeAdapter;
import hu.unideb.inf.smartrecipe.adapter.RecipeAdapter;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText ingredientInput;
    Button searchButton;
    Button favoritesButton;
    Button openIngredientsButton;
    RecipeApi recipeApi;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    String apiKey = "6e46921f7d51428e912f8c4e4f7f0bb7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ingredientInput = findViewById(R.id.ingredientInput);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        openIngredientsButton = findViewById(R.id.openIngredientsButton);

        recyclerView = findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(this, new ArrayList<>(), recipe -> {
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            intent.putExtra("id", recipe.getId());
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("image", recipe.getImage());
            startActivity(intent);
        });
        recyclerView.setAdapter(recipeAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeApi = retrofit.create(RecipeApi.class);

        searchButton.setOnClickListener(v -> {
            String ingredients = ingredientInput.getText().toString();
            fetchRecipes(ingredients);
        });

        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        openIngredientsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IngredientSearchActivity.class);
            startActivity(intent);
        });
    }

    private void fetchRecipes(String ingredients) {
        Call<List<Recipe>> call = recipeApi.getRecipes(ingredients, 5, apiKey);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
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
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e("API_FAIL", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}