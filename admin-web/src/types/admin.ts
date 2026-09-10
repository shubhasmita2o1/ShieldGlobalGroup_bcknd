export type ContentStatus = "PUBLISHED" | "DRAFT" | "ARCHIVED";

export type EnquiryStatus = "NEW" | "IN_PROGRESS" | "CONTACTED" | "CLOSED" | "SPAM";

export type UserRole = "SUPER_ADMIN" | "ADMIN" | "EDITOR";

export type UserStatus = "ACTIVE" | "DISABLED" | "INVITED";

export type PermissionModule =
  | "pages"
  | "services"
  | "companies"
  | "media"
  | "enquiries"
  | "seo"
  | "users"
  | "settings"
  | "audit-logs";

export type PermissionAction = "view" | "create" | "update" | "delete" | "publish";

export interface AdminUser {
  id: string;
  name: string;
  email: string;
  phone?: string;
  role: UserRole;
  status: UserStatus;
  avatarUrl?: string;
  lastLogin: string | null;
  createdAt: string;
  permissions: PermissionModule[];
}

export interface AuthSession {
  user: AdminUser;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface LoginPayload {
  email: string;
  password: string;
  rememberMe?: boolean;
}

/** Generic CMS record shared by every simple content module. */
export interface ContentRecord {
  id: string;
  title: string;
  subtitle?: string;
  category?: string;
  status: ContentStatus;
  author: string;
  updatedAt: string;
  order?: number;
}

export interface Paginated<T> {
  items: T[];
  page: number;
  pageSize: number;
  total: number;
}

export interface ListQuery {
  search?: string;
  status?: string;
  category?: string;
  sort?: string;
  page?: number;
  pageSize?: number;
}

export interface KpiMetric {
  id: string;
  label: string;
  value: number;
  change: number;
  changeLabel: string;
  supporting: string;
  icon: string;
}

export interface ActivityPoint {
  date: string;
  published: number;
  updated: number;
  drafted: number;
}

export interface DistributionSlice {
  name: string;
  value: number;
}

export interface ActivityItem {
  id: string;
  user: string;
  action: string;
  contentType: string;
  target: string;
  timestamp: string;
  tone: "success" | "info" | "warning" | "neutral";
}

export interface Enquiry {
  id: string;
  name: string;
  email: string;
  phone: string;
  company: string;
  subject: string;
  message: string;
  status: EnquiryStatus;
  createdAt: string;
  notes: EnquiryNote[];
  history: { id: string; label: string; at: string; by: string }[];
}

export interface EnquiryNote {
  id: string;
  author: string;
  body: string;
  createdAt: string;
}

export type MediaType = "IMAGE" | "VIDEO" | "DOCUMENT" | "LOGO";

export interface MediaAsset {
  id: string;
  name: string;
  type: MediaType;
  url: string;
  sizeKb: number;
  extension: string;
  uploadedAt: string;
  uploadedBy: string;
  altText: string;
  dimensions?: string;
}

export interface SeoEntry {
  id: string;
  page: string;
  path: string;
  metaTitle: string;
  metaDescription: string;
  slug: string;
  canonicalUrl: string;
  ogTitle: string;
  ogDescription: string;
  ogImage: string;
  robots: "index,follow" | "noindex,follow" | "index,nofollow" | "noindex,nofollow";
  updatedAt: string;
}

export type AuditAction =
  | "CREATE"
  | "UPDATE"
  | "DELETE"
  | "PUBLISH"
  | "UNPUBLISH"
  | "LOGIN"
  | "LOGOUT";

export interface AuditLog {
  id: string;
  user: string;
  action: AuditAction;
  module: string;
  record: string;
  createdAt: string;
  ipAddress: string;
  oldValue: Record<string, string> | null;
  newValue: Record<string, string> | null;
}

export interface SiteSettings {
  general: {
    websiteName: string;
    tagline: string;
    contactEmail: string;
    phone: string;
    address: string;
  };
  social: {
    linkedin: string;
    facebook: string;
    instagram: string;
    youtube: string;
    twitter: string;
  };
  contact: {
    contactEmail: string;
    contactPhone: string;
    officeAddress: string;
  };
  system: {
    maintenanceMode: boolean;
    defaultLanguage: string;
    timezone: string;
  };
}

export interface HealthStatus {
  id: string;
  label: string;
  value: string;
  state: "operational" | "degraded" | "down";
}

export interface NotificationItem {
  id: string;
  title: string;
  description: string;
  at: string;
  read: boolean;
  type: "enquiry" | "content" | "media" | "user";
}
