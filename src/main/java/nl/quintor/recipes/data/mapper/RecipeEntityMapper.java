package nl.quintor.recipes.data.mapper;

import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.domain.models.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryEntityMapper.class, IngredientEntityMapper.class})
public interface RecipeEntityMapper {
    Recipe toDomain(RecipeEntity recipeEntity);
    RecipeEntity toEntity(Recipe recipe);
}
