"use client";

import RestaurantSearch from "@/components/RestaurantSearch";
import RestaurantList from "@/components/RestaurantList";
import Hero from "@/components/Hero";
import { useEffect, useState } from "react";
import { RestaurantSearchParams, RestaurantSummary } from "@/domain/domain";
import { useAppContext } from "@/providers/AppContextProvider";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

/**
 * Home page component displaying restaurant search, list, and pagination.
 *
 * Handles searching restaurants with pagination and displaying results.
 * Uses apiService from context for fetching data.
 *
 * @returns JSX.Element - Rendered Home page component
 */
export default function Home() {
  const { apiService } = useAppContext();

  // Loading state for restaurant data fetch
  const [loading, setLoading] = useState(true);
  // List of restaurant summaries returned from API
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  // Current page in pagination
  const [page, setPage] = useState(1);
  // Total number of pages available (optional)
  const [totalPages, setTotalPages] = useState<number | undefined>(undefined);
  // Indicates if current page is the first page
  const [first, setFirst] = useState<boolean>(true);
  // Indicates if current page is the last page
  const [last, setLast] = useState<boolean>(false);
  // Holds the current search parameters used for filtering restaurants
  const [currentSearchParams, setCurrentSearchParams] =
    useState<RestaurantSearchParams>({});

  /**
   * Fetch restaurants from API with given search params and optional target page.
   * Updates restaurants list, pagination state, and loading flag.
   *
   * @param {RestaurantSearchParams} params - Search filter params
   * @param {number} [targetPage] - Optional page number to fetch
   */
  const searchRestaurants = async (
    params: RestaurantSearchParams,
    targetPage?: number
  ) => {
    try {
      setLoading(true);
      setCurrentSearchParams(params);

      const paginatedParams = {
        ...params,
        page: targetPage || page,
        size: 8, // Page size fixed at 8 restaurants per page
      };

      console.log(`Searching for Restaurants: ${JSON.stringify(params)}`);
      if (!apiService?.searchRestaurants) {
        throw new Error("ApiService is not initialized!");
      }
      const restaurants = await apiService.searchRestaurants(paginatedParams);

      if (restaurants) {
        setTotalPages(restaurants.totalPages);
        setFirst(restaurants.first);
        setLast(restaurants.last);
        setRestaurants(restaurants.content);
      }
    } catch (error) {
      console.error("Error fetching restaurants:", error);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle user clicking a new page number in pagination.
   * Updates current page and triggers a new restaurant search.
   *
   * @param {number} newPage - The new page number to load
   */
  const handlePageChange = async (newPage: number) => {
    setPage(newPage);
    await searchRestaurants(currentSearchParams, newPage);
  };

  /**
   * Generate an array of page numbers and ellipses for pagination display.
   * Limits visible page numbers to a max, adding ellipses where appropriate.
   *
   * @returns {(number | "ellipsis")[]} Array of page numbers and ellipses
   */
  const getPageNumbers = () => {
    if (!totalPages) return [];

    const pageNumbers: (number | "ellipsis")[] = [];
    const maxVisiblePages = 5;

    if (totalPages <= maxVisiblePages) {
      // Show all pages if total pages less or equal max visible pages
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }

    // Always show first page
    pageNumbers.push(1);

    if (page > 3) {
      pageNumbers.push("ellipsis");
    }

    // Add pages around the current page
    for (
      let i = Math.max(2, page - 1);
      i <= Math.min(totalPages - 1, page + 1);
      i++
    ) {
      pageNumbers.push(i);
    }

    if (page < totalPages - 2) {
      pageNumbers.push("ellipsis");
    }

    // Always show last page if more than 1 page
    if (totalPages > 1) {
      pageNumbers.push(totalPages);
    }

    return pageNumbers;
  };

  // On initial load or apiService change, fetch restaurants with empty search params
  useEffect(() => {
    const doUseEffect = async () => {
      await searchRestaurants({});
    };

    if (!apiService) {
      return;
    }

    doUseEffect();
  }, [apiService]);

  return (
    <div>
      <Hero />
      <main className="max-w-[1200px] mx-auto px-4 py-8">
        <RestaurantSearch searchRestaurants={searchRestaurants} />
        <RestaurantList loading={loading} restaurants={restaurants} />
        {totalPages && totalPages > 1 && (
          <div className="mt-8">
            <Pagination>
              <PaginationContent>
                {!first && (
                  <PaginationItem>
                    <PaginationPrevious
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        handlePageChange(page - 1);
                      }}
                    />
                  </PaginationItem>
                )}

                {getPageNumbers().map((pageNum, index) => (
                  <PaginationItem key={`${pageNum}-${index}`}>
                    {pageNum === "ellipsis" ? (
                      <PaginationEllipsis />
                    ) : (
                      <PaginationLink
                        href="#"
                        isActive={pageNum === page}
                        onClick={(e) => {
                          e.preventDefault();
                          handlePageChange(pageNum);
                        }}
                      >
                        {pageNum}
                      </PaginationLink>
                    )}
                  </PaginationItem>
                ))}

                {!last && (
                  <PaginationItem>
                    <PaginationNext
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        handlePageChange(page + 1);
                      }}
                    />
                  </PaginationItem>
                )}
              </PaginationContent>
            </Pagination>
          </div>
        )}
      </main>
    </div>
  );
}
