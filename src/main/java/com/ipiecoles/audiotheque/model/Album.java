package com.ipiecoles.audiotheque.model;

import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;

import javax.persistence.*;

@Entity
public class Album {
    @Id
    private Long id;
    private String title;

    @ManyToOne
    private Artist artist;

    public Album() {}

    public Album(Long id, String title) {
        this.id = id;
        this.title = title;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }



}
