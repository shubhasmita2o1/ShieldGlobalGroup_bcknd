import { apiRequest } from "@/lib/api-client";
import { auditLogs } from "@/lib/mock-data";
import type { AuditLog, Paginated } from "@/types/admin";
import { paginate } from "./query-utils";

export interface AuditQuery {
  search?: string;
  action?: string;
  module?: string;
  user?: string;
  page?: number;
  pageSize?: number;
}

/** Spring Boot mapping: /api/admin/audit-logs */
export const auditApi = {
  list(query: AuditQuery = {}): Promise<Paginated<AuditLog>> {
    return apiRequest<Paginated<AuditLog>>("/audit-logs", {
      query: query as Record<string, string | number | undefined>,
      mockResolver: () => {
        const q = (query.search ?? "").trim().toLowerCase();
        const filtered = auditLogs
          .filter(
            (l) =>
              (!q || l.record.toLowerCase().includes(q) || l.user.toLowerCase().includes(q)) &&
              (!query.action || query.action === "ALL" || l.action === query.action) &&
              (!query.module || query.module === "ALL" || l.module === query.module) &&
              (!query.user || query.user === "ALL" || l.user === query.user),
          )
          .sort((a, b) => b.createdAt.localeCompare(a.createdAt));
        return paginate(filtered, query.page ?? 1, query.pageSize ?? 10);
      },
    });
  },

  filters(): { actions: string[]; modules: string[]; users: string[] } {
    return {
      actions: Array.from(new Set(auditLogs.map((l) => l.action))),
      modules: Array.from(new Set(auditLogs.map((l) => l.module))),
      users: Array.from(new Set(auditLogs.map((l) => l.user))),
    };
  },
};
