import { apiRequest } from "@/lib/api-client";
import { seoEntries } from "@/lib/mock-data";
import type { SeoEntry } from "@/types/admin";

/** Spring Boot mapping: /api/admin/seo */
export const seoApi = {
  list(search = ""): Promise<SeoEntry[]> {
    return apiRequest<SeoEntry[]>("/seo", {
      query: { search },
      mockResolver: () => {
        const q = search.trim().toLowerCase();
        return seoEntries.filter(
          (e) =>
            !q ||
            e.page.toLowerCase().includes(q) ||
            e.path.toLowerCase().includes(q) ||
            e.metaTitle.toLowerCase().includes(q),
        );
      },
    });
  },

  update(id: string, input: Partial<SeoEntry>): Promise<SeoEntry> {
    return apiRequest<SeoEntry>(`/seo/${id}`, {
      method: "PUT",
      body: input,
      mockDelay: 500,
      mockResolver: () => {
        const entry = seoEntries.find((e) => e.id === id);
        if (!entry) throw new Error("SEO entry not found");
        Object.assign(entry, input, { updatedAt: new Date().toISOString() });
        return entry;
      },
    });
  },
};
