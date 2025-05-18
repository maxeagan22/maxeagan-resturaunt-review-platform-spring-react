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
      .nullable(),
    tuesday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
    wednesday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
    thursday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
    friday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
    saturday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
    sunday: z
      .object({
        openTime: z.string(),
        closeTime: z.string(),
      })
      .nullable(),
  }),
  photos: z.array(z.string()),
});

export type RestaurantFormData = z.infer<typeof restaurantSchema>;
