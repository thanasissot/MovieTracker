import { Autocomplete, Box, Select, TextField } from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import { Edit, useAutocomplete } from "@refinedev/mui";
import { useForm } from "@refinedev/react-hook-form";
import React from "react";
import { Controller } from "react-hook-form";

export const MovieEdit = () => {
  const {
    saveButtonProps,
    refineCore: { queryResult, formLoading },
    register,
    control,
    formState: { errors },
  } = useForm({});

  return (
    <Edit isLoading={formLoading} saveButtonProps={saveButtonProps}>
      <Box
        component="form"
        sx={{ display: "flex", flexDirection: "column" }}
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
          InputLabelProps={{ shrink: true }}
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
          InputLabelProps={{ shrink: true }}
          type="text"
          label={"Year"}
          name="year"
        />
      </Box>
    </Edit>
  );
};
