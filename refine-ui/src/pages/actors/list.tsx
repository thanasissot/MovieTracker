import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import {
  DeleteButton,
  EditButton,
  List,
  ShowButton,
  useDataGrid,
} from "@refinedev/mui";
import {HttpError, useList} from "@refinedev/core";
import { FormControl, InputLabel, Select, MenuItem, Box, TextField,
    Autocomplete, Button } from "@mui/material";
import React, { useState, useEffect } from "react";
import {type Genre, type Actor, FlattenedUserMovie, AppUser, UserMovie, Movie} from "../../components/model/all";

export const ActorList = () => {
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");
    const [movies, setMovies] = useState<Movie[]>([]);
    const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);
    const [movieInputValue, setMovieInputValue] = useState("");
    const [movieLoading, setMovieLoading] = useState(false);

    const [movieId, setMovieId] = useState<number | null>(null);
    const [movieSearch, setMovieSearch] = useState("");

    const { dataGridProps } = useDataGrid<Actor>({
        resource: "actors",
        filters: {
            permanent: [
                {
                    field: "movieId",
                    operator: "eq",
                    value: selectedMovie?.id || undefined,
                },
            ],
        },
        syncWithLocation: true,
    });

    const { data: moviesData, refetch: refetchMovies, isLoading: moviesDataIsLoading  } = useList<Movie, HttpError>({
        resource: "movies",
        pagination: {
            current: 1,
            pageSize: 10,
            mode: "server"
        },
        filters: [
            {
                field: "title",
                operator: "contains",
                value: selectedMovie ? undefined : (movieInputValue || undefined),
            },
            {
                field: "genreId",
                operator: "eq",
                value: selectedGenreId || undefined,
            },
        ],
    });

    const { data: genresData, isLoading: genresDataIsLoading } = useList<Genre, HttpError>({
        resource: "genres",
        pagination: {
            mode: "off"
        }
    });

    useEffect(() => {
        if (!genresDataIsLoading) {
            setGenres(genresData?.data ?? []);
        }

        if (!moviesDataIsLoading) {
            setMovies(moviesData?.data ?? []);
            setMovieLoading(false);
        }
    }, [genresData, moviesData])

    // Clear all filters
    const handleClearFilters = () => {
        setSelectedGenreId("");
        setSelectedMovie(null);
        setMovieInputValue(""); // Clear the input value
    };

  const columns = React.useMemo<GridColDef[]>(
    () => [
        {
            field: "id",
            headerName: "ID",
            type: "number",
            width: 120,
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
                maxWidth: '1400px',
                margin: '0 auto',
                width: '100%',
                padding: '16px'
            },
        }}
    >
        <Box sx={{display: "flex", mb: 2, gap: 1, flexWrap: "wrap"}}>
            <Autocomplete
                sx={{ minWidth: 200 }}
                options={genres}
                value={genres.find(genre => genre.id === selectedGenreId) || null}
                onChange={(event, newValue) => {
                    setSelectedGenreId(newValue?.id || "");
                }}
                getOptionLabel={(option) => option.name}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Filter by Genre"
                        variant="outlined"
                    />
                )}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                filterOptions={(options, state) => {
                    return options.filter(option =>
                        option.name.toLowerCase().includes(state.inputValue.toLowerCase())
                    );
                }}
            />

            {/* Actor Filter */}
            <Autocomplete
                sx={{minWidth: 250}}
                options={movies}
                loading={movieLoading}
                value={selectedMovie}
                onChange={(event, newValue) => {
                    setSelectedMovie(newValue);
                }}
                inputValue={movieInputValue}
                onInputChange={(event, newInputValue) => {
                    setMovieInputValue(newInputValue);
                }}
                getOptionLabel={(option) => {
                    return option.title;
                }}
                isOptionEqualToValue={(option, value) => option.id === value.id}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Search Movie"
                        variant="outlined"
                    />
                )}
            />

            {/* Clear Filters Button */}
            {(selectedGenreId || selectedMovie) && (
                <Button
                    variant="outlined"
                    onClick={handleClearFilters}
                    sx={{height: 56}}
                >
                    Clear Filters
                </Button>
            )}
        </Box>
      <DataGrid
          {...dataGridProps}
          columns={columns}
      />
    </List>
  );
};
