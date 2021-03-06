package server.model;

import model.*;
import model.communication.Auth;
import model.communication.LoginStatus;
import server.model.persistance.DBPersistence;
import server.model.persistance.HibernateAdapter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ServerModelManager implements ServerModel {

    private PostsList posts;
    private UsersList users;
    private FamilyList families;
    private DBPersistence db;

    public ServerModelManager() {
        posts = new PostsList();
        users = new UsersList();
        families = new FamilyList();
        db = new HibernateAdapter();
        restoreState();
        Thread dbSynchronize = new Thread(new SynchronizeDatabase());
        dbSynchronize.setDaemon(true);
        dbSynchronize.start();
    }

    private void restoreState() {
        users.addAll(db.getUsers());
        posts.addAll(db.getPosts());
        families.addAll(retrieveFamilies(users.getAll()));
    }

    @Override
    public LoginStatus authenticate(Auth auth) {
        return users.authenticate(auth);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        db.addUser(user);
    }

    @Override
    public void editUser(User user) {
        users.updateUser(user);
        db.updateUser(user);
    }

    @Override
    public void deleteUser(User user) {
        users.delete(user.getId());
        db.deleteUser(user);
    }

    @Override
    public List<Family> getAllFamilies() {
        return families.getAllFamilies();
    }

    @Override
    public void deleteFamily(Family family) {
        families.deleteFamily(family);
        db.deleteFamily(family);
    }

    @Override
    public void addFamily(Family family) {
        families.addFamily(family);
        db.addFamily(family);
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        return users.checkIfEmailExist(email);
    }

    @Override
    public void changePwdWithEmail(String email, String newPwd) {
        User user = users.getUserByEmail(email);
        user.setPwdAndEncrypt(newPwd);
        user.changePassword();
    }

    @Override
    public void editFamily(Family family) {
        families.update(family);
    }

    @Override
    public void addPost(Post post) {
        posts.add(post);
        db.addPost(post);
    }

    @Override
    public void deletePost(Post post) {
        posts.deletePost(post);
        db.deletePost(post);
    }

    @Override
    public void editPost(Post post) {
        posts.editPost(post);
        db.updatePost(post);
    }

    @Override
    public List<Post> getAllPost() {
        return posts.getAll();
    }

    @Override
    public void clear() {
        posts.clear();
        families.clear();
        users.clear();
    }

    @Override
    public List<User> getAllUsers() {
        return users.getAll();
    }

    private void updateDb() {
        List<User> uList = db.getUsers();
        UsersList usersList = new UsersList();
        usersList.addAll(uList);
        List<List<User>> usersDiff = getUserDiff(uList);
        List<User> usersToAdd = usersDiff.get(0);
        List<User> usersToUpdate = usersDiff.get(1);

        List<Family> fList = retrieveFamilies(uList);
        FamilyList familyList = new FamilyList();
        familyList.addAll(fList);
        List<List<Family>> famDiff = getFamilyDiff(fList);
        List<Family> familiesToAdd = famDiff.get(0);
        List<Family> familiesToUpdate = famDiff.get(1);


        List<List<Post>> postsDiff = getPostDiff(db.getPosts());
        List<Post> postsToAdd = postsDiff.get(0);
        List<Post> postsToUpdate = postsDiff.get(1);

        updateFamilies(familiesToAdd, familiesToUpdate);
        updateUsers(usersToAdd, usersToUpdate);
        updatePosts(postsToAdd, postsToUpdate);
    }

    private List<Family> retrieveFamilies(List<User> users) {
        Set<Family> families = new HashSet<>();
        users.stream().filter(user -> user instanceof IFamily)
                .map(user -> (IFamily) user)
                .forEach(iFamily -> families.add(iFamily.getFamily()));
        return new LinkedList<>(families);
    }

    private List<List<Post>> getPostDiff(List<Post> dbPosts) {
        List<Post> toAdd = new LinkedList<>();
        List<Post> toUpdate = new LinkedList<>();

        for (Post post : posts.getAll()) {
            if (!dbPosts.contains(post)) {
                toAdd.add(post);
            } else {
                for (Post dbPost : dbPosts)
                    if (dbPost.getPostId().equals(post.getPostId())) {
                        toUpdate.add(post);
                        break;
                    }
            }
        }

        List<List<Post>> result = new LinkedList<>();
        result.add(toAdd);
        result.add(toUpdate);
        return result;
    }

    private List<List<User>> getUserDiff(List<User> dbUsers) {
        List<User> toAdd = new LinkedList<>();
        List<User> toUpdate = new LinkedList<>();

        for (User user : users.getAll()) {
            if (!dbUsers.contains(user)) {
                toAdd.add(user);
            } else {
                for (User dbUser : dbUsers)
                    if (dbUser.getId().equals(user.getId())) {
                        toUpdate.add(user);
                        break;
                    }
            }
        }

        List<List<User>> result = new LinkedList<>();
        result.add(toAdd);
        result.add(toUpdate);
        return result;
    }

    private List<List<Family>> getFamilyDiff(List<Family> dbFamilies) {
        List<Family> toAdd = new LinkedList<>();
        List<Family> toUpdate = new LinkedList<>();

        for (Family family : families.getAllFamilies()) {
            if (!dbFamilies.contains(family)) {
                toAdd.add(family);
            } else {
                for (Family dbFamily : dbFamilies)
                    if (dbFamily.getId().equals(family.getId())) {
                        toUpdate.add(family);
                        break;
                    }
            }
        }

        List<List<Family>> result = new LinkedList<>();
        result.add(toAdd);
        result.add(toUpdate);
        return result;
    }

    private void updatePosts(List<Post> postsToAdd, List<Post> postsToUpdate) {
        postsToAdd.forEach(post -> db.addPost(post));
        postsToUpdate.forEach(post -> db.updatePost(post));
    }

    private void updateUsers(List<User> usersToAdd, List<User> usersToUpdate) {
        usersToAdd.forEach(user -> db.addUser(user));
        usersToUpdate.forEach(user -> db.updateUser(user));
    }

    private void updateFamilies(List<Family> familiesToAdd, List<Family> familiesToUpdate) {
        familiesToAdd.forEach(family -> db.addFamily(family));
        // familiesToUpdate.forEach(family -> db.updateFamilies(family));
    }

    class SynchronizeDatabase implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 60 * 12);
                } catch (InterruptedException e) {
                    return;
                }
                updateDb();
            }
        }
    }
}
