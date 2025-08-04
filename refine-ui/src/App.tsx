import {
  Refine,
  Authenticated,
  type AuthProvider,
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
import {GenreList} from "./pages/genre";
import {MovieCreate, MovieList, MovieShow} from "./pages/movies";
import {ActorCreate, ActorList, ActorShow} from "./pages/actors";
import { App as AntdApp } from "antd";
import { Homepage } from "./pages/homepage";
import {useKeycloak} from "@react-keycloak/web";
import axios from "axios";
import {Login} from "./pages/login";

const App: React.FC = () => {
  const {keycloak, initialized} = useKeycloak();


  const authProvider: AuthProvider = {
    // --
    login: async () => {
      // You can handle the login process according to your needs.
      const urlSearchParams = new URLSearchParams(window.location.search);
      const { to } = Object.fromEntries(urlSearchParams.entries());
      console.log(to)
      await keycloak.login({
        // redirectUri: 'http://localhost:5173/users'
      });
      return {
        success: false,
        error: new Error("Login failed"),
      };
    },
    logout: async () => {
      try {
        await keycloak.logout({
          redirectUri: window.location.origin,
        });
        return {
          success: true,
          redirectTo: "/login",
        };
      } catch (error) {
        return {
          success: false,
          error: new Error("Logout failed"),
        };
      }
    },
    onError: async (error) => {
      if (error.response?.status === 401) {
        return {
          logout: true,
        };
      }

      return { error };
    },
    check: async () => {
      try {
        const { token } = keycloak;
        if (token) {
          axios.defaults.headers.common = {
            Authorization: `Bearer ${token}`,
          };
          return {
            authenticated: true,
          };
        }
        return {
          authenticated: false,
          logout: true,
          redirectTo: "/login",
          error: {
            message: "Check failed",
            name: "Token not found",
          },
        };
      } catch (error) {
        return {
          authenticated: false,
          logout: true,
          redirectTo: "/login",
          error: {
            message: "Check failed",
            name: "Token not found",
          },
        };
      }
    },
    getPermissions: async () => null,
    getIdentity: async () => {
      if (keycloak?.tokenParsed) {
        return {
          name: keycloak.tokenParsed.family_name,
        };
      }
      return null;
    },
  };

  return (
      <BrowserRouter>
        <RefineKbarProvider>
          <ColorModeContextProvider>
            <CssBaseline/>
            <GlobalStyles styles={{html: {WebkitFontSmoothing: "auto"}}}/>
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
                        },
                        {
                          name: "movies",
                          list: "/movies",
                          create: "/movies/create",
                          show: "/movies/show/:id",
                          meta: {
                            canDelete: true,
                          },
                        },
                        {
                          name: "actors",
                          list: "/actors",
                          create: "/actors/create",
                          show: "/actors/show/:id",
                          meta: {
                            canDelete: true,
                          },
                        },
                      ]}
                      options={{
                        syncWithLocation: true,
                        warnWhenUnsavedChanges: true,
                        projectId: "CvRzHH-QBM2sm-09smWv",
                        title: {text: "Refine Project", icon: <AppIcon/>},
                        reactQuery: {
                          clientConfig: {
                            defaultOptions: {
                              queries: {
                                staleTime: Infinity,
                              },
                            },
                          },
                        },
                      }}
                  >
                    <Routes>
                      <Route
                          element={
                            <Authenticated
                                key="authenticated-routes"
                                fallback={<CatchAllNavigate to="/login"/>}
                            >
                              <ThemedLayoutV2>
                                <Outlet/>
                              </ThemedLayoutV2>
                            </Authenticated>
                          }
                        >

                        <Route index element={<Homepage/>}/>
                        <Route path="/users">
                          <Route index element={<Homepage/>}/>
                        </Route>
                        <Route path="/genres">
                          <Route index element={<GenreList/>}/>
                        </Route>
                        <Route path="/movies">
                          <Route index element={<MovieList/>}/>
                          <Route path="create" element={<MovieCreate/>}/>
                          <Route path="show/:id" element={<MovieShow/>}/>
                        </Route>
                        <Route path="/actors">
                          <Route index element={<ActorList/>}/>
                          <Route path="create" element={<ActorCreate/>}/>
                          <Route path="show/:id" element={<ActorShow/>}/>
                        </Route>
                      </Route>
                      <Route
                          element={
                            <Authenticated key="auth-pages" fallback={<Outlet/>}>
                              <NavigateToResource resource={"users"}/>
                            </Authenticated>
                          }
                      >
                        <Route path="/login" element={<Login/>}/>
                      </Route>
                      <Route
                          element={
                            <Authenticated key="catch-all">
                              <ThemedLayoutV2>
                                <Outlet />
                              </ThemedLayoutV2>
                            </Authenticated>
                          }
                      >
                        <Route path="*" element={<ErrorComponent />} />
                      </Route>
                    </Routes>
                    <RefineKbar/>
                    <UnsavedChangesNotifier/>
                    <DocumentTitleHandler/>
                  </Refine>
                </AntdApp>
                <DevtoolsPanel/>
              </DevtoolsProvider>
            </RefineSnackbarProvider>
          </ColorModeContextProvider>
        </RefineKbarProvider>
      </BrowserRouter>
  );
}

export default App;
