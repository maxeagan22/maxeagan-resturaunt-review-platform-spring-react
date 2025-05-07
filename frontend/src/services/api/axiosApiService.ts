"use client";

import {
  CreateRestaurantRequest,
  CreateReviewRequest,
  PaginatedResponse,
  Photo,
  Restaurant,
  RestaurantSearchParams,
  RestaurantSummary,
  Review,
  UpdateRestaurantRequest,
  UpdateReviewRequest,
} from "@/domain/domain";
import axios, {
    AxiosError,
  AxiosHeaders,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { useAuth } from "react-oidc-context";
import { ApiService } from "./apiService";
import { error } from "console";

export class AxiosApiService implements ApiService {
  // Axios instance to make HTTP requests
  private api: AxiosInstance;

  // `useAuth` return type, used to handle authentication
  private auth: ReturnType<typeof useAuth>;

  /**
   * Constructor to initialize the AxiosApiService
   *
   * @param baseUrl - The base URL for the API service
   * @param auth - The authentication handler returned from `useAuth` hook
   *
   * This constructor creates an instance of Axios configured with:
   * - a base URL for all requests
   * - default headers with "Content-Type" as "application/json"
   * - the option to include cookies with every request
   * - disables CSRF protection by setting the xsrfCookieName to undefined
   */
  constructor(baseUrl: string, auth: ReturnType<typeof useAuth>) {
    this.auth = auth;

    // Create a new Axios instance with the given configuration
    this.api = axios.create({
      baseURL: baseUrl,
      headers: {
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });

    // Disable CSRF protection by setting the xsrf cookie name to undefined
    this.api.defaults.xsrfCookieName = undefined;

    this.setupAuthInterceptor();
  }

  /**
   * Sets up Axios request interceptor to handle authentication token
   */
  private setupAuthInterceptor() {
    this.api.interceptors.request.use(
      async (config: InternalAxiosRequestConfig) => {
        const headers = new AxiosHeaders(config.headers);

        if (this.auth.isAuthenticated) {
          // Check if the token is expiring within the next 60 seconds
          const expiresAt = this.auth.user?.expires_at;
          const isExpiringSoon =
            expiresAt && expiresAt * 1000 - 60000 < Date.now();

          if (isExpiringSoon) {
            try {
              await this.auth.signinSilent();
            } catch (error) {
              console.error("Token refresh failed:", error);
              // Continue using existing token if refresh fails
            }
          }

          headers.setAuthorization(`Bearer ${this.auth.user?.access_token}`);
        }

        config.headers = headers;
        return config;
      },
      (error: AxiosError) => {
        return Promise.reject(error);
      }
    );

    this.api.interceptors.response.use(
        (response) => response,
        async(error: AxiosError) => {
            if (error.response?.status === 401){
                try{
                    await this.auth.signinSilent();
                    // Retry the original request. 
                    if (error.config){
                        const headers = new AxiosHeaders(error.config.headers);
                        headers.setAuthorization(
                            `Bearer ${this.auth.user?.access_token}`,
                        );
                        error.config.headers = headers;
                        return this.api.request(error.config);
                    }
                } catch (refreshError){
                    // if silent refresh fails, redirect to login
                    await this.auth.signinRedirect();
                }
            }
            return Promise.reject(error);
        },
    );
  }

  // Restauraunt endpoints

  // Endpoint to search for a restauraunt. 
  public async searchRestaurants(
    params: RestaurantSearchParams,
    ): Promise<PaginatedResponse<RestaurantSummary>>{ 
        const response: AxiosResponse<PaginatedResponse<RestaurantSummary>> =
        await this.api.get("/restaurants", { params });
    return response.data;        
}

public async getRestaurant(restaurantId: string): Promise<Restaurant> {
}

public async createRestaurant(request: CreateReviewRequest): Promise<Restaurant> {
    
}

public async updateRestaurant(restaurantId: string, request: UpdateRestaurantRequest): Promise<void> {
    
}

public async deleteRestaurant(restaurantId: string): Promise<void> {
    
}

public async getRestaurantReview(restaurantId: string, reviewId: string): Promise<Review> {
    
}

public async createReview(restaurantId: string, request: CreateReviewRequest): Promise<Review> {
    
}

public async updateReview(restaurantId: string, reviewId: string, request: UpdateRestaurantRequest): Promise<void> {
    
}

public async deleteReview(restaurantId: string, reviewId: string): Promise<void> {
    
}

public async uploadPhoto(file: File, caption?: string): Promise<Photo> {
    
}

}
