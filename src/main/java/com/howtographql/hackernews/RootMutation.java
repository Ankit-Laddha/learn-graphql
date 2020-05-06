package com.howtographql.hackernews;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;

public class RootMutation implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    private final UserRepository userRepository;

    public RootMutation(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }

    public User createUser(String name, AuthData authData) {
        User newUser = new User(name, authData.getEmail(), authData.getPassword());
        return userRepository.saveUser(newUser);
    }

    public SigninPayload signinUser(AuthData authData) throws IllegalAccessException {
        User user = userRepository.findByEmail(authData.getEmail());
        if (user.getPassword().equals(authData.getPassword())) {
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }
}
