package nl.quintor.recipes.data.repository;

import nl.quintor.recipes.RecipesApplication;
import nl.quintor.recipes.data.RecipeSpecification;
import nl.quintor.recipes.data.entity.CategoryEntity;
import nl.quintor.recipes.data.entity.IngredientEntity;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.domain.models.Filter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static nl.quintor.recipes.TestDataEntities.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RecipesApplication.class)
@Transactional
class RecipeSpecificationITTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void canInFilterByServings() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        testRecipeEntity.setServings(2);
        recipeRepository.save(testRecipeEntity);

        Filter filter = new Filter();
        filter.setProperty("servings");
        filter.setValue("4");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(filter), List.of());
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getServings()).isEqualTo(4);
    }

    @Test
    void canExFilterByServings() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        testRecipeEntity.setServings(2);
        recipeRepository.save(testRecipeEntity);

        Filter filter = new Filter();
        filter.setProperty("servings");
        filter.setValue("4");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(), List.of(filter));
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getServings()).isEqualTo(2);
    }

    @Test
    void canFilterByInstructions() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setId(null);
        testRecipeEntity.setInstructions("Bake in the oven for 2 hours.");
        recipeRepository.save(testRecipeEntity);

        Filter filter = new Filter();
        filter.setProperty("instructions");
        filter.setValue("oven");
        filter.setExact(false);
        RecipeSpecification spec = new RecipeSpecification(List.of(filter), List.of());
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getInstructions()).isEqualTo("Bake in the oven for 2 hours.");
    }

    @Test
    void canInFilterByCategory() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setName("Vegan rec");
        CategoryEntity vegetarian = categoryRepository.save(vegetarianCategoryEntity());
        CategoryEntity vegan = categoryRepository.save(veganCategoryEntity());
        IngredientEntity egg = ingredientRepository.save(eggIngredientEntity());

        testRecipeEntity.getCategories().add(vegetarian);
        testRecipeEntity.getCategories().add(vegan);
        testRecipeEntity.getIngredients().add(egg);
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        RecipeEntity testRecipeEntity2 = testRecipeEntity();
        testRecipeEntity2.setName("Non vegan rec");
        testRecipeEntity2.getCategories().add(vegetarian);
        testRecipeEntity2.getIngredients().add(egg);
        testRecipeEntity2.setId(null);
        recipeRepository.save(testRecipeEntity2);

        Filter filter = new Filter();
        filter.setProperty("categories");
        filter.setValue("Vegan");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(filter), List.of());
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo("Vegan rec");
    }

    @Test
    void canExFilterByCategory() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setName("Vegan rec");
        CategoryEntity vegetarian = categoryRepository.save(vegetarianCategoryEntity());
        CategoryEntity vegan = categoryRepository.save(veganCategoryEntity());
        IngredientEntity egg = ingredientRepository.save(eggIngredientEntity());

        testRecipeEntity.getCategories().add(vegetarian);
        testRecipeEntity.getCategories().add(vegan);
        testRecipeEntity.getIngredients().add(egg);
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        RecipeEntity testRecipeEntity2 = testRecipeEntity();
        testRecipeEntity2.setName("Non vegan rec");
        testRecipeEntity2.getCategories().add(vegetarian);
        testRecipeEntity2.getIngredients().add(egg);
        testRecipeEntity2.setId(null);
        recipeRepository.save(testRecipeEntity2);

        Filter filter = new Filter();
        filter.setProperty("categories");
        filter.setValue("Vegan");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(), List.of(filter));
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo("Non vegan rec");
    }

    @Test
    void canInFilterByIngredient() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setName("Potato rec");
        CategoryEntity vegetarian = categoryRepository.save(vegetarianCategoryEntity());
        CategoryEntity vegan = categoryRepository.save(veganCategoryEntity());
        IngredientEntity egg = ingredientRepository.save(eggIngredientEntity());
        IngredientEntity potato = ingredientRepository.save(potatoIngredientEntity());
        IngredientEntity salmon = ingredientRepository.save(salmonIngredientEntity());

        testRecipeEntity.getCategories().add(vegetarian);
        testRecipeEntity.getCategories().add(vegan);
        testRecipeEntity.getIngredients().add(egg);
        testRecipeEntity.getIngredients().add(potato);
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        RecipeEntity testRecipeEntity2 = testRecipeEntity();
        testRecipeEntity2.setName("Salmon rec");
        testRecipeEntity2.getCategories().add(vegetarian);
        testRecipeEntity2.getIngredients().add(salmon);
        testRecipeEntity2.getIngredients().add(potato);
        testRecipeEntity2.setId(null);
        recipeRepository.save(testRecipeEntity2);

        Filter filter = new Filter();
        filter.setProperty("ingredients");
        filter.setValue("Salmon");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(filter), List.of());
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo("Salmon rec");
    }

    @Test
    void canExFilterByIngredient() {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.setName("Potato rec");
        CategoryEntity vegetarian = categoryRepository.save(vegetarianCategoryEntity());
        CategoryEntity vegan = categoryRepository.save(veganCategoryEntity());
        IngredientEntity egg = ingredientRepository.save(eggIngredientEntity());
        IngredientEntity potato = ingredientRepository.save(potatoIngredientEntity());
        IngredientEntity salmon = ingredientRepository.save(salmonIngredientEntity());

        testRecipeEntity.getCategories().add(vegetarian);
        testRecipeEntity.getCategories().add(vegan);
        testRecipeEntity.getIngredients().add(egg);
        testRecipeEntity.getIngredients().add(potato);
        testRecipeEntity.setId(null);
        recipeRepository.save(testRecipeEntity);

        RecipeEntity testRecipeEntity2 = testRecipeEntity();
        testRecipeEntity2.setName("Salmon rec");
        testRecipeEntity2.getCategories().add(vegetarian);
        testRecipeEntity2.getIngredients().add(salmon);
        testRecipeEntity2.getIngredients().add(potato);
        testRecipeEntity2.setId(null);
        recipeRepository.save(testRecipeEntity2);

        Filter filter = new Filter();
        filter.setProperty("ingredients");
        filter.setValue("Salmon");
        filter.setExact(true);
        RecipeSpecification spec = new RecipeSpecification(List.of(), List.of(filter));
        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo("Potato rec");
    }
}