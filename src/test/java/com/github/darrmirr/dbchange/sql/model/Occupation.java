package com.github.darrmirr.dbchange.sql.model;

public class Occupation {
    private final Integer id;
    private final String name;

    public Occupation(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
