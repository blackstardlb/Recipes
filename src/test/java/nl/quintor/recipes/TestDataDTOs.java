package nl.quintor.recipes;

import nl.quintor.recipes.presentation.dto.RecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTOWithId;

public interface TestDataDTOs {
    static RecipeDTO testRecipe() {
        RecipeDTO recipe = new RecipeDTO();
        recipe.setName("Eggs!");
        recipe.setServings(4);
        recipe.setInstructions("Boil for an hour");
        recipe.getCategories().add("Vegetarian");
        recipe.getCategories().add("Vegan");
        recipe.getIngredients().add("Egg");
        return recipe;
    }

    static RecipeDTOWithId testRecipeWithId() {
        RecipeDTOWithId recipe = new RecipeDTOWithId();
        recipe.setId(1L);
        recipe.setName("Eggs!");
        recipe.setServings(4);
        recipe.setInstructions("Boil for an hour");
        recipe.getCategories().add("Vegetarian");
        recipe.getCategories().add("Vegan");
        recipe.getIngredients().add("Egg");
        return recipe;
    }
}
