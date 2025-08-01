import {
    DeleteButton,
    EditButton,
    List,
    ShowButton,
    useDataGrid,
} from "@refinedev/mui";
import {DataGrid, type GridColDef} from "@mui/x-data-grid";
import {useApiUrl} from "@refinedev/core";
import {
    Typography, Box, FormControl, InputLabel, Select, MenuItem,
    Button, TextField, Autocomplete
} from "@mui/material";
import React, {useState, useEffect} from "react";
import axios from "axios";
import {type Genre, type Actor} from "../../components/model/all";
import {debounce} from 'lodash';

export const MovieList = () => {
    const apiUrl = useApiUrl();
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");
    const [actors, setActors] = useState<Actor[]>([]);
    const [selectedActor, setSelectedActor] = useState<Actor | null>(null);
    const [actorInputValue, setActorInputValue] = useState("");
    const [actorLoading, setActorLoading] = useState(false);

    // Fetch all genres once when component mounts
    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const response = await axios.get(`${apiUrl}/genres`, {
                    params: {
                        _sort: 'name',
                        _order: 'asc'
                    }
                });
                setGenres(response.data);
            } catch (error) {
                console.error("Error fetching genres:", error);
            }
        };

        fetchGenres().then(r => console.log("Genres are fetched"));
    }, [apiUrl]);

    // Initial fetch of top actors
    useEffect(() => {
        const fetchInitialActors = async () => {
            try {
                setActorLoading(true);
                const response = await axios.get(`${apiUrl}/actors`, {
                    params: {
                        _limit: 10,
                        _sort: 'lastname',
                        _order: 'asc'
                    }
                });
                setActors(response.data);
                setActorLoading(false);
            } catch (error) {
                console.error("Error fetching actors:", error);
                setActorLoading(false);
            }
        };

        fetchInitialActors();
    }, [apiUrl]);

    // Debounced search for actors
    const debouncedFetchActors = React.useCallback(
        debounce(async (searchQuery: string) => {
            try {
                setActorLoading(true);
                const response = await axios.get(`${apiUrl}/actors`, {
                    params: {
                        ...(searchQuery ? {name_like: searchQuery} : {}),
                        _limit: 10,
                        _sort: 'lastname',
                        _order: 'asc'
                    }
                });
                setActors(response.data);
                setActorLoading(false);
            } catch (error) {
                console.error("Error searching actors:", error);
                setActorLoading(false);
            }
        }, 500),
        [apiUrl]
    );

    // Handle actor input change
    useEffect(() => {
        debouncedFetchActors(actorInputValue);
    }, [actorInputValue, debouncedFetchActors]);


    // Clear all filters
    // Modify the handleClearFilters function to reset actor options
    const handleClearFilters = () => {
        setSelectedGenreId("");
        setSelectedActor(null);
        setActorInputValue(""); // Clear the input value
        debouncedFetchActors(""); // Fetch the default actors
    };

    // Setup filters based on selected genre
    const {dataGridProps} = useDataGrid({
        filters: {
            permanent: [
                {
                    field: "genreId",
                    operator: "eq",
                    value: selectedGenreId || undefined,
                },
                {
                    field: "actorId",
                    operator: "eq",
                    value: selectedActor?.id || undefined,
                }
            ],
        },
    });

    const columns = React.useMemo<GridColDef[]>(
        () => [
            {
                field: "id",
                headerName: "ID",
                type: "number",
                width: 60,
                align: "center",
                headerAlign: "center",
            },
            {
                field: "title",
                headerName: "Title",
                flex: 2,
                minWidth: 350,
                renderHeader: (params) => (
                    <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
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
                            padding: '0px 0 6px 22px'
                        }}
                    >
                        {params.value}
                    </div>
                ),
            },
            {
                field: "year",
                headerName: "Year",
                flex: 1,
                minWidth: 100,
                align: "center",
                headerAlign: "center",
            },
            {
                field: "actions",
                headerName: "Actions",
                flex: 1,
                minWidth: 150,
                align: "center",
                headerAlign: "center",
                sortable: false,
                renderCell: function render({row}) {
                    return (
                        <div style={{
                            display: "flex",
                            gap: "8px",
                            justifyContent: "center",
                            width: "100%"
                        }}>
                            <EditButton hideText recordItemId={row.id}/>
                            <ShowButton hideText recordItemId={row.id}/>
                            <DeleteButton hideText recordItemId={row.id}/>
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
                    maxWidth: '1200px',
                    margin: '0 auto',
                    width: '100%',
                    padding: '16px'
                },
            }}
        >
            <Box sx={{display: "flex", mb: 2, gap: 1, flexWrap: "wrap"}}>
                <FormControl variant="outlined" sx={{minWidth: 200}}>
                    <InputLabel id="genre-filter-label">Filter by Genre</InputLabel>
                    <Select
                        labelId="genre-filter-label"
                        value={selectedGenreId}
                        onChange={(e) => setSelectedGenreId(e.target.value as number)}
                        label="Filter by Genre"
                    >
                        <MenuItem value="">
                            <em>All Genres</em>
                        </MenuItem>
                        {genres.map((genre) => (
                            <MenuItem key={genre.id} value={genre.id}>
                                {genre.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                {/* Actor Filter */}
                <Autocomplete
                    sx={{minWidth: 250}}
                    options={actors}
                    loading={actorLoading}
                    value={selectedActor}
                    onChange={(event, newValue) => {
                        setSelectedActor(newValue);
                    }}
                    inputValue={actorInputValue}
                    onInputChange={(event, newInputValue) => {
                        setActorInputValue(newInputValue);
                    }}
                    getOptionLabel={(option) => {
                        return option.firstname + ' ' + option.lastname;
                    }}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            label="Search Actor"
                            variant="outlined"
                        />
                    )}
                />

                {/* Clear Filters Button */}
                {(selectedGenreId || selectedActor) && (
                    <Button
                        variant="outlined"
                        onClick={handleClearFilters}
                        sx={{height: 56}}
                    >
                        Clear Filters
                    </Button>
                )}
            </Box>
            <DataGrid {...dataGridProps} columns={columns} filterModel={undefined} autoHeight/>
        </List>
    );
};
