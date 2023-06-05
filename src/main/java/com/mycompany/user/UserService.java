package com.mycompany.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository repo;

    @Autowired private TagRepository tagRepo;

    public List<User> listAll() {
        List<User> users = (List<User>) repo.findAll();
        // Force initialization of tags
        for (User user : users) {
            user.getTags().size();
        }
        return users;
    }
//this method saves
    public void save(User user) throws UserNotFoundException {
        String[] tagNames = user.getTagString().split(",");
        List<Tag> newTags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepo.findByName(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
                tagRepo.save(tag);
            }
            newTags.add(tag);
        }
        if(user.getId() != null) {
            User existingUser = repo.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("Could not find any users with ID " + user.getId()));
            List<Tag> existingTags = existingUser.getTags();
            // Remove the association between the user and tags that are not in newTags
            for (Tag existingTag : existingTags) {
                if(!newTags.contains(existingTag)) {
                    //existingTag.getUsers().remove(existingUser);
                }
            }
        }
        user.setTags(newTags);
        repo.save(user);
    }

    public User get(Integer id) throws UserNotFoundException {
        Optional<User> result = repo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new UserNotFoundException("Could not find any users with ID" + id);
    }
    public void delete(Integer id) throws UserNotFoundException {
        Long count = repo.countById(id);
        if(count == null || count== 0){
            throw new UserNotFoundException("Could not find any users with ID" + id);
        }
        repo.deleteById(id);

    }
    public Optional<User> getUserDetailsById(Integer id) {
        return repo.findById(id);
    }

}
