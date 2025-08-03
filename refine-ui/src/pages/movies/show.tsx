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
    Card, CardContent,
} from "@mui/material";
import {useShow} from "@refinedev/core";
import {Show} from "@refinedev/mui";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import React from "react";
import {type Movie} from "../../components/model/all";

export const MovieShow = () => {
    const {query} = useShow({});
    const {data, isLoading} = query;
    const record = data?.data as Movie | undefined;

    // Add columns definition for actors
    const actorColumns: GridColDef[] = [
        {
            field: "id",
            headerName: "ID",
            width: 180,
            align: "left",
            headerAlign: "left",
        },
        {
            field: "firstname",
            headerName: "First Name",
            renderHeader: (params) => (
                <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
                    {params.colDef.headerName}
                </div>
            ),
            minWidth: 250,
            flex: 1,
            renderCell: (params) => (
                <div style={{
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    padding: '0px 0 6px 22px'
                }}>
                    {params.value}
                </div>
            ),
        },
        {
            field: "lastname",
            headerName: "Last Name",
            minWidth: 250,
            flex: 1,
            renderHeader: (params) => (
                <div style={{paddingLeft: '22px', display: 'flex', alignItems: 'center', height: '100%'}}>
                    {params.colDef.headerName}
                </div>
            ),
            renderCell: (params) => (
                <div style={{
                    width: '100%',
                    fontWeight: 500,
                    fontSize: '0.875rem',
                    padding: '0px 0 6px 22px'
                }}>
                    {params.value}
                </div>
            ),
        }
    ];

    return (
        <Show isLoading={isLoading}
              wrapperProps={{
                  style: {
                      maxWidth: '1200px',
                      margin: '0 auto',
                      width: '100%',
                      padding: '16px'
                  },
              }}
        >
            <Stack gap={1}>
                <Card>
                    <CardContent>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell component="th" scope="row" width="180px"
                                                   sx={{fontWeight: "bold"}}>ID</TableCell>
                                        <TableCell component="th" scope="row" width="250px"
                                                   sx={{fontWeight: "bold"}}>Title</TableCell>
                                        <TableCell component="th" scope="row" width="250px" sx={{fontWeight: "bold"}}>Release
                                            Year</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>{record?.id}</TableCell>
                                        <TableCell>{record?.title}</TableCell>
                                        <TableCell>{record?.year}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </CardContent>
                </Card>

                <Divider sx={{my: 2}}/>

                <Card>
                    <CardContent>
                        <Typography variant="h6" sx={{my: 2}}>Actors</Typography>
                        {(record?.actors && record.actors.length > 0) ? (
                            <Box sx={{maxWidth: '1200px'}}>
                                <DataGrid
                                    rows={record?.actors}
                                    columns={actorColumns}
                                    disableRowSelectionOnClick
                                    initialState={{
                                        pagination: {
                                            paginationModel: {pageSize: 20},
                                        },
                                    }}
                                />
                            </Box>
                        ) : (
                            <Typography variant="body2" color="text.secondary">
                                This movie has no actors assigned.
                            </Typography>
                        )}
                    </CardContent>
                </Card>
            </Stack>
        </Show>
    );
};
