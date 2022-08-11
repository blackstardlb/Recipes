package nl.quintor.recipes.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.quintor.recipes.data.entity.RecipeEntity;
import nl.quintor.recipes.domain.models.Filter;
import nl.quintor.recipes.domain.exceptions.BadRequestException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class RecipeSpecification implements Specification<RecipeEntity> {
    private final List<Filter> inclusions;
    private final List<Filter> exclusion;
    private final List<String> listChecks = List.of("categories", "ingredients");

    @Override
    public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> inclusions = this.inclusions.stream().map(inclusion -> handleInclusion(root, criteriaBuilder, inclusion)).collect(Collectors.toList());
        List<Predicate> exclusions = this.exclusion.stream().map(exclusion -> handleExclusion(root, criteriaBuilder, query, exclusion)).collect(Collectors.toList());

        Optional<Predicate> predicate = Stream.of(inclusions, exclusions).flatMap(Collection::stream).reduce(criteriaBuilder::and);
        return predicate.orElse(null);
    }

    public Predicate handleInclusion(Root<RecipeEntity> root, CriteriaBuilder builder, Filter filter) {
        try {
            Path<String> x = root.get(filter.getProperty());
            if (listChecks.contains(filter.getProperty())) {
                Join<Object, Object> fetch = root.join(filter.getProperty());
                return builder.equal(fetch.get("name"), filter.getValue());
            }
            if (filter.isExact()) {
                return builder.equal(x, filter.getValue());
            }
            return builder.like(x, "%" + filter.getValue() + "%");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    public Predicate handleExclusion(Root<RecipeEntity> root, CriteriaBuilder builder, CriteriaQuery<?> query, Filter filter) {
        try {
            Path<String> x = root.get(filter.getProperty());
            if (listChecks.contains(filter.getProperty())) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<RecipeEntity> from = subquery.from(RecipeEntity.class);
                Join<Object, Object> join = from.join(filter.getProperty());
                subquery.where(builder.equal(join.get("name"), filter.getValue()));
                subquery.select(from.get("id"));
                return builder.not(root.in(subquery));
            }
            if (filter.isExact()) {
                return builder.notEqual(x, filter.getValue());
            }
            return builder.notLike(x, "%" + filter.getValue() + "%");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }
}
