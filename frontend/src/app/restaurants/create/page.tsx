"use client";

import { useForm, FormProvider } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Card, CardContent } from "@/components/ui/card";
import { useAppContext } from "@/providers/AppContextProvider";
import { CreateRestaurantRequest, Photo } from "@/domain/domain";
import CreateRestaurantForm from "@/components/CreateRestaurantForm";
import { useState } from "react";
import axios from "axios";
import { restaurantSchema, RestaurantFormData } from "@/schemas/restuarant";

export default function CreateRestaurantPage() {
  const { apiService } = useAppContext();
  const [error, setError] = useState<string | undefined>();

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

  const uploadPhoto = async (file: File, caption?: string): Promise<Photo> => {
    if (!apiService) throw new Error("API service not available.");
    return apiService.uploadPhoto(file, caption);
  };

  const onSubmit = async (data: RestaurantFormData) => {
    try {
      if (!apiService) throw new Error("API service not available.");
      setError(undefined);

      const payload: CreateRestaurantRequest = {
        name: data.name,
        cuisineType: data.cuisineType,
        contactInformation: data.contactInformation,
        address: data.address,
        operatingHours: data.operatingHours,
        photoIds: data.photos,
      };

      await apiService.createRestaurant(payload);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 400) {
          setError(err.response.data?.message);
        } else {
          setError(`API Error: ${err.response?.status}: ${err.response?.data}`);
        }
      } else {
        setError((err as Error).message || "Unknown error");
      }
    }
  };

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Create a New Restaurant</h1>
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
