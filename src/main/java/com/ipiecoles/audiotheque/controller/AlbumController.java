package com.ipiecoles.audiotheque.controller;

import com.google.gson.*;
import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

//afficher infos
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}", //URL
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Album findAlbumById(@PathVariable Long id){
        Optional<Album> album = albumRepository.findById(id);
        if(album.isPresent()){
            return album.get();
        }

        throw new EntityNotFoundException("L'album d'identifiant " + id + " n'existe pas !");
    }

    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    @ResponseStatus(HttpStatus.CREATED)
    //@Transactional
    public Album createAlbum(
            @RequestBody @NotNull String infos

            ){
        Artist artist = new Artist();


        String artistName="";
        String albumTitle="";
        String[]  arrayTitleAndArtistName = new String[2];
        int i = 0;



        Pattern findQuotes = Pattern.compile("\"([^\"]*)\""); 
        Pattern findId = Pattern.compile("([1-9]|[1-9][0-9]|[1-9][0-9][0-9])"); //
        Matcher m = findQuotes.matcher(infos);
        Matcher matchId = findId.matcher(infos);
        while (m.find()) {
            if(m.group(1).equals("title") || m.group(1).equals("id")  || m.group(1).equals("artist")  || m.group(1).equals("name") ){
                continue;

            }
            else{

                arrayTitleAndArtistName[i] = (m.group(1));
                i++;

            }

        }

        albumTitle = arrayTitleAndArtistName[0];
        artistName = arrayTitleAndArtistName[1];

        artist = artistRepository.findByNameOnly(artistName);



        Album album = new Album();
        Album lastAlbum = albumRepository.findTopByOrderByIdDesc();
        Long lastId = lastAlbum.getId();
        album.setId(lastId++);
        album.setTitle(albumTitle);
        album.setArtist(artist);

        return albumRepository.save(album);


    }



    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(
            @PathVariable Long id
    ){
        albumRepository.deleteById(id);
    }
}



