// Base API URL
const API_URL = 'http://localhost:8080/actors';

export interface Actor {
    id: number;
    firstname: string;
    lastname: string;
}

// Service class with methods for API operations
class ActorService {
    /**
     * Get all genres
     * @returns Promise with array of genres
     */
    async getAllActors(): Promise<Actor[]> {
        const response = await fetch(`${API_URL}/all`);
        if (!response.ok) {
            throw new Error('Failed to fetch Actors');
        }
        return await response.json();
    }

    /**
     * Create a new genre
     * @returns Promise with the created genre
     * @param firstname
     * @param lastname
     */
    async createActor(firstname: string, lastname: string): Promise<Actor> {
        const response = await fetch(`${API_URL}/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ firstname, lastname })
        });

        if (!response.ok) {
            throw new Error('Failed to create genre');
        }
        return await response.json();
    }

    async updateActor(actor: Actor): Promise<Actor> {
        const response = await fetch(`${API_URL}/edit`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(actor)
        });

        if (!response.ok) {
            throw new Error('Failed to update Actor');
        }
        return await response.json();
    }

    /**
     * Delete an Actor by id
     * @param id - ID of the Actor to delete
     * @returns Promise with the delete operation result
     */
    async deleteActor(id: number): Promise<void> {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Failed to delete Actor');
        }

        if (response.status === 200)
            return Promise.resolve();
    }
}

// Export a single instance of the service
export default new ActorService();