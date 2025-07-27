import { Autocomplete, Box, Select, TextField } from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import { Edit, useAutocomplete } from "@refinedev/mui";
import { useForm } from "@refinedev/react-hook-form";
import React from "react";
import { Controller } from "react-hook-form";

export const ActorEdit = () => {
  const {
    saveButtonProps,
    refineCore: { queryResult, formLoading },
    register,
    control,
    formState: { errors },
  } = useForm({});

  const actorData = queryResult?.data?.data;

  return (
    <Edit isLoading={formLoading} saveButtonProps={saveButtonProps}>
      <Box
        component="form"
        sx={{ display: "flex", flexDirection: "column" }}
        autoComplete="off"
      >
        <TextField
            {...register("firstname", {
              required: "This field is required",
            })}
            error={!!(errors as any)?.firstname}
            helperText={(errors as any)?.firstname?.message}
            margin="normal"
            fullWidth
            InputLabelProps={{ shrink: true }}
            type="text"
            label={"Firstname"}
            name="firstname"
        />
        <TextField
            {...register("lastname", {
              required: "This field is required",
            })}
            error={!!(errors as any)?.lastname}
            helperText={(errors as any)?.lastname?.message}
            margin="normal"
            fullWidth
            InputLabelProps={{ shrink: true }}
            type="text"
            label={"Lastname"}
            name="lastname"
        />
      </Box>
    </Edit>
  );
};
