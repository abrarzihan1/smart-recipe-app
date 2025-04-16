package hu.unideb.inf.smartrecipe.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import hu.unideb.inf.smartrecipe.api.RecipeApi;
import hu.unideb.inf.smartrecipe.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientSearchViewModel extends ViewModel {
    private MutableLiveData<List<Recipe>> recipesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private RecipeApi api;

    public LiveData<List<Recipe>> getRecipesLiveData() { return recipesLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    String apiKey = "6e46921f7d51428e912f8c4e4f7f0bb7";

    public void searchByIngredients(String ingredients) {
        api.searchByIngredients(ingredients, 5, apiKey)
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        if (response.isSuccessful()) {
                            recipesLiveData.postValue(response.body());
                        } else {
                            errorLiveData.postValue("Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        errorLiveData.postValue("Network error: " + t.getMessage());
                    }
                });
    }
}

