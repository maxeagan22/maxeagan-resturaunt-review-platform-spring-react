"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { ApiService } from "@/services/api/apiService";
import { useAuth } from "react-oidc-context";
import { AxiosApiService } from "@/services/api/axiosApiService";

/**
 * AppContextProvider
 *
 * This provider sets up and exposes a global application context for accessing the API service
 * across the client-side React app. It initializes an instance of AxiosApiService, which is
 * injected with the current authentication state from `eact-oidc-context.
 *
 * Usage:
 * - Wrap your application with <AppContextProvider> to provide access to the API service.
 * - Use useAppContext() in any child component to access:
 *    - apiService: the configured Axios API service instance
 *    - isInitalized: a boolean indicating when the service is ready to use
 *
 * Requirements:
 * - NEXT_PUBLIC_API_URL must be defined in your .env.local
 * - Must be used inside a browser environment (`window` check included)
 *
 * Example:
 * tsx
 * const { apiService, isInitalized } = useAppContext();
 *
 * useEffect(() => {
 *   if (isInitalized) {
 *     apiService.get("/endpoint").then(...);
 *   }
 * }, [isInitalized]);
 *
 *
 * Throws:
 * - Calling useAppContext() outside of <AppContextProvider> will throw a runtime error.
 */

interface AppContextType {
  apiService: ApiService | null;
  isInitalized: boolean;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export function AppContextProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [apiService, setApiService] = useState<ApiService | null>(null);
  const [isInitalized, setIsInitalized] = useState(false);
  const auth = useAuth();

  useEffect(() => {
    if (typeof window === "undefined") {
      return;
    }

    try {
      const baseUrl = process.env.NEXT_PUBLIC_API_URL;
      if (!baseUrl) {
        throw Error("Base URL not defined");
      }

      // Create new api service instance when auth changes.
      const axiosApiService = new AxiosApiService(baseUrl, auth);
      setApiService(axiosApiService);
      setIsInitalized(true);
    } catch (error) {
      console.error("Failed to initalize services: ", error);
    }
  }, [auth]); // Add auth as dependency.

  return (
    <AppContext.Provider value={{ apiService, isInitalized }}>
      {children}
    </AppContext.Provider>
  );
}
export const useAppContext = () => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error("useAppContext must be within AppContextProvider");
  }
  return context;
};
