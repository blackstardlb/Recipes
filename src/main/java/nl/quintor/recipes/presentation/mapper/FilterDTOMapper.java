package nl.quintor.recipes.presentation.mapper;

import nl.quintor.recipes.domain.models.Filter;
import nl.quintor.recipes.presentation.dto.FilterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FilterDTOMapper {
    Filter toDomain(FilterDTO filterDTO);
}
