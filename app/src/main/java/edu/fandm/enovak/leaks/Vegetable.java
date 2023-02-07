package edu.fandm.enovak.leaks;

public class Vegetable {

    private String name;
    private double weight;
    public String color;



    public Vegetable(String name, double weight){
        this.name = name;
        this.weight = weight;
    }

    public String getName(){
        return this.name;
    }
}
