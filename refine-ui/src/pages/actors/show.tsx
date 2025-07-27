import { TextField, Stack, Autocomplete, Typography, Divider, Box, Table, TableBody, TableCell, TableContainer, TableRow, Paper, Button } from "@mui/material";
import { useShow } from "@refinedev/core";
import {
  Show,
} from "@refinedev/mui";
import {DataGrid, GridColDef, GridRenderCellParams} from "@mui/x-data-grid";
import { useApiUrl } from "@refinedev/core";
import React from "react";
import { Delete as DeleteIcon } from "@mui/icons-material";
import axios from "axios";
import { useState, useEffect } from "react";
import { Movie, Actor } from "../../components/model/all";

export const ActorShow = () => {
  const apiUrl = useApiUrl();
  const [searchText, setSearchText] = useState<string>("");
  const [movieOptions, setMovieOptions] = useState<Movie[]>([]);
  const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);

  const { query } = useShow({});
  const { data, isLoading } = query;
  const record = data?.data as Actor | undefined;
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    if (record?.movies) {
      setMovies(record.movies || []);
    }
  }, [record]);

  const filteredMovies = (response: any) : Movie[] => {
      return response.data.filter((movie: Movie) =>
        !movies.some(m => m.id === movie.id)
      );
  };

  // Load default movies for dropdown
  const fetchDefaultMovies = async () => {
    try {
      const response = await axios.get(`${apiUrl}/movies`, {
        params: {
          _sort: 'title',
          _order: 'asc',
          _start: 0,
          _end: 20
        }
      });
      setMovieOptions(filteredMovies(response));
    } catch (error) {
      console.error("Error fetching default movies:", error);
    }
  };

  useEffect(() => {
    fetchDefaultMovies();
  }, []);

  // Search movies as user types
  useEffect(() => {
    const fetchMovies = async () => {
      if (searchText.length >= 2) {
        try {
          const response = await axios.get(`${apiUrl}/movies`, {
            params: {
              title_like: searchText,
              _sort: 'title',
              _order: 'asc',
              _start: 0,
              _end: 20
            }
          });
          setMovieOptions(filteredMovies(response));
        } catch (error) {
          console.error("Error fetching movies:", error);
        }
      }
    };

    const timeoutId = setTimeout(() => {
      fetchMovies();
    }, 300);

    return () => clearTimeout(timeoutId);
  }, [searchText, apiUrl]);

  const handleRemoveMovie = async (movieId: number) => {
    if (!record?.id) return;

    try {
      await axios.delete(`${apiUrl}/actors/${record.id}/movies`, {
        data: [movieId]
      });

      // Update local state
      setMovies(movies.filter(movie => movie.id !== movieId));
      await fetchDefaultMovies();
    } catch (error) {
      console.error("Error removing movie:", error);
    }
  };

  const handleAddMovie = async () => {
    if (!selectedMovie || !record?.id) return;

    try {
      await axios.post(`${apiUrl}/actors/${record.id}/movies`, [selectedMovie.id]);

      // Update local state
      if (!movies.some(m => m.id === selectedMovie.id)) {
        setMovies([...movies, selectedMovie]);
      }

      // Reset selection
      setSelectedMovie(null);
      setSearchText("");
      await fetchDefaultMovies();
    } catch (error) {
      console.error("Error adding movie:", error);
    }
  };

  const movieColumns: GridColDef[] = [
    {
      field: "id",
      headerName: "ID",
      width: 50,
    },
    {
      field: "title",
      headerName: "Movie Title",
      flex: 1,
      minWidth: 120,
    },
    {
      field: "year",
      headerName: "Release Year",
      width: 120,
    },
    {
      field: "actions",
      headerName: "Actions",
      sortable: false,
      width: 100,
      renderCell: (params: GridRenderCellParams) => (
          <Button
              color="error"
              onClick={() => handleRemoveMovie(params.row.id)}
              startIcon={<DeleteIcon />}
              size="small"
          >
            Remove
          </Button>
      ),
    },
  ];

  return (
    <Show isLoading={isLoading}>
      <Stack gap={1}>
        <TableContainer component={Paper}>
          <Table>
            <TableBody>
              <TableRow>
                <TableCell component="th" scope="row" width="50px" sx={{ fontWeight: "bold" }}>ID</TableCell>
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>First Name</TableCell>
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>Last Name</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>{record?.id}</TableCell>
                <TableCell>{record?.firstname}</TableCell>
                <TableCell>{record?.lastname}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>

        <Divider sx={{ my: 2 }} />


        <Typography variant="h6" sx={{ mb: 2 }}>Manage Movies</Typography>

        {/* Search and Add Movies */}
        <Box sx={{ display: "flex", mb: 2, gap: 1 }}>
          <Autocomplete
              fullWidth
              value={selectedMovie}
              onChange={(event, newValue) => {
                setSelectedMovie(newValue);
              }}
              inputValue={searchText}
              onInputChange={(event, newInputValue) => {
                setSearchText(newInputValue);
              }}
              options={movieOptions}
              getOptionLabel={(option) => `${option.title} (${option.year})`}
              renderInput={(params) => (
                  <TextField
                      {...params}
                      label="Search Movies"
                      variant="outlined"
                      placeholder="Type to search movies..."
                  />
              )}
          />
          <Button
              variant="contained"
              onClick={handleAddMovie}
              disabled={!selectedMovie}
              sx={{ minWidth: 100 }}
          >
            Add Movie
          </Button>
        </Box>

        <Typography variant="h6">Movies</Typography>
        {movies?.length > 0 ? (
            <Box sx={{ height: 400, width: '100%' }}>
              <DataGrid
                  rows={movies}
                  columns={movieColumns}
                  disableRowSelectionOnClick
                  autoHeight
                  pageSizeOptions={[5, 10, 25]}
                  initialState={{
                    pagination: {
                      paginationModel: { pageSize: 5 },
                    },
                  }}
              />
            </Box>
        ) : (
            <Typography variant="body2" color="text.secondary">
              This actor has not been cast in any movies.
            </Typography>
        )}
      </Stack>
    </Show>
  );
};
