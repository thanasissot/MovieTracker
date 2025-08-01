export interface Movie {
    id: number;
    title: string;
    year: number;
    genres: Genre[];
    genreIds?: number[];
    actors: Actor[];
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

export interface AppUser {
    id: number;
    username: string;
    userMovies: UserMovie[];
}

export interface UserMovie {
    appUserId: number;
    movieId: number;
    movie: Movie;
    watched: boolean;
    favorite: boolean;
}

export interface FlattenedUserMovie {
    movieId: number;
    title: string;
    year: number;
    watched: boolean;
    favorite: boolean;
}