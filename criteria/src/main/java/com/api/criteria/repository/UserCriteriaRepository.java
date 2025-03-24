package com.api.criteria.repository;

import com.api.criteria.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity findByUsername(String username) {
        // Creating new criteria for dynamic queries
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Defines root of the query
        CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);

        // This generates a WHERE username = :username condition.
        Predicate usernamePredicate = criteriaBuilder.equal(root.get("username"), username);

        // Executes the query and returns a single result (or throw exception if not found).
        query.where(usernamePredicate);
        TypedQuery<UserEntity> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultStream().findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void save(UserEntity user) {
        // Check if a user with the same username already exists
        UserEntity existingUser = entityManager.createQuery(
                        "SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", user.getUsername())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existingUser != null) {
            throw new IllegalStateException("User already exists with username: " + user.getUsername());
        }

        entityManager.persist(user);  // Save new user
    }


    public List<UserEntity> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = cb.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);

        query.select(root); // find all

        return entityManager.createQuery(query).getResultList();
    }

    @Transactional
    public void delete(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity != null) {
            entityManager.remove(userEntity);
        } else {
            throw new IllegalStateException("User not found with ID: " + id);
        }
    }
}
