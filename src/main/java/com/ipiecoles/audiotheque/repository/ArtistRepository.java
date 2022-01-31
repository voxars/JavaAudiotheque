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

    boolean existsByName(String name);



    @Query(value = "SELECT * FROM artist a WHERE a.name LIKE :name% ", nativeQuery = true)
    List<Artist> findByName(@Param("name") String name); 



    @Query(value = "SELECT * FROM artist a WHERE a.name LIKE ?1", nativeQuery = true)
    Artist findByNameOnly(String name);

    @Modifying
    @Query(value = "DELETE FROM artist WHERE id = ?1 ;", nativeQuery = true)
    void deleteArtistById(Long artiste_id);





}
