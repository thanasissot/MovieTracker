export interface Movie {
    id: number;
    title: string;
    year: number;
}

export interface Actor {
    id: number;
    firstname: string;
    lastname: string;
    movies: Movie[];
}