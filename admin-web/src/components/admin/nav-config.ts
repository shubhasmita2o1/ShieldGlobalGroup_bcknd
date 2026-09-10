import type { ContentModule } from "@/api/contentApi";
import type { PermissionModule } from "@/types/admin";

export interface ContentModuleConfig {
  slug: ContentModule;
  label: string;
  description: string;
  permission: PermissionModule;
  itemNoun: string;
}

export const contentModules: ContentModuleConfig[] = [
  {
    slug: "home",
    label: "Home Page",
    description: "Hero banners, highlight strips and homepage sections.",
    permission: "pages",
    itemNoun: "Section",
  },
  {
    slug: "pages",
    label: "Pages",
    description: "About, careers, compliance and other static pages.",
    permission: "pages",
    itemNoun: "Page",
  },
  {
    slug: "services",
    label: "Services",
    description: "Service lines offered across the group.",
    permission: "services",
    itemNoun: "Service",
  },
  {
    slug: "companies",
    label: "Group Companies",
    description: "Operating companies within Shield Global Group.",
    permission: "companies",
    itemNoun: "Company",
  },
  {
    slug: "timeline",
    label: "Timeline",
    description: "Corporate milestones shown on the about page.",
    permission: "pages",
    itemNoun: "Milestone",
  },
  {
    slug: "achievements",
    label: "Achievements",
    description: "Awards, certifications and recognitions.",
    permission: "pages",
    itemNoun: "Achievement",
  },
  {
    slug: "testimonials",
    label: "Testimonials",
    description: "Client quotes published across the website.",
    permission: "pages",
    itemNoun: "Testimonial",
  },
  {
    slug: "partners",
    label: "Partners & Clients",
    description: "Partner and client logos with display order.",
    permission: "companies",
    itemNoun: "Partner",
  },
  {
    slug: "global-presence",
    label: "Global Presence",
    description: "Regional offices and country operations.",
    permission: "companies",
    itemNoun: "Location",
  },
];

export function findContentModule(slug: string): ContentModuleConfig | undefined {
  return contentModules.find((m) => m.slug === slug);
}
