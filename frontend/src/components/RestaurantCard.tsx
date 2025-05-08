import Image from "next/image";
import Link from "next/link";
import { Star, Heart, Share2 } from "lucide-react";
import { Card } from "./ui/card";
import { RestaurantSummary } from "@/domain/domain";

interface RestaurantCardProps {
  restaurant: RestaurantSummary;
}

const getImage = (restaurant: RestaurantSummary) => {
  return restaurant.photos?.[0]?.url
    ? `/api/photos/${restaurant.photos[0].url}`
    : "/placeholder.svg";
};

const RatingStars = ({ rating }: { rating: number }) => {
  return (
    <div className="flex items-center gap-1 mb-2">
      {Array.from({ length: 5 }).map((_, i) => {
        return (
          <Star
            key={i}
            className={`w-4 h-4 ${
              i < Math.floor(rating)
                ? "text-yellow-400 fill-yellow-400"
                : "text-gray-300"
            }`}
          />
        );
      })}
    </div>
  );
};

const IconButton = ({
  icon,
  onClick,
}: {
  icon: React.ReactNode;
  onClick?: () => void;
}) => (
  <button
    onClick={onClick}
    className="flex items-center gap-1 text-muted-foreground hover:text-primary"
  >
    {icon}
  </button>
);

export default function RestaurantCard({ restaurant }: RestaurantCardProps) {
  return (
    <Link href={`/restaurants/${restaurant.id}`}>
      <Card className="overflow-hidden:show-lg transition-shadow">
        <div className="p-4">
          <div className="flex items-center gap-3 mb-4">
            <div>
              <p className="font-medium">{restaurant.name}</p>
              <p className="text-sm text-muted-foreground">
                {restaurant.address.city}, {restaurant.address.country}
              </p>
            </div>
          </div>
        </div>

        <div className="aspect-[4/3] relative mb-4">
          <Image
            src={getImage(restaurant)}
            alt={restaurant.name}
            fill
            className="object-cover rounded-md"
          />
        </div>

        <div className="p-4">
          <h2 className="text-xl font-semibold mb-2">{restaurant.name}</h2>
          <RatingStars rating={restaurant.averageRating ?? 0} />
          <p className="text-sm text-muted-foreground mb-4">
            {restaurant.cuisineType} Cuisine
          </p>

          <div className="flex justify-between border-t pt-4">
            <IconButton icon={<Heart className="w-5 h-5" />} />
            <IconButton icon={<Share2 className="w-5 h-5" />} />
          </div>
        </div>
      </Card>
    </Link>
  );
}
