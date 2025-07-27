import {
  Divider,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  Box,
  Button,
  TextField,
  Autocomplete,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from "@mui/material";
import { useShow, useApiUrl } from "@refinedev/core";
import { Show } from "@refinedev/mui";
import { DataGrid, GridColDef, GridRenderCellParams } from "@mui/x-data-grid";
import { Delete as DeleteIcon } from "@mui/icons-material";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { type Movie, type Genre } from "../../components/model/all";

export const MovieShow = () => {
  const apiUrl = useApiUrl();
  const [allGenres, setAllGenres] = useState<Genre[]>([]);
  const [genreOptions, setGenreOptions] = useState<Genre[]>([]);
  const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");
  const [genres, setGenres] = useState<Genre[]>([]);

  const { query } = useShow({});
  const { data, isLoading } = query;
  const record = data?.data as Movie | undefined;

  // Fetch all genres once
  useEffect(() => {
    const fetchAllGenres = async () => {
      try {
        const response = await axios.get(`${apiUrl}/genres`, {
          params: {
            _sort: 'name',
            _order: 'asc'
          }
        });
        setAllGenres(response.data);
      } catch (error) {
        console.error("Error fetching all genres:", error);
      }
    };

    fetchAllGenres();
  }, [apiUrl]);

  // Setup movie genres and available options
  useEffect(() => {
    if (record?.genreIds && record.genreIds.length > 0 && allGenres.length > 0) {
      // Map genre IDs to actual genre objects
      const movieGenres = record.genreIds
          .map(id => allGenres.find((genre: Genre) => genre.id === id))
          .filter(Boolean); // Remove any undefined entries

      setGenres(movieGenres ? movieGenres as Genre[] : []);

      // Update available genre options
      updateGenreOptions(record.genreIds);
    } else if (allGenres.length > 0) {
      setGenres([]);
      setGenreOptions(allGenres);
    }
  }, [record, allGenres]);

  // Helper function to update available genre options
  const updateGenreOptions = (usedGenreIds: number[]) => {
    const filteredOptions = allGenres.filter(
        genre => !usedGenreIds.includes(genre.id)
    );
    setGenreOptions(filteredOptions);
  };

  const handleRemoveGenre = async (genreId: number) => {
    if (!record?.id) return;

    try {
      await axios.delete(`${apiUrl}/movies/${record.id}/genres`, {
        data: [genreId]
      });

      // Update local state
      const updatedGenres = genres.filter(genre => genre.id !== genreId);
      setGenres(updatedGenres);

      // Update available options
      updateGenreOptions(updatedGenres.map(g => g.id));
    } catch (error) {
      console.error("Error removing genre:", error);
    }
  };

  const handleAddGenre = async () => {
    if (selectedGenreId === "" || !record?.id) return;

    try {
      await axios.post(`${apiUrl}/movies/${record.id}/genres`, [selectedGenreId]);

      // Find the selected genre object
      const selectedGenre = genreOptions.find(g => g.id === selectedGenreId);

      if (selectedGenre) {
        const updatedGenres = [...genres, selectedGenre];
        setGenres(updatedGenres);

        // Update available options
        updateGenreOptions(updatedGenres.map(g => g.id));
      }

      // Reset selection
      setSelectedGenreId("");
    } catch (error) {
      console.error("Error adding genre:", error);
    }
  };

  const genreColumns: GridColDef[] = [
    {
      field: "id",
      headerName: "ID",
      width: 80,
    },
    {
      field: "name",
      headerName: "Genre Name",
      flex: 1,
      minWidth: 200,
    },
    {
      field: "actions",
      headerName: "Actions",
      sortable: false,
      width: 100,
      renderCell: (params: GridRenderCellParams) => (
          <Button
              color="error"
              onClick={() => handleRemoveGenre(params.row.id)}
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
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>Title</TableCell>
                <TableCell component="th" scope="row" width="150px" sx={{ fontWeight: "bold" }}>Release Year</TableCell>
              </TableRow>
              <TableRow>
                <TableCell>{record?.id}</TableCell>
                <TableCell>{record?.title}</TableCell>
                <TableCell>{record?.year}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>

        <Divider sx={{ my: 2 }} />

        <Typography variant="h6" sx={{ mb: 2 }}>Manage Genres</Typography>

        {/* Search and Add Genres */}
        <Box sx={{ display: "flex", mb: 2, gap: 1 }}>
          <FormControl fullWidth variant="outlined">
            <InputLabel id="genre-select-label">Select Genre</InputLabel>
            <Select
                labelId="genre-select-label"
                value={selectedGenreId}
                onChange={(e) => setSelectedGenreId(e.target.value as number)}
                label="Select Genre"
            >
              <MenuItem value="">
                <em>Select a genre</em>
              </MenuItem>
              {genreOptions.map((genre) => (
                  <MenuItem key={genre.id} value={genre.id}>
                    {genre.name}
                  </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button
              variant="contained"
              onClick={handleAddGenre}
              disabled={selectedGenreId === ""}
              sx={{ minWidth: 100 }}
          >
            Add Genre
          </Button>
        </Box>

        <Typography variant="h6">Genres</Typography>
        {genres?.length > 0 ? (
            <Box sx={{ height: 400, width: '100%' }}>
              <DataGrid
                  rows={genres}
                  columns={genreColumns}
                  disableRowSelectionOnClick
                  autoHeight
                  pageSizeOptions={[10, 15, 25]}
                  initialState={{
                    pagination: {
                      paginationModel: { pageSize: 10 },
                    },
                  }}
              />
            </Box>
        ) : (
            <Typography variant="body2" color="text.secondary">
              This movie has not been assigned any genres.
            </Typography>
        )}
      </Stack>
    </Show>
  );
};
