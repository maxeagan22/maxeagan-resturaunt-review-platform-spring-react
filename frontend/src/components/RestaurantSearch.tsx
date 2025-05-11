"use client";

import { useState, useRef } from "react";
import { Search, Star } from "lucide-react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { RestaurantSearchParams } from "@/domain/domain";

/**
 * Props for the RestaurantSearch component.
 */
interface RestaurantSearchProps {
  /**
   * Callback function to perform a restaurant search.
   * Takes query text and minimum rating as parameters.
   */
  searchRestaurants: (params: RestaurantSearchParams) => Promise<void>;
}

/**
 * RestaurantSearch Component
 *
 * A search and filter UI for querying restaurants by text and minimum star rating.
 * This component includes:
 * - A text input for the query.
 * - Three buttons to filter by 2+, 3+, or 4+ star ratings.
 * - A submit button to execute the search manually.
 *
 * State:
 * query: the search string typed by the user.
 * minRating`: the minimum star rating selected.
 *
 * This component is controlled and stateless with regard to results—it only triggers searches via props.
 */
export default function RestaurantSearch(props: RestaurantSearchProps) {
  const { searchRestaurants } = props;

  const [query, setQuery] = useState("");
  const [minRating, setMinRating] = useState<number | undefined>(undefined);

  // Track initial render to avoid unintended side effects.
  const inInitialRender = useRef(true);

  const handleSearch = async () => {
    await searchRestaurants({
      q: query,
      minRating,
    });
  };

  const handleSearchFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // ← FIX: you missed the parentheses here
    await handleSearch();
  };

  const handleMinRatingFilter = async (value: number) => {
    // Toggle rating filter
    if (minRating !== value) {
      setMinRating(value);
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: value,
        });
      }, 0);
    } else {
      setMinRating(undefined);
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: undefined,
        });
      }, 0);
    }
  };

  return (
    <div>
      <form onSubmit={handleSearchFormSubmit} className="flex gap-2 mb-4">
        <Input
          type="text"
          placeholder="Searching for restaurants..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="flex-grow"
        />
        <Button type="submit">
          <Search className="mr-2 h-4 w-4" /> Search
        </Button>
      </form>

      <div className="flex mb-8 gap-2">
        <Button
          onClick={() => handleMinRatingFilter(2)}
          variant={minRating === 2 ? "default" : "outline"}
        >
          <Star /> 2+
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(3)}
          variant={minRating === 3 ? "default" : "outline"}
        >
          <Star /> 3+
        </Button>

        <Button
          onClick={() => handleMinRatingFilter(4)}
          variant={minRating === 4 ? "default" : "outline"}
        >
          <Star /> 4+
        </Button>
      </div>
    </div>
  );
}
