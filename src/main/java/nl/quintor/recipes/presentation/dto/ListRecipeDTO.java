package nl.quintor.recipes.presentation.dto;

import lombok.Data;

@Data
public class ListRecipeDTO {
    private Long id;
    private Integer servings;
    private String name;
}
