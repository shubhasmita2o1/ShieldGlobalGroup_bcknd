import { apiRequest, tokenStorage } from "@/lib/api-client";
import { currentUser } from "@/lib/mock-data";
import type { AdminUser, AuthSession, LoginPayload } from "@/types/admin";

/**
 * Auth service abstraction.
 * Spring Security / JWT endpoints:
 *   POST /api/admin/auth/login
 *   GET  /api/admin/auth/me
 *   POST /api/admin/auth/refresh
 *   POST /api/admin/auth/logout
 */

const DEMO_EMAIL = "admin@shieldglobalgroup.com";
const DEMO_PASSWORD = "Shield@2026";

export const authApi = {
  login(payload: LoginPayload): Promise<AuthSession> {
    return apiRequest<AuthSession>("/auth/login", {
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
  },

  me(): Promise<AdminUser> {
    return apiRequest<AdminUser>("/auth/me", {
      mockDelay: 120,
      mockResolver: () => {
        if (!tokenStorage.access) throw new Error("Unauthorized");
        return currentUser;
      },
    });
  },

  refresh(): Promise<Pick<AuthSession, "accessToken" | "refreshToken">> {
    return apiRequest("/auth/refresh", {
      method: "POST",
      body: { refreshToken: tokenStorage.refresh },
      mockResolver: () => ({ accessToken: "mock.access.token", refreshToken: "mock.refresh.token" }),
    });
  },

  logout(): Promise<void> {
    return apiRequest<void>("/auth/logout", {
      method: "POST",
      mockDelay: 150,
      mockResolver: () => undefined,
    });
  },

  updateProfile(input: Partial<AdminUser>): Promise<AdminUser> {
    return apiRequest<AdminUser>("/auth/profile", {
      method: "PUT",
      body: input,
      mockResolver: () => ({ ...currentUser, ...input }),
    });
  },

  changePassword(input: { currentPassword: string; newPassword: string }): Promise<void> {
    return apiRequest<void>("/auth/change-password", {
      method: "POST",
      body: input,
      mockDelay: 500,
      mockResolver: () => undefined,
    });
  },

  demoCredentials: { email: DEMO_EMAIL, password: DEMO_PASSWORD },
};
