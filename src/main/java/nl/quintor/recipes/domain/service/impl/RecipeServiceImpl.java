package nl.quintor.recipes.domain.service.impl;

import nl.quintor.recipes.data.RecipeSpecification;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.data.mapper.CategoryEntityMapper;
import nl.quintor.recipes.data.mapper.IngredientEntityMapper;
import nl.quintor.recipes.data.mapper.RecipeEntityMapper;
import nl.quintor.recipes.data.repository.CategoryRepository;
import nl.quintor.recipes.data.repository.IngredientRepository;
import nl.quintor.recipes.data.repository.RecipeRepository;
import nl.quintor.recipes.domain.models.Category;
import nl.quintor.recipes.domain.models.Filter;
import nl.quintor.recipes.domain.models.Ingredient;
import nl.quintor.recipes.domain.models.Recipe;
import nl.quintor.recipes.domain.service.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeEntityMapper recipeEntityMapper;

    private final CategoryEntityMapper categoryEntityMapper;

    private final IngredientEntityMapper ingredientEntityMapper;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;


    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeEntityMapper recipeEntityMapper,
                             CategoryEntityMapper categoryEntityMapper,
                             IngredientEntityMapper ingredientEntityMapper,
                             CategoryRepository categoryRepository,
                             IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeEntityMapper = recipeEntityMapper;
        this.categoryEntityMapper = categoryEntityMapper;
        this.ingredientEntityMapper = ingredientEntityMapper;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(recipeEntityMapper::toDomain);
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        if (recipe.getId() != null)
            throw new IllegalArgumentException("Can't create a recipe with that already has an id");
        recipe.setCategories(this.fetchOrCreateCategories(recipe.getCategories()));
        recipe.setIngredients(this.fetchOrCreateIngredients(recipe.getIngredients()));
        RecipeEntity recipeEntity = recipeRepository.save(recipeEntityMapper.toEntity(recipe));
        return recipeEntityMapper.toDomain(recipeEntity);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        if (recipe.getId() == null) throw new IllegalArgumentException("Can't update a recipe without an id");
        recipe.setCategories(this.fetchOrCreateCategories(recipe.getCategories()));
        recipe.setIngredients(this.fetchOrCreateIngredients(recipe.getIngredients()));
        RecipeEntity recipeEntity = recipeRepository.save(recipeEntityMapper.toEntity(recipe));
        return recipeEntityMapper.toDomain(recipeEntity);
    }

    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public List<Recipe> getRecipesByFilters(List<Filter> inclusions, List<Filter> exclusions) {
        return recipeRepository.findAll(new RecipeSpecification(inclusions, exclusions))
                .stream().map(recipeEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    private List<Category> fetchOrCreateCategories(List<Category> categories) {
        return categories.stream().map(category -> categoryRepository.findOneByName(category.getName()).orElseGet(() -> categoryRepository.save(categoryEntityMapper.toEntity(category)))).map(categoryEntityMapper::toDomain).collect(Collectors.toList());
    }

    private List<Ingredient> fetchOrCreateIngredients(List<Ingredient> ingredients) {
        return ingredients.stream().map(ingredient -> ingredientRepository.findOneByName(ingredient.getName()).orElseGet(() -> ingredientRepository.save(ingredientEntityMapper.toEntity(ingredient)))).map(ingredientEntityMapper::toDomain).collect(Collectors.toList());
    }
}
