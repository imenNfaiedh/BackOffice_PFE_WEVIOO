package com.example.user_service.service;

import com.example.user_service.models.UserDetailsWithGroupsAndRoles;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class UserService {

    @Autowired
    private Keycloak keycloak; //injecte une instance de Keycloak pour interagir avec le serveur Keycloak.

    private final String realm = "spring-micro-main";

    public void createUser(String username, String email, String firstName, String lastName, String password
            , String roleName) {

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

       //Un objet CredentialRepresentation est utilisé pour stocker les informations d'identification
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        // Ajouter les credentials à l'utilisateur
        user.setCredentials(Collections.singletonList(credential));

        //Création de l'utilisateur dans Keycloak
        Response response = keycloak.realm(realm).users().create(user);
        System.out.println("jjj");

        //Récupération de l'ID de l'utilisateur créé
        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        //Récupération du rôle et assignation à l'utilisateur
        RoleRepresentation role = getRole(roleName);
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));


    }


    public void assignRoleToUser(String userId, String roleName) {
        RoleRepresentation role = getRole(roleName);
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));

    }


    public UserDetailsWithGroupsAndRoles getUserDetailsWithGroupsAndRoles(String userId) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();

        ///// Fetch user's groups
        List<GroupRepresentation> groupRepresentations = keycloak.realm(realm).users().get(userId).groups();
        List<String> groupNames = groupRepresentations.stream()
                .map(GroupRepresentation::getName)
                .collect(Collectors.toList());

        /// Fetch user's realm roles
        List<RoleRepresentation> realmRoles = keycloak.realm(realm).users().get(userId).roles().realmLevel().listAll();
        List<String> realmRoleNames = realmRoles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());


        return new UserDetailsWithGroupsAndRoles(user, groupNames, realmRoleNames, null);
    }

    public void updateUser(String userId, Map<String, Object> updates) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setFirstName((String) updates.get("firstName"));
        user.setLastName((String) updates.get("lastName"));
        user.setEmail((String) updates.get("email"));

        keycloak.realm(realm).users().get(userId).update(user);
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm).users().delete(userId);
    }

    public List<UserRepresentation> listUsers() {
        return keycloak.realm(realm).users().list();
    }

    public RoleRepresentation getRole(String roleName) {
        return keycloak.realm(realm).roles().get(roleName).toRepresentation();
    }



    public void assignUserToGroup(String userId, String groupName) {
        try {
            GroupRepresentation group = getGroupByName(groupName);
            if (group == null) {
                throw new RuntimeException("Group not found: " + groupName);
            }
            keycloak.realm(realm).users().get(userId).joinGroup(group.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign user to group", e);
        }
    }

    private GroupRepresentation getGroupByName(String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(realm).groups().groups();
        for (GroupRepresentation group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }

    public List<UserRepresentation> getUsersByGroupAndRole(String groupName, String roleName) {
        List<UserRepresentation> users = new ArrayList<>();
        try {
            GroupRepresentation group = getGroupByName(groupName);
            if (group == null) {
                throw new RuntimeException("Group not found: " + groupName);
            }

            List<UserRepresentation> allUsers = keycloak.realm(realm).users().list();
            for (UserRepresentation user : allUsers) {
                if (isUserInGroup(user.getId(), group.getId()) && hasUserRole(user.getId(), roleName)) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get users by group and role", e);
        }
        return users;
    }
    private boolean isUserInGroup(String userId, String groupId) {
        List<GroupRepresentation> userGroups = keycloak.realm(realm).users().get(userId).groups();
        return userGroups.stream().anyMatch(group -> group.getId().equals(groupId));
    }

    private boolean hasUserRole(String userId, String roleName) {
        List<RoleRepresentation> roles = keycloak.realm(realm).users().get(userId).roles().realmLevel().listAll();
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
