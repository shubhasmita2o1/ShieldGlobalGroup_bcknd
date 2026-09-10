import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Pencil, Search } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { seoApi } from "@/api/seoApi";
import { EmptyState } from "@/components/admin/EmptyState";
import { PageHeader } from "@/components/admin/PageHeader";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
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
import { Textarea } from "@/components/ui/textarea";
import { formatDate } from "@/lib/format";
import type { SeoEntry } from "@/types/admin";

export const Route = createFileRoute("/_admin/seo")({
  head: () => ({
    meta: [
      { title: "SEO Manager | Shield Global CMS" },
      { name: "description", content: "Manage meta titles, descriptions, canonical URLs and Open Graph tags." },
      { property: "og:title", content: "SEO Manager | Shield Global CMS" },
      { property: "og:description", content: "Search and social metadata for every public page." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: SeoPage,
});

const schema = z.object({
  metaTitle: z.string().min(1, "Meta title is required").max(70, "Keep under 70 characters"),
  metaDescription: z.string().min(1, "Meta description is required").max(160, "Keep under 160 characters"),
  canonicalUrl: z.string().url("Enter a valid URL").or(z.literal("")),
  ogTitle: z.string(),
  ogDescription: z.string(),
  ogImage: z.string().url("Enter a valid URL").or(z.literal("")),
  robots: z.enum(["index,follow", "noindex,follow", "index,nofollow", "noindex,nofollow"]),
});

type SeoForm = z.infer<typeof schema>;

function SeoPage() {
  const queryClient = useQueryClient();
  const [search, setSearch] = useState("");
  const [editing, setEditing] = useState<SeoEntry | null>(null);

  const entries = useQuery({ queryKey: ["seo", search], queryFn: () => seoApi.list(search) });

  const form = useForm<SeoForm>({
    resolver: zodResolver(schema),
    defaultValues: {
      metaTitle: "",
      metaDescription: "",
      canonicalUrl: "",
      ogTitle: "",
      ogDescription: "",
      ogImage: "",
      robots: "index,follow",
    },
  });

  useEffect(() => {
    if (editing) {
      form.reset({
        metaTitle: editing.metaTitle,
        metaDescription: editing.metaDescription,
        canonicalUrl: editing.canonicalUrl,
        ogTitle: editing.ogTitle,
        ogDescription: editing.ogDescription,
        ogImage: editing.ogImage,
        robots: editing.robots,
      });
    }
  }, [editing, form]);

  const save = useMutation({
    mutationFn: ({ id, input }: { id: string; input: SeoForm }) => seoApi.update(id, input),
    onSuccess: () => {
      toast.success("SEO settings saved.");
      setEditing(null);
      void queryClient.invalidateQueries({ queryKey: ["seo"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const err = (name: keyof SeoForm) =>
    form.formState.errors[name] ? (
      <p className="text-xs text-destructive">{form.formState.errors[name]?.message}</p>
    ) : null;

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Search"
        title="SEO Manager"
        description="Meta titles, descriptions, canonical URLs, robots directives and Open Graph tags per page."
      />

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search pages…"
          className="pl-9"
        />
      </div>

      <div className="rounded-lg border border-border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Page</TableHead>
              <TableHead className="hidden md:table-cell">Meta title</TableHead>
              <TableHead className="hidden lg:table-cell">Robots</TableHead>
              <TableHead className="hidden lg:table-cell">Updated</TableHead>
              <TableHead className="w-12" />
            </TableRow>
          </TableHeader>
          <TableBody>
            {entries.isPending ? (
              Array.from({ length: 5 }).map((_, i) => (
                <TableRow key={i}>
                  <TableCell colSpan={5}>
                    <Skeleton className="h-8 w-full" />
                  </TableCell>
                </TableRow>
              ))
            ) : !entries.data?.length ? (
              <TableRow>
                <TableCell colSpan={5} className="p-0">
                  <EmptyState title="No pages found" description="Try a different search term." />
                </TableCell>
              </TableRow>
            ) : (
              entries.data.map((entry) => (
                <TableRow key={entry.id}>
                  <TableCell>
                    <p className="font-medium text-foreground">{entry.page}</p>
                    <p className="text-xs text-muted-foreground">{entry.path}</p>
                  </TableCell>
                  <TableCell className="hidden max-w-72 truncate md:table-cell">
                    {entry.metaTitle}
                  </TableCell>
                  <TableCell className="hidden lg:table-cell">
                    <Badge variant="outline">{entry.robots}</Badge>
                  </TableCell>
                  <TableCell className="hidden text-sm text-muted-foreground lg:table-cell">
                    {formatDate(entry.updatedAt)}
                  </TableCell>
                  <TableCell>
                    <Button variant="ghost" size="icon" onClick={() => setEditing(entry)}>
                      <Pencil className="size-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <Dialog open={Boolean(editing)} onOpenChange={(open) => !open && setEditing(null)}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Edit SEO — {editing?.page}</DialogTitle>
            <DialogDescription>{editing?.path}</DialogDescription>
          </DialogHeader>
          <form
            className="grid gap-4 sm:grid-cols-2"
            onSubmit={form.handleSubmit((input) =>
              editing && save.mutate({ id: editing.id, input }),
            )}
          >
            <div className="space-y-1.5 sm:col-span-2">
              <Label htmlFor="metaTitle">Meta title</Label>
              <Input id="metaTitle" {...form.register("metaTitle")} />
              {err("metaTitle")}
            </div>
            <div className="space-y-1.5 sm:col-span-2">
              <Label htmlFor="metaDescription">Meta description</Label>
              <Textarea id="metaDescription" rows={3} {...form.register("metaDescription")} />
              {err("metaDescription")}
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="canonicalUrl">Canonical URL</Label>
              <Input id="canonicalUrl" {...form.register("canonicalUrl")} />
              {err("canonicalUrl")}
            </div>
            <div className="space-y-1.5">
              <Label>Robots</Label>
              <Select
                value={form.watch("robots")}
                onValueChange={(v) => form.setValue("robots", v as SeoForm["robots"])}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="index,follow">index, follow</SelectItem>
                  <SelectItem value="noindex,follow">noindex, follow</SelectItem>
                  <SelectItem value="index,nofollow">index, nofollow</SelectItem>
                  <SelectItem value="noindex,nofollow">noindex, nofollow</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="ogTitle">OG title</Label>
              <Input id="ogTitle" {...form.register("ogTitle")} />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="ogImage">OG image URL</Label>
              <Input id="ogImage" {...form.register("ogImage")} />
              {err("ogImage")}
            </div>
            <div className="space-y-1.5 sm:col-span-2">
              <Label htmlFor="ogDescription">OG description</Label>
              <Textarea id="ogDescription" rows={2} {...form.register("ogDescription")} />
            </div>
            <DialogFooter className="sm:col-span-2">
              <Button type="button" variant="outline" onClick={() => setEditing(null)}>
                Cancel
              </Button>
              <Button type="submit" disabled={save.isPending}>
                {save.isPending ? "Saving…" : "Save changes"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
