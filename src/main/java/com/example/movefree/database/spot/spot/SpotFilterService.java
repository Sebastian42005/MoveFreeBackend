package com.example.movefree.database.spot.spot;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpotFilterService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<SpotDTO> findFilteredSpots(Double minRating, String sport, String search, String city,
                                           List<Long> attributeIds, List<Integer> alreadySeenList, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpotDTO> query = cb.createQuery(SpotDTO.class);
        Root<Spot> spot = query.from(Spot.class);

        Join<Object, Object> sportJoin = spot.join("sport", JoinType.LEFT);
        Join<Object, Object> locationJoin = spot.join("location", JoinType.LEFT);
        Join<Object, Object> userJoin = spot.join("user", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Filter: minRating (AVG von ratings)
        Expression<Double> avgRating = cb.<Double>selectCase()
                .when(cb.isNotEmpty(spot.get("ratings")),
                        cb.function("coalesce", Double.class, cb.avg(spot.join("ratings").get("stars")), cb.literal(0.0)))
                .otherwise(0.0);

        if (minRating != null) {
            predicates.add(cb.greaterThanOrEqualTo(avgRating, minRating));
        }

        // Filter: sport
        if (sport != null && !sport.isBlank()) {
            predicates.add(cb.equal(cb.lower(sportJoin.get("name")), sport.toLowerCase()));
        }

        // Filter: search (title)
        if (search != null && !search.isBlank()) {
            predicates.add(cb.like(cb.lower(spot.get("title")), "%" + search.toLowerCase() + "%"));
        }

        // Filter: city
        if (city != null && !city.isBlank()) {
            predicates.add(cb.equal(cb.lower(locationJoin.get("city")), city.toLowerCase()));
        }

        // Filter: attributeIds
        if (attributeIds != null && !attributeIds.isEmpty()) {
            Join<Object, Object> attrJoin = spot.join("attributes", JoinType.LEFT);
            predicates.add(attrJoin.get("id").in(attributeIds));
        }

        // Filter: already seen
        if (alreadySeenList != null && !alreadySeenList.isEmpty()) {
            predicates.add(cb.not(spot.get("id").in(alreadySeenList)));
        }

        // Query build
        query.select(cb.construct(
                SpotDTO.class,
                spot.get("id"),
                spot.get("title"),
                spot.get("description"),
                locationJoin,
                cb.literal(new ArrayList<>()), // <-- leerer Platzhalter
                sportJoin.get("name"),
                userJoin.get("username"),
                cb.function("to_char", String.class, spot.get("createdAt"), cb.literal("YYYY-MM-DD\"T\"HH24:MI:SS")),
                avgRating,
                cb.literal(new ArrayList<>()) // <-- leerer Platzhalter
        ))
                .distinct(true)
                .where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
