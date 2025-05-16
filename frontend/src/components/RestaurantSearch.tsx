"use client";

import { useState, useRef } from "react";
import { Search, Star } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
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

  // Use a ref to track if this is the initial render
  const isInitialMount = useRef(true);

  const handleSearch = async () => {
    await searchRestaurants({
      q: query,
      minRating,
    });
  };

  const handleSearchFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await handleSearch();
  };

  const handleMinRatingFilter = async (value: number) => {
    // First update the state
    if (minRating !== value) {
      // Selecting a new rating
      setMinRating(value);

      // Wait until next tick to ensure state is updated
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: value, // Use the new value directly
        });
      }, 0);
    } else {
      // Deselecting the current rating
      setMinRating(undefined);

      // Wait until next tick to ensure state is updated
      setTimeout(() => {
        searchRestaurants({
          q: query,
          minRating: undefined, // Use undefined directly
        });
      }, 0);
    }
  };

  return (
    <div>
      <form onSubmit={handleSearchFormSubmit} className="flex gap-2 mb-4">
        <Input
          type="text"
          placeholder="Search for restaurants..."
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
