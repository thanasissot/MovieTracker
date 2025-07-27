import {
  Refine,
  GitHubBanner,
  Authenticated,
} from "@refinedev/core";
import { DevtoolsPanel, DevtoolsProvider } from "@refinedev/devtools";
import { RefineKbar, RefineKbarProvider } from "@refinedev/kbar";

import {
  AuthPage,
  ErrorComponent,
  useNotificationProvider,
  RefineSnackbarProvider,
  ThemedLayoutV2,
} from "@refinedev/mui";

import dataProvider from "@refinedev/simple-rest";
import CssBaseline from "@mui/material/CssBaseline";
import GlobalStyles from "@mui/material/GlobalStyles";
import { BrowserRouter, Route, Routes, Outlet } from "react-router";
import routerBindings, {
  NavigateToResource,
  CatchAllNavigate,
  UnsavedChangesNotifier,
  DocumentTitleHandler,
} from "@refinedev/react-router";
import { AppIcon } from "./components/app-icon";
import { ColorModeContextProvider } from "./contexts/color-mode";
import { Header } from "./components/header";
import {GenreCreate, GenreEdit, GenreList, GenreShow} from "./pages/genre";
import {MovieCreate, MovieEdit, MovieList, MovieShow} from "./pages/movies";
import {ActorCreate, ActorEdit, ActorList, ActorShow} from "./pages/actors";

function App() {
  return (
    <BrowserRouter>
      <GitHubBanner />
      <RefineKbarProvider>
        <ColorModeContextProvider>
          <CssBaseline />
          <GlobalStyles styles={{ html: { WebkitFontSmoothing: "auto" } }} />
          <RefineSnackbarProvider>
            <DevtoolsProvider>
              <Refine
                dataProvider={dataProvider("http://localhost:8080")}
                notificationProvider={useNotificationProvider}
                routerProvider={routerBindings}
                resources={[
                  {
                    name: "genres",
                    list: "/genres",
                    create: "/genres/create",
                    edit: "/genres/edit/:id",
                    show: "/genres/show/:id",
                    meta: {
                      canDelete: true,
                    },
                  },
                  {
                    name: "movies",
                    list: "/movies",
                    create: "/movies/create",
                    edit: "/movies/edit/:id",
                    show: "/movies/show/:id",
                    meta: {
                      canDelete: true,
                    },
                  },
                  {
                    name: "actors",
                    list: "/actors",
                    create: "/actors/create",
                    edit: "/actors/edit/:id",
                    show: "/actors/show/:id",
                    meta: {
                      canDelete: true,
                    },
                  },
                ]}
                options={{
                  syncWithLocation: true,
                  warnWhenUnsavedChanges: true,
                  useNewQueryKeys: true,
                  projectId: "CvRzHH-QBM2sm-09smWv",
                  title: { text: "Refine Project", icon: <AppIcon /> },
                }}
              >
                <Routes>
                  <Route
                    element={
                      <ThemedLayoutV2 Header={() => <Header sticky />}>
                        <Outlet />
                      </ThemedLayoutV2>
                    }
                  >
                    <Route
                      index
                      element={<NavigateToResource resource="movies" />}
                    />
                    {/*<Route path="/favories">*/}
                    {/*  <Route index element={<GenreList />} />*/}
                    {/*  <Route path="create" element={<GenreCreate />} />*/}
                    {/*  <Route path="edit/:id" element={<GenreEdit />} />*/}
                    {/*  <Route path="show/:id" element={<GenreShow />} />*/}
                    {/*</Route>*/}
                    <Route path="/genres">
                      <Route index element={<GenreList />} />
                      <Route path="create" element={<GenreCreate />} />
                      <Route path="edit/:id" element={<GenreEdit />} />
                      <Route path="show/:id" element={<GenreShow />} />
                    </Route>
                    <Route path="/movies">
                      <Route index element={<MovieList />} />
                      <Route path="create" element={<MovieCreate />} />
                      <Route path="edit/:id" element={<MovieEdit />} />
                      <Route path="show/:id" element={<MovieShow />} />
                    </Route>
                    <Route path="/actors">
                      <Route index element={<ActorList />} />
                      <Route path="create" element={<ActorCreate />} />
                      <Route path="edit/:id" element={<ActorEdit />} />
                      <Route path="show/:id" element={<ActorShow />} />
                    </Route>

                    <Route path="*" element={<ErrorComponent />} />
                  </Route>
                </Routes>

                <RefineKbar />
                <UnsavedChangesNotifier />
                <DocumentTitleHandler />
              </Refine>
              <DevtoolsPanel />
            </DevtoolsProvider>
          </RefineSnackbarProvider>
        </ColorModeContextProvider>
      </RefineKbarProvider>
    </BrowserRouter>
  );
}

export default App;
