"use client";

import { useEffect, useRef, useState } from "react";
import { GeoLocation } from "@/domain/domain";
import dynamic from "next/dynamic";
import { MapPin } from "lucide-react";
import ReactDOMServer from "react-dom/server";

/**
 * OpenStreetMap
 *
 * A client-only, interactive map component that displays a static view
 * centered on a given geolocation using OpenStreetMap tiles and Leaflet.
 *
 * - SSR is disabled via next/dynamic.
 * - Leaflet is loaded dynamically on the client to avoid SSR issues.
 * - All user interactions (zoom, drag, scroll) are disabled.
 * - A custom marker icon is rendered using Lucide's `MapPin` component via react-dom/server.
 *
 * Props:
 * - location: GeoLocation – An object with `latitude` and `longitude` fields used to center the map.
 *
 * Dependencies:
 * - leaflet (runtime import only)
 * - lucide-react for icon rendering
 * - react-dom/server to convert JSX icon to HTML string for use in Leaflet
 *
 * Behavior:
 * - Renders nothing on the server (SSR false)
 * - On client mount:
 *   - Loads Leaflet and its CSS.
 *   - Instantiates a non-interactive map.
 *   - Adds a single custom marker at the specified coordinates.
 */

interface OpenStreetMapProps {
  location: GeoLocation;
}

// Create a component that will only be rendered on the client side
const MapComponent = ({ location }: OpenStreetMapProps) => {
  const mapRef = useRef<HTMLDivElement>(null);
  const [isClient, setIsClient] = useState(false);

  useEffect(() => {
    setIsClient(true);
  }, []);

  useEffect(() => {
    if (!isClient || !mapRef.current) return;

    const initializeMap = async () => {
      if (location && location.latitude && location.longitude) {
        // Dynamically import Leaflet
        const L = await import("leaflet");
        // Import the CSS
        await import("leaflet/dist/leaflet.css");

        const map = L.map(mapRef.current, {
          zoomControl: false, // Remove zoom controls
          dragging: false, // Disable panning
          touchZoom: false, // Disable touch zoom
          scrollWheelZoom: false, // Disable scroll zoom
          doubleClickZoom: false, // Disable double click zoom
          boxZoom: false, // Disable box zoom
          keyboard: false, // Disable keyboard navigation
        }).setView([location.latitude, location.longitude], 16);

        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map);

        // Create custom icon using Lucide React
        const markerSvg = ReactDOMServer.renderToString(
          <MapPin color="#000000" size={36} />
        );

        const customIcon = L.divIcon({
          html: markerSvg,
          className: "custom-marker",
          iconSize: [24, 24],
          iconAnchor: [12, 24], // Center bottom of the marker
        });

        // Add marker with custom icon
        L.marker([location.latitude, location.longitude], {
          icon: customIcon,
        }).addTo(map);

        return () => {
          map.remove();
        };
      } else {
        console.log("Unable to load map");
      }
    };

    const cleanup = initializeMap();

    return () => {
      cleanup.then((cleanupFn) => cleanupFn?.());
    };
  }, [location, isClient]);

  // Add CSS for the custom marker
  return (
    <>
      <style jsx global>{`
        .custom-marker {
          filter: drop-shadow(0 1px 2px rgb(0 0 0 / 0.1));
        }
      `}</style>
      <div ref={mapRef} className="w-full h-60 rounded-lg z-10" />
    </>
  );
};

// Create a dynamic component with SSR disabled
const OpenStreetMap = dynamic(() => Promise.resolve(MapComponent), {
  ssr: false,
});

export default OpenStreetMap;
