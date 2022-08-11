package nl.quintor.recipes.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.quintor.recipes.presentation.dto.FiltersDTO;

import java.beans.PropertyEditorSupport;

public class FiltersDTOEditor extends PropertyEditorSupport {
    private final ObjectMapper objectMapper;

    public FiltersDTOEditor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.isEmpty()) {
            setValue(null);
        } else {
            FiltersDTO filtersDTO;
            try {
                filtersDTO = objectMapper.readValue(text, FiltersDTO.class);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(e);
            }
            setValue(filtersDTO);
        }
    }
}
