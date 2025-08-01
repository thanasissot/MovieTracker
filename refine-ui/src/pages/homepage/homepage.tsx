import {DataGrid, type GridColDef} from "@mui/x-data-grid";
import {List} from "@refinedev/mui";
import React, {useEffect, useMemo, useState} from "react";
import {type AppUser, FlattenedUserMovie, UserMovie} from "../../components/model/all";
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
        let flatMovies;
        if (parsedUser?.userMovies) {
            flatMovies = flattenUserMovies(parsedUser?.userMovies);
        }
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
                if (parsedUser) {
                    parsedUser.userMovies = response.data;
                    localStorage.setItem('user', JSON.stringify(parsedUser))
                }
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
                width: 70,
                align: "center",
                headerAlign: "center"
            },
            {
                field: "title",
                headerName: "Title",
                renderHeader: (params) => (
                    <div style={{ paddingLeft: '12px', display: 'flex', alignItems: 'center', height: '100%' }}>
                        {params.colDef.headerName}
                    </div>
                ),
                headerClassName: "pl-5",
                flex: 1,
                minWidth: 180,
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
                        }}
                    >
                        {params.value}
                    </div>
                ),
            },
            {
                field: "year",
                headerName: "Year",
                width: 100,
                align: "center",
                headerAlign: "center",
            },
            {
                field: "watched",
                headerName: "Watched",
                width: 120,
                align: "center",
                headerAlign: "center",
                renderCell: (params) =>  (
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
                            padding: '6px 0 6px 16px'
                        }}
                    >
                        {params.value ? (
                            <VisibilityIcon color="primary" />
                        ) : (
                            <VisibilityOffIcon color="action" sx={{ opacity: 0.6 }} />
                        )}
                    </div>

                ),
            },
            {
                field: "favorite",
                headerName: "Favorite",
                width: 120,
                align: "center",
                headerAlign: "center",
                renderCell: (params) => (
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
                                padding: '6px 0 6px 16px'
                            }}
                        >
                            {params.value ? (
                                <FavoriteIcon color="error" />
                            ) : (
                                <FavoriteBorderIcon color="action" sx={{ opacity: 0.6 }} />
                            )}
                        </div>

                ),
            },
            {
                field: "actions",
                headerName: "Actions",
                width: 120,
                align: "center",
                headerAlign: "center",
                renderCell: (params) => (
                    <div style={{ display: 'flex', justifyContent: 'center', paddingTop: '7px' }}>
                        <IconButton
                            size="small"
                            onClick={() => {
                                setSelectedAction({
                                    movieId: params.row.movieId,
                                    field: 'watched',
                                    value: !params.row.watched
                                });
                                setDialogOpen(true);
                            }}
                        >
                            {params.row.watched ? <VisibilityOffIcon /> : <VisibilityIcon />}
                        </IconButton>
                        <IconButton
                            size="small"
                            onClick={() => {
                                setSelectedAction({
                                    movieId: params.row.movieId,
                                    field: 'favorite',
                                    value: !params.row.favorite
                                });
                                setDialogOpen(true);
                            }}
                        >
                            {params.row.favorite ? <FavoriteBorderIcon /> : <FavoriteIcon />}
                        </IconButton>
                    </div>
                ),
            },
        ],
        []
    );

    const rows = useMemo(() => flatenedUserMovies ?? [], [flatenedUserMovies]);

    if (loading) return <Typography>Loading...</Typography>;

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