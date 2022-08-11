package nl.quintor.recipes.presentation.mapper;

import nl.quintor.recipes.domain.models.Category;
import nl.quintor.recipes.domain.models.Ingredient;
import nl.quintor.recipes.domain.models.Recipe;
import nl.quintor.recipes.presentation.dto.ListRecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTOWithId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeDTOMapper {
    RecipeDTO toDTO(Recipe recipe);

    RecipeDTOWithId toDTOWithId(Recipe recipe);

    ListRecipeDTO toListDTO(Recipe recipe);


    default String toString(Category category) {
        return category.getName();
    }

    default String toString(Ingredient ingredient) {
        return ingredient.getName();
    }

    default Ingredient toIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientName);
        return ingredient;
    }

    default Category toCategory(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }

    @Mapping(target = "id", ignore = true)
    Recipe toDomain(RecipeDTO recipeDTO);
    Recipe toDomain(RecipeDTOWithId recipeDTO);
}
