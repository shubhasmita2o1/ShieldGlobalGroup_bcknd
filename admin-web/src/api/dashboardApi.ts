import { apiRequest } from "@/lib/api-client";
import {
  activitySeries,
  contentStatusBreakdown,
  distribution,
  health,
  kpis,
  notifications,
  recentActivity,
} from "@/lib/mock-data";
import type {
  ActivityItem,
  ActivityPoint,
  DistributionSlice,
  HealthStatus,
  KpiMetric,
  NotificationItem,
} from "@/types/admin";

export type DateRange = "7d" | "30d" | "90d";

export const dashboardApi = {
  kpis(): Promise<KpiMetric[]> {
    return apiRequest<KpiMetric[]>("/dashboard/kpis", { mockResolver: () => kpis });
  },
  activity(range: DateRange = "30d"): Promise<ActivityPoint[]> {
    const days = range === "7d" ? 7 : range === "90d" ? 90 : 30;
    return apiRequest<ActivityPoint[]>("/dashboard/activity", {
      query: { range },
      mockResolver: () => activitySeries.slice(Math.max(0, activitySeries.length - days)),
    });
  },
  distribution(): Promise<DistributionSlice[]> {
    return apiRequest<DistributionSlice[]>("/dashboard/distribution", {
      mockResolver: () => distribution,
    });
  },
  statusBreakdown(): Promise<typeof contentStatusBreakdown> {
    return apiRequest("/dashboard/status-breakdown", { mockResolver: () => contentStatusBreakdown });
  },
  recentActivity(): Promise<ActivityItem[]> {
    return apiRequest<ActivityItem[]>("/dashboard/recent-activity", {
      mockResolver: () => recentActivity,
    });
  },
  /** Replace with real Spring Boot actuator/health aggregation. */
  health(): Promise<HealthStatus[]> {
    return apiRequest<HealthStatus[]>("/dashboard/health", { mockResolver: () => health });
  },
  notifications(): Promise<NotificationItem[]> {
    return apiRequest<NotificationItem[]>("/notifications", { mockResolver: () => notifications });
  },
};
