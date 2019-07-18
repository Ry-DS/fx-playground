package me.ryan_s;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Movie {//used to store a movie as an object
    //these special fields let the table know when any of these change, allowing dynamic updating
    private SimpleStringProperty title;
    private SimpleIntegerProperty rating;
    private SimpleObjectProperty<Genre> genre;

    public Movie(String title, int rating, Genre genre) {//creating a movie constructor
        this.title = new SimpleStringProperty(title);
        this.rating = new SimpleIntegerProperty(rating);
        this.genre = new SimpleObjectProperty<>(genre);
    }

    //getters and setters
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public Genre getGenre() {
        return genre.get();
    }

    public void setGenre(Genre genre) {
        this.genre.set(genre);

    }

}
