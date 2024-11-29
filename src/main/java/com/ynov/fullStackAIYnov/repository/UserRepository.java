package com.ynov.fullStackAIYnov.repository;

import com.ynov.fullStackAIYnov.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
