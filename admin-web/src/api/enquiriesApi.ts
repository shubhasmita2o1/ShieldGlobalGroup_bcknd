import { apiRequest } from "@/lib/api-client";
import { enquiries } from "@/lib/mock-data";
import type { Enquiry, EnquiryStatus, ListQuery, Paginated } from "@/types/admin";
import { paginate } from "./query-utils";

let store: Enquiry[] = enquiries;

export const enquiriesApi = {
  list(query: ListQuery = {}): Promise<Paginated<Enquiry>> {
    return apiRequest<Paginated<Enquiry>>("/enquiries", {
      query: { ...query } as Record<string, string | number | undefined>,
      mockResolver: () => {
        const search = (query.search ?? "").trim().toLowerCase();
        const filtered = store
          .filter((e) => {
            const matchesSearch =
              !search ||
              e.name.toLowerCase().includes(search) ||
              e.email.toLowerCase().includes(search) ||
              e.company.toLowerCase().includes(search) ||
              e.subject.toLowerCase().includes(search);
            const matchesStatus = !query.status || query.status === "ALL" || e.status === query.status;
            return matchesSearch && matchesStatus;
          })
          .sort((a, b) => b.createdAt.localeCompare(a.createdAt));
        return paginate(filtered, query.page ?? 1, query.pageSize ?? 8);
      },
    });
  },

  get(id: string): Promise<Enquiry> {
    return apiRequest<Enquiry>(`/enquiries/${id}`, {
      mockResolver: () => {
        const found = store.find((e) => e.id === id);
        if (!found) throw new Error("Enquiry not found");
        return found;
      },
    });
  },

  setStatus(id: string, status: EnquiryStatus): Promise<Enquiry> {
    return apiRequest<Enquiry>(`/enquiries/${id}/status`, {
      method: "PATCH",
      body: { status },
      mockResolver: () => {
        const found = store.find((e) => e.id === id);
        if (!found) throw new Error("Enquiry not found");
        found.status = status;
        found.history = [
          ...found.history,
          {
            id: `h-${Date.now()}`,
            label: `Status changed to ${status.replace("_", " ")}`,
            at: new Date().toISOString(),
            by: "Arjun Mehta",
          },
        ];
        return found;
      },
    });
  },

  addNote(id: string, body: string): Promise<Enquiry> {
    return apiRequest<Enquiry>(`/enquiries/${id}/notes`, {
      method: "POST",
      body: { body },
      mockResolver: () => {
        const found = store.find((e) => e.id === id);
        if (!found) throw new Error("Enquiry not found");
        found.notes = [
          ...found.notes,
          { id: `n-${Date.now()}`, author: "Arjun Mehta", body, createdAt: new Date().toISOString() },
        ];
        return found;
      },
    });
  },

  remove(id: string): Promise<void> {
    return apiRequest<void>(`/enquiries/${id}`, {
      method: "DELETE",
      mockResolver: () => {
        store = store.filter((e) => e.id !== id);
      },
    });
  },
};
