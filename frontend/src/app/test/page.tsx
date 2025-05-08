import RestaurantCard from "@/components/RestaurantCard";
import { RestaurantSummary } from "@/domain/domain";

const mockRestaurant: RestaurantSummary = {
  id: "1",
  name: "Mock Bistro",
  cuisineType: "Italian",
  averageRating: 4.2,
  address: {
    city: "Rome",
    country: "Italy",
  },
  photos: [],
};

export default function TestPage() {
  return (
    <div className="p-10">
      <RestaurantCard restaurant={mockRestaurant} />
    </div>
  );
}
