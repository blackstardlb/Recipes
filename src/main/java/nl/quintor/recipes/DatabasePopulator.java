package nl.quintor.recipes;

import nl.quintor.recipes.data.entity.CategoryEntity;
import nl.quintor.recipes.data.entity.IngredientEntity;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.data.repository.CategoryRepository;
import nl.quintor.recipes.data.repository.IngredientRepository;
import nl.quintor.recipes.data.repository.RecipeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

public class DatabasePopulator {
    public void populate(ApplicationContext context) {
        RecipeRepository recipeRepository = context.getBean(RecipeRepository.class);
        IngredientRepository ingredientRepository = context.getBean(IngredientRepository.class);
        CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);

        categoryRepository.save(veganCategoryEntity());
        categoryRepository.save(vegetarianCategoryEntity());
        categoryRepository.save(ketoCategoryEntity());
        categoryRepository.save(pescatarianCategoryEntity());
        categoryRepository.flush();

        ingredientRepository.save(eggIngredientEntity());
        ingredientRepository.save(spaghettiIngredientEntity());
        ingredientRepository.save(salmonIngredientEntity());
        ingredientRepository.save(potatoIngredientEntity());
        ingredientRepository.flush();

        List.of(vegetarianRecipe1(), vegetarianRecipe2(), salmonRecipe(), veganRecipe())
                .forEach(recipeEntity -> {
                    recipeEntity.setIngredients(
                            recipeEntity.getIngredients()
                                    .stream()
                                    .map(ingredientEntity -> ingredientRepository.findOneByName(ingredientEntity.getName()).orElseThrow(() -> new IllegalArgumentException(ingredientEntity.getName())))
                                    .collect(Collectors.toList())
                    );
                    recipeEntity.setCategories(
                            recipeEntity.getCategories()
                                    .stream()
                                    .map(ingredientEntity -> categoryRepository.findOneByName(ingredientEntity.getName()).orElseThrow(() -> new IllegalArgumentException(ingredientEntity.getName())))
                                    .collect(Collectors.toList())
                    );
                    recipeRepository.save(recipeEntity);
                });
        recipeRepository.flush();
    }
    static CategoryEntity veganCategoryEntity() {
        CategoryEntity vegan = new CategoryEntity();
        vegan.setName("Vegan");
        return vegan;
    }

    static CategoryEntity vegetarianCategoryEntity() {
        CategoryEntity vegetarian = new CategoryEntity();
        vegetarian.setName("Vegetarian");
        return vegetarian;
    }

    static CategoryEntity ketoCategoryEntity() {
        CategoryEntity keto = new CategoryEntity();
        keto.setName("Keto");
        return keto;
    }

    static CategoryEntity pescatarianCategoryEntity() {
        CategoryEntity pescatarian = new CategoryEntity();
        pescatarian.setName("Pescatarian");
        return pescatarian;
    }


    static IngredientEntity potatoIngredientEntity() {
        IngredientEntity potato = new IngredientEntity();
        potato.setName("Potato");
        return potato;
    }

    static IngredientEntity eggIngredientEntity() {
        IngredientEntity egg = new IngredientEntity();
        egg.setName("Egg");
        return egg;
    }

    static IngredientEntity salmonIngredientEntity() {
        IngredientEntity salmon = new IngredientEntity();
        salmon.setName("Salmon");
        return salmon;
    }

    static IngredientEntity spaghettiIngredientEntity() {
        IngredientEntity spaghetti = new IngredientEntity();
        spaghetti.setName("Spaghetti");
        return spaghetti;
    }

    static RecipeEntity vegetarianRecipe1() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("VegetarianRecipe1!");
        recipe.setServings(2);
        recipe.setInstructions("Do some magic with spaghetti in a pot.");
        recipe.getCategories().add(vegetarianCategoryEntity());
        recipe.getIngredients().add(spaghettiIngredientEntity());
        return recipe;
    }

    static RecipeEntity vegetarianRecipe2() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("VegetarianRecipe2!");
        recipe.setServings(4);
        recipe.setInstructions("Do some magic with potatoes in the oven.");
        recipe.getCategories().add(vegetarianCategoryEntity());
        recipe.getIngredients().add(potatoIngredientEntity());
        return recipe;
    }

    static RecipeEntity veganRecipe() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("Vegan Recipe");
        recipe.setServings(5);
        recipe.setInstructions("Do some magic with potatoes on the stove like a vegan.");
        recipe.getCategories().add(vegetarianCategoryEntity());
        recipe.getCategories().add(veganCategoryEntity());
        recipe.getIngredients().add(potatoIngredientEntity());
        return recipe;
    }

    static RecipeEntity salmonRecipe() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("SalmonRecipe!");
        recipe.setServings(1);
        recipe.setInstructions("Put the salmon in the oven");
        recipe.getCategories().add(pescatarianCategoryEntity());
        recipe.getIngredients().add(potatoIngredientEntity());
        recipe.getIngredients().add(salmonIngredientEntity());
        return recipe;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RecipesApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context = application.run(args);
        new DatabasePopulator().populate(context);
    }
}
