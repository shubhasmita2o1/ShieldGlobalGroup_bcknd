import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { ChevronLeft, ChevronRight, Search } from "lucide-react";
import { Fragment, useState } from "react";

import { auditApi } from "@/api/auditApi";
import { EmptyState } from "@/components/admin/EmptyState";
import { PageHeader } from "@/components/admin/PageHeader";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { formatDateTime } from "@/lib/format";
import type { AuditLog } from "@/types/admin";

export const Route = createFileRoute("/_admin/audit-logs")({
  head: () => ({
    meta: [
      { title: "Audit Logs | Shield Global CMS" },
      { name: "description", content: "Full audit trail of admin actions across the CMS." },
      { property: "og:title", content: "Audit Logs | Shield Global CMS" },
      { property: "og:description", content: "Every create, update, delete and login, recorded." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: AuditLogsPage,
});

const actionTone: Record<string, string> = {
  CREATE: "bg-success/12 text-success border-success/25",
  UPDATE: "bg-info/12 text-info border-info/25",
  PUBLISH: "bg-primary/10 text-primary border-primary/25",
  UNPUBLISH: "bg-warning/15 text-warning-foreground border-warning/35",
  DELETE: "bg-destructive/12 text-destructive border-destructive/25",
  LOGIN: "bg-muted text-muted-foreground border-border",
  LOGOUT: "bg-muted text-muted-foreground border-border",
};

function AuditLogsPage() {
  const [search, setSearch] = useState("");
  const [action, setAction] = useState("ALL");
  const [module, setModule] = useState("ALL");
  const [page, setPage] = useState(1);
  const [expanded, setExpanded] = useState<string | null>(null);

  const filters = auditApi.filters();
  const logs = useQuery({
    queryKey: ["audit-logs", search, action, module, page],
    queryFn: () => auditApi.list({ search, action, module, page, pageSize: 10 }),
  });

  const totalPages = logs.data ? Math.max(1, Math.ceil(logs.data.total / logs.data.pageSize)) : 1;

  const renderDiff = (log: AuditLog) => (
    <TableRow key={`${log.id}-diff`}>
      <TableCell colSpan={5} className="bg-muted/40">
        <div className="grid gap-4 py-2 sm:grid-cols-2">
          <div>
            <p className="mb-1 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
              Before
            </p>
            <pre className="overflow-x-auto rounded-md border border-border bg-background p-3 text-xs">
              {log.oldValue ? JSON.stringify(log.oldValue, null, 2) : "—"}
            </pre>
          </div>
          <div>
            <p className="mb-1 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
              After
            </p>
            <pre className="overflow-x-auto rounded-md border border-border bg-background p-3 text-xs">
              {log.newValue ? JSON.stringify(log.newValue, null, 2) : "—"}
            </pre>
          </div>
        </div>
      </TableCell>
    </TableRow>
  );

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Compliance"
        title="Audit Logs"
        description="An immutable trail of every administrative action, with before/after values."
      />

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(1);
            }}
            placeholder="Search by record or user…"
            className="pl-9"
          />
        </div>
        <Select
          value={action}
          onValueChange={(v) => {
            setAction(v);
            setPage(1);
          }}
        >
          <SelectTrigger className="w-full sm:w-40">
            <SelectValue placeholder="All actions" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL">All actions</SelectItem>
            {filters.actions.map((a) => (
              <SelectItem key={a} value={a}>
                {a}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Select
          value={module}
          onValueChange={(v) => {
            setModule(v);
            setPage(1);
          }}
        >
          <SelectTrigger className="w-full sm:w-40">
            <SelectValue placeholder="All modules" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL">All modules</SelectItem>
            {filters.modules.map((m) => (
              <SelectItem key={m} value={m}>
                {m}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="rounded-lg border border-border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Action</TableHead>
              <TableHead>Record</TableHead>
              <TableHead className="hidden md:table-cell">User</TableHead>
              <TableHead className="hidden lg:table-cell">IP</TableHead>
              <TableHead>When</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {logs.isPending ? (
              Array.from({ length: 8 }).map((_, i) => (
                <TableRow key={i}>
                  <TableCell colSpan={5}>
                    <Skeleton className="h-8 w-full" />
                  </TableCell>
                </TableRow>
              ))
            ) : !logs.data?.items.length ? (
              <TableRow>
                <TableCell colSpan={5} className="p-0">
                  <EmptyState title="No log entries" description="Adjust your filters or search." />
                </TableCell>
              </TableRow>
            ) : (
              logs.data.items.map((log) => (
                <Fragment key={log.id}>
                  <TableRow
                    className="cursor-pointer"
                    onClick={() => setExpanded(expanded === log.id ? null : log.id)}
                  >
                    <TableCell>
                      <Badge variant="outline" className={actionTone[log.action]}>
                        {log.action}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <p className="font-medium text-foreground">{log.record}</p>
                      <p className="text-xs text-muted-foreground">{log.module}</p>
                    </TableCell>
                    <TableCell className="hidden md:table-cell">{log.user}</TableCell>
                    <TableCell className="hidden text-sm text-muted-foreground lg:table-cell">
                      {log.ipAddress}
                    </TableCell>
                    <TableCell className="text-sm text-muted-foreground">
                      {formatDateTime(log.createdAt)}
                    </TableCell>
                  </TableRow>
                  {expanded === log.id ? renderDiff(log) : null}
                </Fragment>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between text-sm text-muted-foreground">
        <span>
          Page {logs.data?.page ?? 1} of {totalPages} · {logs.data?.total ?? 0} entries
        </span>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" disabled={page <= 1} onClick={() => setPage((p) => p - 1)}>
            <ChevronLeft className="size-4" /> Previous
          </Button>
          <Button
            variant="outline"
            size="sm"
            disabled={page >= totalPages}
            onClick={() => setPage((p) => p + 1)}
          >
            Next <ChevronRight className="size-4" />
          </Button>
        </div>
      </div>
      <p className="text-xs text-muted-foreground">
        Tip: click a row to inspect the before/after values recorded for that change.
      </p>
    </div>
  );
}
