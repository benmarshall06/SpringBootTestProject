package com.mycompany.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TagRepository tagRepo;

    public List<User> listAll() {
        List<User> users = (List<User>) repo.findAll();
        // Force initialization of tags
        for (User user : users) {
            user.getTags().size();
        }
        return users;
    }

    @Transactional
    // This method saves the user
    public void save(User user) throws UserNotFoundException {
        String[] deleteTags = user.getTagsString().split(",");
        String newTag = user.getNewTag();

        // Delete selected tags
        if (deleteTags != null && deleteTags.length > 0) {
            for (String deleteTag : deleteTags) {
                System.out.println("deleteTags = " + deleteTag);
                user.getTags().removeIf((tag -> tag.getName().equalsIgnoreCase(deleteTag)));
                tagRepo.deleteByNameIgnoreCase(deleteTag);
            }
        }

        // Add new tag
        if (newTag != null && !newTag.isEmpty()) {
            Tag tag = new Tag(newTag);
            tag.setUser(user);
            user.getTags().add(tag);
        }

        // Save the user
        if (user.getId() != null) {
            User existingUser = repo.findById(user.getId()).orElseThrow(() ->
                    new UserNotFoundException("Could not find any users with ID " + user.getId()));
            // Update the existing user's tags
            System.out.println();
            for (Tag tag : user.getTags()){
                System.out.println("tag: " + tag);
                System.out.println("-----existing user: " + existingUser);
                tag.setUser(existingUser);
            }
            existingUser.setTags(user.getTags());
            System.out.println(user+"\n");
            System.out.println(existingUser);
            repo.save(user);
        } else {
            repo.save(user);
        }
        System.out.print("user first name: " + user.getFirstname());
    }

    public User get(Integer id) throws UserNotFoundException {
        Optional<User> result = repo.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new UserNotFoundException("Could not find any users with ID" + id);
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new UserNotFoundException("Could not find any users with ID " + id);
        }
        repo.deleteById(id);
    }

    public Optional<User> getUserDetailsById(Integer id) {
        return repo.findById(id);
    }
}
