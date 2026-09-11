import { apiRequest, tokenStorage, USE_MOCKS } from "@/lib/api-client";
import { currentUser } from "@/lib/mock-data";
import type { AdminUser, AuthSession, LoginPayload, PermissionModule, UserRole } from "@/types/admin";

/**
 * Auth service.
 * Real Spring Boot endpoints:
 *   POST /api/auth/login   body: { username, password }
 *   GET  /api/auth/me      header: Authorization: Bearer <token>
 *
 * Login UI keeps an email field; the value is sent as `username` to the backend.
 */

const DEMO_EMAIL = "admin@shieldglobalgroup.com";
const DEMO_PASSWORD = "Admin@123";

const ALL_PERMISSIONS: PermissionModule[] = [
  "pages",
  "services",
  "companies",
  "media",
  "enquiries",
  "seo",
  "users",
  "settings",
  "audit-logs",
];

interface BackendLoginData {
  token: string;
  username: string;
  message?: string;
}

interface BackendMeData {
  id: number | string;
  username: string;
  role?: string;
  lastLogin?: string | null;
  createdAt?: string | null;
}

function mapRole(role?: string): UserRole {
  const normalized = (role ?? "ADMIN").toUpperCase().replace(/-/g, "_");
  if (normalized === "SUPER_ADMIN") return "SUPER_ADMIN";
  if (normalized === "EDITOR") return "EDITOR";
  return "ADMIN";
}

function mapBackendUser(data: BackendMeData): AdminUser {
  const role = mapRole(data.role);
  return {
    id: String(data.id),
    name: data.username,
    email: data.username,
    role,
    status: "ACTIVE",
    lastLogin: data.lastLogin ?? null,
    createdAt: data.createdAt ?? new Date().toISOString(),
    permissions: role === "SUPER_ADMIN" || role === "ADMIN" ? ALL_PERMISSIONS : ["pages", "media", "enquiries"],
  };
}

export const authApi = {
  async login(payload: LoginPayload): Promise<AuthSession> {
    if (USE_MOCKS) {
      return apiRequest<AuthSession>("/api/auth/login", {
        method: "POST",
        body: payload,
        mockDelay: 700,
        mockResolver: () => {
          if (
            payload.email.trim().toLowerCase() !== DEMO_EMAIL ||
            payload.password !== DEMO_PASSWORD
          ) {
            throw new Error("Invalid email address or password.");
          }
          return {
            user: currentUser,
            accessToken: "mock.access.token",
            refreshToken: "mock.refresh.token",
            expiresIn: 3600,
          } satisfies AuthSession;
        },
      });
    }

    // Real backend: map UI email → username
    const data = await apiRequest<BackendLoginData>("/api/auth/login", {
      method: "POST",
      body: {
        username: payload.email.trim(),
        password: payload.password,
      },
    });

    if (!data?.token) {
      throw new Error("Login succeeded but no token was returned.");
    }

    // Single-token backend: store token as both access and refresh for now
    tokenStorage.set(data.token, data.token);

    const user = await authApi.me();

    return {
      user,
      accessToken: data.token,
      refreshToken: data.token,
      expiresIn: 28800,
    };
  },

  async me(): Promise<AdminUser> {
    if (USE_MOCKS) {
      return apiRequest<AdminUser>("/api/auth/me", {
        mockDelay: 120,
        mockResolver: () => {
          if (!tokenStorage.access) throw new Error("Unauthorized");
          return currentUser;
        },
      });
    }

    const data = await apiRequest<BackendMeData>("/api/auth/me");
    return mapBackendUser(data);
  },

  refresh(): Promise<Pick<AuthSession, "accessToken" | "refreshToken">> {
    return apiRequest("/api/auth/refresh", {
      method: "POST",
      body: { refreshToken: tokenStorage.refresh },
      mockResolver: () => ({
        accessToken: tokenStorage.access ?? "mock.access.token",
        refreshToken: tokenStorage.refresh ?? "mock.refresh.token",
      }),
    });
  },

  async logout(): Promise<void> {
    if (USE_MOCKS) {
      return apiRequest<void>("/api/auth/logout", {
        method: "POST",
        mockDelay: 150,
        mockResolver: () => undefined,
      });
    }
    // Backend has no logout endpoint yet — clear client-side session only
    tokenStorage.clear();
  },

  updateProfile(input: Partial<AdminUser>): Promise<AdminUser> {
    return apiRequest<AdminUser>("/api/auth/profile", {
      method: "PUT",
      body: input,
      mockResolver: () => ({ ...currentUser, ...input }),
    });
  },

  changePassword(input: { currentPassword: string; newPassword: string }): Promise<void> {
    return apiRequest<void>("/api/auth/change-password", {
      method: "POST",
      body: input,
      mockDelay: 500,
      mockResolver: () => undefined,
    });
  },

  demoCredentials: { email: DEMO_EMAIL, password: DEMO_PASSWORD },
};