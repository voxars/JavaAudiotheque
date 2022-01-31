package com.ipiecoles.audiotheque.model;

import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;

import javax.persistence.*;

@Entity
//@Table(name="album")
public class Album {
    @Id
    //@Column(name="albumleid")
    private Long id;
    //@Column(name="albumletitle")
    private String title;

    @ManyToOne
    private Artist artist;
    //private int artist_id = artist.getId();

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
