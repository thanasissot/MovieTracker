import React from "react";
import { createRoot } from "react-dom/client";
import { ReactKeycloakProvider } from "@react-keycloak/web";

import App from "./App";
import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
    url: 'http://localhost:8180/',
    realm: 'movietracker',
    clientId: 'external-client'
});

const container = document.getElementById("root") as HTMLElement;
const root = createRoot(container);

root.render(
  <ReactKeycloakProvider authClient={keycloak}>
    <App />
  </ReactKeycloakProvider>
);
