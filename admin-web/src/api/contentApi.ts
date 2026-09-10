import { apiRequest } from "@/lib/api-client";
import { contentSets } from "@/lib/mock-data";
import type { ContentRecord, ContentStatus, ListQuery, Paginated } from "@/types/admin";
import { filterContent, paginate } from "./query-utils";

/**
 * Shared CRUD surface for the simple content modules.
 * Spring Boot mapping: /api/admin/{module}
 */
export type ContentModule =
  | "home"
  | "pages"
  | "services"
  | "companies"
  | "timeline"
  | "achievements"
  | "testimonials"
  | "partners"
  | "global-presence";

const store: Record<string, ContentRecord[]> = contentSets;

function bucket(module: ContentModule): ContentRecord[] {
  return store[module] ?? [];
}

export const contentApi = {
  list(module: ContentModule, query: ListQuery = {}): Promise<Paginated<ContentRecord>> {
    return apiRequest<Paginated<ContentRecord>>(`/${module}`, {
      query: { ...query } as Record<string, string | number | undefined>,
      mockResolver: () =>
        paginate(filterContent(bucket(module), query), query.page ?? 1, query.pageSize ?? 8),
    });
  },

  categories(module: ContentModule): string[] {
    return Array.from(new Set(bucket(module).map((r) => r.category ?? "General")));
  },

  remove(module: ContentModule, id: string): Promise<void> {
    return apiRequest<void>(`/${module}/${id}`, {
      method: "DELETE",
      mockResolver: () => {
        store[module] = bucket(module).filter((r) => r.id !== id);
      },
    });
  },

  setStatus(module: ContentModule, id: string, status: ContentStatus): Promise<ContentRecord> {
    return apiRequest<ContentRecord>(`/${module}/${id}/status`, {
      method: "PATCH",
      body: { status },
      mockResolver: () => {
        const list = bucket(module);
        const item = list.find((r) => r.id === id);
        if (!item) throw new Error("Record not found");
        item.status = status;
        item.updatedAt = new Date().toISOString();
        return item;
      },
    });
  },

  create(module: ContentModule, input: Partial<ContentRecord>): Promise<ContentRecord> {
    return apiRequest<ContentRecord>(`/${module}`, {
      method: "POST",
      body: input,
      mockResolver: () => {
        const created: ContentRecord = {
          id: `${module}-${Date.now()}`,
          title: input.title ?? "Untitled",
          subtitle: input.subtitle ?? "",
          category: input.category ?? "General",
          status: input.status ?? "DRAFT",
          author: input.author ?? "Arjun Mehta",
          updatedAt: new Date().toISOString(),
          order: bucket(module).length + 1,
        };
        store[module] = [created, ...bucket(module)];
        return created;
      },
    });
  },

  update(module: ContentModule, id: string, input: Partial<ContentRecord>): Promise<ContentRecord> {
    return apiRequest<ContentRecord>(`/${module}/${id}`, {
      method: "PUT",
      body: input,
      mockResolver: () => {
        const item = bucket(module).find((r) => r.id === id);
        if (!item) throw new Error("Record not found");
        Object.assign(item, input, { updatedAt: new Date().toISOString() });
        return item;
      },
    });
  },
};
