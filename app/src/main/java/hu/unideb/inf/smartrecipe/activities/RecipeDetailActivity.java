package hu.unideb.inf.smartrecipe.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    TextView recipeTitle, recipeIngredients;
    WebView instructionsWebView;
    private Button saveFavoriteBtn;
    private RecipeDatabase recipeDatabase;
    RecipeApi recipeApi;
    String apiKey = "6e46921f7d51428e912f8c4e4f7f0bb7";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Set up the toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize views
        recipeImage = findViewById(R.id.detailImage);
        recipeTitle = findViewById(R.id.detailTitle);
        recipeIngredients = findViewById(R.id.detailIngredients);
        instructionsWebView = findViewById(R.id.instructionsWebView);
        saveFavoriteBtn = findViewById(R.id.saveFavoriteBtn);

        // Configure WebView for instructions
        instructionsWebView.getSettings().setJavaScriptEnabled(false);
        instructionsWebView.setBackgroundColor(Color.TRANSPARENT);

        // Initialize database
        recipeDatabase = RecipeDatabase.getInstance(this);

        // Get intent data
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("image");

        // Set title and image
        recipeTitle.setText(title);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(recipeImage);

        // Handle navigation back
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Set up favorite button click listener
        saveFavoriteBtn.setOnClickListener(v -> {
            FavoriteRecipe favorite = new FavoriteRecipe(recipeId, title, imageUrl);

            FavoriteRecipe existing = recipeDatabase.recipeDao().getById(recipeId);
            if (existing == null) {
                recipeDatabase.recipeDao().insert(favorite);
                Toast.makeText(this, "Saved to favorites!", Toast.LENGTH_SHORT).show();
                saveFavoriteBtn.setText("Saved to Favorites");
                saveFavoriteBtn.setEnabled(false);
            } else {
                Toast.makeText(this, "Already in favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if recipe is already in favorites
        FavoriteRecipe existing = recipeDatabase.recipeDao().getById(recipeId);
        if (existing != null) {
            saveFavoriteBtn.setText("Saved to Favorites");
            saveFavoriteBtn.setEnabled(false);
        }

        // Set up API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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
                    // Handle instructions - convert to HTML and display in WebView
                    String instructions = detail.getInstructions();
                    if (instructions != null && !instructions.isEmpty()) {
                        String htmlData = "<html><body style='text-align:left; color:#333333; padding:0px;'>"
                                + instructions
                                + "</body></html>";
                        instructionsWebView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
                    } else {
                        instructionsWebView.loadDataWithBaseURL(null, "<html><body>No instructions available.</body></html>", "text/html", "UTF-8", null);
                    }

                    // Handle ingredients
                    StringBuilder ingredientsBuilder = new StringBuilder();
                    if (detail.getExtendedIngredients() != null && !detail.getExtendedIngredients().isEmpty()) {
                        for (int i = 0; i < detail.getExtendedIngredients().size(); i++) {
                            ingredientsBuilder.append("• ").append(detail.getExtendedIngredients().get(i).getOriginal());
                            if (i < detail.getExtendedIngredients().size() - 1) {
                                ingredientsBuilder.append("\n\n");
                            }
                        }
                    } else {
                        ingredientsBuilder.append("No ingredients information available.");
                    }

                    recipeIngredients.setText(ingredientsBuilder.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeDetail> call, Throwable t) {
                Log.e("RecipeDetail", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(RecipeDetailActivity.this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
