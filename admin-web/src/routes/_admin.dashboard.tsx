import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import {
  ArrowDownRight,
  ArrowUpRight,
  Building2,
  CheckCircle2,
  FileText,
  Images,
  Layers,
  Mail,
  Minus,
  PencilLine,
  Users,
} from "lucide-react";
import { useState } from "react";
import {
  Area,
  AreaChart,
  CartesianGrid,
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import { dashboardApi, type DateRange } from "@/api/dashboardApi";
import { PageHeader } from "@/components/admin/PageHeader";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { Skeleton } from "@/components/ui/skeleton";
import { formatDate, relativeTime } from "@/lib/format";
import { cn } from "@/lib/utils";

export const Route = createFileRoute("/_admin/dashboard")({
  head: () => ({
    meta: [
      { title: "Dashboard | Shield Global CMS" },
      {
        name: "description",
        content:
          "Content, enquiry and platform performance overview for Shield Global Group administrators.",
      },
      { property: "og:title", content: "Dashboard | Shield Global CMS" },
      { property: "og:description", content: "Live content and enquiry metrics for Shield Global Group." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: DashboardPage,
});

const iconMap: Record<string, typeof FileText> = {
  FileText,
  CheckCircle2,
  PencilLine,
  Layers,
  Building2,
  Mail,
  Images,
  Users,
};

const pieColors = [
  "var(--chart-1)",
  "var(--chart-2)",
  "var(--chart-3)",
  "var(--chart-4)",
  "var(--chart-5)",
  "var(--muted-foreground)",
];

function DashboardPage() {
  const [range, setRange] = useState<DateRange>("30d");

  const kpis = useQuery({ queryKey: ["kpis"], queryFn: () => dashboardApi.kpis() });
  const activity = useQuery({
    queryKey: ["activity", range],
    queryFn: () => dashboardApi.activity(range),
  });
  const distribution = useQuery({
    queryKey: ["distribution"],
    queryFn: () => dashboardApi.distribution(),
  });
  const breakdown = useQuery({
    queryKey: ["status-breakdown"],
    queryFn: () => dashboardApi.statusBreakdown(),
  });
  const recent = useQuery({
    queryKey: ["recent-activity"],
    queryFn: () => dashboardApi.recentActivity(),
  });
  const health = useQuery({ queryKey: ["health"], queryFn: () => dashboardApi.health() });

  return (
    <>
      <PageHeader
        eyebrow="Overview"
        title="Operations dashboard"
        description="A consolidated view of website content, client enquiries and platform health across the group."
        actions={
          <div className="flex rounded-md border border-border p-0.5">
            {(["7d", "30d", "90d"] as const).map((option) => (
              <Button
                key={option}
                size="sm"
                variant={range === option ? "secondary" : "ghost"}
                className="h-8 px-3 text-xs font-semibold"
                onClick={() => setRange(option)}
              >
                {option.toUpperCase()}
              </Button>
            ))}
          </div>
        }
      />

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {kpis.isLoading
          ? Array.from({ length: 8 }).map((_, i) => <Skeleton key={i} className="h-[124px] rounded-lg" />)
          : (kpis.data ?? []).map((kpi) => {
              const Icon = iconMap[kpi.icon] ?? FileText;
              const Trend = kpi.change > 0 ? ArrowUpRight : kpi.change < 0 ? ArrowDownRight : Minus;
              return (
                <Card key={kpi.id} className="border-border/80">
                  <CardContent className="space-y-3 p-5">
                    <div className="flex items-start justify-between">
                      <p className="text-xs font-semibold uppercase tracking-[0.12em] text-muted-foreground">
                        {kpi.label}
                      </p>
                      <span className="flex size-8 items-center justify-center rounded-md bg-primary/8 text-primary">
                        <Icon className="size-4" />
                      </span>
                    </div>
                    <p className="font-display text-3xl font-semibold tabular-nums text-foreground">
                      {kpi.value.toLocaleString()}
                    </p>
                    <div className="flex items-center gap-2 text-xs">
                      <span
                        className={cn(
                          "inline-flex items-center gap-0.5 font-semibold",
                          kpi.change > 0
                            ? "text-success"
                            : kpi.change < 0
                              ? "text-destructive"
                              : "text-muted-foreground",
                        )}
                      >
                        <Trend className="size-3.5" />
                        {Math.abs(kpi.change)}%
                      </span>
                      <span className="text-muted-foreground">{kpi.changeLabel}</span>
                    </div>
                    <p className="text-[11px] text-muted-foreground/80">{kpi.supporting}</p>
                  </CardContent>
                </Card>
              );
            })}
      </div>

      <div className="grid gap-4 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="font-display text-base">Content activity</CardTitle>
            <CardDescription>Publishing, editing and drafting volume over the selected period.</CardDescription>
          </CardHeader>
          <CardContent className="h-[300px] pl-0">
            {activity.isLoading ? (
              <Skeleton className="mx-6 h-[260px]" />
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={activity.data ?? []} margin={{ top: 8, right: 20, left: 0, bottom: 0 }}>
                  <defs>
                    {["published", "updated", "drafted"].map((key, i) => (
                      <linearGradient key={key} id={`g-${key}`} x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor={pieColors[i]} stopOpacity={0.35} />
                        <stop offset="100%" stopColor={pieColors[i]} stopOpacity={0} />
                      </linearGradient>
                    ))}
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" vertical={false} />
                  <XAxis
                    dataKey="date"
                    tickFormatter={(v: string) => formatDate(v).slice(0, 6)}
                    tick={{ fontSize: 11, fill: "var(--muted-foreground)" }}
                    tickLine={false}
                    axisLine={false}
                    minTickGap={24}
                  />
                  <YAxis
                    tick={{ fontSize: 11, fill: "var(--muted-foreground)" }}
                    tickLine={false}
                    axisLine={false}
                    width={32}
                  />
                  <Tooltip
                    contentStyle={{
                      background: "var(--popover)",
                      border: "1px solid var(--border)",
                      borderRadius: 8,
                      fontSize: 12,
                    }}
                    labelFormatter={(v: string) => formatDate(v)}
                  />
                  {["published", "updated", "drafted"].map((key, i) => (
                    <Area
                      key={key}
                      type="monotone"
                      dataKey={key}
                      stroke={pieColors[i]}
                      strokeWidth={2}
                      fill={`url(#g-${key})`}
                    />
                  ))}
                </AreaChart>
              </ResponsiveContainer>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="font-display text-base">Content distribution</CardTitle>
            <CardDescription>Records by module.</CardDescription>
          </CardHeader>
          <CardContent className="h-[300px]">
            {distribution.isLoading ? (
              <Skeleton className="h-[260px]" />
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={distribution.data ?? []}
                    dataKey="value"
                    nameKey="name"
                    innerRadius={54}
                    outerRadius={92}
                    paddingAngle={2}
                    stroke="var(--card)"
                  >
                    {(distribution.data ?? []).map((_, i) => (
                      <Cell key={i} fill={pieColors[i % pieColors.length]} />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{
                      background: "var(--popover)",
                      border: "1px solid var(--border)",
                      borderRadius: 8,
                      fontSize: 12,
                    }}
                  />
                </PieChart>
              </ResponsiveContainer>
            )}
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-4 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="font-display text-base">Recent activity</CardTitle>
            <CardDescription>Latest changes made by the content team.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-0">
            {(recent.data ?? []).map((item) => (
              <div
                key={item.id}
                className="flex items-start gap-3 border-b border-border/70 py-3 last:border-0"
              >
                <span
                  className={cn(
                    "mt-1.5 size-2 shrink-0 rounded-full",
                    item.tone === "success" && "bg-success",
                    item.tone === "info" && "bg-info",
                    item.tone === "warning" && "bg-warning",
                    item.tone === "neutral" && "bg-muted-foreground",
                  )}
                />
                <div className="min-w-0 flex-1">
                  <p className="text-sm text-foreground">
                    <span className="font-medium">{item.user}</span> {item.action}{" "}
                    <span className="font-medium">{item.target}</span>
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {item.contentType} · {relativeTime(item.timestamp)}
                  </p>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        <div className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="font-display text-base">Status breakdown</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {(breakdown.data ?? []).map((row) => (
                <div key={row.label} className="space-y-1.5">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-foreground">{row.label}</span>
                    <span className="tabular-nums text-muted-foreground">
                      {row.count} · {row.value}%
                    </span>
                  </div>
                  <Progress value={row.value} className="h-1.5" />
                </div>
              ))}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="font-display text-base">Platform health</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {(health.data ?? []).map((item) => (
                <div key={item.id} className="flex items-center justify-between text-sm">
                  <span className="text-muted-foreground">{item.label}</span>
                  <Badge
                    variant="outline"
                    className={cn(
                      "rounded-full text-[11px] font-semibold",
                      item.state === "operational" && "border-success/25 bg-success/10 text-success",
                      item.state === "degraded" && "border-warning/35 bg-warning/15 text-warning-foreground",
                      item.state === "down" && "border-destructive/25 bg-destructive/10 text-destructive",
                    )}
                  >
                    {item.value}
                  </Badge>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>
    </>
  );
}
