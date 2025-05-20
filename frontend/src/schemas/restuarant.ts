import { z } from "zod";

/**
 * Schema for validating restaurant form data.
 * Ensures required fields are present and substructures (like address and hours) are shaped correctly.
 */
export const restaurantSchema = z.object({
  // Name of the restaurant
  name: z.string().min(1, "Name is required"),

  // Type of cuisine the restaurant offers
  cuisineType: z.string().min(1, "Cuisine type is required"),

  // Contact details (e.g., phone number or email).
  contactInformation: z.string().min(1, "Contact information is required"),

  // Full physical address of the restaurant.
  address: z.object({
    streetNumber: z.string().min(1, "Street number is required"),
    streetName: z.string().min(1, "Street name is required"),
    unit: z.string().optional(),
    city: z.string().min(1, "City is required"),
    state: z.string().min(1, "State is required"),
    postalCode: z.string().min(1, "Postal code is required"),
    country: z.string().min(1, "Country is required"),
  }),

  /**
   * Weekly operating hours.
   * Each day is optional and contains an open and close time (as 24h strings like "09:00").
   */
  operatingHours: z.object({
    monday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    tuesday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    wednesday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    thursday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    friday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    saturday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
    sunday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .optional(),
  }),

  // Array of uploaded photo identifiers (e.g., filenames or URLs).
  photos: z.array(z.string()),
});

/**
 * Type representing the validated shape of the restaurant form data.
 */
export type RestaurantFormData = z.infer<typeof restaurantSchema>;
