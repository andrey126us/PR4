package com.example.sqlitelab2;

public class Animal {
    private int id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String owner;

    public Animal(int id, String name, String species, String breed, int age, String owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.owner = owner;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public String getOwner() { return owner; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecies(String species) { this.species = species; }
    public void setBreed(String breed) { this.breed = breed; }
    public void setAge(int age) { this.age = age; }
    public void setOwner(String owner) { this.owner = owner; }
}