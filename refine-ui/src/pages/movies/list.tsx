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
    Box, FormControl, InputLabel, Select, MenuItem,
    Button, TextField, Autocomplete, IconButton, Dialog, DialogTitle,
    DialogContent, DialogContentText, DialogActions
} from "@mui/material";
import React, {useState, useEffect} from "react";
import axios from "axios";
import {type Genre, type Actor, FlattenedUserMovie, AppUser, UserMovie} from "../../components/model/all";
import {debounce} from 'lodash';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder'

export const MovieList = () => {
    const apiUrl = useApiUrl();
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | "">("");
    const [actors, setActors] = useState<Actor[]>([]);
    const [selectedActor, setSelectedActor] = useState<Actor | null>(null);
    const [actorInputValue, setActorInputValue] = useState("");
    const [actorLoading, setActorLoading] = useState(false);
    // For watched/favorite functionality
    const [flatenedUserMovies, setFlatenedUserMovies] = useState<FlattenedUserMovie[] | []>([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedAction, setSelectedAction] = useState<{
        movieId: number,
        field: 'watched'|'favorite',
        value: boolean
    }|null>(null);


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

        fetchGenres().then(r  => {
            // nothing
        });
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

        fetchInitialActors().then(r  => {
            // nothing
        });
    }, [apiUrl]);

    // Function to flatten user movies
    function flattenUserMovies(userMovies: any[]) {
        return userMovies?.map(userMovie => {
            return {
                movieId: userMovie.movieId,
                watched: userMovie.watched,
                favorite: userMovie.favorite,
                title: userMovie.movie?.title,
                year: userMovie.movie?.year
            } as FlattenedUserMovie;
        }) || [];
    }

    // Fetch user movies data
    const fetchUserMovies = () => {
        const userStr = localStorage.getItem('user');
        const parsedUser = userStr ? JSON.parse(userStr) as AppUser : null;
        const flatMovies = flattenUserMovies(parsedUser?.userMovies || []);
        setFlatenedUserMovies(flatMovies);
    };

    useEffect(() => {
        fetchUserMovies();
    }, []);

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

    // Handle updating watched/favorite status
    const handleUpdate = async () => {
        if (!selectedAction) return;

        // Get appUserId from localStorage
        const userStr = localStorage.getItem('user');
        const parsedUser = userStr ? JSON.parse(userStr) : null;
        const appUserId = parsedUser?.id;

        if (!appUserId) {
            console.error('User ID not found');
            return;
        }

        // Find the current movie to get current watched and favorite values
        const currentMovie =
            flatenedUserMovies.find(movie => movie.movieId === selectedAction.movieId);

        // If movie is not in user's list, use default values
        let watched = false;
        let favorite = false;

        if (currentMovie) {
            watched = currentMovie.watched;
            favorite = currentMovie.favorite;
        }

        // Create payload with all required fields
        const payload = {
            appUserId: appUserId,
            movieId: selectedAction.movieId,
            watched: selectedAction.field === 'watched' ? selectedAction.value : watched,
            favorite: selectedAction.field === 'favorite' ? selectedAction.value : favorite
        };

        try {
            const response = await axios.patch(`${apiUrl}/user-movie`, payload, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (response.status === 200) {
                const userStr = localStorage.getItem('user');
                const parsedUser = userStr ? JSON.parse(userStr) as AppUser : null;
                if (parsedUser) {
                    parsedUser.userMovies = response.data as UserMovie[];
                    localStorage.setItem('user', JSON.stringify(parsedUser))
                }
                const flatMovies = flattenUserMovies(response.data);
                setFlatenedUserMovies(flatMovies);
            } else {
                console.error('Failed to update movie preference');
            }
        } catch (error) {
            console.error('Error updating movie preference:', error);
        } finally {
            setDialogOpen(false);
            setSelectedAction(null);
        }
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

    // Helper functions to check watched/favorite status
    const isMovieWatched = (movieId: number) => {
        const userMovie = flatenedUserMovies.find(m => m.movieId === movieId);
        return userMovie ? userMovie.watched : false;
    };

    const isMovieFavorite = (movieId: number) => {
        const userMovie = flatenedUserMovies.find(m => m.movieId === movieId);
        return userMovie ? userMovie.favorite : false;
    };

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
                minWidth: 300,
                flex: 3,
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
                minWidth: 60,
                flex: 1,
                align: "center",
                headerAlign: "center",
            },
            {
                field: "watched",
                headerName: "Watched",
                minWidth: 60,
                flex: 1,
                align: "center",
                headerAlign: "center",
                renderCell: (params) =>  {
                    const watched = isMovieWatched(params.row.id);
                    return (<div
                        style={{
                            width: '100%',
                            fontWeight: 500,
                            whiteSpace: 'normal',
                            wordWrap: 'break-word',
                            display: '-webkit-box',
                            overflow: 'hidden',
                            WebkitLineClamp: 2,
                            WebkitBoxOrient: 'vertical',
                            padding: '6px 0 6px 16px'
                        }}
                    >
                        {watched ? (
                            <VisibilityIcon color="primary" />
                        ) : (
                            <VisibilityOffIcon color="action" sx={{ opacity: 0.6 }} />
                        )}
                    </div>

                )},
            },
            {
                field: "favorite",
                headerName: "Favorite",
                minWidth: 60,
                flex: 1,
                align: "center",
                headerAlign: "center",
                renderCell: (params) => {
                    const favorite = isMovieFavorite(params.row.id);
                    return (
                        <div
                            style={{
                                width: '100%',
                                fontWeight: 500,
                                whiteSpace: 'normal',
                                wordWrap: 'break-word',
                                display: '-webkit-box',
                                overflow: 'hidden',
                                WebkitLineClamp: 2,
                                WebkitBoxOrient: 'vertical',
                                padding: '6px 0 0px 16px'
                            }}
                        >
                            {favorite ? (
                                <FavoriteIcon color="error"/>
                            ) : (
                                <FavoriteBorderIcon color="action" sx={{opacity: 0.6}}/>
                            )}
                        </div>
                    )
                },
            },
            {
                field: "actions",
                headerName: "Actions",
                minWidth: 220,
                flex: 2,
                align: "center",
                headerAlign: "center",
                sortable: false,
                renderCell: function render({row}) {
                    const watched = isMovieWatched(row.id);
                    const favorite = isMovieFavorite(row.id);

                    return (
                        <div style={{
                            display: "flex",
                            gap: "8px",
                            justifyContent: "center",
                            width: "100%"
                        }}>
                            <IconButton
                                size="small"
                                onClick={() => {
                                    setSelectedAction({
                                        movieId: row.id,
                                        field: 'watched',
                                        value: !watched
                                    });
                                    setDialogOpen(true);
                                }}
                            >
                                {watched ? <VisibilityOffIcon /> : <VisibilityIcon />}
                            </IconButton>
                            <IconButton
                                size="small"
                                onClick={() => {
                                    setSelectedAction({
                                        movieId: row.id,
                                        field: 'favorite',
                                        value: !favorite
                                    });
                                    setDialogOpen(true);
                                }}
                            >
                                {favorite ? <FavoriteBorderIcon /> : <FavoriteIcon />}
                            </IconButton>
                            <EditButton hideText recordItemId={row.id} />
                            <ShowButton hideText recordItemId={row.id} />
                            <DeleteButton hideText recordItemId={row.id} />
                        </div>
                    );
                },
            },
        ],
        [flatenedUserMovies]
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
            <DataGrid
                {...dataGridProps}
                columns={columns}
                filterModel={undefined}
                autoHeight
            />
            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
                <DialogTitle>Confirm Update</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to {selectedAction?.field === 'watched'
                        ? (selectedAction.value ? 'mark as watched' : 'mark as unwatched')
                        : (selectedAction?.value ? 'add to favorites' : 'remove from favorites')}?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
                    <Button onClick={handleUpdate} color="primary" autoFocus>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>
        </List>
    );
};
