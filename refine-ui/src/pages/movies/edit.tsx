import {Box, TextField, Card, CardContent} from "@mui/material";
import {Edit} from "@refinedev/mui";
import {useForm} from "@refinedev/react-hook-form";
import React from "react";

export const MovieEdit = () => {
    const {
        saveButtonProps,
        refineCore: {formLoading},
        register,
        formState: {errors},
    } = useForm({});

    return (
        <Edit isLoading={formLoading} saveButtonProps={saveButtonProps}
              wrapperProps={{
                  style: {
                      maxWidth: '1200px',
                      margin: '0 auto',
                      width: '100%',
                      padding: '16px'
                  },
              }}
        >
            <Card>
                <CardContent>
                    <Box
                        component="form"
                        sx={{display: "flex", flexDirection: "column"}}
                        autoComplete="off"
                    >
                        <TextField
                            {...register("title", {
                                required: "This field is required",
                            })}
                            error={!!(errors as any)?.title}
                            helperText={(errors as any)?.title?.message}
                            margin="normal"
                            fullWidth
                            InputLabelProps={{shrink: true}}
                            type="text"
                            label={"Title"}
                            name="title"
                        />
                        <TextField
                            {...register("year", {
                                required: "This field is required",
                            })}
                            error={!!(errors as any)?.year}
                            helperText={(errors as any)?.year?.message}
                            margin="normal"
                            fullWidth
                            InputLabelProps={{shrink: true}}
                            type="text"
                            label={"Year"}
                            name="year"
                        />
                    </Box>
                </CardContent>
            </Card>
        </Edit>
    );
};
