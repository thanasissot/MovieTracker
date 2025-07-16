// frontend/src/service/movieService.ts
// Base API URL
const API_URL = 'http://localhost:8080/movies';

export interface Movie {
    id: number;
    title: string;
    year: number | null;
    genreIds?: number[];
}

// Service class with methods for API operations
class MovieService {
    /**
     * Get all movies
     * @returns Promise with array of movies
     */
    async getAllMovies(): Promise<Movie[]> {
        const response = await fetch(`${API_URL}/all`);
        if (!response.ok) {
            throw new Error('Failed to fetch movies');
        }
        return await response.json();
    }

    /**
     * Create a new movie
     * @param title - Title of the movie
     * @param year - Release year of the movie
     * @returns Promise with the created movie
     */
    async createMovie(title: string, year: number): Promise<Movie> {
        const response = await fetch(`${API_URL}/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, year })
        });

        if (!response.ok) {
            throw new Error('Failed to create movie');
        }
        return await response.json();
    }

    /**
     * Update a movie
     * @param movie - Movie data to update
     * @returns Promise with the updated movie
     */
    async updateMovie(movie: Movie): Promise<Movie> {
        const response = await fetch(`${API_URL}/edit`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movie)
        });

        if (!response.ok) {
            throw new Error('Failed to update movie');
        }
        return await response.json();
    }

    /**
     * Delete a movie by id
     * @param id - ID of the movie to delete
     * @returns Promise resolving when deletion is complete
     */
    async deleteMovie(id: number): Promise<void> {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Failed to delete movie');
        }

        if (response.status === 200)
            return Promise.resolve();
    }
}

// Export a single instance of the service
export default new MovieService();