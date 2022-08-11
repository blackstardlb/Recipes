package nl.quintor.recipes.presentation.dto;

import lombok.Data;

@Data
public class FilterDTO {
    private String property;
    private String value;
    private boolean isExact;
}
