import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";

import { authApi } from "@/api/authApi";
import { onUnauthorized, tokenStorage } from "@/lib/api-client";
import type { AdminUser, LoginPayload, PermissionModule } from "@/types/admin";

const USER_KEY = "sgg.cms.user";

interface AuthContextValue {
  user: AdminUser | null;
  isAuthenticated: boolean;
  isReady: boolean;
  login: (payload: LoginPayload) => Promise<AdminUser>;
  logout: () => Promise<void>;
  setUser: (user: AdminUser) => void;
  can: (module: PermissionModule) => boolean;
}

const AuthContext = createContext<AuthContextValue | null>(null);

function readStoredUser(): AdminUser | null {
  if (typeof window === "undefined") return null;
  try {
    const raw = window.localStorage.getItem(USER_KEY);
    return raw ? (JSON.parse(raw) as AdminUser) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUserState] = useState<AdminUser | null>(null);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const stored = readStoredUser();
    if (stored && tokenStorage.access) setUserState(stored);
    setIsReady(true);
  }, []);

  const setUser = useCallback((next: AdminUser) => {
    setUserState(next);
    window.localStorage.setItem(USER_KEY, JSON.stringify(next));
  }, []);

  const login = useCallback(
    async (payload: LoginPayload) => {
      const session = await authApi.login(payload);
      tokenStorage.set(session.accessToken, session.refreshToken);
      setUser(session.user);
      return session.user;
    },
    [setUser],
  );

  const logout = useCallback(async () => {
    try {
      await authApi.logout();
    } finally {
      tokenStorage.clear();
      window.localStorage.removeItem(USER_KEY);
      setUserState(null);
    }
  }, []);

  useEffect(() => {
    onUnauthorized(() => {
      tokenStorage.clear();
      if (typeof window !== "undefined") window.localStorage.removeItem(USER_KEY);
      setUserState(null);
    });
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      isAuthenticated: Boolean(user),
      isReady,
      login,
      logout,
      setUser,
      can: (module) => Boolean(user && (user.role === "SUPER_ADMIN" || user.permissions.includes(module))),
    }),
    [user, isReady, login, logout, setUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
