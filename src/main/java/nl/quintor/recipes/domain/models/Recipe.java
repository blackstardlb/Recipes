package nl.quintor.recipes.domain.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Recipe {
    private Long id;
    private List<Category> categories = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();
    private Integer servings;
    private String instructions;
    private String name;
}
