import {
  Refine,
  GitHubBanner,
  Authenticated,
} from "@refinedev/core";
import { DevtoolsPanel, DevtoolsProvider } from "@refinedev/devtools";
import { RefineKbar, RefineKbarProvider } from "@refinedev/kbar";

import {
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
import {GenreCreate, GenreEdit, GenreList, GenreShow} from "./pages/genre";
import {MovieCreate, MovieEdit, MovieList, MovieShow} from "./pages/movies";
import {ActorCreate, ActorEdit, ActorList, ActorShow} from "./pages/actors";
import { AuthProvider } from "@refinedev/core";
import { Login } from "./pages/login";
import axios from "axios";
import { App as AntdApp } from "antd";
import { Homepage } from "./pages/homepage";

const authProvider: AuthProvider = {
  // --
  login: async ({ username }) => {
    // You can handle the login process according to your needs.
    const response = await axios.get(`http://localhost:8080/users/username`, {
      params: {
        username
      }
    });
    // If the process is successful.
    if (response.status === 200) {
      localStorage.setItem("user",  JSON.stringify(response.data))
      return {
        success: true,
        redirectTo: "/",
      };
    }

    return {
      success: false,
      error: {
        name: "Login Error",
        message: "Invalid email or password",
      },
    };
  },
  // --
  logout: async () => {
    localStorage.removeItem("user");
    return {
      success: true,
      redirectTo: "/login",
    };
  },
  onError: async (error) => {
    if (error.response?.status === 401) {
      return {
        logout: true,
      };
    }

    return { error };
  },
  check: async () =>
      localStorage.getItem("user")
          ? {
            authenticated: true,
          }
          : {
            authenticated: false,
            redirectTo: "/login",
          },
  getPermissions: async () => ["admin"],
};

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
              <AntdApp>
              <Refine
                dataProvider={dataProvider("http://localhost:8080")}
                notificationProvider={useNotificationProvider}
                routerProvider={routerBindings}
                authProvider={authProvider}
                resources={[
                  {
                    name: "Homepage",
                    list: "/users",
                    options: {
                      label: "Homepage"
                    }
                  },
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
                        <Authenticated
                            key="authenticated-routes"
                            fallback={<CatchAllNavigate to="/login" />}
                        >
                          <ThemedLayoutV2>
                            <Outlet />
                          </ThemedLayoutV2>
                        </Authenticated>
                      }
                  >
                    <Route
                      index
                      element={<Homepage />}
                    />
                    <Route path="/users">
                      <Route index element={<Homepage />} />
                    </Route>
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
                  <Route
                      element={
                        <Authenticated key="auth-pages" fallback={<Outlet />}>
                          <NavigateToResource />
                        </Authenticated>
                      }
                  >
                    <Route path="/login" element={<Login />} />
                  </Route>
                </Routes>
                <RefineKbar />
                <UnsavedChangesNotifier />
                <DocumentTitleHandler />
              </Refine>
              </AntdApp>
              <DevtoolsPanel />
            </DevtoolsProvider>
          </RefineSnackbarProvider>
        </ColorModeContextProvider>
      </RefineKbarProvider>
    </BrowserRouter>
  );
}

export default App;
