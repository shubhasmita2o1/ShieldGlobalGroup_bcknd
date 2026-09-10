import { apiRequest } from "@/lib/api-client";
import { users } from "@/lib/mock-data";
import type { AdminUser, PermissionModule, UserRole, UserStatus } from "@/types/admin";

export interface UserQuery {
  search?: string;
  role?: string;
  status?: string;
}

/** Spring Boot mapping: /api/admin/users */
export const usersApi = {
  list(query: UserQuery = {}): Promise<AdminUser[]> {
    return apiRequest<AdminUser[]>("/users", {
      query: query as Record<string, string | undefined>,
      mockResolver: () => {
        const q = (query.search ?? "").trim().toLowerCase();
        return users.filter(
          (u) =>
            (!q || u.name.toLowerCase().includes(q) || u.email.toLowerCase().includes(q)) &&
            (!query.role || query.role === "ALL" || u.role === query.role) &&
            (!query.status || query.status === "ALL" || u.status === query.status),
        );
      },
    });
  },

  create(input: {
    name: string;
    email: string;
    phone?: string;
    role: UserRole;
    permissions: PermissionModule[];
  }): Promise<AdminUser> {
    return apiRequest<AdminUser>("/users", {
      method: "POST",
      body: input,
      mockDelay: 500,
      mockResolver: () => {
        const created: AdminUser = {
          id: `usr-${Date.now()}`,
          name: input.name,
          email: input.email,
          phone: input.phone ?? "",
          role: input.role,
          status: "INVITED",
          lastLogin: null,
          createdAt: new Date().toISOString(),
          permissions: input.permissions,
        };
        users.unshift(created);
        return created;
      },
    });
  },

  update(id: string, input: Partial<AdminUser>): Promise<AdminUser> {
    return apiRequest<AdminUser>(`/users/${id}`, {
      method: "PUT",
      body: input,
      mockDelay: 450,
      mockResolver: () => {
        const user = users.find((u) => u.id === id);
        if (!user) throw new Error("User not found");
        Object.assign(user, input);
        return user;
      },
    });
  },

  setStatus(id: string, status: UserStatus): Promise<AdminUser> {
    return usersApi.update(id, { status });
  },

  remove(id: string): Promise<void> {
    return apiRequest<void>(`/users/${id}`, {
      method: "DELETE",
      mockResolver: () => {
        const index = users.findIndex((u) => u.id === id);
        if (index >= 0) users.splice(index, 1);
      },
    });
  },
};
