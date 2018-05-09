package model;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

public class Family{
	private String id;
	private ArrayList<Student> children;
	private ArrayList<Parent> parents;
	
	public Family() {
		id = UUID.randomUUID().toString();
		children= new ArrayList<>();
		parents= new ArrayList<>();
	}
	public void addChild(Student child) {
		children.add(child);
	}

	public void deleteChild(Student child) {
		children.remove(child);
	}

	public ArrayList<Student> getChildren() {
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

	public void addParent(Parent parent) {
		parents.add(parent);
	}

	public void deleteParent(Parent parent) {
		parents.remove(parent);
	}

	public ArrayList<Parent> getParents() {
		return parents;
	}

	public ArrayList<Classs> getClasses() {
		ArrayList<Classs> classes = new ArrayList<>();
		for (Student e : children) {
		    if(classes.contains(e.getClasss()))
				classes.add(e.getClasss());
		}
		return classes;
	}

    public String getId() {
        return id;
	}
}
