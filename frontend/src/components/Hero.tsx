/**
 * Hero component
 *
 * This is a  prominent section usually placed at the top of a landing page.
 * It features a full-width background image with a dark overlay, a headline, and a subtitle.
 * Designed to grab the user's attention and provide a quick value proposition.
 *
 * - Background image is loaded from /hero-image.jpg.
 * - Responsive design for better readability across devices.
 */
export default function Hero() {
  return (
    <div className="relative h-[400px] mb-8">
      <div className="absolute inset-0">
        <img
          src="/hero-image.jpg?height=400&width=1920"
          alt="Restaurant interior"
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/50" />
      </div>
      <div className="relative max-w-[1200px] mx-auto h-full flex flex-col justify-center px-4">
        <h1 className="text-4xl md:text-5xl font-bold text-white mb-4">
          Discover Your Next Favorite Restaurant
        </h1>
        <p className="text-lg md:text-xl text-white/90 max-w-[600px]">
          Read authentic reviews from real diners and find the perfect spot for
          your next meal.
        </p>
      </div>
    </div>
  );
}
