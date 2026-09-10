import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

const tones: Record<string, string> = {
  PUBLISHED: "bg-success/12 text-success border-success/25",
  ACTIVE: "bg-success/12 text-success border-success/25",
  CLOSED: "bg-success/12 text-success border-success/25",
  DRAFT: "bg-warning/15 text-warning-foreground border-warning/35",
  INVITED: "bg-warning/15 text-warning-foreground border-warning/35",
  IN_PROGRESS: "bg-info/12 text-info border-info/25",
  CONTACTED: "bg-info/12 text-info border-info/25",
  NEW: "bg-primary/10 text-primary border-primary/25",
  ARCHIVED: "bg-muted text-muted-foreground border-border",
  DISABLED: "bg-muted text-muted-foreground border-border",
  SPAM: "bg-destructive/12 text-destructive border-destructive/25",
};

export function StatusBadge({ status, className }: { status: string; className?: string }) {
  return (
    <Badge
      variant="outline"
      className={cn(
        "rounded-full px-2.5 py-0.5 text-[11px] font-semibold uppercase tracking-wide",
        tones[status] ?? "bg-muted text-muted-foreground border-border",
        className,
      )}
    >
      {status.replace(/_/g, " ")}
    </Badge>
  );
}
