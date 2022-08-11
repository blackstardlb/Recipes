package nl.quintor.recipes.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.recipes.domain.models.Filter;
import nl.quintor.recipes.domain.models.Recipe;
import nl.quintor.recipes.presentation.FiltersDTOEditor;
import nl.quintor.recipes.presentation.dto.FiltersDTO;
import nl.quintor.recipes.presentation.dto.ListRecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTO;
import nl.quintor.recipes.presentation.dto.RecipeDTOWithId;
import nl.quintor.recipes.presentation.mapper.FilterDTOMapper;
import nl.quintor.recipes.presentation.mapper.RecipeDTOMapper;
import nl.quintor.recipes.domain.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeDTOMapper recipeDTOMapper;
    private final FilterDTOMapper filterDTOMapper;
    private final ObjectMapper objectMapper;

    public RecipeController(RecipeService recipeService,
                            RecipeDTOMapper recipeDTOMapper,
                            FilterDTOMapper filterDTOMapper,
                            ObjectMapper objectMapper) {
        this.recipeService = recipeService;
        this.recipeDTOMapper = recipeDTOMapper;
        this.filterDTOMapper = filterDTOMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * {@code GET  /recipe/:id} : Get a recipe.
     *
     * @param id the id of recipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipe.
     */
    @GetMapping("{id}")
    public ResponseEntity<RecipeDTOWithId> getRecipe(@PathVariable Long id) {
        Optional<RecipeDTOWithId> recipeDTO = recipeService.getRecipeById(id).map(recipeDTOMapper::toDTOWithId);
        return ResponseEntity.of(recipeDTO);
    }

    /**
     * {@code POST  /recipe} : Create a new recipe.
     * @param recipeDTO the recipeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDTO.
     */
    @PostMapping("")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        recipeService.createRecipe(recipeDTOMapper.toDomain(recipeDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * {@code PUT  /recipe/:id} : Updates an existing recipe.
     * @param id the id of the recipe to update.
     * @param recipeDTO the new recipe
     * @return the {@link ResponseEntity} with status {@code 202 (ACCEPTED)}.
     */
    @PutMapping("{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        recipeService.updateRecipe(recipeDTOMapper.toDomain(new RecipeDTOWithId(recipeDTO, id)));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * {@code DELETE  /recipes/:id} : delete a recipe.
     * @param id the id of the recipe to delete.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<RecipeDTO> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * {@code GET  /orders?:filters} : get all recipes that match the given filters.
     * @param filters the filters to apply to the request
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list filtered of recipes in body or {@code 400(BAD_REQUEST)} if a bad filter was given.
     */
    @GetMapping("")
    public ResponseEntity<List<ListRecipeDTO>> getRecipes(@RequestParam(required = false) FiltersDTO filters) {
        List<Filter> inclusion = List.of();
        List<Filter> exclusions = List.of();
        if (filters != null) {
            inclusion = filters.getInclusions().stream().map(filterDTOMapper::toDomain).collect(Collectors.toList());
            exclusions = filters.getExclusions().stream().map(filterDTOMapper::toDomain).collect(Collectors.toList());
        }
        List<Recipe> recipes = recipeService.getRecipesByFilters(inclusion, exclusions);
        return ResponseEntity.ok(recipes.stream().map(recipeDTOMapper::toListDTO).collect(Collectors.toList()));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FiltersDTO.class, new FiltersDTOEditor(objectMapper));
    }
}
