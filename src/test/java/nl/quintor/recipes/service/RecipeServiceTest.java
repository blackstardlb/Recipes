package nl.quintor.recipes.service;

import nl.quintor.recipes.RecipesApplication;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.data.repository.CategoryRepository;
import nl.quintor.recipes.data.repository.IngredientRepository;
import nl.quintor.recipes.data.repository.RecipeRepository;
import nl.quintor.recipes.domain.models.Recipe;
import nl.quintor.recipes.domain.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static nl.quintor.recipes.TestDataDomain.*;
import static nl.quintor.recipes.TestDataEntities.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(classes = RecipesApplication.class)
class RecipeServiceTest {
    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;

    @Test
    void canGetRecipeById() {
        RecipeEntity recipeEntity = testRecipeEntity();
        recipeEntity.getCategories().add(veganCategoryEntity());
        recipeEntity.getIngredients().add(potatoIngredientEntity());

        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        Optional<Recipe> optionalRecipe = recipeService.getRecipeById(1L);
        assertThat(optionalRecipe).isPresent();
        Recipe recipe = optionalRecipe.get();
        assertThat(recipe.getName()).isEqualTo("Eggs!");
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Boil for an hour");
        assertThat(recipe.getCategories()).isNotNull();
        assertThat(recipe.getCategories()).hasSize(1);
        assertThat(recipe.getIngredients()).isNotNull();
        assertThat(recipe.getIngredients()).hasSize(1);
    }

    @Test
    void createRecipe() {
        RecipeEntity recipeEntity = testRecipeEntity();
        recipeEntity.getCategories().add(veganCategoryEntity());
        recipeEntity.getCategories().add(vegetarianCategoryEntity());
        recipeEntity.getIngredients().add(eggIngredientEntity());

        Mockito.when(recipeRepository.save(any())).thenReturn(recipeEntity);
        Mockito.when(categoryRepository.findOneByName(any())).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(any())).thenReturn(veganCategoryEntity(), vegetarianCategoryEntity());

        Mockito.when(ingredientRepository.findOneByName(any())).thenReturn(Optional.empty());
        Mockito.when(ingredientRepository.save(any())).thenReturn(eggIngredientEntity());

        Recipe recipe1 = testRecipe();
        recipe1.setId(null);
        recipe1.getCategories().add(veganCategory());
        recipe1.getCategories().add(vegetarianCategory());
        recipe1.getIngredients().add(eggIngredient());

        Recipe recipe = recipeService.createRecipe(recipe1);

        Mockito.verify(recipeRepository, Mockito.times(1)).save(any());
        Mockito.verify(categoryRepository, Mockito.times(2)).save(any());
        Mockito.verify(ingredientRepository, Mockito.times(1)).save(any());

        assertThat(recipe).isNotNull();
        assertThat(recipe.getName()).isEqualTo("Eggs!");
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Boil for an hour");
        assertThat(recipe.getCategories()).isNotNull();
        assertThat(recipe.getCategories()).hasSize(2);
        assertThat(recipe.getCategories().get(1)).isEqualTo(vegetarianCategory());
        assertThat(recipe.getIngredients()).isNotNull();
        assertThat(recipe.getIngredients()).hasSize(1);
        assertThat(recipe.getIngredients().get(0)).isEqualTo(eggIngredient());
    }
}