package nl.quintor.recipes.domain.service;

import nl.quintor.recipes.domain.models.Filter;
import nl.quintor.recipes.domain.models.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<Recipe> getRecipeById(Long id);

    Recipe createRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe);

    void deleteRecipe(Long id);

    List<Recipe> getRecipesByFilters(List<Filter> inclusions, List<Filter> exclusions);
}
