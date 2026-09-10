import { Link, useNavigate, useRouterState } from "@tanstack/react-router";
import {
  Bell,
  ChevronDown,
  ExternalLink,
  FileText,
  Gauge,
  Images,
  LayoutDashboard,
  LogOut,
  Mail,
  Menu,
  Moon,
  PanelsTopLeft,
  ScrollText,
  Search,
  Settings,
  ShieldCheck,
  Sun,
  UserCog,
  Users,
} from "lucide-react";
import { useEffect, useMemo, useState, type ReactNode } from "react";
import { toast } from "sonner";

import { dashboardApi } from "@/api/dashboardApi";
import { contentModules } from "@/components/admin/nav-config";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { Sheet, SheetContent } from "@/components/ui/sheet";
import { useAuth } from "@/hooks/use-auth";
import { initials, relativeTime } from "@/lib/format";
import { cn } from "@/lib/utils";
import { useQuery } from "@tanstack/react-query";

const primaryNav = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/media", label: "Media Library", icon: Images },
  { to: "/enquiries", label: "Enquiries", icon: Mail },
  { to: "/seo", label: "SEO Manager", icon: Gauge },
] as const;

const adminNav = [
  { to: "/users", label: "Users & Roles", icon: Users },
  { to: "/audit-logs", label: "Audit Logs", icon: ScrollText },
  { to: "/settings", label: "Settings", icon: Settings },
] as const;

function useTheme() {
  const [dark, setDark] = useState(false);
  useEffect(() => {
    const stored = window.localStorage.getItem("sgg.cms.theme");
    const isDark = stored === "dark";
    setDark(isDark);
    document.documentElement.classList.toggle("dark", isDark);
  }, []);
  const toggle = () => {
    setDark((prev) => {
      const next = !prev;
      document.documentElement.classList.toggle("dark", next);
      window.localStorage.setItem("sgg.cms.theme", next ? "dark" : "light");
      return next;
    });
  };
  return { dark, toggle };
}

function SidebarContent({ onNavigate }: { onNavigate?: () => void }) {
  const pathname = useRouterState({ select: (s) => s.location.pathname });
  const [contentOpen, setContentOpen] = useState(true);

  const linkClass = (active: boolean) =>
    cn(
      "flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium transition-colors",
      active
        ? "bg-sidebar-accent text-sidebar-accent-foreground shadow-[inset_2px_0_0_0_var(--sidebar-primary)]"
        : "text-sidebar-foreground/75 hover:bg-sidebar-accent/60 hover:text-sidebar-accent-foreground",
    );

  return (
    <div className="flex h-full flex-col bg-sidebar text-sidebar-foreground">
      <div className="flex items-center gap-3 border-b border-sidebar-border px-5 py-5">
        <div className="flex size-9 items-center justify-center rounded-md bg-sidebar-primary/15 text-sidebar-primary">
          <ShieldCheck className="size-5" />
        </div>
        <div className="leading-tight">
          <p className="font-display text-sm font-semibold text-sidebar-accent-foreground">
            Shield Global
          </p>
          <p className="text-[11px] uppercase tracking-[0.18em] text-sidebar-foreground/60">
            Content Studio
          </p>
        </div>
      </div>

      <ScrollArea className="flex-1 px-3 py-4">
        <p className="px-3 pb-2 text-[10px] font-semibold uppercase tracking-[0.18em] text-sidebar-foreground/50">
          Overview
        </p>
        <nav className="space-y-1">
          <Link to="/dashboard" onClick={onNavigate} className={linkClass(pathname === "/dashboard")}>
            <LayoutDashboard className="size-4" />
            Dashboard
          </Link>
        </nav>

        <p className="px-3 pb-2 pt-5 text-[10px] font-semibold uppercase tracking-[0.18em] text-sidebar-foreground/50">
          Content
        </p>
        <button
          type="button"
          onClick={() => setContentOpen((v) => !v)}
          className={cn(linkClass(pathname.startsWith("/content")), "w-full justify-between")}
        >
          <span className="flex items-center gap-3">
            <FileText className="size-4" />
            Website Content
          </span>
          <ChevronDown className={cn("size-4 transition-transform", contentOpen && "rotate-180")} />
        </button>
        {contentOpen ? (
          <div className="mt-1 space-y-0.5 border-l border-sidebar-border pl-3 ml-4">
            {contentModules.map((module) => (
              <Link
                key={module.slug}
                to="/content/$module"
                params={{ module: module.slug }}
                onClick={onNavigate}
                className={cn(
                  "block rounded-md px-3 py-1.5 text-[13px] transition-colors",
                  pathname === `/content/${module.slug}`
                    ? "bg-sidebar-accent text-sidebar-accent-foreground font-medium"
                    : "text-sidebar-foreground/65 hover:bg-sidebar-accent/50 hover:text-sidebar-accent-foreground",
                )}
              >
                {module.label}
              </Link>
            ))}
          </div>
        ) : null}

        <p className="px-3 pb-2 pt-5 text-[10px] font-semibold uppercase tracking-[0.18em] text-sidebar-foreground/50">
          Operations
        </p>
        <nav className="space-y-1">
          {primaryNav.slice(1).map((item) => (
            <Link key={item.to} to={item.to} onClick={onNavigate} className={linkClass(pathname === item.to)}>
              <item.icon className="size-4" />
              {item.label}
            </Link>
          ))}
        </nav>

        <p className="px-3 pb-2 pt-5 text-[10px] font-semibold uppercase tracking-[0.18em] text-sidebar-foreground/50">
          Administration
        </p>
        <nav className="space-y-1">
          {adminNav.map((item) => (
            <Link key={item.to} to={item.to} onClick={onNavigate} className={linkClass(pathname === item.to)}>
              <item.icon className="size-4" />
              {item.label}
            </Link>
          ))}
        </nav>
      </ScrollArea>

      <div className="border-t border-sidebar-border px-5 py-4 text-[11px] text-sidebar-foreground/55">
        <p className="font-medium text-sidebar-foreground/80">Shield Global CMS v2.4</p>
        <p>Connected to mock API layer</p>
      </div>
    </div>
  );
}

export function AdminShell({ children }: { children: ReactNode }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const { dark, toggle } = useTheme();
  const [mobileOpen, setMobileOpen] = useState(false);
  const pathname = useRouterState({ select: (s) => s.location.pathname });

  const { data: notifications = [] } = useQuery({
    queryKey: ["notifications"],
    queryFn: () => dashboardApi.notifications(),
  });
  const unread = useMemo(() => notifications.filter((n) => !n.read).length, [notifications]);

  const crumbs = pathname.split("/").filter(Boolean);

  async function handleLogout() {
    await logout();
    toast.success("You have been signed out.");
    navigate({ to: "/login", replace: true });
  }

  return (
    <div className="flex min-h-screen bg-background">
      <aside className="hidden w-[264px] shrink-0 lg:block">
        <div className="fixed inset-y-0 left-0 w-[264px]">
          <SidebarContent />
        </div>
      </aside>

      <Sheet open={mobileOpen} onOpenChange={setMobileOpen}>
        <SheetContent side="left" className="w-[280px] border-0 p-0">
          <SidebarContent onNavigate={() => setMobileOpen(false)} />
        </SheetContent>
      </Sheet>

      <div className="flex min-w-0 flex-1 flex-col">
        <header className="sticky top-0 z-30 border-b border-border bg-card/85 backdrop-blur">
          <div className="flex h-16 items-center gap-3 px-4 sm:px-6">
            <Button
              variant="ghost"
              size="icon"
              className="lg:hidden"
              onClick={() => setMobileOpen(true)}
              aria-label="Open navigation"
            >
              <Menu className="size-5" />
            </Button>

            <div className="hidden items-center gap-1.5 text-xs text-muted-foreground md:flex">
              <PanelsTopLeft className="size-3.5" />
              <span>CMS</span>
              {crumbs.map((crumb) => (
                <span key={crumb} className="flex items-center gap-1.5">
                  <span>/</span>
                  <span className="capitalize text-foreground">{crumb.replace(/-/g, " ")}</span>
                </span>
              ))}
            </div>

            <div className="ml-auto flex items-center gap-2">
              <div className="relative hidden xl:block">
                <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
                <Input placeholder="Search content, media, enquiries" className="h-9 w-72 pl-9" />
              </div>

              <Button variant="ghost" size="icon" asChild aria-label="View website">
                <a href="https://www.shieldglobalgroup.com" target="_blank" rel="noreferrer">
                  <ExternalLink className="size-4" />
                </a>
              </Button>

              <Button variant="ghost" size="icon" onClick={toggle} aria-label="Toggle theme">
                {dark ? <Sun className="size-4" /> : <Moon className="size-4" />}
              </Button>

              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="icon" className="relative" aria-label="Notifications">
                    <Bell className="size-4" />
                    {unread > 0 ? (
                      <span className="absolute right-1.5 top-1.5 flex size-4 items-center justify-center rounded-full bg-destructive text-[10px] font-semibold text-destructive-foreground">
                        {unread}
                      </span>
                    ) : null}
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-80">
                  <DropdownMenuLabel className="flex items-center justify-between">
                    Notifications
                    <Badge variant="secondary">{unread} new</Badge>
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  {notifications.slice(0, 5).map((item) => (
                    <DropdownMenuItem key={item.id} className="flex flex-col items-start gap-0.5 py-2.5">
                      <span className="text-sm font-medium text-foreground">{item.title}</span>
                      <span className="text-xs text-muted-foreground">{item.description}</span>
                      <span className="text-[11px] text-muted-foreground/80">{relativeTime(item.at)}</span>
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>

              <Separator orientation="vertical" className="mx-1 hidden h-8 sm:block" />

              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <button className="flex items-center gap-2 rounded-md px-1.5 py-1 transition-colors hover:bg-accent">
                    <Avatar className="size-8">
                      <AvatarFallback className="bg-primary/10 text-xs font-semibold text-primary">
                        {initials(user?.name ?? "SG")}
                      </AvatarFallback>
                    </Avatar>
                    <span className="hidden text-left leading-tight sm:block">
                      <span className="block text-sm font-medium text-foreground">{user?.name}</span>
                      <span className="block text-[11px] text-muted-foreground">
                        {user?.role.replace("_", " ")}
                      </span>
                    </span>
                  </button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-56">
                  <DropdownMenuLabel>{user?.email}</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onSelect={() => navigate({ to: "/profile" })}>
                    <UserCog className="mr-2 size-4" /> My profile
                  </DropdownMenuItem>
                  <DropdownMenuItem onSelect={() => navigate({ to: "/settings" })}>
                    <Settings className="mr-2 size-4" /> Site settings
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onSelect={handleLogout} className="text-destructive">
                    <LogOut className="mr-2 size-4" /> Sign out
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
        </header>

        <main className="flex-1 px-4 py-6 sm:px-6 lg:px-8">
          <div className="mx-auto w-full max-w-[1400px] space-y-6">{children}</div>
        </main>

        <footer className="border-t border-border px-6 py-4 text-xs text-muted-foreground">
          © 2026 Shield Global Group. Internal content management system.
        </footer>
      </div>
    </div>
  );
}
