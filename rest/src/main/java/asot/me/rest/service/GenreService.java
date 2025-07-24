package asot.me.rest.service;

import asot.me.rest.dom.Genre;
import asot.me.rest.dto.GenreDto;
import asot.me.rest.mapper.GenreMapper;
import asot.me.rest.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreDto> getAllGenres() {
        return genreMapper.toDtoList(genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    public GenreDto getGenreById(Long id) {
        return genreMapper.toDTO(genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id)));
    }

    public GenreDto getGenreByName(String genreName) {
        return genreMapper.toDTO(genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new RuntimeException("Genre not found with genreName: " + genreName)));
    }

    @Transactional
    public GenreDto createGenre(GenreDto genreDto) {
        Genre genre = Genre.builder().genreName(genreDto.getGenre()).build();
        genreRepository.save(genre);
        return genreMapper.toDTO(genre);
    }

    @Transactional
    public GenreDto updateGenreName(Long id, String newName) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        genre.setGenreName(newName);
        genreRepository.save(genre);
        return genreMapper.toDTO(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        genreRepository.delete(genre);
    }


}
