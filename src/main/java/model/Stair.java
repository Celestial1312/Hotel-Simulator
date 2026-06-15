package model;

import java.util.ArrayList;
import java.util.List;

public class Stair {
    private int ticksPerLevel = 2;
    private List<Person> people = new ArrayList<>();

    public Stair() {

    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void removePerson(Person person) {
        people.remove(person);
    }

    public List<Person> getPeople() {
        return people;
    }

    public int getTicksPerLevel() {
        return ticksPerLevel;
    }

    public boolean hasPeople() {
        return !people.isEmpty();
    }

}