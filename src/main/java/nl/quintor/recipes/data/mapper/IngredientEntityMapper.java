package nl.quintor.recipes.data.mapper;

import nl.quintor.recipes.data.entity.IngredientEntity;
import nl.quintor.recipes.domain.models.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientEntityMapper {
    Ingredient toDomain(IngredientEntity ingredientEntity);

    IngredientEntity toEntity(Ingredient ingredient);
}
