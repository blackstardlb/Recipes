package nl.quintor.recipes.data.mapper;

import nl.quintor.recipes.data.entity.CategoryEntity;
import nl.quintor.recipes.domain.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {
    Category toDomain(CategoryEntity categoryEntity);
    CategoryEntity toEntity(Category category);
}
