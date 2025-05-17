"use client";

import React, { useState, useEffect } from "react";
import { Upload, Search } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useAppContext } from "@/providers/AppContextProvider";
import { useAuth } from "react-oidc-context";
import { z } from "zod";

// Schemas
const fileSchema = z
  .instanceof(File)
  .refine((file) => file.size < 5 * 1024 * 1024, "File size must be under 5MB")
  .refine((file) => file.type.startsWith("image/"), "Must be an image");

const responseSchema = z.object({
  url: z.string().url(),
  id: z.string(),
  filename: z.string(),
});

const filenameSchema = z
  .string()
  .min(1)
  .regex(/^[\w,\s-]+\.[A-Za-z]{3,4}$/);

export default function ImageUploadTestPage() {
  const { apiService } = useAppContext();
  const {
    isAuthenticated,
    signinRedirect,
    isLoading: isAuthLoading,
  } = useAuth();

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [response, setResponse] = useState<any>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<any>(null);
  const [filename, setFileName] = useState("");
  const [retrievedImage, setRetrievedImage] = useState<string | null>(null);
  const [retrieveError, setRetrieveError] = useState<string | null>(null);

  useEffect(() => {
    const doUseEffect = async () => {
      if (!isAuthenticated && !isAuthLoading) {
        await signinRedirect();
      }
    };
    doUseEffect();
  }, [isAuthenticated, isAuthLoading, signinRedirect]);

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    setResponse(null);
    setError(null);

    if (!file) {
      setSelectedFile(null);
      return;
    }

    const parsed = fileSchema.safeParse(file);
    if (!parsed.success) {
      setError({ message: parsed.error.errors[0].message });
      setSelectedFile(null);
    } else {
      setSelectedFile(file);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile || !apiService) return;

    setIsLoading(true);
    setError(null);

    try {
      const startTime = performance.now();
      const photo = await apiService.uploadPhoto(selectedFile);
      const endTime = performance.now();

      const parsed = responseSchema.safeParse(photo);
      if (!parsed.success) {
        throw new Error("Invalid response structure");
      }

      setResponse({
        status: 200,
        timing: `${(endTime - startTime).toFixed(2)}ms`,
        data: parsed.data,
      });
    } catch (err: any) {
      setError({
        status: err.status || 500,
        message: err.message || "Unknown error occurred",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleRetrievePhoto = async () => {
    if (!filename.trim()) return;

    const parsed = filenameSchema.safeParse(filename);
    if (!parsed.success) {
      setRetrieveError("Invalid filename format.");
      return;
    }

    setRetrievedImage(null);
    setRetrieveError(null);

    try {
      const res = await fetch(`/api/photos/${filename}`);
      if (!res.ok)
        throw new Error(`Failed to retrieve photo: ${res.statusText}`);

      const blob = await res.blob();
      const imageUrl = URL.createObjectURL(blob);
      setRetrievedImage(imageUrl);
    } catch (err: any) {
      setRetrieveError(err.message);
    }
  };

  return (
    <div className="max-w-3xl mx-auto p-6">
      <Card>
        <CardHeader>
          <CardTitle>Image Upload Test Interface</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div className="border-2 border-dashed rounded-lg p-8">
            <div className="flex flex-col items-center justify-center gap-4">
              <Upload className="w-12 h-12 text-gray-400" />
              <label className="cursor-pointer">
                <span className="bg-primary text-primary-foreground px-4 py-2 rounded-md hover:bg-primary/90">
                  Select Image
                </span>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileSelect}
                  className="hidden"
                />
              </label>
              {selectedFile && (
                <div className="text-sm text-gray-600">
                  Selected: {selectedFile.name} (
                  {(selectedFile.size / 1024).toFixed(2)} KB)
                </div>
              )}
            </div>
          </div>

          <Button
            onClick={handleUpload}
            disabled={!selectedFile || isLoading}
            className="w-full"
          >
            {isLoading ? "Uploading..." : "Upload Image"}
          </Button>

          {(response || error) && (
            <div className="mt-8">
              <h3 className="text-lg font-semibold mb-2">Response Details</h3>
              <div className="bg-gray-100 p-4 rounded-lg font-mono text-sm">
                <pre className="whitespace-pre-wrap overflow-x-auto">
                  {JSON.stringify(error || response, null, 2)}
                </pre>
              </div>
            </div>
          )}

          <div className="border-t pt-6">
            <h3 className="text-lg font-semibold mb-4">Retrieve Photo</h3>
            <div className="flex gap-2">
              <Input
                type="text"
                placeholder="Enter filename (e.g., photo.jpg)"
                value={filename}
                onChange={(e) => setFileName(e.target.value)}
                className="flex-1"
              />
              <Button
                onClick={handleRetrievePhoto}
                disabled={!filename.trim()}
                className="flex items-center gap-2"
              >
                <Search className="w-4 h-4" />
                Retrieve
              </Button>
            </div>

            {retrieveError && (
              <div className="mt-4 p-4 bg-red-50 text-red-600 rounded-lg">
                {retrieveError}
              </div>
            )}

            {retrievedImage && (
              <div className="mt-4">
                <img
                  src={retrievedImage}
                  alt="Retrieved photo"
                  className="max-w-full h-auto rounded-lg"
                />
              </div>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
