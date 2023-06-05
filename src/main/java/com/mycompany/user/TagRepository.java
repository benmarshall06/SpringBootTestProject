package com.mycompany.user;
import org.springframework.data.repository.CrudRepository;
public interface TagRepository extends CrudRepository<Tag, Integer> {
    Tag findByName(String name);
}
