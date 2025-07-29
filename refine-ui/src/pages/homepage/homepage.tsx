import { DataGrid, type GridColDef } from "@mui/x-data-grid";
import {
    List,

} from "@refinedev/mui";
import React, { useState, useEffect, useMemo } from "react";
import {type AppUser, FlattenedUserMovie} from "../../components/model/all";
import { Typography } from '@mui/material';

export const Homepage = () => {
    const [flatenedUserMovies, setFlatenedUserMovies] = useState<FlattenedUserMovie[] | []>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const userStr = localStorage.getItem('user');
        const parsedUser = userStr ? JSON.parse(userStr) : null;
        const flatMovies = (parsedUser as AppUser)?.userMovies.map(userMovie => {
            return {
                movieId: userMovie.movieId,
                watched: userMovie.watched,
                favorite: userMovie.favorite,
                title: userMovie.movie.title,
                year: userMovie.movie.year
            } as FlattenedUserMovie;
        });
        setFlatenedUserMovies(flatMovies?? []);
        setLoading(false);
    }, []);

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
        </List>
    );
};