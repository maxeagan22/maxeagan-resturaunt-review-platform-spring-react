import { useFormContext } from "react-hook-form";
import { Input } from "./ui/input";
import { Label } from "@radix-ui/react-label";
import { Button } from "./ui/button";
import { useEffect, useState } from "react";
import Image from "next/image";
import { Photo } from "@/domain/domain";
import { Trash2 } from "lucide-react";

/**
 * RestaurantForm Component
 *
 * This component renders a dynamic form for creating or editing restaurant entries.
 * It uses `react-hook-form` for form state management, integrates image upload
 * functionality, and supports interactive preview and deletion of uploaded images.
 *
 * Sections:
 * - Basic Information: Restaurant name, cuisine type, contact info.
 * - Address: Street, city, state, country, postal code.
 * - Operating Hours: Open/close times for each day of the week.
 * - Photo Upload: Upload, preview, and remove images.
 *
 * Props:
 * @param uploadPhoto - A callback to handle photo upload, returns a Photo object.
 * @param error - Optional string to display form-level errors.
 *
 * Behavior:
 * - Pre-populates photo previews if photos are already stored in form state.
 * - Handles file input for uploading multiple images with live preview.
 * - Removes photos from previews and form state when deleted.
 * - Sets time values conditionally, allowing nullable operating hours.
 *
 * Usage:
 * Must be used inside a FormProvider from react-hook-form.
 */

const daysOfWeek = [
  "monday",
  "tuesday",
  "wednesday",
  "thursday",
  "friday",
  "saturday",
  "sunday",
];

interface RestaurantFormProps {
  uploadPhoto: (file: File, caption?: string) => Promise<Photo>;
  error?: string;
}

export default function RestaurantForm({
  uploadPhoto,
  error,
}: RestaurantFormProps) {
  const methods = useFormContext();
  const { register, setValue } = methods;
  const [previews, setPreviews] = useState<string[]>([]);

  /**
   * useEffect to load existing photos into preview state on component mount.
   * Converts stored photo IDs into accessible URLs and sets preview images.
   */
  useEffect(() => {
    const exisitngPhotoIds = methods.getValues("photos") || [];
    if (exisitngPhotoIds.length > 0) {
      const existingPreviews = exisitngPhotoIds.map(
        (photoId: string) => `/api/photos/${photoId}`
      );
      setPreviews(existingPreviews);
    }
  }, [methods]);

  /**
   * Handles photo file input changes.
   * Uploads selected files, updates form state with photo URLs,
   * and generates local preview URLs for client display.
   *
   * @param e - React.ChangeEvent from file input.
   */
  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    const uplodadedPhotos = await Promise.all(
      files.map((photo) => uploadPhoto(photo))
    );

    const existingPhotosIds = methods.getValues("photos");

    const photoIds = [
      ...existingPhotosIds,
      ...uplodadedPhotos.map((photo) => photo.url),
    ];

    setValue("photos", photoIds);

    const newPreviews = files.map((file) => URL.createObjectURL(file));
    setPreviews([...previews, ...newPreviews]);
  };

  /**
   * Removes a photo preview and its corresponding form value.
   * Also revokes object URL if the preview is a blob to free memory.
   *
   * @param indexToRemove - Index of the photo to be removed.
   */
  const handleRemovePhoto = (indexToRemove: number) => {
    const updatedPreviews = previews.filter(
      (_, index) => index !== indexToRemove
    );
    setPreviews(updatedPreviews);

    const photoValues = methods.getValues("photos");
    const existingPhotoIds = Array.isArray(photoValues) ? photoValues : [];
    const updatedPhotoIds = existingPhotoIds.filter(
      (_, index) => index !== indexToRemove
    );
    setValue("photos", updatedPhotoIds);

    if (previews[indexToRemove].startsWith("blob:")) {
      URL.revokeObjectURL(previews[indexToRemove]);
    }
  };

  return (
    <div className="space-y-8">
      {/* Basic Info Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Basic Information</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="name">Restaurant Name</Label>
            <Input id="name" {...register("name", { required: true })} />
          </div>
          <div>
            <Label htmlFor="cuisineType">Cuisine Type</Label>
            <Input
              id="cuisineType"
              {...register("cuisineType", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="contactInformation">Contact Information</Label>
            <Input
              id="contactInformation"
              {...register("contactInformation", { required: true })}
            />
          </div>
        </div>
      </div>

      {/* Address Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Address</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="streetNumber">Street Number</Label>
            <Input
              id="streetNumber"
              {...register("address.streetNumber", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="streetName">Street Name</Label>
            <Input
              id="streetName"
              {...register("address.streetName", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="unit">Unit (Optional)</Label>
            <Input id="unit" {...register("address.unit")} />
          </div>
          <div>
            <Label htmlFor="city">City</Label>
            <Input
              id="city"
              {...register("address.city", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="state">State</Label>
            <Input
              id="state"
              {...register("address.state", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="postalCode">Postal Code</Label>
            <Input
              id="postalCode"
              {...register("address.postalCode", { required: true })}
            />
          </div>
          <div>
            <Label htmlFor="country">Country</Label>
            <Input
              id="country"
              {...register("address.country", { required: true })}
            />
          </div>
        </div>
      </div>

      {/* Operating Hours Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Operating Hours</h2>
        <div className="space-y-4">
          {daysOfWeek.map((day) => (
            <div key={day} className="flex items-center space-x-4">
              <Label className="w-24 capitalize">{day}</Label>
              <div className="flex-1 space-x-2">
                <Input
                  type="time"
                  className="w-[120px] inline-block"
                  onChange={(e) => {
                    if (e.target.value) {
                      // Only create/update if the user enters a value
                      const currentValues = methods.getValues(
                        `operatingHours.${day}`
                      ) || { openTime: "", closeTime: "" };
                      setValue(`operatingHours.${day}`, {
                        openTime: e.target.value,
                        closeTime: currentValues.closeTime || "",
                      });
                    } else {
                      // If input is cleared and closeTime is also empty, set day to null
                      const currentValues = methods.getValues(
                        `operatingHours.${day}`
                      );
                      if (!currentValues || !currentValues.closeTime) {
                        setValue(`operatingHours.${day}`, null);
                      } else {
                        setValue(`operatingHours.${day}`, {
                          openTime: "",
                          closeTime: currentValues.closeTime,
                        });
                      }
                    }
                  }}
                  defaultValue={
                    methods.getValues(`operatingHours.${day}`)?.openTime || ""
                  }
                />

                <span>to</span>

                <Input
                  type="time"
                  className="w-[120px] inline-block"
                  onChange={(e) => {
                    if (e.target.value) {
                      // Only create/update if the user enters a value
                      const currentValues = methods.getValues(
                        `operatingHours.${day}`
                      ) || { openTime: "", closeTime: "" };
                      setValue(`operatingHours.${day}`, {
                        openTime: currentValues.openTime || "",
                        closeTime: e.target.value,
                      });
                    } else {
                      // If input is cleared and openTime is also empty, set day to null
                      const currentValues = methods.getValues(
                        `operatingHours.${day}`
                      );
                      if (!currentValues || !currentValues.openTime) {
                        setValue(`operatingHours.${day}`, null);
                      } else {
                        setValue(`operatingHours.${day}`, {
                          openTime: currentValues.openTime,
                          closeTime: "",
                        });
                      }
                    }
                  }}
                  defaultValue={
                    methods.getValues(`operatingHours.${day}`)?.closeTime || ""
                  }
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Photos Section */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Restaurant Photos</h2>
        <div className="space-y-4">
          <div>
            <Label htmlFor="photos">Upload Restaurant Photos</Label>
            <Input
              id="photos"
              type="file"
              accept="image/*"
              multiple
              onChange={handleFileChange}
              className="hidden"
            />
            <Button
              type="button"
              variant="outline"
              onClick={() => document.getElementById("photos")?.click()}
            >
              Select Photos
            </Button>
          </div>
          {previews.length > 0 && (
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mt-4">
              {previews.map((preview, index) => (
                <div key={index} className="relative aspect-square group">
                  <Image
                    src={preview || "/placeholder.svg"}
                    alt={`Preview ${index + 1}`}
                    fill
                    className="object-cover rounded-md"
                  />
                  {/* Trash can overlay */}
                  <div className="absolute inset-0 bg-black bg-opacity-40 opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center rounded-md">
                    <button
                      type="button"
                      className="p-2 bg-red-500 rounded-full text-white hover:bg-red-600 transition-colors"
                      onClick={() => handleRemovePhoto(index)}
                      aria-label="Remove photo"
                    >
                      <Trash2 size={20} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {error && <p>Error: {error}</p>}

      {/* Submit Button */}
      <Button type="submit" className="w-full">
        Submit Restaurant
      </Button>
    </div>
  );
}
