package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class Family implements Serializable {
    private String id;
    private ArrayList<Student> children = new ArrayList<>();
    private ArrayList<Parent> parents = new ArrayList<>();

    public Family() {
        id = UUID.randomUUID().toString();
    }

    public Family(String id) {
        this.id = id;
    }

    public void addChild(Student child) {
        children.add(child);
    }

    public void deleteChild(Student child) {
        children.remove(child);
    }

    public List<Student> getChildren() {
        return children;
    }

    public Student getChild(String name) throws NoSuchElementException {
        for (Student e : children)
            if (e.getName().equals(name))
                return e;
        throw new NoSuchElementException(name + "was not found.");
    }

    public Parent getParent(String name) throws NoSuchElementException {
        for (Parent e : parents)
            if (e.getName().equals(name))
                return e;
        throw new NoSuchElementException(name + "was not found.");
    }

    public String getMembersNames() {
        String parentsNames = parents.stream().map(s -> s.getName() + ", ").collect(Collectors.joining());
        String childrenNames = children.stream().map(s -> s.getName() + ", ").collect(Collectors.joining());
        if (parentsNames.length() + childrenNames.length() > 0)
            return parentsNames + childrenNames.substring(0, childrenNames.length() - 2);
        return "";
    }

    public void addParent(Parent parent) {
        parents.add(parent);
    }

    public void deleteParent(Parent parent) {
        parents.remove(parent);
    }

    public ArrayList<Parent> getParents() {
        return parents;
    }

    public ArrayList<ClassNo> getClasses() {
        ArrayList<ClassNo> classes = new ArrayList<>();
        for (Student e : children) {
            if (classes.contains(e.getClassNo()))
                classes.add(e.getClassNo());
        }
        return classes;
    }

    public String getId() {
        return id;
    }

    public void deleteMember(User user) {
        if (user instanceof Student)
            children.remove(user);
        if (user instanceof Parent)
            parents.remove(user);
    }

    public void clear() {
        children.forEach(c -> c.setFamily(null));
        parents.forEach(p -> p.setFamily(null));
        children.clear();
        parents.clear();
        id = "removed";//experimental
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Family family = (Family) o;
//
//        if (id != null ? !id.equals(family.id) : family.id != null) return false;
//        if (children != null ? !children.equals(family.children) : family.children != null) return false;
//        return parents != null ? parents.equals(family.parents) : family.parents == null;

        if (!(o instanceof Family))
            return false;

        Family other = (Family) o;
        if (!(this.id.equals(other.getId())))
            return false;

        if (children.size() != other.getChildren().size())
            return false;

        for (int i = 0; i < children.size(); i++) {
            if (!(children.get(i).getId().equals(other.getChildren().get(i).getId())))
                return false;
        }


        if (parents.size() != other.getParents().size())
            return false;

        for (int i = 0; i < parents.size(); i++) {
            if (!(parents.get(i).getId().equals(other.getParents().get(i).getId())))
                return false;
        }

        return true;
    }
}
