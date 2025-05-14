"use client";

import { AuthProvider } from "react-oidc-context";
import { PropsWithChildren } from "react";

/**
 * oidcRedirectUrl
 *
 * The redirect URI for OIDC login callbacks.
 * This must match the "Valid Redirect URIs" set in your Keycloak client config.
 */
const oidcRedirectUrl = `${process.env.NEXT_PUBLIC_BASE_URL}`;

/**
 * oldConfig
 *
 * OIDC (OpenID Connect) configuration object used by react-oidc-context.
 * Pulls values from environment variables:
 * - NEXT_PUBLIC_KEYCLOAK_URL: The base URL of your Keycloak server.
 * - NEXT_PUBLIC_KEYCLOAK_REALM: The realm to authenticate against.
 * - NEXT_PUBLIC_KEYCLOAK_CLIENT_ID: The client ID registered with Keycloak.
 * - NEXT_PUBLIC_BASE_URL: The application's public base URL (used for redirect).
 *
 * onSignInCallback: Cleans up the URL after successful login to avoid full page reload.
 */
export const oldConfig = {
  authority:
    process.env.NEXT_PUBLIC_KEYCLOAK_URL +
    "/realms/" +
    process.env.NEXT_PUBLIC_KEYCLOAK_REALM,
  client_id: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID,
  redirect_uri: oidcRedirectUrl,

  onSignInCallback: () => {
    // Avoid page reload on succuessful sign-in.
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

/**
 * AppAuthProvider
 *
 * Wraps the application with OIDC authentication context.
 * Must be used at the root level (e.g., in _app.tsx or layout.tsx) to provide
 * authentication state to the rest of the application via `useAuth()` hook.
 *
 * @param children - React children to render inside the provider
 */
export function AppAuthProvider({ children }: PropsWithChildren) {
  return <AuthProvider {...oldConfig}>{children}</AuthProvider>;
}
