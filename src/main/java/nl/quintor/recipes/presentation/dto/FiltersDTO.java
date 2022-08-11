package nl.quintor.recipes.presentation.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FiltersDTO {
    List<FilterDTO> exclusions = new ArrayList<>();
    List<FilterDTO> inclusions = new ArrayList<>();
}