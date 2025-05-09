import RestaurantCard from "@/components/RestaurantCard";
import { RestaurantSummary } from "@/domain/domain";

const mockRestaurant: RestaurantSummary = {
  id: "1",
  name: "Mock Bistro",
  cuisineType: "Italian",
  averageRating: 4.2,
  address: {
    streeNumber: "111",
    streetName: "Fakeaddress Drive",
    city: "Maryvile",
    state: "Missouri",
    postalCode: "64468",
    country: "United States",
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
