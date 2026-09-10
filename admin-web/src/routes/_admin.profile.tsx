import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { authApi } from "@/api/authApi";
import { PageHeader } from "@/components/admin/PageHeader";
import { StatusBadge } from "@/components/admin/StatusBadge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuth } from "@/hooks/use-auth";
import { formatDate, formatDateTime, initials } from "@/lib/format";

export const Route = createFileRoute("/_admin/profile")({
  head: () => ({
    meta: [
      { title: "My Profile | Shield Global CMS" },
      { name: "description", content: "Manage your admin account details and password." },
      { property: "og:title", content: "My Profile | Shield Global CMS" },
      { property: "og:description", content: "Your Shield Global CMS account." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: ProfilePage,
});

const profileSchema = z.object({
  name: z.string().min(2, "Name is required"),
  phone: z.string(),
});

const passwordSchema = z
  .object({
    currentPassword: z.string().min(1, "Enter your current password"),
    newPassword: z.string().min(8, "At least 8 characters"),
    confirmPassword: z.string(),
  })
  .refine((v) => v.newPassword === v.confirmPassword, {
    path: ["confirmPassword"],
    message: "Passwords do not match",
  });

type ProfileForm = z.infer<typeof profileSchema>;
type PasswordForm = z.infer<typeof passwordSchema>;

function ProfilePage() {
  const { user, setUser } = useAuth();

  const profileForm = useForm<ProfileForm>({
    resolver: zodResolver(profileSchema),
    values: { name: user?.name ?? "", phone: user?.phone ?? "" },
  });

  const passwordForm = useForm<PasswordForm>({
    resolver: zodResolver(passwordSchema),
    defaultValues: { currentPassword: "", newPassword: "", confirmPassword: "" },
  });

  const saveProfile = useMutation({
    mutationFn: (input: ProfileForm) => authApi.updateProfile(input),
    onSuccess: (updated) => {
      setUser(updated);
      toast.success("Profile updated.");
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const changePassword = useMutation({
    mutationFn: (input: { currentPassword: string; newPassword: string }) =>
      authApi.changePassword(input),
    onSuccess: () => {
      toast.success("Password changed.");
      passwordForm.reset();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  if (!user) return null;

  const pwErr = (name: keyof PasswordForm) =>
    passwordForm.formState.errors[name] ? (
      <p className="text-xs text-destructive">{passwordForm.formState.errors[name]?.message}</p>
    ) : null;

  return (
    <div className="space-y-6">
      <PageHeader eyebrow="Account" title="My Profile" description="Your account details and security settings." />

      <Card>
        <CardContent className="flex flex-col items-start gap-4 p-6 sm:flex-row sm:items-center">
          <Avatar className="size-16">
            <AvatarFallback className="text-lg">{initials(user.name)}</AvatarFallback>
          </Avatar>
          <div className="space-y-1">
            <div className="flex flex-wrap items-center gap-2">
              <p className="font-display text-lg font-semibold text-foreground">{user.name}</p>
              <Badge variant="secondary">{user.role.replace(/_/g, " ")}</Badge>
              <StatusBadge status={user.status} />
            </div>
            <p className="text-sm text-muted-foreground">{user.email}</p>
            <p className="text-xs text-muted-foreground">
              Member since {formatDate(user.createdAt)}
              {user.lastLogin ? ` · Last login ${formatDateTime(user.lastLogin)}` : ""}
            </p>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Profile details</CardTitle>
            <CardDescription>Your name and contact number shown across the CMS.</CardDescription>
          </CardHeader>
          <CardContent>
            <form
              className="space-y-4"
              onSubmit={profileForm.handleSubmit((v) => saveProfile.mutate(v))}
            >
              <div className="space-y-1.5">
                <Label htmlFor="name">Full name</Label>
                <Input id="name" {...profileForm.register("name")} />
                {profileForm.formState.errors.name && (
                  <p className="text-xs text-destructive">{profileForm.formState.errors.name.message}</p>
                )}
              </div>
              <div className="space-y-1.5">
                <Label htmlFor="email">Email</Label>
                <Input id="email" value={user.email} disabled />
                <p className="text-xs text-muted-foreground">
                  Email changes require a Super Admin.
                </p>
              </div>
              <div className="space-y-1.5">
                <Label htmlFor="phone">Phone</Label>
                <Input id="phone" {...profileForm.register("phone")} />
              </div>
              <Button type="submit" disabled={saveProfile.isPending}>
                {saveProfile.isPending ? "Saving…" : "Save profile"}
              </Button>
            </form>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Change password</CardTitle>
            <CardDescription>Use a strong password you don't reuse elsewhere.</CardDescription>
          </CardHeader>
          <CardContent>
            <form
              className="space-y-4"
              onSubmit={passwordForm.handleSubmit((v) =>
                changePassword.mutate({
                  currentPassword: v.currentPassword,
                  newPassword: v.newPassword,
                }),
              )}
            >
              <div className="space-y-1.5">
                <Label htmlFor="currentPassword">Current password</Label>
                <Input id="currentPassword" type="password" {...passwordForm.register("currentPassword")} />
                {pwErr("currentPassword")}
              </div>
              <div className="space-y-1.5">
                <Label htmlFor="newPassword">New password</Label>
                <Input id="newPassword" type="password" {...passwordForm.register("newPassword")} />
                {pwErr("newPassword")}
              </div>
              <div className="space-y-1.5">
                <Label htmlFor="confirmPassword">Confirm new password</Label>
                <Input id="confirmPassword" type="password" {...passwordForm.register("confirmPassword")} />
                {pwErr("confirmPassword")}
              </div>
              <Button type="submit" variant="secondary" disabled={changePassword.isPending}>
                {changePassword.isPending ? "Updating…" : "Update password"}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
