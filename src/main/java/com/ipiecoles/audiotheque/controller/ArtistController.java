package com.ipiecoles.audiotheque.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import com.ipiecoles.audiotheque.repository.AlbumRepository;
import com.ipiecoles.audiotheque.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.Console;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;



    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}", //URL
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Artist findArtistById(@PathVariable Long id){
        Optional<Artist> artist = artistRepository.findById(id);
        if(artist.isPresent()){
            return artist.get();
        }
        throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'existe pas !");
        /*Artist artist = artistRepository.findById(id);
        return artist;*/

    }


 //liste d'artistes avec noms
    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    public List<Artist> findArtistByName(
            @RequestParam String name
    ){

        //String nameShort = name.substring(0,4);

        List<Artist> artists = artistRepository.findByName(name);
            if(artists != null){
                return artists;
            }
            throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Page<Artist> listArtist(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ){

        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("La page et la taille doivent être positifs !");
        }
        List<String> properties = Arrays.asList("id", "name", "nom");
        if(!properties.contains(sortProperty)){
            throw new IllegalArgumentException("La propriété de tri " + sortProperty + " est incorrecte !");
        }
        if(size > 50){
            throw new IllegalArgumentException("La taille doit être inférieure ou égale à 50 !");
        }
            Long nbArtists = artistRepository.count();
        if((long) size * page > nbArtists){
            throw new IllegalArgumentException("Le couple numéro de page et taille de page est incorrect !");
        }
        return artistRepository.findAll(PageRequest.of(page, size, sortDirection, sortProperty));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(
            @RequestBody Artist artist
    ){
        if(artist.getId() != null && artistRepository.existsById(artist.getId()) ||
                artistRepository.existsByName(artist.getName())){
            throw new EntityExistsException("Il existe déjà un artiste identique en base");
        }

        try {
            return artistRepository.save(artist);
        }
        catch(Exception e){
            throw new IllegalArgumentException("Problème lors de la sauvegarde de l'artiste");
        }
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Artist updateArtistName(
            @PathVariable Long id,
            @RequestBody String infos 

    ){


        Optional<Artist> artist = artistRepository.findById(id); //On recup l'id.

        String name="";

        Pattern p = Pattern.compile("\"([^\"]*)\""); 
        Matcher m = p.matcher(infos);
        while (m.find()) {
            if(m.group(1).equals("name") || m.group(1).equals("id") ){
                continue;
            }
            else{
                name = m.group(1);
                //System.out.println(name);

            }
        }

        artist.get().setName(name);   
        return artistRepository.save(artist.get());


    }



    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteArtist(
            @PathVariable Long id
    ){
        Optional<Artist> artist = artistRepository.findById(id);
        List<Album> albums = albumRepository.findAlbumByArtist(artist.get().getId());
        for(int i = 0; i<albums.size();i++){
            System.out.println("albums : " +albums.get(i).toString() );
            albums.remove(i);
        }
        for (Album album : albums) {
            System.out.println("album : " +album.getTitle() );
            albumRepository.deleteAlbumFromArtist(artist.get().getId());
        }
        artistRepository.deleteArtistById(id);
    }




}
