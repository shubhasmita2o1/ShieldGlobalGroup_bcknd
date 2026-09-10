import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, notFound } from "@tanstack/react-router";
import { ArrowUpDown, MoreHorizontal, Pencil, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { contentApi, type ContentModule } from "@/api/contentApi";
import { EmptyState } from "@/components/admin/EmptyState";
import { PageHeader } from "@/components/admin/PageHeader";
import { StatusBadge } from "@/components/admin/StatusBadge";
import { findContentModule } from "@/components/admin/nav-config";
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
import { Card, CardContent } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
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
import type { ContentRecord } from "@/types/admin";

export const Route = createFileRoute("/_admin/content/$module")({
  beforeLoad: ({ params }) => {
    if (!findContentModule(params.module)) throw notFound();
  },
  head: ({ params }) => {
    const config = findContentModule(params.module);
    const title = `${config?.label ?? "Content"} | Shield Global CMS`;
    const description = config?.description ?? "Manage website content for Shield Global Group.";
    return {
      meta: [
        { title },
        { name: "description", content: description },
        { property: "og:title", content: title },
        { property: "og:description", content: description },
        { name: "robots", content: "noindex,nofollow" },
      ],
    };
  },
  component: ContentModulePage,
});

const schema = z.object({
  title: z.string().min(2, "Title is required"),
  subtitle: z.string(),
  category: z.string().min(1, "Category is required"),
  status: z.enum(["PUBLISHED", "DRAFT", "ARCHIVED"]),
});
type FormValues = z.infer<typeof schema>;

const PAGE_SIZE = 8;

function ContentModulePage() {
  const { module } = Route.useParams();
  const config = findContentModule(module)!;
  const slug = module as ContentModule;
  const queryClient = useQueryClient();

  const [search, setSearch] = useState("");
  const [status, setStatus] = useState("ALL");
  const [sort, setSort] = useState("updated-desc");
  const [page, setPage] = useState(1);
  const [editing, setEditing] = useState<ContentRecord | null>(null);
  const [formOpen, setFormOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<ContentRecord | null>(null);

  useEffect(() => {
    setSearch("");
    setStatus("ALL");
    setPage(1);
  }, [module]);

  const listQuery = useQuery({
    queryKey: ["content", slug, { search, status, sort, page }],
    queryFn: () => contentApi.list(slug, { search, status, sort, page, pageSize: PAGE_SIZE }),
  });

  const categories = useMemo(() => contentApi.categories(slug), [slug, listQuery.data]);

  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: { title: "", subtitle: "", category: "General", status: "DRAFT" },
  });

  function openCreate() {
    setEditing(null);
    form.reset({ title: "", subtitle: "", category: categories[0] ?? "General", status: "DRAFT" });
    setFormOpen(true);
  }

  function openEdit(record: ContentRecord) {
    setEditing(record);
    form.reset({
      title: record.title,
      subtitle: record.subtitle ?? "",
      category: record.category ?? "General",
      status: record.status,
    });
    setFormOpen(true);
  }

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ["content", slug] });

  const saveMutation = useMutation({
    mutationFn: (values: FormValues) =>
      editing ? contentApi.update(slug, editing.id, values) : contentApi.create(slug, values),
    onSuccess: async () => {
      await invalidate();
      toast.success(editing ? `${config.itemNoun} updated.` : `${config.itemNoun} created.`);
      setFormOpen(false);
    },
    onError: (error: Error) => toast.error(error.message),
  });

  const statusMutation = useMutation({
    mutationFn: ({ id, next }: { id: string; next: ContentRecord["status"] }) =>
      contentApi.setStatus(slug, id, next),
    onSuccess: async () => {
      await invalidate();
      toast.success("Status updated.");
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: string) => contentApi.remove(slug, id),
    onSuccess: async () => {
      await invalidate();
      toast.success(`${config.itemNoun} deleted.`);
      setDeleteTarget(null);
    },
  });

  const data = listQuery.data;
  const totalPages = data ? Math.max(1, Math.ceil(data.total / PAGE_SIZE)) : 1;

  return (
    <>
      <PageHeader
        eyebrow="Website content"
        title={config.label}
        description={config.description}
        actions={
          <Button onClick={openCreate}>
            <Plus className="mr-2 size-4" /> New {config.itemNoun.toLowerCase()}
          </Button>
        }
      />

      <Card>
        <CardContent className="space-y-4 p-5">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
            <div className="relative flex-1">
              <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                value={search}
                onChange={(e) => {
                  setSearch(e.target.value);
                  setPage(1);
                }}
                placeholder={`Search ${config.label.toLowerCase()}`}
                className="h-9 pl-9"
              />
            </div>
            <Select
              value={status}
              onValueChange={(v) => {
                setStatus(v);
                setPage(1);
              }}
            >
              <SelectTrigger className="h-9 w-full sm:w-[170px]">
                <SelectValue placeholder="Status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">All statuses</SelectItem>
                <SelectItem value="PUBLISHED">Published</SelectItem>
                <SelectItem value="DRAFT">Draft</SelectItem>
                <SelectItem value="ARCHIVED">Archived</SelectItem>
              </SelectContent>
            </Select>
            <Select value={sort} onValueChange={setSort}>
              <SelectTrigger className="h-9 w-full sm:w-[190px]">
                <ArrowUpDown className="mr-2 size-3.5 text-muted-foreground" />
                <SelectValue placeholder="Sort" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="updated-desc">Recently updated</SelectItem>
                <SelectItem value="updated-asc">Oldest updated</SelectItem>
                <SelectItem value="title-asc">Title A–Z</SelectItem>
                <SelectItem value="title-desc">Title Z–A</SelectItem>
                <SelectItem value="order">Display order</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {listQuery.isLoading ? (
            <div className="space-y-2">
              {Array.from({ length: 6 }).map((_, i) => (
                <Skeleton key={i} className="h-12 w-full" />
              ))}
            </div>
          ) : !data || data.items.length === 0 ? (
            <EmptyState
              title={`No ${config.itemNoun.toLowerCase()}s found`}
              description="Adjust your filters or create a new record to get started."
              action={
                <Button size="sm" onClick={openCreate}>
                  <Plus className="mr-2 size-4" /> New {config.itemNoun.toLowerCase()}
                </Button>
              }
            />
          ) : (
            <div className="overflow-x-auto rounded-md border border-border">
              <Table>
                <TableHeader>
                  <TableRow className="bg-muted/50">
                    <TableHead className="min-w-[240px]">{config.itemNoun}</TableHead>
                    <TableHead>Category</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Author</TableHead>
                    <TableHead>Last updated</TableHead>
                    <TableHead className="w-12" />
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {data.items.map((record) => (
                    <TableRow key={record.id}>
                      <TableCell>
                        <p className="font-medium text-foreground">{record.title}</p>
                        {record.subtitle ? (
                          <p className="line-clamp-1 text-xs text-muted-foreground">{record.subtitle}</p>
                        ) : null}
                      </TableCell>
                      <TableCell className="text-sm text-muted-foreground">{record.category}</TableCell>
                      <TableCell>
                        <StatusBadge status={record.status} />
                      </TableCell>
                      <TableCell className="text-sm text-muted-foreground">{record.author}</TableCell>
                      <TableCell className="text-sm text-muted-foreground">
                        {formatDateTime(record.updatedAt)}
                      </TableCell>
                      <TableCell>
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon" className="size-8" aria-label="Actions">
                              <MoreHorizontal className="size-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem onSelect={() => openEdit(record)}>
                              <Pencil className="mr-2 size-4" /> Edit
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            {(["PUBLISHED", "DRAFT", "ARCHIVED"] as const)
                              .filter((s) => s !== record.status)
                              .map((s) => (
                                <DropdownMenuItem
                                  key={s}
                                  onSelect={() => statusMutation.mutate({ id: record.id, next: s })}
                                >
                                  Move to {s.toLowerCase()}
                                </DropdownMenuItem>
                              ))}
                            <DropdownMenuSeparator />
                            <DropdownMenuItem
                              className="text-destructive"
                              onSelect={() => setDeleteTarget(record)}
                            >
                              <Trash2 className="mr-2 size-4" /> Delete
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}

          {data && data.total > PAGE_SIZE ? (
            <div className="flex items-center justify-between text-sm text-muted-foreground">
              <span>
                Page {data.page} of {totalPages} · {data.total} records
              </span>
              <div className="flex gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page <= 1}
                  onClick={() => setPage((p) => p - 1)}
                >
                  Previous
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page >= totalPages}
                  onClick={() => setPage((p) => p + 1)}
                >
                  Next
                </Button>
              </div>
            </div>
          ) : null}
        </CardContent>
      </Card>

      <Dialog open={formOpen} onOpenChange={setFormOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle className="font-display">
              {editing ? `Edit ${config.itemNoun.toLowerCase()}` : `New ${config.itemNoun.toLowerCase()}`}
            </DialogTitle>
            <DialogDescription>{config.description}</DialogDescription>
          </DialogHeader>
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit((values) => saveMutation.mutate(values))}
              className="space-y-4"
            >
              <FormField
                control={form.control}
                name="title"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Title</FormLabel>
                    <FormControl>
                      <Input {...field} placeholder={`${config.itemNoun} title`} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="subtitle"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Summary</FormLabel>
                    <FormControl>
                      <Input {...field} placeholder="Short supporting description" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="grid gap-4 sm:grid-cols-2">
                <FormField
                  control={form.control}
                  name="category"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Category</FormLabel>
                      <FormControl>
                        <Input {...field} placeholder="General" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="status"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Status</FormLabel>
                      <Select value={field.value} onValueChange={field.onChange}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="PUBLISHED">Published</SelectItem>
                          <SelectItem value="DRAFT">Draft</SelectItem>
                          <SelectItem value="ARCHIVED">Archived</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <DialogFooter>
                <Button type="button" variant="outline" onClick={() => setFormOpen(false)}>
                  Cancel
                </Button>
                <Button type="submit" disabled={saveMutation.isPending}>
                  {saveMutation.isPending ? "Saving…" : "Save"}
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>

      <AlertDialog open={Boolean(deleteTarget)} onOpenChange={(open) => !open && setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete “{deleteTarget?.title}”?</AlertDialogTitle>
            <AlertDialogDescription>
              This removes the record from the website. The action is recorded in the audit log.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
              onClick={() => deleteTarget && deleteMutation.mutate(deleteTarget.id)}
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
}
