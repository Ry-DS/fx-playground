package me.ryan_s;

import java.util.stream.Stream;


public enum Genre {//stores all possible genres, easily add one universally across application by editing this

    ACTION("Action"), COMEDY("Comedy"), DRAMA("Drama"), HORROR("Horror"), ROMANCE("Romance");
    String name;

    Genre(String name) {
        this.name = name;
    }

    public static Genre parse(String s) {//allows reading from human format to computer stored format (this enum)
        return Stream.of(values()).filter(g -> g.name.equalsIgnoreCase(s)).findAny().orElse(null);
    }

    @Override
    public String toString() {//so table shows it properly
        return name;
    }
}
