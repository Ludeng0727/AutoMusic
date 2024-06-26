package one.design.repository;

import one.design.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, String> {

    Optional<Song> findByFileName(String fileName);
    List<Song> findAllByUserId(String userId);
    @Transactional
    void deleteByFileName(String fileName);
}

