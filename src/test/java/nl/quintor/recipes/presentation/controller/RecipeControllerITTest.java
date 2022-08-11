package nl.quintor.recipes.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.recipes.RecipesApplication;
import nl.quintor.recipes.Utils;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.data.repository.CategoryRepository;
import nl.quintor.recipes.data.repository.IngredientRepository;
import nl.quintor.recipes.data.repository.RecipeRepository;
import nl.quintor.recipes.presentation.dto.RecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTOWithId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static nl.quintor.recipes.TestDataDTOs.testRecipe;
import static nl.quintor.recipes.TestDataDTOs.testRecipeWithId;
import static nl.quintor.recipes.TestDataEntities.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RecipesApplication.class)
@AutoConfigureMockMvc
@Transactional
class RecipeControllerITTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void canGetRecipe() throws Exception {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.getCategories().add(categoryRepository.save(vegetarianCategoryEntity()));
        testRecipeEntity.getCategories().add(categoryRepository.save(veganCategoryEntity()));
        testRecipeEntity.getIngredients().add(ingredientRepository.save(eggIngredientEntity()));
        RecipeEntity recipe = recipeRepository.saveAndFlush(testRecipeEntity);

        mockMvc.perform(get("/recipes/" + recipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.getId()))
                .andExpect(jsonPath("$.name").value("Eggs!"))
                .andExpect(jsonPath("$.instructions").value("Boil for an hour"))
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories.length()").value(2))
                .andExpect(jsonPath("$.categories[0]").value("Vegetarian"))
                .andExpect(jsonPath("$.categories[1]").value("Vegan"))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients.length()").value(1))
                .andExpect(jsonPath("$.ingredients[0]").value("Egg"));
    }

    @Test
    void throws404OnRecipeThatDoesntExist() throws Exception {
        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void canCreateRecipe() throws Exception {
        int initialSize = recipeRepository.findAll().size();
        String jsonString = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(testRecipe());
        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andDo(log())
                .andExpect(status().isCreated());

        List<RecipeEntity> results = recipeRepository.findAll();
        assertThat(results.size()).isEqualTo(initialSize + 1);
        Optional<RecipeEntity> recipeOptional = Utils.getLast(results);
        assertThat(recipeOptional).isPresent();
        RecipeEntity recipe = recipeOptional.get();
        assertThat(recipe.getName()).isEqualTo("Eggs!");
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Boil for an hour");
        assertThat(recipe.getCategories()).isNotNull();
        assertThat(recipe.getCategories()).hasSize(2);
        assertThat(recipe.getCategories().get(1).getName()).isEqualTo("Vegan");
        assertThat(recipe.getIngredients()).isNotNull();
        assertThat(recipe.getIngredients()).hasSize(1);
        assertThat(recipe.getIngredients().get(0).getName()).isEqualTo("Egg");
    }

    @Test
    void canCreateRecipeWithExistingCategoriesAndIngredients() throws Exception {
        categoryRepository.save(veganCategoryEntity());
        categoryRepository.save(vegetarianCategoryEntity());
        ingredientRepository.save(eggIngredientEntity());

        int initialSizeR = recipeRepository.findAll().size();
        int initialSizeC = categoryRepository.findAll().size();
        int initialSizeI = ingredientRepository.findAll().size();

        String jsonString = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(testRecipe());
        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isCreated());

        assertThat(recipeRepository.findAll().size()).isEqualTo(initialSizeR + 1);
        assertThat(categoryRepository.findAll().size()).isEqualTo(initialSizeC);
        assertThat(ingredientRepository.findAll().size()).isEqualTo(initialSizeI);

        List<RecipeEntity> results = recipeRepository.findAll();
        assertThat(results.size()).isEqualTo(initialSizeR + 1);
        Optional<RecipeEntity> recipeOptional = Utils.getLast(results);
        assertThat(recipeOptional).isPresent();
        RecipeEntity recipe = recipeOptional.get();
        assertThat(recipe.getName()).isEqualTo("Eggs!");
        assertThat(recipe.getServings()).isEqualTo(4);
        assertThat(recipe.getInstructions()).isEqualTo("Boil for an hour");
        assertThat(recipe.getCategories()).isNotNull();
        assertThat(recipe.getCategories()).hasSize(2);
        assertThat(recipe.getCategories().get(1).getName()).isEqualTo("Vegan");
        assertThat(recipe.getIngredients()).isNotNull();
        assertThat(recipe.getIngredients()).hasSize(1);
        assertThat(recipe.getIngredients().get(0).getName()).isEqualTo("Egg");
    }

    @Test
    void canUpdateRecipe() throws Exception {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.getCategories().add(categoryRepository.save(vegetarianCategoryEntity()));
        testRecipeEntity.getCategories().add(categoryRepository.save(veganCategoryEntity()));
        testRecipeEntity.getIngredients().add(ingredientRepository.save(eggIngredientEntity()));
        RecipeEntity firstRecipe = recipeRepository.saveAndFlush(testRecipeEntity);

        int initialSizeR = recipeRepository.findAll().size();
        int initialSizeC = categoryRepository.findAll().size();
        int initialSizeI = ingredientRepository.findAll().size();

        RecipeDTO testRecipe = testRecipe();
        testRecipe.setName("Name1");
        testRecipe.setServings(1);
        testRecipe.setInstructions("HELLO WORLD!");
        testRecipe.getCategories().remove(0);
        testRecipe.getIngredients().add(salmonIngredientEntity().getName());

        String jsonString = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(testRecipe);
        mockMvc.perform(put("/recipes/" + firstRecipe.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isAccepted());

        assertThat(recipeRepository.findAll().size()).isEqualTo(initialSizeR);
        assertThat(categoryRepository.findAll().size()).isEqualTo(initialSizeC);
        assertThat(ingredientRepository.findAll().size()).isEqualTo(initialSizeI + 1);

        Optional<RecipeEntity> recipeOptional = recipeRepository.findById(firstRecipe.getId());
        assertThat(recipeOptional).isPresent();
        RecipeEntity recipe = recipeOptional.get();
        assertThat(recipe.getName()).isEqualTo("Name1");
        assertThat(recipe.getServings()).isEqualTo(1);
        assertThat(recipe.getInstructions()).isEqualTo("HELLO WORLD!");
        assertThat(recipe.getCategories()).isNotNull();
        assertThat(recipe.getCategories()).hasSize(1);
        assertThat(recipe.getCategories().get(0).getName()).isEqualTo("Vegan");
        assertThat(recipe.getIngredients()).isNotNull();
        assertThat(recipe.getIngredients()).hasSize(2);
        assertThat(recipe.getIngredients().get(1).getName()).isEqualTo("Salmon");
    }

    @Test
    void canDeleteRecipe() throws Exception {
        RecipeEntity testRecipeEntity = testRecipeEntity();
        testRecipeEntity.getCategories().add(categoryRepository.save(vegetarianCategoryEntity()));
        testRecipeEntity.getCategories().add(categoryRepository.save(veganCategoryEntity()));
        testRecipeEntity.getIngredients().add(ingredientRepository.save(eggIngredientEntity()));
        RecipeEntity recipe = recipeRepository.saveAndFlush(testRecipeEntity);

        mockMvc.perform(delete("/recipes/" + recipe.getId()))
                .andExpect(status().isOk());

        Optional<RecipeEntity> recipeOptional = recipeRepository.findById(recipe.getId());
        assertThat(recipeOptional).isNotPresent();
    }
}