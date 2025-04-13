package hu.unideb.inf.smartrecipe.model;

import java.util.List;

public class RecipeDetail {
    private int id;
    private String title;
    private String image;
    private String instructions;
    private List<ExtendedIngredient> extendedIngredients;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<ExtendedIngredient> getExtendedIngredients() {
        return extendedIngredients;
    }
}
