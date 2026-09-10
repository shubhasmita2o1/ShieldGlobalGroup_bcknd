import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { ChevronLeft, ChevronRight, Search, Trash2 } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";

import { enquiriesApi } from "@/api/enquiriesApi";
import { EmptyState } from "@/components/admin/EmptyState";
import { PageHeader } from "@/components/admin/PageHeader";
import { StatusBadge } from "@/components/admin/StatusBadge";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Separator } from "@/components/ui/separator";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Textarea } from "@/components/ui/textarea";
import { formatDateTime, relativeTime } from "@/lib/format";
import type { Enquiry, EnquiryStatus } from "@/types/admin";

export const Route = createFileRoute("/_admin/enquiries")({
  head: () => ({
    meta: [
      { title: "Enquiries | Shield Global CMS" },
      { name: "description", content: "Manage website contact enquiries and follow-ups." },
      { property: "og:title", content: "Enquiries | Shield Global CMS" },
      { property: "og:description", content: "Track and respond to Shield Global website enquiries." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: EnquiriesPage,
});

const statuses: (EnquiryStatus | "ALL")[] = ["ALL", "NEW", "IN_PROGRESS", "CONTACTED", "CLOSED", "SPAM"];

function EnquiriesPage() {
  const queryClient = useQueryClient();
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState<string>("ALL");
  const [page, setPage] = useState(1);
  const [selected, setSelected] = useState<Enquiry | null>(null);
  const [toDelete, setToDelete] = useState<Enquiry | null>(null);
  const [note, setNote] = useState("");

  const list = useQuery({
    queryKey: ["enquiries", search, status, page],
    queryFn: () => enquiriesApi.list({ search, status, page, pageSize: 8 }),
  });

  const invalidate = () => void queryClient.invalidateQueries({ queryKey: ["enquiries"] });

  const setStatusMutation = useMutation({
    mutationFn: ({ id, next }: { id: string; next: EnquiryStatus }) => enquiriesApi.setStatus(id, next),
    onSuccess: (updated) => {
      toast.success("Enquiry status updated.");
      setSelected(updated);
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const addNote = useMutation({
    mutationFn: ({ id, body }: { id: string; body: string }) => enquiriesApi.addNote(id, body),
    onSuccess: (updated) => {
      toast.success("Note added.");
      setNote("");
      setSelected(updated);
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const remove = useMutation({
    mutationFn: (id: string) => enquiriesApi.remove(id),
    onSuccess: () => {
      toast.success("Enquiry deleted.");
      setToDelete(null);
      setSelected(null);
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const totalPages = list.data ? Math.max(1, Math.ceil(list.data.total / list.data.pageSize)) : 1;

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Communication"
        title="Enquiries"
        description="Contact-form submissions from the public website, with follow-up tracking."
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
            placeholder="Search by name, email, company or subject…"
            className="pl-9"
          />
        </div>
        <Select
          value={status}
          onValueChange={(v) => {
            setStatus(v);
            setPage(1);
          }}
        >
          <SelectTrigger className="w-full sm:w-44">
            <SelectValue placeholder="All statuses" />
          </SelectTrigger>
          <SelectContent>
            {statuses.map((s) => (
              <SelectItem key={s} value={s}>
                {s === "ALL" ? "All statuses" : s.replace(/_/g, " ")}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="rounded-lg border border-border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Contact</TableHead>
              <TableHead className="hidden md:table-cell">Subject</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="hidden lg:table-cell">Received</TableHead>
              <TableHead className="w-12" />
            </TableRow>
          </TableHeader>
          <TableBody>
            {list.isPending ? (
              Array.from({ length: 6 }).map((_, i) => (
                <TableRow key={i}>
                  <TableCell colSpan={5}>
                    <Skeleton className="h-8 w-full" />
                  </TableCell>
                </TableRow>
              ))
            ) : !list.data?.items.length ? (
              <TableRow>
                <TableCell colSpan={5} className="p-0">
                  <EmptyState title="No enquiries found" description="Adjust your filters or search." />
                </TableCell>
              </TableRow>
            ) : (
              list.data.items.map((enquiry) => (
                <TableRow
                  key={enquiry.id}
                  className="cursor-pointer"
                  onClick={() => setSelected(enquiry)}
                >
                  <TableCell>
                    <p className="font-medium text-foreground">{enquiry.name}</p>
                    <p className="text-xs text-muted-foreground">
                      {enquiry.company} · {enquiry.email}
                    </p>
                  </TableCell>
                  <TableCell className="hidden max-w-56 truncate md:table-cell">
                    {enquiry.subject}
                  </TableCell>
                  <TableCell>
                    <StatusBadge status={enquiry.status} />
                  </TableCell>
                  <TableCell className="hidden text-sm text-muted-foreground lg:table-cell">
                    {relativeTime(enquiry.createdAt)}
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="text-destructive hover:text-destructive"
                      onClick={(e) => {
                        e.stopPropagation();
                        setToDelete(enquiry);
                      }}
                    >
                      <Trash2 className="size-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between text-sm text-muted-foreground">
        <span>
          Page {list.data?.page ?? 1} of {totalPages} · {list.data?.total ?? 0} enquiries
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

      <Dialog open={Boolean(selected)} onOpenChange={(open) => !open && setSelected(null)}>
        <DialogContent className="max-w-2xl">
          {selected ? (
            <>
              <DialogHeader>
                <DialogTitle>{selected.subject}</DialogTitle>
                <DialogDescription>
                  {selected.name} · {selected.company} · {selected.email} · {selected.phone}
                </DialogDescription>
              </DialogHeader>
              <div className="space-y-5">
                <div className="rounded-lg bg-muted/50 p-4 text-sm text-foreground">
                  {selected.message}
                </div>
                <div className="flex flex-wrap items-center gap-3">
                  <span className="text-sm text-muted-foreground">Status</span>
                  <Select
                    value={selected.status}
                    onValueChange={(v) =>
                      setStatusMutation.mutate({ id: selected.id, next: v as EnquiryStatus })
                    }
                  >
                    <SelectTrigger className="w-44">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {statuses.filter((s) => s !== "ALL").map((s) => (
                        <SelectItem key={s} value={s}>
                          {s.replace(/_/g, " ")}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <span className="text-xs text-muted-foreground">
                    Received {formatDateTime(selected.createdAt)}
                  </span>
                </div>
                <Separator />
                <div className="space-y-3">
                  <p className="text-sm font-semibold text-foreground">Internal notes</p>
                  {selected.notes.length ? (
                    <ul className="space-y-2">
                      {selected.notes.map((n) => (
                        <li key={n.id} className="rounded-lg border border-border p-3 text-sm">
                          <p className="text-foreground">{n.body}</p>
                          <p className="mt-1 text-xs text-muted-foreground">
                            {n.author} · {formatDateTime(n.createdAt)}
                          </p>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-sm text-muted-foreground">No notes yet.</p>
                  )}
                  <div className="flex gap-2">
                    <Textarea
                      value={note}
                      onChange={(e) => setNote(e.target.value)}
                      placeholder="Add an internal note…"
                      rows={2}
                    />
                    <Button
                      disabled={!note.trim() || addNote.isPending}
                      onClick={() => addNote.mutate({ id: selected.id, body: note.trim() })}
                    >
                      Add
                    </Button>
                  </div>
                </div>
                <Separator />
                <div className="space-y-2">
                  <p className="text-sm font-semibold text-foreground">Activity</p>
                  <ul className="space-y-1.5 text-sm text-muted-foreground">
                    {selected.history.map((h) => (
                      <li key={h.id}>
                        {h.label} — {h.by}, {formatDateTime(h.at)}
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            </>
          ) : null}
        </DialogContent>
      </Dialog>

      <AlertDialog open={Boolean(toDelete)} onOpenChange={(open) => !open && setToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete this enquiry?</AlertDialogTitle>
            <AlertDialogDescription>
              The enquiry from {toDelete?.name} ({toDelete?.email}) will be permanently removed.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
              onClick={() => toDelete && remove.mutate(toDelete.id)}
            >
              Delete enquiry
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
