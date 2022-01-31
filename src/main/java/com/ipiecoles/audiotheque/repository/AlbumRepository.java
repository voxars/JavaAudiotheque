package com.ipiecoles.audiotheque.repository;

import com.ipiecoles.audiotheque.model.Album;
import com.ipiecoles.audiotheque.model.Artist;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    boolean existsByTitle(String title);

    @Modifying
    @Query(value = "INSERT INTO album (title,artist_id) VALUES (?1,?2)", nativeQuery = true)
    int addAlbumToArtist(String a_title,Long an_artist_id);


    @Query(value = "SELECT * FROM album WHERE artist_id = id;", nativeQuery = true)
    List<Album> findAlbumByArtist(Long id);

    @Modifying
    @Query(value = "DELETE FROM album WHERE artist_id = ?1 ;", nativeQuery = true)
    void deleteAlbumFromArtist(Long artiste_id);


    Album findTopByOrderByIdDesc();


}
