/**
 * Centralised API client.
 *
 * Every module in `src/api` goes through this client. When
 * `VITE_USE_MOCKS=false` and `VITE_API_BASE_URL` points at the Spring Boot
 * backend, the same call signatures hit real REST endpoints.
 */

export const API_BASE_URL: string =
  (import.meta.env["VITE_API_BASE_URL"] as string | undefined) ?? "http://localhost:8080";

export const USE_MOCKS: boolean =
  (import.meta.env["VITE_USE_MOCKS"] as string | undefined) !== "false";

const ACCESS_TOKEN_KEY = "sgg.cms.accessToken";
const REFRESH_TOKEN_KEY = "sgg.cms.refreshToken";

export const tokenStorage = {
  get access() {
    if (typeof window === "undefined") return null;
    return window.localStorage.getItem(ACCESS_TOKEN_KEY);
  },
  get refresh() {
    if (typeof window === "undefined") return null;
    return window.localStorage.getItem(REFRESH_TOKEN_KEY);
  },
  set(access: string, refresh: string) {
    if (typeof window === "undefined") return;
    window.localStorage.setItem(ACCESS_TOKEN_KEY, access);
    window.localStorage.setItem(REFRESH_TOKEN_KEY, refresh);
  },
  clear() {
    if (typeof window === "undefined") return;
    window.localStorage.removeItem(ACCESS_TOKEN_KEY);
    window.localStorage.removeItem(REFRESH_TOKEN_KEY);
  },
};

export class ApiError extends Error {
  status: number;
  constructor(message: string, status = 500) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

type UnauthorizedHandler = () => void;
let unauthorizedHandler: UnauthorizedHandler | null = null;
export function onUnauthorized(handler: UnauthorizedHandler) {
  unauthorizedHandler = handler;
}

export interface RequestOptions<TBody = unknown> {
  method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
  body?: TBody;
  query?: Record<string, string | number | boolean | undefined>;
  /** Local stand-in used until the Spring Boot API is wired up. */
  mockResolver?: () => Promise<unknown> | unknown;
  /** Artificial latency for mock resolvers, ms. */
  mockDelay?: number;
  signal?: AbortSignal;
}

function buildUrl(path: string, query?: RequestOptions["query"]) {
  const url = `${API_BASE_URL.replace(/\/$/, "")}/${path.replace(/^\//, "")}`;
  if (!query) return url;
  const params = new URLSearchParams();
  Object.entries(query).forEach(([key, value]) => {
    if (value !== undefined && value !== "") params.set(key, String(value));
  });
  const qs = params.toString();
  return qs ? `${url}?${qs}` : url;
}

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/** Unwrap Spring Boot `{ success, message, data }` envelope when present. */
function unwrapResponse<T>(json: unknown): T {
  if (
    json !== null &&
    typeof json === "object" &&
    "success" in json &&
    typeof (json as { success: unknown }).success === "boolean"
  ) {
    const envelope = json as { success: boolean; message?: string; data?: unknown };
    if (!envelope.success) {
      throw new ApiError(envelope.message || "Request failed", 400);
    }
    return envelope.data as T;
  }
  return json as T;
}

async function readErrorMessage(response: Response): Promise<string> {
  try {
    const json = (await response.json()) as { message?: string; success?: boolean };
    if (json && typeof json.message === "string" && json.message.trim()) {
      return json.message;
    }
  } catch {
    // ignore parse errors
  }
  return `Request failed with status ${response.status}`;
}

export async function apiRequest<TResponse, TBody = unknown>(
  path: string,
  options: RequestOptions<TBody> = {},
): Promise<TResponse> {
  const { mockResolver, mockDelay = 320 } = options;

  if (USE_MOCKS && mockResolver) {
    await delay(mockDelay);
    return (await mockResolver()) as TResponse;
  }

  const response = await fetch(buildUrl(path, options.query), {
    method: options.method ?? "GET",
    signal: options.signal ?? null,
    headers: {
      "Content-Type": "application/json",
      ...(tokenStorage.access ? { Authorization: `Bearer ${tokenStorage.access}` } : {}),
    },
    body: options.body ? JSON.stringify(options.body) : null,
  });

  if (response.status === 401) {
    unauthorizedHandler?.();
    const message = await readErrorMessage(response);
    throw new ApiError(message || "Session expired. Please sign in again.", 401);
  }

  if (!response.ok) {
    const message = await readErrorMessage(response);
    throw new ApiError(message, response.status);
  }

  if (response.status === 204) return undefined as TResponse;

  const json = await response.json();
  return unwrapResponse<TResponse>(json);
}