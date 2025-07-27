import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import {
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import {  useList } from "@refinedev/core";
import { FormControl, InputLabel, Select, MenuItem, Box, TextField, InputAdornment, IconButton } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import React, { useState, useEffect, useMemo } from "react";

export const ActorList = () => {
    const [movieId, setMovieId] = useState<number | null>(null);
    const [movieSearch, setMovieSearch] = useState("");

    const { dataGridProps } = useDataGrid({
        filters: {
            permanent: [
                {
                    field: "movieId",
                    operator: "eq",
                    value: movieId,
                },
            ],
        },
    });

    const { data: moviesData, refetch: refetchMovies } = useList({
        resource: "movies",
        filters: [
            {
                field: "title",
                operator: "contains",
                value: movieSearch.length > 0 ? movieSearch : undefined,
            }
        ],
    });

    const movies = moviesData?.data || [];

    const handleMovieChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        setMovieId(event.target.value as number | null);
    };

    const handleMovieSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setMovieSearch(event.target.value);
    };

    const handleClearSearch = () => {
        setMovieSearch("");
    };

    // Debounced refetch to avoid too many requests while typing
    useEffect(() => {
        const timer = setTimeout(() => {
            refetchMovies();
        }, 500);

        return () => clearTimeout(timer);
    }, [movieSearch, refetchMovies]);

  const columns = React.useMemo<GridColDef[]>(
    () => [
      {
        field: "id",
        headerName: "ID",
        type: "number",
        minWidth: 50,
        display: "flex",
        align: "left",
        headerAlign: "left",
      },
      {
        field: "firstname",
        headerName: "Firstname",
        minWidth: 200,
        display: "flex",
      },
      {
        field: "lastname",
        headerName: "Lastname",
        minWidth: 200,
        display: "flex",
      },
      {
        field: "actions",
        headerName: "Actions",
        align: "right",
        headerAlign: "right",
        minWidth: 120,
        sortable: false,
        display: "flex",
        renderCell: function render({ row }) {
          return (
            <>
              <EditButton hideText recordItemId={row.id} />
              <ShowButton hideText recordItemId={row.id} />
              <DeleteButton hideText recordItemId={row.id} />
            </>
          );
        },
      },
    ],
    []
  );

  return (
    <List>
        <Box display="flex" flexDirection="column" gap={2}>
            <TextField
                label="Search Movies"
                variant="outlined"
                value={movieSearch}
                onChange={handleMovieSearchChange}
                fullWidth
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            {movieSearch && (
                                <IconButton
                                    aria-label="clear search"
                                    onClick={handleClearSearch}
                                    edge="end"
                                >
                                    <ClearIcon />
                                </IconButton>
                            )}
                        </InputAdornment>
                    ),
                }}
            />
            <FormControl fullWidth variant="outlined">
                <InputLabel id="movie-select-label">Filter by Movie</InputLabel>
                <Select
                    labelId="movie-select-label"
                    value={movieId || ""}
                    onChange={handleMovieChange}
                    label="Filter by Movie"
                >
                    <MenuItem value="">
                        <em>All Actors</em>
                    </MenuItem>
                    {movies.map((movie) => (
                        <MenuItem key={movie.id} value={movie.id}>
                            {movie.title || `Movie ${movie.id}`}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
        </Box>
      <DataGrid {...dataGridProps} columns={columns} />
    </List>
  );
};
