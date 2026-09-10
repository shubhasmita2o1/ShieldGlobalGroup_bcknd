import { apiRequest } from "@/lib/api-client";
import { media } from "@/lib/mock-data";
import type { MediaAsset, MediaType } from "@/types/admin";

let store: MediaAsset[] = media;

export interface MediaQuery {
  search?: string;
  type?: MediaType | "ALL";
}

export const mediaApi = {
  list(query: MediaQuery = {}): Promise<MediaAsset[]> {
    return apiRequest<MediaAsset[]>("/media", {
      query: { ...query } as Record<string, string | undefined>,
      mockResolver: () => {
        const search = (query.search ?? "").trim().toLowerCase();
        return store.filter((m) => {
          const matchesSearch = !search || m.name.toLowerCase().includes(search);
          const matchesType = !query.type || query.type === "ALL" || m.type === query.type;
          return matchesSearch && matchesType;
        });
      },
    });
  },

  /**
   * Upload abstraction. A Spring Boot endpoint returning a pre-signed S3 /
   * Cloudinary URL can replace the mock without touching the uploader UI.
   */
  upload(file: { name: string; sizeKb: number }): Promise<MediaAsset> {
    const extension = file.name.split(".").pop() ?? "bin";
    const type: MediaType = ["png", "jpg", "jpeg", "webp", "gif"].includes(extension)
      ? "IMAGE"
      : ["mp4", "mov", "webm"].includes(extension)
        ? "VIDEO"
        : ["svg"].includes(extension)
          ? "LOGO"
          : "DOCUMENT";
    return apiRequest<MediaAsset>("/media/upload", {
      method: "POST",
      body: file,
      mockDelay: 400,
      mockResolver: () => {
        const asset: MediaAsset = {
          id: `md-${Date.now()}-${Math.round(Math.random() * 1000)}`,
          name: file.name,
          type,
          extension,
          sizeKb: file.sizeKb,
          url: `https://cdn.shieldglobalgroup.com/media/${file.name}`,
          uploadedAt: new Date().toISOString(),
          uploadedBy: "Arjun Mehta",
          altText: file.name.replace(/[-_]/g, " ").replace(/\.\w+$/, ""),
        };
        store = [asset, ...store];
        return asset;
      },
    });
  },

  update(id: string, input: Partial<MediaAsset>): Promise<MediaAsset> {
    return apiRequest<MediaAsset>(`/media/${id}`, {
      method: "PUT",
      body: input,
      mockResolver: () => {
        const asset = store.find((m) => m.id === id);
        if (!asset) throw new Error("Asset not found");
        Object.assign(asset, input);
        return asset;
      },
    });
  },

  remove(id: string): Promise<void> {
    return apiRequest<void>(`/media/${id}`, {
      method: "DELETE",
      mockResolver: () => {
        store = store.filter((m) => m.id !== id);
      },
    });
  },
};
