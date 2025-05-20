import { Photo } from "@/domain/domain";
import { ApiService } from "@/services/api/apiService";

// Upload a photo file to the server using the provided API service.
// Returns the photo URL, which acts as the ID.

export const uploadPhoto = async (
  apiService: ApiService,
  file: File,
  caption?: string
): Promise<string> => {
  if (!apiService) throw new Error("API Service not available");
  const response = await apiService.uploadPhoto(file, caption);
  return response.url;
};

// Get preview URLs from a list of File objects.

export const createImagePreviews = (files: File[]): string[] => {
  return files.map((file) => URL.createObjectURL(file));
};

//Merge newly uploaded photos with existing ones to create a full photo ID list.

export const mergePhotoIds = (
  existingPhotos: Photo[],
  newUrls: string[]
): string[] => {
  const existingIds = existingPhotos.map((photo) => photo.url);
  return [...existingIds, ...newUrls];
};

//Removes a photo by index from existing or new arrays.
//Updates all 3 states: previews, files, and existingPhotos.

export const removePhotoByIndex = (
  index: number,
  previews: string[],
  existingPhotos: Photo[],
  newImages: File[]
): {
  previews: string[];
  existingPhotos: Photo[];
  newImages: File[];
} => {
  if (index < existingPhotos.length) {
    return {
      previews: previews.filter((_, i) => i !== index),
      existingPhotos: existingPhotos.filter((_, i) => i !== index),
      newImages,
    };
  } else {
    const adjustedIndex = index - existingPhotos.length;
    return {
      previews: previews.filter((_, i) => i !== index),
      existingPhotos,
      newImages: newImages.filter((_, i) => i !== adjustedIndex),
    };
  }
};
