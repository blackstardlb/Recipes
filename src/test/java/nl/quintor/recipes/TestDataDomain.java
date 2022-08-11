package nl.quintor.recipes;

import nl.quintor.recipes.domain.models.Category;
import nl.quintor.recipes.domain.models.Ingredient;
import nl.quintor.recipes.domain.models.Recipe;

public interface TestDataDomain {
    static Recipe testRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Eggs!");
        recipe.setServings(4);
        recipe.setInstructions("Boil for an hour");
        return recipe;
    }

    static Category veganCategory() {
        Category vegan = new Category();
        vegan.setName("Vegan");
        return vegan;
    }

    static Category vegetarianCategory() {
        Category vegetarian = new Category();
        vegetarian.setName("Vegetarian");
        return vegetarian;
    }

    static Ingredient potatoIngredient() {
        Ingredient potato = new Ingredient();
        potato.setName("Potato");
        return potato;
    }

    static Ingredient eggIngredient() {
        Ingredient egg = new Ingredient();
        egg.setName("Egg");
        return egg;
    }

    static Ingredient salmonIngredient() {
        Ingredient salmon = new Ingredient();
        salmon.setName("Potato");
        return salmon;
    }
}
