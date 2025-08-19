package at.boot.repositories;

import at.boot.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Optional<User> findByUsername(String username) {
        var query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        var query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    @Transactional
    public Optional<User> findByForgotPasswordToken(String token) {
        var query = em.createQuery("SELECT u FROM User u WHERE u.forgotPasswordToken = :token", User.class);
        query.setParameter("token", token);
        return query.getResultStream().findFirst();
    }

    @Transactional
    public Optional<User> findByConfirmationToken(String token) {
        var query = em.createQuery("SELECT u FROM User u WHERE u.confirmationToken = :token", User.class);
        query.setParameter("token", token);
        return query.getResultStream().findFirst();
    }

    @Transactional
    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }


    @Transactional
    public User saveNew(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("New user must not have an ID.");
        }
        em.persist(user);
        return user;
    }

    @Transactional
    public void update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Cannot update user without ID.");
        }
        em.merge(user);
    }

    @Transactional
    public void saveOrUpdate(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }
}
