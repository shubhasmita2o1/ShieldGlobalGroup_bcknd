import type { ContentRecord, ListQuery, Paginated } from "@/types/admin";

export function paginate<T>(items: T[], page = 1, pageSize = 8): Paginated<T> {
  const start = (page - 1) * pageSize;
  return { items: items.slice(start, start + pageSize), page, pageSize, total: items.length };
}

export function filterContent(records: ContentRecord[], query: ListQuery): ContentRecord[] {
  const search = (query.search ?? "").trim().toLowerCase();
  let out = records.filter((r) => {
    const matchesSearch =
      !search ||
      r.title.toLowerCase().includes(search) ||
      (r.subtitle ?? "").toLowerCase().includes(search) ||
      (r.category ?? "").toLowerCase().includes(search);
    const matchesStatus = !query.status || query.status === "ALL" || r.status === query.status;
    const matchesCategory =
      !query.category || query.category === "ALL" || r.category === query.category;
    return matchesSearch && matchesStatus && matchesCategory;
  });

  switch (query.sort) {
    case "title-asc":
      out = [...out].sort((a, b) => a.title.localeCompare(b.title));
      break;
    case "title-desc":
      out = [...out].sort((a, b) => b.title.localeCompare(a.title));
      break;
    case "updated-asc":
      out = [...out].sort((a, b) => a.updatedAt.localeCompare(b.updatedAt));
      break;
    case "order":
      out = [...out].sort((a, b) => (a.order ?? 0) - (b.order ?? 0));
      break;
    default:
      out = [...out].sort((a, b) => b.updatedAt.localeCompare(a.updatedAt));
  }
  return out;
}
