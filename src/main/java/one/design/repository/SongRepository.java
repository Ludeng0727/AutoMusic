package one.design.repository;

import one.design.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, String> {

    boolean existsSongByFileNameEquals(String fileName);

    Optional<Song> findByFileName(String fileName);
    List<Song> findAllByUserId(String userId);
}

