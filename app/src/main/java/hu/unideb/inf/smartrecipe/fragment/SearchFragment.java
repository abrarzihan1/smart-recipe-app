package hu.unideb.inf.smartrecipe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.activities.FavoritesActivity;
import hu.unideb.inf.smartrecipe.activities.IngredientSearchActivity;
import hu.unideb.inf.smartrecipe.activities.RecipeDetailActivity;
import hu.unideb.inf.smartrecipe.adapter.RecipeAdapter;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    EditText ingredientInput;
    Button searchButton;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    RecipeApi recipeApi;

    String apiKey = "6e46921f7d51428e912f8c4e4f7f0bb7"; // Replace with your real API key

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ingredientInput = view.findViewById(R.id.ingredientInput);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.recipeRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeAdapter(getContext(), new ArrayList<>(), recipe -> {
            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
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

        return view;
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
