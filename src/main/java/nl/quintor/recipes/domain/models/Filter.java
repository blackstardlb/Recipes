package nl.quintor.recipes.domain.models;

import lombok.Data;

@Data
public class Filter {
    private String property;
    private String value;
    private boolean isExact;
}
