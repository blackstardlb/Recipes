package nl.quintor.recipes.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.recipes.RecipesApplication;
import nl.quintor.recipes.data.repository.CategoryRepository;
import nl.quintor.recipes.data.repository.IngredientRepository;
import nl.quintor.recipes.data.repository.RecipeRepository;
import nl.quintor.recipes.presentation.dto.FilterDTO;
import nl.quintor.recipes.presentation.dto.FiltersDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static nl.quintor.recipes.TestDataEntities.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RecipesApplication.class)
@AutoConfigureMockMvc
@Transactional
class RecipeControllerListITTest {
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

    @BeforeEach
    public void setup() {
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

    @Test
    public void canGetRecipes() throws Exception {
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    public void canGetAllVegetarianRecipes() throws Exception {
        FiltersDTO filtersDTO = new FiltersDTO();
        FilterDTO filter = new FilterDTO();
        filter.setExact(true);
        filter.setProperty("categories");
        filter.setValue("Vegetarian");
        filtersDTO.setInclusions(List.of(filter));
        mockMvc.perform(get("/recipes").param("filters", objectMapper.writeValueAsString(filtersDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.[0].name").value("VegetarianRecipe1!"))
                .andExpect(jsonPath("$.[1].name").value("VegetarianRecipe2!"))
                .andExpect(jsonPath("$.[2].name").value("Vegan Recipe"));
    }

    @Test
    public void canGetRecipesThatServe4PeopleAndHavePotatoes() throws Exception {
        FiltersDTO filtersDTO = new FiltersDTO();
        FilterDTO servingsFilter = new FilterDTO();
        servingsFilter.setExact(true);
        servingsFilter.setProperty("servings");
        servingsFilter.setValue("4");
        FilterDTO potatoFilter = new FilterDTO();
        potatoFilter.setValue("Potato");
        potatoFilter.setProperty("ingredients");
        potatoFilter.setExact(true);
        filtersDTO.setInclusions(List.of(servingsFilter, potatoFilter));
        mockMvc.perform(get("/recipes").param("filters", objectMapper.writeValueAsString(filtersDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].name").value("VegetarianRecipe2!"));
    }

    @Test
    public void canGetRecipesWithoutSalmonAndWithOvenInInstructions() throws Exception {
        FiltersDTO filtersDTO = new FiltersDTO();
        FilterDTO oven = new FilterDTO();
        oven.setExact(false);
        oven.setProperty("instructions");
        oven.setValue("oven");
        FilterDTO salmon = new FilterDTO();
        salmon.setValue("Salmon");
        salmon.setProperty("ingredients");
        salmon.setExact(true);
        filtersDTO.setInclusions(List.of(oven));
        filtersDTO.setExclusions(List.of(salmon));
        mockMvc.perform(get("/recipes").param("filters", objectMapper.writeValueAsString(filtersDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].name").value("VegetarianRecipe2!"));
    }
}