package nl.quintor.recipes.presentation.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeDTO {
    private List<String> categories = new ArrayList<>();
    private List<String> ingredients = new ArrayList<>();
    private Integer servings;
    private String instructions;
    private String name;
}
