package hu.unideb.inf.smartrecipe.api;

import java.util.List;

import hu.unideb.inf.smartrecipe.model.Recipe;
import hu.unideb.inf.smartrecipe.model.RecipeDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("recipes/findByIngredients")
    Call<List<Recipe>> getRecipes(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeInformation(
//            @Query("instructions") String instructions,
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/findByIngredients")
    Call<List<Recipe>> searchByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );
}
