package asot.me.rest.service;

import asot.me.rest.dom.Genre;
import asot.me.rest.dto.GenreDto;
import asot.me.rest.mapper.GenreMapper;
import asot.me.rest.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public Page<GenreDto> getAllGenres(
        Pageable pageable
    ) {
        Page<Genre> genresPage = genreRepository.findAll(pageable);
        return genresPage.map(genreMapper::toDTO);
    }

    public GenreDto getGenreById(Long id) {
        return genreMapper.toDTO(genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id)));
    }

    public GenreDto getGenreByName(String genreName) {
        return genreMapper.toDTO(genreRepository.findByName(genreName)
                .orElseThrow(() -> new RuntimeException("Genre not found with genreName: " + genreName)));
    }

    @Transactional
    public GenreDto createGenre(GenreDto genreDto) {
        Genre genre = Genre.builder().name(genreDto.getName()).build();
        genreRepository.save(genre);
        return genreMapper.toDTO(genre);
    }

    @Transactional
    public GenreDto updateGenreName(Long id, String newName) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        genre.setName(newName);
        genreRepository.save(genre);
        return genreMapper.toDTO(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
        genreRepository.delete(genre);
    }


}
