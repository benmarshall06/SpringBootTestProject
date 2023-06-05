package com.mycompany.user;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "tag_id")
    private String name;
    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @Column(length = 15,nullable=false)
    private String password;
    @Column(length = 45,nullable=false,name="first_name")
    private String firstname;
    @Column(length = 45,nullable=false,name="last_name")
    private String lastname;

    @Column(length=150, name="note")
    private String note;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    public User(){

    }

    public User(String name){
        this.name = name;
    }

    private boolean enabled;

    @Transient  // This means it won't be mapped to the database.
    private String tagString;

    public String getTagString(){
        return tagString;
    }

    public void setTagString(String tagString){
        this.tagString = tagString;
    }

    public List<Tag> getTags(){
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    private String newTag;

    public String getNewTag() {
        return newTag;
    }

    public void setNewTag(String newTag) {
        this.newTag = newTag;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note = note;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Return tags as a comma-separated string
    public String getTagsString() {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
    }

}
