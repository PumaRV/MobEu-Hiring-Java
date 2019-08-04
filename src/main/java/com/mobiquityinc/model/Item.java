package com.mobiquityinc.model;

public class Item {

    private int id;
    private int value;
    private int weight;

    public Item(final int id, final int weight, final int value){
        this.id = id;
        this.value = value;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
