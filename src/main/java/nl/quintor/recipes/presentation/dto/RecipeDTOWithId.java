package nl.quintor.recipes.presentation.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Constructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecipeDTOWithId extends RecipeDTO {
    @NotNull
    private Long id;

    public RecipeDTOWithId(RecipeDTO recipeDTO, Long id) {
        this.setId(id);
        this.setIngredients(recipeDTO.getIngredients());
        this.setCategories(recipeDTO.getCategories());
        this.setServings(recipeDTO.getServings());
        this.setInstructions(recipeDTO.getInstructions());
        this.setName(recipeDTO.getName());
    }
}
