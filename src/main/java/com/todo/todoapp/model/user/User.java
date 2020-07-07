package com.todo.todoapp.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Document(collection = "User")
public class User {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_GITHUB_ID = "github_id";
    private static final String FIELD_GOOGLE_ID = "google_id";

    @Id
    private String id;

    @Email
    @Field(name = FIELD_EMAIL)
    private String email;

    @Field(name = FIELD_GITHUB_ID)
    private String githubId;

    @Field(name = FIELD_GOOGLE_ID)
    private String googleId;

    public User(String id, @Email String email, @NotNull @NotEmpty @Valid String githubId, @NotNull @NotEmpty @Valid String googleId) {
        this.id = id;
        this.email = email;
        this.githubId = githubId;
        this.googleId = googleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(githubId, user.githubId) &&
                Objects.equals(googleId, user.googleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, githubId, googleId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", githubId='" + githubId + '\'' +
                ", googleId='" + googleId + '\'' +
                '}';
    }

    public static class Builder {

        private String id;

        private String email;

        private String githubId;

        private String googleId;

        public User.Builder withId(String id) {
            this.id = id;

            return this;
        }

        public User.Builder withEmail(String email) {
            this.email = email;

            return this;
        }

        public User.Builder withGithubId(String githubId) {
            this.githubId = githubId;

            return this;
        }

        public User.Builder withGoogleId(String googleId) {
            this.googleId = googleId;

            return this;
        }

        public User build() {
            return new User(id, email, githubId, googleId);
        }
    }
}
