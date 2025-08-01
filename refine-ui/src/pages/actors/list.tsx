import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import {
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import {  useList } from "@refinedev/core";
import { FormControl, InputLabel, Select, MenuItem, Box, TextField, InputAdornment, IconButton, SelectChangeEvent } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';
import React, { useState, useEffect } from "react";

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

    const handleMovieChange = (event: SelectChangeEvent<number | string>) => {
        setMovieId(event.target.value === "" ? null : Number(event.target.value));
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
            width: 70,
            align: "center",
            headerAlign: "center",
        },
        {
            field: "firstname",
            headerName: "Firstname",
            minWidth: 150,
            flex: 2,
            align: "center",
            headerAlign: "center",
            renderHeader: (params) => (
                <div style={{ paddingLeft: '16px', display: 'flex', alignItems: 'center', height: '100%' }}>
                    {params.colDef.headerName}
                </div>
            ),
            renderCell: (params) => (
                <div
                    style={{
                        width: '100%',
                        fontWeight: 500,
                        fontSize: '0.875rem',
                        whiteSpace: 'normal',
                        wordWrap: 'break-word',
                        display: '-webkit-box',
                        overflow: 'hidden',
                        WebkitLineClamp: 2,
                        WebkitBoxOrient: 'vertical',
                        padding: '0px 0 6px 16px'
                    }}
                >
                    {params.value}
                </div>
            ),
        },
        {
            field: "lastname",
            headerName: "Lastname",
            minWidth: 150,
            flex: 2,
            align: "center",
            headerAlign: "center",
            renderHeader: (params) => (
                <div style={{ paddingLeft: '16px', display: 'flex', alignItems: 'center', height: '100%' }}>
                    {params.colDef.headerName}
                </div>
            ),
            renderCell: (params) => (
                <div
                    style={{
                        width: '100%',
                        fontWeight: 500,
                        fontSize: '0.875rem',
                        whiteSpace: 'normal',
                        wordWrap: 'break-word',
                        display: '-webkit-box',
                        overflow: 'hidden',
                        WebkitLineClamp: 2,
                        WebkitBoxOrient: 'vertical',
                        padding: '0px 0 6px 16px'
                    }}
                >
                    {params.value}
                </div>
            ),
        },
        {
            field: "actions",
            headerName: "Actions",
            minWidth: 150,
            flex: 1,
            align: "center",
            headerAlign: "center",
            sortable: false,
            renderCell: function render({ row }) {
                return (
                    <div style={{
                        display: "flex",
                        gap: "8px",
                        justifyContent: "center",
                        width: "100%"
                    }}>
                        <EditButton hideText recordItemId={row.id} />
                        <ShowButton hideText recordItemId={row.id} />
                        <DeleteButton hideText recordItemId={row.id} />
                    </div>
                );
            },
        },
    ],
    []
  );

  return (
    <List
        wrapperProps={{
            style: {
                maxWidth: '1800px',
                margin: '0 auto',
                width: '100%',
                padding: '16px'
            },
        }}
    >
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
