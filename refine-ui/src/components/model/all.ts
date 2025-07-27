export interface Movie {
    id: number;
    title: string;
    year: number;
    genres: Genre[];
    genreIds?: number[];
}

export interface Actor {
    id: number;
    firstname: string;
    lastname: string;
    movies: Movie[];
}

export interface Genre {
    id: number;
    name: string;
}