import {DataGrid, type GridColDef} from "@mui/x-data-grid";
import {List} from "@refinedev/mui";
import React, {useEffect, useMemo, useState} from "react";
import {type AppUser, FlattenedUserMovie} from "../../components/model/all";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton,
    Typography
} from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import {useApiUrl} from "@refinedev/core";
import axios from "axios";

export const Homepage = () => {
    const apiUrl = useApiUrl();
    const [flatenedUserMovies, setFlatenedUserMovies] = useState<FlattenedUserMovie[] | []>([]);
    const [loading, setLoading] = useState(true);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedAction, setSelectedAction] = useState<{
        movieId: number,
        field: 'watched'|'favorite',
        value: boolean
    }|null>(null);

    function flattenUserMovies(userMovies: UserMovie[]) {
        return userMovies.map(userMovie => {
            return {
                movieId: userMovie.movieId,
                watched: userMovie.watched,
                favorite: userMovie.favorite,
                title: userMovie.movie.title,
                year: userMovie.movie.year
            } as FlattenedUserMovie;
        });
    }

    const fetchUserMovies = () => {
        const userStr = localStorage.getItem('user');
        const parsedUser = userStr ? JSON.parse(userStr) as AppUser : null;
        const flatMovies = flattenUserMovies(parsedUser?.userMovies);
        setFlatenedUserMovies(flatMovies ?? []);
        setLoading(false);
    };

    useEffect(() => {
        fetchUserMovies();
    }, []);

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

        if (!currentMovie) {
            console.error('Movie not found');
            return;
        }

        // Create payload with all required fields
        const payload = {
            appUserId: appUserId,
            movieId: selectedAction.movieId,
            watched: selectedAction.field === 'watched' ? selectedAction.value : currentMovie.watched,
            favorite: selectedAction.field === 'favorite' ? selectedAction.value : currentMovie.favorite
        };

        try {
            const response = await axios.patch(`${apiUrl}/user-movie`,  payload, {
                // method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    // 'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                // body: JSON.stringify(payload),
            });

            if (response.status === 200) {
                setLoading(true)
                const userStr = localStorage.getItem('user');
                const parsedUser = userStr ? JSON.parse(userStr) as AppUser : null;
                parsedUser.userMovies = response.data as UserMovie[];
                localStorage.setItem('user', JSON.stringify(parsedUser))
                const flatMovies = flattenUserMovies(response.data as UserMovie[]);
                setFlatenedUserMovies(flatMovies ?? []);
                setLoading(false);
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

    const columns = React.useMemo<GridColDef[]>(
        () => [
            {
                field: "movieId",
                headerName: "ID",
                type: "number",
                minWidth: 50,
                display: "flex",
                align: "left",
                headerAlign: "left",
            },
            {
                field: "title",
                headerName: "Title",
                minWidth: 200,
                display: "flex",
            },
            {
                field: "year",
                headerName: "Year",
                minWidth: 200,
                display: "flex",
            },
            {
                field: "watched",
                headerName: "Watched",
                minWidth: 200,
                display: "flex",
                type: "boolean",
            },
            {
                field: "favorite",
                headerName: "Favorite",
                minWidth: 200,
                display: "flex",
                type: "boolean",
            },
            {
                field: "actions",
                headerName: "Actions",
                minWidth: 200,
                renderCell: (params) => (
                    <>
                        <IconButton
                            onClick={() => {
                                setSelectedAction({
                                    movieId: params.row.movieId,
                                    field: 'watched',
                                    value: !params.row.watched
                                });
                                setDialogOpen(true);
                            }}
                        >
                            {params.row.watched ? <VisibilityIcon color="primary" /> : <VisibilityOffIcon />}
                        </IconButton>

                        <IconButton
                            onClick={() => {
                                setSelectedAction({
                                    movieId: params.row.movieId,
                                    field: 'favorite',
                                    value: !params.row.favorite
                                });
                                setDialogOpen(true);
                            }}
                        >
                            {params.row.favorite ? <FavoriteIcon color="error" /> : <FavoriteBorderIcon />}
                        </IconButton>
                    </>
                ),
            },
        ],
        []
    );

    const rows = useMemo(() => flatenedUserMovies ?? [], [flatenedUserMovies]);

    if (loading) return <Typography>Loading...</Typography>;

    return (
        <List>
            <DataGrid
                rows={rows}
                getRowId={(row) => row.movieId}
                columns={columns}
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