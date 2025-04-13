package hu.unideb.inf.smartrecipe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Objects;

import hu.unideb.inf.smartrecipe.R;
import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.database.RecipeDatabase;
import hu.unideb.inf.smartrecipe.model.FavoriteRecipe;
import hu.unideb.inf.smartrecipe.model.RecipeDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView recipeImage;
    TextView recipeTitle, recipeInstructions, recipeIngredients;
    private Button saveFavoriteBtn;
    private RecipeDatabase recipeDatabase;
    RecipeApi recipeApi;
    String apiKey = "6e46921f7d51428e912f8c4e4f7f0bb7";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeImage = findViewById(R.id.detailImage);
        recipeTitle = findViewById(R.id.detailTitle);
        recipeInstructions = findViewById(R.id.detailInstructions);
        recipeIngredients = findViewById(R.id.detailIngredients);
        saveFavoriteBtn = findViewById(R.id.saveFavoriteBtn);

        recipeDatabase = RecipeDatabase.getInstance(this);

        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("image");

        recipeTitle.setText(title);
        Glide.with(this)
                .load(imageUrl)
                .into(recipeImage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        saveFavoriteBtn.setOnClickListener(v -> {
            FavoriteRecipe favorite = new FavoriteRecipe(recipeId, title, imageUrl);

            FavoriteRecipe existing = recipeDatabase.recipeDao().getById(recipeId);
            if (existing == null) {
                recipeDatabase.recipeDao().insert(favorite);
                Toast.makeText(this, "Saved to favorites!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already in favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        recipeApi = retrofit.create(RecipeApi.class);
        fetchRecipeDetails(recipeId);
    }

    private void fetchRecipeDetails(int recipeId) {
        Call<RecipeDetail> call = recipeApi.getRecipeInformation(recipeId, apiKey);
        call.enqueue(new Callback<RecipeDetail>() {
            @Override
            public void onResponse(Call<RecipeDetail> call, Response<RecipeDetail> response) {
                if (!response.isSuccessful()) {
                    Log.e("RecipeDetail", "Code: " + response.code());
                    return;
                }

                RecipeDetail detail = response.body();
                if (detail != null) {
                    if (detail.getInstructions() != null) {
                        recipeInstructions.setText(detail.getInstructions());
                    } else {
                        recipeInstructions.setText("No instructions available.");
                    }
//                    recipeInstructions.setText(detail.getInstructions() != null
//                            ? detail.getInstructions() : "No instructions available.");

                    StringBuilder ingredientsBuilder = new StringBuilder();
                    if (detail.getExtendedIngredients() != null && !detail.getExtendedIngredients().isEmpty()) {
                        detail.getExtendedIngredients().forEach(ingredient -> ingredientsBuilder.append("- ").append(ingredient.getOriginal()).append("\n"));
                    } else {
                        ingredientsBuilder.append("No ingredients info available.");
                    }

                    recipeIngredients.setText(ingredientsBuilder.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeDetail> call, Throwable t) {
                Log.e("RecipeDetail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}
