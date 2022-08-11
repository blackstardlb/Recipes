package nl.quintor.recipes;

import java.util.List;
import java.util.Optional;

public class Utils {
    public static <T> Optional<T> getLast(List<T> list) {
        if (list.isEmpty()) return Optional.empty();
        return Optional.ofNullable(list.get(list.size() - 1));
    }
}
