import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";
import { toast } from "sonner";

import { settingsApi } from "@/api/settingsApi";
import { PageHeader } from "@/components/admin/PageHeader";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
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
import { Switch } from "@/components/ui/switch";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import type { SiteSettings } from "@/types/admin";

export const Route = createFileRoute("/_admin/settings")({
  head: () => ({
    meta: [
      { title: "Site Settings | Shield Global CMS" },
      { name: "description", content: "General, social, contact and system configuration." },
      { property: "og:title", content: "Site Settings | Shield Global CMS" },
      { property: "og:description", content: "Global configuration for the Shield Global website." },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: SettingsPage,
});

type Section = keyof SiteSettings;

function SettingsPage() {
  const queryClient = useQueryClient();
  const settings = useQuery({ queryKey: ["settings"], queryFn: () => settingsApi.get() });
  const [draft, setDraft] = useState<SiteSettings | null>(null);

  useEffect(() => {
    if (settings.data && !draft) setDraft(structuredClone(settings.data));
  }, [settings.data, draft]);

  const save = useMutation({
    mutationFn: ({ section, input }: { section: Section; input: Partial<SiteSettings[Section]> }) =>
      settingsApi.update(section, input),
    onSuccess: (next) => {
      toast.success("Settings saved.");
      setDraft(structuredClone(next));
      void queryClient.invalidateQueries({ queryKey: ["settings"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  if (settings.isPending || !draft) {
    return (
      <div className="space-y-6">
        <PageHeader eyebrow="Configuration" title="Site Settings" />
        <Skeleton className="h-96 w-full rounded-lg" />
      </div>
    );
  }

  const set = <S extends Section>(section: S, key: keyof SiteSettings[S], value: string | boolean) =>
    setDraft({ ...draft, [section]: { ...draft[section], [key]: value } });

  const field = (
    section: Section,
    key: string,
    label: string,
    opts: { textarea?: boolean } = {},
  ) => {
    const value = String(
      (draft[section] as Record<string, string | boolean>)[key] ?? "",
    );
    return (
      <div className="space-y-1.5">
        <Label>{label}</Label>
        {opts.textarea ? (
          <Textarea rows={3} value={value} onChange={(e) => set(section, key as never, e.target.value)} />
        ) : (
          <Input value={value} onChange={(e) => set(section, key as never, e.target.value)} />
        )}
      </div>
    );
  };

  const saveBar = (section: Section) => (
    <div className="flex justify-end border-t border-border pt-4">
      <Button
        disabled={save.isPending}
        onClick={() => save.mutate({ section, input: draft[section] })}
      >
        {save.isPending ? "Saving…" : "Save changes"}
      </Button>
    </div>
  );

  return (
    <div className="space-y-6">
      <PageHeader
        eyebrow="Configuration"
        title="Site Settings"
        description="Global website identity, social profiles, contact details and system behaviour."
      />

      <Tabs defaultValue="general">
        <TabsList>
          <TabsTrigger value="general">General</TabsTrigger>
          <TabsTrigger value="social">Social</TabsTrigger>
          <TabsTrigger value="contact">Contact</TabsTrigger>
          <TabsTrigger value="system">System</TabsTrigger>
        </TabsList>

        <TabsContent value="general">
          <Card>
            <CardHeader>
              <CardTitle>General</CardTitle>
              <CardDescription>Website identity used across pages and metadata.</CardDescription>
            </CardHeader>
            <CardContent className="grid gap-4 sm:grid-cols-2">
              {field("general", "websiteName", "Website name")}
              {field("general", "tagline", "Tagline")}
              {field("general", "contactEmail", "Primary email")}
              {field("general", "phone", "Phone")}
              <div className="sm:col-span-2">{field("general", "address", "Registered address", { textarea: true })}</div>
              <div className="sm:col-span-2">{saveBar("general")}</div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="social">
          <Card>
            <CardHeader>
              <CardTitle>Social profiles</CardTitle>
              <CardDescription>Linked from the website footer and contact page.</CardDescription>
            </CardHeader>
            <CardContent className="grid gap-4 sm:grid-cols-2">
              {field("social", "linkedin", "LinkedIn URL")}
              {field("social", "facebook", "Facebook URL")}
              {field("social", "instagram", "Instagram URL")}
              {field("social", "youtube", "YouTube URL")}
              {field("social", "twitter", "X (Twitter) URL")}
              <div className="sm:col-span-2">{saveBar("social")}</div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="contact">
          <Card>
            <CardHeader>
              <CardTitle>Contact details</CardTitle>
              <CardDescription>Shown on the public contact page and enquiry auto-replies.</CardDescription>
            </CardHeader>
            <CardContent className="grid gap-4 sm:grid-cols-2">
              {field("contact", "contactEmail", "Contact email")}
              {field("contact", "contactPhone", "Contact phone")}
              <div className="sm:col-span-2">{field("contact", "officeAddress", "Head office address", { textarea: true })}</div>
              <div className="sm:col-span-2">{saveBar("contact")}</div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="system">
          <Card>
            <CardHeader>
              <CardTitle>System</CardTitle>
              <CardDescription>Operational switches — apply with care.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-5">
              <div className="flex items-center justify-between rounded-lg border border-border p-4">
                <div>
                  <p className="text-sm font-medium text-foreground">Maintenance mode</p>
                  <p className="text-xs text-muted-foreground">
                    Visitors see a holding page while enabled.
                  </p>
                </div>
                <Switch
                  checked={draft.system.maintenanceMode}
                  onCheckedChange={(v) => set("system", "maintenanceMode", v)}
                />
              </div>
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="space-y-1.5">
                  <Label>Default language</Label>
                  <Select
                    value={draft.system.defaultLanguage}
                    onValueChange={(v) => set("system", "defaultLanguage", v)}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="en">English</SelectItem>
                      <SelectItem value="hi">Hindi</SelectItem>
                      <SelectItem value="ar">Arabic</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-1.5">
                  <Label>Timezone</Label>
                  <Select
                    value={draft.system.timezone}
                    onValueChange={(v) => set("system", "timezone", v)}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="Asia/Kolkata">Asia/Kolkata (IST)</SelectItem>
                      <SelectItem value="Asia/Dubai">Asia/Dubai (GST)</SelectItem>
                      <SelectItem value="Europe/London">Europe/London (GMT)</SelectItem>
                      <SelectItem value="America/New_York">America/New_York (ET)</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              {saveBar("system")}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
