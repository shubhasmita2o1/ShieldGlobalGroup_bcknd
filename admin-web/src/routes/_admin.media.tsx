import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Copy, FileText, Film, Image as ImageIcon, PenTool, Search, Trash2, Upload } from "lucide-react";
import { useRef, useState } from "react";
import { toast } from "sonner";

import { mediaApi } from "@/api/mediaApi";
import { EmptyState } from "@/components/admin/EmptyState";
import { PageHeader } from "@/components/admin/PageHeader";
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
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Skeleton } from "@/components/ui/skeleton";
import { formatDate, formatFileSize } from "@/lib/format";
import type { MediaAsset, MediaType } from "@/types/admin";

export const Route = createFileRoute("/_admin/media")({
  head: () => ({
    meta: [
      { title: "Media Library | Shield Global CMS" },
      { name: "description", content: "Upload and manage images, videos, logos and documents." },
      { property: "og:title", content: "Media Library | Shield Global CMS" },
      { property: "og:description", content: "Central asset library for the Shield Global website." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: MediaPage,
});

const typeIcon: Record<MediaType, typeof ImageIcon> = {
  IMAGE: ImageIcon,
  VIDEO: Film,
  DOCUMENT: FileText,
  LOGO: PenTool,
};

function MediaPage() {
  const queryClient = useQueryClient();
  const fileInput = useRef<HTMLInputElement>(null);
  const [search, setSearch] = useState("");
  const [type, setType] = useState<MediaType | "ALL">("ALL");
  const [toDelete, setToDelete] = useState<MediaAsset | null>(null);

  const assets = useQuery({
    queryKey: ["media", search, type],
    queryFn: () => mediaApi.list({ search, type }),
  });

  const upload = useMutation({
    mutationFn: (file: { name: string; sizeKb: number }) => mediaApi.upload(file),
    onSuccess: (asset) => {
      toast.success(`"${asset.name}" uploaded.`);
      void queryClient.invalidateQueries({ queryKey: ["media"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const remove = useMutation({
    mutationFn: (id: string) => mediaApi.remove(id),
    onSuccess: () => {
      toast.success("Asset deleted.");
      setToDelete(null);
      void queryClient.invalidateQueries({ queryKey: ["media"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const copyUrl = (url: string) => {
    void navigator.clipboard?.writeText(url);
    toast.success("Asset URL copied to clipboard.");
  };

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Assets"
        title="Media Library"
        description="Images, videos, logos and documents used across the Shield Global website."
        actions={
          <>
            <input
              ref={fileInput}
              type="file"
              className="hidden"
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) upload.mutate({ name: file.name, sizeKb: Math.max(1, file.size / 1024) });
                e.target.value = "";
              }}
            />
            <Button onClick={() => fileInput.current?.click()} disabled={upload.isPending}>
              <Upload className="size-4" />
              {upload.isPending ? "Uploading…" : "Upload asset"}
            </Button>
          </>
        }
      />

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search assets by name…"
            className="pl-9"
          />
        </div>
        <Select value={type} onValueChange={(v) => setType(v as MediaType | "ALL")}>
          <SelectTrigger className="w-full sm:w-44">
            <SelectValue placeholder="All types" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL">All types</SelectItem>
            <SelectItem value="IMAGE">Images</SelectItem>
            <SelectItem value="VIDEO">Videos</SelectItem>
            <SelectItem value="LOGO">Logos</SelectItem>
            <SelectItem value="DOCUMENT">Documents</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {assets.isPending ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {Array.from({ length: 8 }).map((_, i) => (
            <Skeleton key={i} className="h-44 rounded-lg" />
          ))}
        </div>
      ) : !assets.data?.length ? (
        <EmptyState
          title="No assets found"
          description="Try a different search, or upload your first media asset."
        />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {assets.data.map((asset) => {
            const Icon = typeIcon[asset.type];
            return (
              <Card key={asset.id} className="overflow-hidden">
                <div className="flex h-28 items-center justify-center bg-muted/60">
                  <Icon className="size-8 text-muted-foreground" />
                </div>
                <CardContent className="space-y-2 p-4">
                  <div className="flex items-start justify-between gap-2">
                    <p className="truncate text-sm font-medium text-foreground" title={asset.name}>
                      {asset.name}
                    </p>
                    <Badge variant="outline" className="shrink-0 text-[10px] uppercase">
                      {asset.extension}
                    </Badge>
                  </div>
                  <p className="text-xs text-muted-foreground">
                    {formatFileSize(asset.sizeKb)} · {formatDate(asset.uploadedAt)} · {asset.uploadedBy}
                  </p>
                  <div className="flex items-center gap-1 pt-1">
                    <Button variant="ghost" size="sm" onClick={() => copyUrl(asset.url)}>
                      <Copy className="size-3.5" />
                      Copy URL
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      className="text-destructive hover:text-destructive"
                      onClick={() => setToDelete(asset)}
                    >
                      <Trash2 className="size-3.5" />
                      Delete
                    </Button>
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>
      )}

      <AlertDialog open={Boolean(toDelete)} onOpenChange={(open) => !open && setToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete this asset?</AlertDialogTitle>
            <AlertDialogDescription>
              "{toDelete?.name}" will be permanently removed from the media library. Content using
              this URL may break.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
              onClick={() => toDelete && remove.mutate(toDelete.id)}
            >
              Delete asset
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
