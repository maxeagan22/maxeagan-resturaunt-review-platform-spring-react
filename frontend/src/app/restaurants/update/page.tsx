"use client";

import { useForm, FormProvider } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { restaurantSchema, RestaurantFormData } from "@/schemas/restuarant";
import { Card, CardContent } from "@/components/ui/card";
import { useAppContext } from "@/providers/AppContextProvider";
import { CreateRestaurantRequest, Photo } from "@/domain/domain";
import CreateRestaurantForm from "@/components/CreateRestaurantForm";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import axios from "axios";

/**
 * Page for creating or updating a restaurant.
 *
 * - Uses `react-hook-form` with Zod schema for validation.
 * - Fetches existing restaurant data if `id` is present in URL params.
 * - Handles photo upload and restaurant form submission (create/update).
 *
 * @returns {JSX.Element} The page component
 */
export default function UpdateRestaurantPage() {
  const { apiService } = useAppContext();
  const router = useRouter();
  const searchParams = useSearchParams();

  const restaurantId = searchParams.get("id");
  const [error, setError] = useState<string | undefined>();
  const [loading, setLoading] = useState<boolean>(true);

  // Initialize form with default empty values and validation schema
  const methods = useForm<RestaurantFormData>({
    resolver: zodResolver(restaurantSchema),
    defaultValues: {
      name: "",
      cuisineType: "",
      contactInformation: "",
      address: {
        streetNumber: "",
        streetName: "",
        unit: "",
        city: "",
        state: "",
        postalCode: "",
        country: "",
      },
      operatingHours: {
        monday: undefined,
        tuesday: undefined,
        wednesday: undefined,
        thursday: undefined,
        friday: undefined,
        saturday: undefined,
        sunday: undefined,
      },
      photos: [],
    },
  });

  /**
   * Fetch existing restaurant data and populate form if `restaurantId` is present.
   */
  useEffect(() => {
    const fetchRestaurant = async () => {
      if (!apiService) return;

      setLoading(true);
      setError(undefined);

      if (!restaurantId) {
        setError("Restaurant ID must be provided");
        setLoading(false);
        return;
      }

      try {
        const restaurant = await apiService.getRestaurant(restaurantId);
        methods.reset({
          name: restaurant.name,
          cuisineType: restaurant.cuisineType,
          contactInformation: restaurant.contactInformation,
          address: {
            streetNumber: restaurant.address.streetNumber,
            streetName: restaurant.address.streetName,
            unit: restaurant.address.unit || "",
            city: restaurant.address.city,
            state: restaurant.address.state,
            postalCode: restaurant.address.postalCode,
            country: restaurant.address.country,
          },
          operatingHours: restaurant.operatingHours,
          photos: restaurant.photos?.map((p) => p.url) || [],
        });
      } catch (err) {
        if (axios.isAxiosError(err)) {
          if (err.response?.status === 404) {
            setError("Restaurant not found");
          } else {
            setError(
              `Error fetching restaurant: ${err.response?.status}: ${err.response?.data}`
            );
          }
        } else {
          setError("Error fetching restaurant data");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurant();
  }, [apiService, restaurantId, methods]);

  /**
   * Upload a photo file via API service.
   *
   * @param {File} file - The file to upload
   * @param {string} [caption] - Optional caption for the photo
   * @returns {Promise<Photo>} The uploaded photo object
   */
  const uploadPhoto = async (file: File, caption?: string): Promise<Photo> => {
    if (!apiService) throw new Error("API Service not available");
    return apiService.uploadPhoto(file, caption);
  };

  /**
   * Submit handler for the restaurant form.
   * Sends either a create or update request depending on presence of `restaurantId`.
   *
   * @param {RestaurantFormData} data - The form data
   */
  const onSubmit = async (data: RestaurantFormData) => {
    try {
      if (!apiService) throw new Error("API Service not available");

      const payload: CreateRestaurantRequest = {
        name: data.name,
        cuisineType: data.cuisineType,
        contactInformation: data.contactInformation,
        address: data.address,
        operatingHours: data.operatingHours,
        photoIds: data.photos,
      };

      setError(undefined);

      if (restaurantId) {
        await apiService.updateRestaurant(restaurantId, payload);
      } else {
        await apiService.createRestaurant(payload);
      }

      router.push("/");
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 400) {
          setError(err.response.data?.message);
        } else {
          setError(`API Error: ${err.response?.status}: ${err.response?.data}`);
        }
      } else {
        setError(String(err));
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-[100vh] h-full flex items-center justify-center">
        <p>Loading 🍝</p>
      </div>
    );
  }

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">
        {restaurantId ? "Update a Restaurant" : "Create a Restaurant"}
      </h1>
      <Card>
        <CardContent className="pt-6">
          <FormProvider {...methods}>
            <form onSubmit={methods.handleSubmit(onSubmit)}>
              <CreateRestaurantForm uploadPhoto={uploadPhoto} error={error} />
            </form>
          </FormProvider>
        </CardContent>
      </Card>
    </div>
  );
}
