// Base API URL
const API_URL = 'http://localhost:8080/genres';

export interface Genre {
    id: number;
    genreName: string;
}

// Service class with methods for API operations
class GenreService {
    /**
     * Get all genres
     * @returns Promise with array of genres
     */
    async getAllGenres(): Promise<Genre[]> {
        const response = await fetch(`${API_URL}/all`);
        if (!response.ok) {
            throw new Error('Failed to fetch genres');
        }
        return await response.json();
    }

    /**
     * Create a new genre
     * @param genreName - Name of the genre to create
     * @returns Promise with the created genre
     */
    async createGenre(genreName: string): Promise<Genre> {
        const response = await fetch(`${API_URL}/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ genreName })
        });

        if (!response.ok) {
            throw new Error('Failed to create genre');
        }
        return await response.json();
    }

    async updateGenre(genre: Genre): Promise<Genre> {
        const response = await fetch(`${API_URL}/edit`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(genre)
        });

        if (!response.ok) {
            throw new Error('Failed to update genre');
        }
        return await response.json();
    }

    /**
     * Delete a genre by id
     * @param id - ID of the genre to delete
     * @returns Promise with the delete operation result
     */
    async deleteGenre(id: number): Promise<void> {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Failed to delete genre');
        }

        if (response.status === 200)
            return Promise.resolve();
    }
}

// Export a single instance of the service
export default new GenreService();