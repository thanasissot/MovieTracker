// frontend/src/stores/genreStore.ts
import { defineStore } from 'pinia'
import genreService, { type Genre } from '../service/genreService'

export const useGenreStore = defineStore('genre', {
  state: () => ({
    genres: [] as Genre[],
    loading: false,
    initialized: false,
    error: null as string | null,
  }),

  getters: {
    allGenres: state => state.genres,

    getGenreById: state => (id: number) => {
      return state.genres.find(genre => genre.id === id)
    },

    getGenreByName: state => (name: string) => {
      return state.genres.find(genre => genre.genreName === name)
    },

    getGenreNameById: state => (id: number) => {
      const genre = state.genres.find(genre => genre.id === id)
      return genre ? genre.genreName : `Unknown (${id})`
    },
  },

  actions: {
    async loadGenres () {
      if (this.initialized && this.genres.length > 0) {
        return
      }

      this.loading = true
      this.error = null

      try {
        this.genres = await genreService.getAllGenres()
        this.initialized = true
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to load genres'
        console.error('Failed to load genres:', error)
      } finally {
        this.loading = false
      }
    },

    async addGenre (genreName: string) {
      try {
        const newGenre = await genreService.createGenre(genreName)
        this.genres.push(newGenre)
        return newGenre
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to add genre'
        console.error('Failed to add genre:', error)
        throw error
      }
    },

    async updateGenre (id: number, genreName: string) {
      try {
        const updatedGenre = await genreService.updateGenre({ id, genreName })
        const index = this.genres.findIndex(g => g.id === id)
        if (index !== -1) {
          this.genres[index] = updatedGenre
        }
        return updatedGenre
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to update genre'
        console.error('Failed to update genre:', error)
        throw error
      }
    },

    async deleteGenre (id: number) {
      try {
        await genreService.deleteGenre(id)
        const index = this.genres.findIndex(g => g.id === id)
        if (index !== -1) {
          this.genres.splice(index, 1)
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : 'Failed to delete genre'
        console.error('Failed to delete genre:', error)
        throw error
      }
    },
  },
})
