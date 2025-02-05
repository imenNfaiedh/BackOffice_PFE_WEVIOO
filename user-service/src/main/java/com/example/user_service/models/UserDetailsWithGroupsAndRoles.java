package com.example.user_service.models;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
@Getter
@Setter
public class UserDetailsWithGroupsAndRoles {

    private UserRepresentation user;
    private List<String> groupNames;
    private List<String> realmRoleNames;
    private List<String> clientRoleNames;

    public UserDetailsWithGroupsAndRoles(UserRepresentation user, List<String> groupNames, List<String> realmRoleNames, List<String> clientRoleNames) {
        this.user = user;
        this.groupNames = groupNames;
        this.realmRoleNames = realmRoleNames;
        this.clientRoleNames = clientRoleNames;
    }

    // Getters and setters...

    public UserRepresentation getUser() {
        return user;
    }

    public void setUser(UserRepresentation user) {
        this.user = user;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public List<String> getRealmRoleNames() {
        return realmRoleNames;
    }

    public void setRealmRoleNames(List<String> realmRoleNames) {
        this.realmRoleNames = realmRoleNames;
    }

    public List<String> getClientRoleNames() {
        return clientRoleNames;
    }

    public void setClientRoleNames(List<String> clientRoleNames) {
        this.clientRoleNames = clientRoleNames;
    }
}