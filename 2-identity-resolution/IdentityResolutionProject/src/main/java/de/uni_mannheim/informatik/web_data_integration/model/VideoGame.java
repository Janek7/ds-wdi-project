package de.uni_mannheim.informatik.web_data_integration.model;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

public class VideoGame implements Matchable {

    protected String id;
    protected String provenance;
    private String title;
    private String platform;
    private String publisher;
    private LocalDateTime publishingDate;
    private List<Genre> genres;
    private List<GameMode> gameModes;
    private String developer;
    private int age;
    private double price;

    public VideoGame(String identifier, String provenance) {
        id = identifier;
        this.provenance = provenance;
        genres = new LinkedList<>();
        gameModes = new LinkedList<>();
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDateTime getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDateTime publishingDate) {
        this.publishingDate = publishingDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<GameMode> getGameModes() {
        return gameModes;
    }

    public void setGameModes(List<GameMode> gameModes) {
        this.gameModes = gameModes;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("[VideoGame %s: %s / %s / %s / %s]\n", getIdentifier(), getTitle(), getPlatform(),
                getPublishingDate().toString(), getGenres().toString());
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VideoGame) {
            return this.getIdentifier().equals(((VideoGame) obj).getIdentifier());
        } else
            return false;
    }

}