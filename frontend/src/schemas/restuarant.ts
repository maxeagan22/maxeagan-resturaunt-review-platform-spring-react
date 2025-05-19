import { z } from "zod";

export const restaurantSchema = z.object({
  name: z.string().min(1, "Name is required"),
  cuisineType: z.string().min(1, "Cuisine type is required"),
  contactInformation: z.string().min(1, "Contact information is required"),
  address: z.object({
    streetNumber: z.string().min(1, "Street number is required"),
    streetName: z.string().min(1, "Street name is required"),
    unit: z.string().optional(),
    city: z.string().min(1, "City is required"),
    state: z.string().min(1, "State is required"),
    postalCode: z.string().min(1, "Postal code is required"),
    country: z.string().min(1, "Country is required"),
  }),
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
  photos: z.array(z.string()),
});

export type RestaurantFormData = z.infer<typeof restaurantSchema>;
