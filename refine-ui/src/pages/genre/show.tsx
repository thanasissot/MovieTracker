import { Stack, Typography, Card, CardContent, Divider } from "@mui/material";
import { useShow } from "@refinedev/core";
import { Show,  } from "@refinedev/mui";

export const GenreShow = () => {
  const { queryResult } = useShow({});
  const { data, isLoading } = queryResult;
  const record = data?.data;

  return (
      <Show isLoading={isLoading}>
        <Card>
          <CardContent>
            <Stack spacing={2}>
              <Stack direction="row" spacing={2} alignItems="center">
                <Typography variant="subtitle1" fontWeight="bold" width="100px">
                  ID:
                </Typography>
                <Typography variant="body1">{record?.id}</Typography>
              </Stack>

              <Divider />

              <Stack direction="row" spacing={2} alignItems="center">
                <Typography variant="subtitle1" fontWeight="bold" width="100px">
                  Genre:
                </Typography>
                <Typography variant="body1">{record?.name}</Typography>
              </Stack>
            </Stack>
          </CardContent>
        </Card>
      </Show>
  );
};
