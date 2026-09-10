import { zodResolver } from "@hookform/resolvers/zod";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { Eye, EyeOff, Loader2, Lock, Mail, ShieldCheck } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

import { authApi } from "@/api/authApi";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/hooks/use-auth";

export const Route = createFileRoute("/login")({
  head: () => ({
    meta: [
      { title: "Sign in | Shield Global CMS" },
      {
        name: "description",
        content:
          "Secure sign-in for the Shield Global Group content management system. Authorised administrators only.",
      },
      { property: "og:title", content: "Sign in | Shield Global CMS" },
      {
        property: "og:description",
        content: "Secure administrator access to the Shield Global Group content management system.",
      },
      { name: "robots", content: "noindex,nofollow" },
    ],
  }),
  component: LoginPage,
});

const schema = z.object({
  email: z.string().min(1, "Email address is required").email("Enter a valid email address"),
  password: z.string().min(6, "Password must be at least 6 characters"),
  rememberMe: z.boolean(),
});

type FormValues = z.infer<typeof schema>;

function LoginPage() {
  const { login, isAuthenticated, isReady } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: { email: "", password: "", rememberMe: true },
  });

  useEffect(() => {
    if (isReady && isAuthenticated) navigate({ to: "/dashboard", replace: true });
  }, [isReady, isAuthenticated, navigate]);

  async function onSubmit(values: FormValues) {
    try {
      const user = await login(values);
      toast.success(`Welcome back, ${user.name.split(" ")[0]}.`);
      navigate({ to: "/dashboard", replace: true });
    } catch (error) {
      const message = error instanceof Error ? error.message : "Unable to sign in.";
      form.setError("password", { message });
      toast.error(message);
    }
  }

  return (
    <div className="grid min-h-screen lg:grid-cols-[1.05fr_1fr]">
      <div className="relative hidden flex-col justify-between bg-sidebar px-12 py-14 text-sidebar-foreground lg:flex">
        <div
          className="pointer-events-none absolute inset-0 opacity-[0.14]"
          style={{
            backgroundImage:
              "radial-gradient(circle at 20% 20%, var(--sidebar-primary) 0, transparent 45%), radial-gradient(circle at 80% 70%, var(--sidebar-primary) 0, transparent 40%)",
          }}
        />
        <div className="relative flex items-center gap-3">
          <div className="flex size-10 items-center justify-center rounded-md bg-sidebar-primary/15 text-sidebar-primary">
            <ShieldCheck className="size-5" />
          </div>
          <div className="leading-tight">
            <p className="font-display text-base font-semibold text-sidebar-accent-foreground">
              Shield Global Group
            </p>
            <p className="text-[11px] uppercase tracking-[0.2em] text-sidebar-foreground/60">
              Content Studio
            </p>
          </div>
        </div>

        <div className="relative max-w-lg space-y-6">
          <h2 className="font-display text-4xl font-semibold leading-tight text-sidebar-accent-foreground">
            Governed content operations for a global security enterprise.
          </h2>
          <p className="text-sm leading-relaxed text-sidebar-foreground/70">
            Publish website content, manage enquiries, control media assets and audit every change
            across seven country operations from a single, permission-controlled workspace.
          </p>
          <dl className="grid grid-cols-3 gap-6 border-t border-sidebar-border pt-6">
            {[
              ["7", "Countries"],
              ["12", "Group companies"],
              ["99.98%", "Platform uptime"],
            ].map(([value, label]) => (
              <div key={label}>
                <dt className="font-display text-2xl font-semibold text-sidebar-accent-foreground">
                  {value}
                </dt>
                <dd className="text-xs uppercase tracking-wider text-sidebar-foreground/60">
                  {label}
                </dd>
              </div>
            ))}
          </dl>
        </div>

        <p className="relative text-[11px] text-sidebar-foreground/50">
          © 2026 Shield Global Group. Unauthorised access is monitored and logged.
        </p>
      </div>

      <div className="flex items-center justify-center px-6 py-14 sm:px-12">
        <div className="w-full max-w-md">
          <div className="mb-8 flex items-center gap-3 lg:hidden">
            <div className="flex size-10 items-center justify-center rounded-md bg-primary/10 text-primary">
              <ShieldCheck className="size-5" />
            </div>
            <p className="font-display text-base font-semibold">Shield Global CMS</p>
          </div>

          <h1 className="font-display text-2xl font-semibold text-foreground">
            Sign in to the admin panel
          </h1>
          <p className="mt-2 text-sm text-muted-foreground">
            Use your Shield Global Group corporate credentials to continue.
          </p>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="mt-8 space-y-5">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email address</FormLabel>
                    <FormControl>
                      <div className="relative">
                        <Mail className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
                        <Input
                          {...field}
                          type="email"
                          autoComplete="email"
                          placeholder="name@shieldglobalgroup.com"
                          className="h-11 pl-9"
                        />
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <div className="relative">
                        <Lock className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
                        <Input
                          {...field}
                          type={showPassword ? "text" : "password"}
                          autoComplete="current-password"
                          placeholder="••••••••"
                          className="h-11 px-9"
                        />
                        <button
                          type="button"
                          onClick={() => setShowPassword((v) => !v)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                          aria-label={showPassword ? "Hide password" : "Show password"}
                        >
                          {showPassword ? <EyeOff className="size-4" /> : <Eye className="size-4" />}
                        </button>
                      </div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex items-center justify-between">
                <FormField
                  control={form.control}
                  name="rememberMe"
                  render={({ field }) => (
                    <FormItem className="flex flex-row items-center gap-2 space-y-0">
                      <FormControl>
                        <Checkbox checked={field.value} onCheckedChange={field.onChange} />
                      </FormControl>
                      <FormLabel className="text-sm font-normal text-muted-foreground">
                        Keep me signed in
                      </FormLabel>
                    </FormItem>
                  )}
                />
                <button
                  type="button"
                  onClick={() => toast.info("Contact IT support to reset your password.")}
                  className="text-sm font-medium text-primary hover:underline"
                >
                  Forgot password?
                </button>
              </div>

              <Button type="submit" className="h-11 w-full" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? (
                  <>
                    <Loader2 className="mr-2 size-4 animate-spin" /> Signing in…
                  </>
                ) : (
                  "Sign in"
                )}
              </Button>
            </form>
          </Form>

          <div className="mt-8 rounded-lg border border-dashed border-border bg-muted/40 p-4 text-sm">
            <p className="font-medium text-foreground">Demo credentials</p>
            <p className="mt-1 text-muted-foreground">
              {authApi.demoCredentials.email} / {authApi.demoCredentials.password}
            </p>
            <Button
              type="button"
              variant="outline"
              size="sm"
              className="mt-3"
              onClick={() =>
                form.reset({
                  email: authApi.demoCredentials.email,
                  password: authApi.demoCredentials.password,
                  rememberMe: true,
                })
              }
            >
              Fill demo credentials
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
