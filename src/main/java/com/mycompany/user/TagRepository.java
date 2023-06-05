package com.mycompany.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TagRepository extends CrudRepository<Tag, Integer> {
    Tag findByName(String name);

    @Modifying
    @Query("DELETE FROM Tag t WHERE LOWER(t.name) = LOWER(:name)")
    void deleteByNameIgnoreCase(String name);
}
