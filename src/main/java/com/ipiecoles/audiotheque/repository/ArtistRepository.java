package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    //Artist findById(int id);
    //@Query("select * from artist where id = " + unId);
    boolean existsByName(String name);

    //Artist findArtistById(Integer id);

    @Query(value = "SELECT * FROM artist a WHERE a.name LIKE :name% ", nativeQuery = true)
    List<Artist> findByName(@Param("name") String name); //On met dans le paramètre du nom raccourci dans la requète  ex :'aero%'



    @Query(value = "SELECT * FROM artist a WHERE a.name LIKE ?1", nativeQuery = true)
    Artist findByNameOnly(String name);

    /*
    @Query(value = "SELECT art.id,art.name,alb.title FROM artist art INNER JOIN album alb ON art.id = alb.artist_id WHERE artist_id;", nativeQuery = true)
    Set<Album> getAllAlbumsByArtist(Long artist_id);*/

    @Modifying
    @Query(value = "DELETE FROM artist WHERE id = ?1 ;", nativeQuery = true)
    void deleteArtistById(Long artiste_id);

    //Boolean existsById(Long id);
    //Artist findById(Integer id);

    /*@Query(value = "SELECT MAX(id) FROM artist;", nativeQuery = true)
    Long findLastId();*/



}