import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Pencil, Search, Trash2, UserPlus } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { usersApi } from "@/api/usersApi";
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
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
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
import { formatDate, initials, relativeTime } from "@/lib/format";
import type { AdminUser, PermissionModule, UserRole } from "@/types/admin";

export const Route = createFileRoute("/_admin/users")({
  head: () => ({
    meta: [
      { title: "Users & Roles | Shield Global CMS" },
      { name: "description", content: "Manage admin users, roles and module permissions." },
      { property: "og:title", content: "Users & Roles | Shield Global CMS" },
      { property: "og:description", content: "Admin access control for Shield Global CMS." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: UsersPage,
});

const allPermissions: PermissionModule[] = [
  "pages",
  "services",
  "companies",
  "media",
  "enquiries",
  "seo",
  "users",
  "settings",
  "audit-logs",
];

const schema = z.object({
  name: z.string().min(2, "Name is required"),
  email: z.string().email("Enter a valid email"),
  phone: z.string(),
  role: z.enum(["SUPER_ADMIN", "ADMIN", "EDITOR"]),
});

type UserForm = z.infer<typeof schema>;

function UsersPage() {
  const queryClient = useQueryClient();
  const [search, setSearch] = useState("");
  const [role, setRole] = useState("ALL");
  const [status, setStatus] = useState("ALL");
  const [editing, setEditing] = useState<AdminUser | null>(null);
  const [creating, setCreating] = useState(false);
  const [toDelete, setToDelete] = useState<AdminUser | null>(null);
  const [permissions, setPermissions] = useState<PermissionModule[]>([]);

  const list = useQuery({
    queryKey: ["users", search, role, status],
    queryFn: () => usersApi.list({ search, role, status }),
  });

  const form = useForm<UserForm>({
    resolver: zodResolver(schema),
    defaultValues: { name: "", email: "", phone: "", role: "EDITOR" },
  });

  useEffect(() => {
    if (editing) {
      form.reset({
        name: editing.name,
        email: editing.email,
        phone: editing.phone ?? "",
        role: editing.role,
      });
      setPermissions(editing.permissions);
    } else if (creating) {
      form.reset({ name: "", email: "", phone: "", role: "EDITOR" });
      setPermissions([]);
    }
  }, [editing, creating, form]);

  const invalidate = () => void queryClient.invalidateQueries({ queryKey: ["users"] });
  const dialogOpen = Boolean(editing) || creating;
  const closeDialog = () => {
    setEditing(null);
    setCreating(false);
  };

  const save = useMutation({
    mutationFn: (input: UserForm) =>
      editing
        ? usersApi.update(editing.id, { ...input, permissions })
        : usersApi.create({ ...input, permissions }),
    onSuccess: () => {
      toast.success(editing ? "User updated." : "User invited.");
      closeDialog();
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const toggleStatus = useMutation({
    mutationFn: (user: AdminUser) =>
      usersApi.setStatus(user.id, user.status === "ACTIVE" ? "DISABLED" : "ACTIVE"),
    onSuccess: (u) => {
      toast.success(`${u.name} is now ${u.status.toLowerCase()}.`);
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const remove = useMutation({
    mutationFn: (id: string) => usersApi.remove(id),
    onSuccess: () => {
      toast.success("User removed.");
      setToDelete(null);
      invalidate();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const togglePermission = (p: PermissionModule) =>
    setPermissions((prev) => (prev.includes(p) ? prev.filter((x) => x !== p) : [...prev, p]));

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Access control"
        title="Users & Roles"
        description="Admin accounts, role assignment and per-module permissions."
        actions={
          <Button onClick={() => setCreating(true)}>
            <UserPlus className="size-4" /> Invite user
          </Button>
        }
      />

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by name or email…"
            className="pl-9"
          />
        </div>
        <Select value={role} onValueChange={setRole}>
          <SelectTrigger className="w-full sm:w-40">
            <SelectValue placeholder="All roles" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL">All roles</SelectItem>
            <SelectItem value="SUPER_ADMIN">Super Admin</SelectItem>
            <SelectItem value="ADMIN">Admin</SelectItem>
            <SelectItem value="EDITOR">Editor</SelectItem>
          </SelectContent>
        </Select>
        <Select value={status} onValueChange={setStatus}>
          <SelectTrigger className="w-full sm:w-40">
            <SelectValue placeholder="All statuses" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="ALL">All statuses</SelectItem>
            <SelectItem value="ACTIVE">Active</SelectItem>
            <SelectItem value="INVITED">Invited</SelectItem>
            <SelectItem value="DISABLED">Disabled</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="rounded-lg border border-border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>User</TableHead>
              <TableHead>Role</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="hidden lg:table-cell">Last login</TableHead>
              <TableHead className="hidden xl:table-cell">Permissions</TableHead>
              <TableHead className="w-28 text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {list.isPending ? (
              Array.from({ length: 5 }).map((_, i) => (
                <TableRow key={i}>
                  <TableCell colSpan={6}>
                    <Skeleton className="h-8 w-full" />
                  </TableCell>
                </TableRow>
              ))
            ) : !list.data?.length ? (
              <TableRow>
                <TableCell colSpan={6} className="p-0">
                  <EmptyState title="No users found" description="Adjust your filters or invite a new user." />
                </TableCell>
              </TableRow>
            ) : (
              list.data.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <Avatar className="size-8">
                        <AvatarFallback className="text-xs">{initials(user.name)}</AvatarFallback>
                      </Avatar>
                      <div>
                        <p className="font-medium text-foreground">{user.name}</p>
                        <p className="text-xs text-muted-foreground">{user.email}</p>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="secondary">{user.role.replace(/_/g, " ")}</Badge>
                  </TableCell>
                  <TableCell>
                    <button
                      type="button"
                      onClick={() => toggleStatus.mutate(user)}
                      title="Click to toggle active/disabled"
                    >
                      <StatusBadge status={user.status} />
                    </button>
                  </TableCell>
                  <TableCell className="hidden text-sm text-muted-foreground lg:table-cell">
                    {user.lastLogin ? relativeTime(user.lastLogin) : "Never"}
                  </TableCell>
                  <TableCell className="hidden text-xs text-muted-foreground xl:table-cell">
                    {user.role === "SUPER_ADMIN" ? "All modules" : user.permissions.join(", ") || "—"}
                  </TableCell>
                  <TableCell className="text-right">
                    <Button variant="ghost" size="icon" onClick={() => setEditing(user)}>
                      <Pencil className="size-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="text-destructive hover:text-destructive"
                      onClick={() => setToDelete(user)}
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

      <Dialog open={dialogOpen} onOpenChange={(open) => !open && closeDialog()}>
        <DialogContent className="max-w-xl">
          <DialogHeader>
            <DialogTitle>{editing ? `Edit ${editing.name}` : "Invite user"}</DialogTitle>
            <DialogDescription>
              {editing
                ? `Member since ${formatDate(editing.createdAt)}.`
                : "An invitation email will be sent to the new user."}
            </DialogDescription>
          </DialogHeader>
          <form className="grid gap-4 sm:grid-cols-2" onSubmit={form.handleSubmit((v) => save.mutate(v))}>
            <div className="space-y-1.5">
              <Label htmlFor="name">Full name</Label>
              <Input id="name" {...form.register("name")} />
              {form.formState.errors.name && (
                <p className="text-xs text-destructive">{form.formState.errors.name.message}</p>
              )}
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" {...form.register("email")} />
              {form.formState.errors.email && (
                <p className="text-xs text-destructive">{form.formState.errors.email.message}</p>
              )}
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="phone">Phone</Label>
              <Input id="phone" {...form.register("phone")} />
            </div>
            <div className="space-y-1.5">
              <Label>Role</Label>
              <Select
                value={form.watch("role")}
                onValueChange={(v) => form.setValue("role", v as UserRole)}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="SUPER_ADMIN">Super Admin</SelectItem>
                  <SelectItem value="ADMIN">Admin</SelectItem>
                  <SelectItem value="EDITOR">Editor</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2 sm:col-span-2">
              <Label>Module permissions</Label>
              <div className="grid grid-cols-2 gap-2 sm:grid-cols-3">
                {allPermissions.map((p) => (
                  <label
                    key={p}
                    className="flex items-center gap-2 rounded-md border border-border px-3 py-2 text-sm capitalize"
                  >
                    <Checkbox
                      checked={permissions.includes(p)}
                      onCheckedChange={() => togglePermission(p)}
                    />
                    {p.replace("-", " ")}
                  </label>
                ))}
              </div>
            </div>
            <DialogFooter className="sm:col-span-2">
              <Button type="button" variant="outline" onClick={closeDialog}>
                Cancel
              </Button>
              <Button type="submit" disabled={save.isPending}>
                {save.isPending ? "Saving…" : editing ? "Save changes" : "Send invitation"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      <AlertDialog open={Boolean(toDelete)} onOpenChange={(open) => !open && setToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Remove this user?</AlertDialogTitle>
            <AlertDialogDescription>
              {toDelete?.name} ({toDelete?.email}) will lose all access to the CMS.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
              onClick={() => toDelete && remove.mutate(toDelete.id)}
            >
              Remove user
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
