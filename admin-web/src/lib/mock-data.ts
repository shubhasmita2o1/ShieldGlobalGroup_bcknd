/**
 * Single source of mock data for the CMS.
 * Only `src/api/*` reads from this file — UI never imports it directly.
 */
import type {
  ActivityItem,
  ActivityPoint,
  AdminUser,
  AuditLog,
  ContentRecord,
  DistributionSlice,
  Enquiry,
  HealthStatus,
  KpiMetric,
  MediaAsset,
  NotificationItem,
  SeoEntry,
  SiteSettings,
} from "@/types/admin";

const now = new Date("2026-09-01T06:00:00Z");

function daysAgo(n: number, hour = 10) {
  const d = new Date(now);
  d.setUTCDate(d.getUTCDate() - n);
  d.setUTCHours(hour, 0, 0, 0);
  return d.toISOString();
}

export const currentUser: AdminUser = {
  id: "usr-001",
  name: "Arjun Mehta",
  email: "admin@shieldglobalgroup.com",
  phone: "+971 4 512 8800",
  role: "SUPER_ADMIN",
  status: "ACTIVE",
  lastLogin: daysAgo(0, 5),
  createdAt: daysAgo(420),
  permissions: [
    "pages",
    "services",
    "companies",
    "media",
    "enquiries",
    "seo",
    "users",
    "settings",
    "audit-logs",
  ],
};

export const users: AdminUser[] = [
  currentUser,
  {
    id: "usr-002",
    name: "Fatima Al Rashid",
    email: "fatima.rashid@shieldglobalgroup.com",
    phone: "+971 4 512 8812",
    role: "ADMIN",
    status: "ACTIVE",
    lastLogin: daysAgo(1, 9),
    createdAt: daysAgo(310),
    permissions: ["pages", "services", "companies", "media", "enquiries", "seo"],
  },
  {
    id: "usr-003",
    name: "Daniel Okafor",
    email: "daniel.okafor@shieldglobalgroup.com",
    phone: "+44 20 7946 0110",
    role: "EDITOR",
    status: "ACTIVE",
    lastLogin: daysAgo(2, 14),
    createdAt: daysAgo(180),
    permissions: ["pages", "services", "media"],
  },
  {
    id: "usr-004",
    name: "Priya Nair",
    email: "priya.nair@shieldglobalgroup.com",
    phone: "+91 22 4004 1188",
    role: "EDITOR",
    status: "INVITED",
    lastLogin: null,
    createdAt: daysAgo(12),
    permissions: ["pages", "media"],
  },
  {
    id: "usr-005",
    name: "Marcus Lin",
    email: "marcus.lin@shieldglobalgroup.com",
    phone: "+65 6812 3300",
    role: "ADMIN",
    status: "DISABLED",
    lastLogin: daysAgo(64, 11),
    createdAt: daysAgo(500),
    permissions: ["pages", "services", "enquiries", "seo"],
  },
];

function record(
  id: string,
  title: string,
  subtitle: string,
  category: string,
  status: ContentRecord["status"],
  author: string,
  ago: number,
  order: number,
): ContentRecord {
  return { id, title, subtitle, category, status, author, updatedAt: daysAgo(ago), order };
}

export const contentSets: Record<string, ContentRecord[]> = {
  home: [
    record("home-1", "Hero — Securing Global Enterprise", "Primary homepage banner", "Hero", "PUBLISHED", "Arjun Mehta", 1, 1),
    record("home-2", "Group Overview Statement", "Intro paragraph block", "Section", "PUBLISHED", "Fatima Al Rashid", 3, 2),
    record("home-3", "Capability Highlights", "Four-column capability grid", "Section", "PUBLISHED", "Daniel Okafor", 5, 3),
    record("home-4", "Global Footprint Strip", "Map + country counters", "Section", "DRAFT", "Daniel Okafor", 2, 4),
    record("home-5", "Leadership Message", "Chairman statement block", "Section", "PUBLISHED", "Arjun Mehta", 9, 5),
    record("home-6", "Client Logos Marquee", "Trusted-by logo band", "Section", "ARCHIVED", "Marcus Lin", 41, 6),
    record("home-7", "Enquiry CTA Band", "Bottom conversion band", "CTA", "PUBLISHED", "Fatima Al Rashid", 6, 7),
  ],
  pages: [
    record("pg-1", "About Shield Global Group", "/about", "Corporate", "PUBLISHED", "Arjun Mehta", 4, 1),
    record("pg-2", "Leadership & Governance", "/leadership", "Corporate", "PUBLISHED", "Fatima Al Rashid", 8, 2),
    record("pg-3", "Sustainability Commitment", "/sustainability", "Corporate", "DRAFT", "Daniel Okafor", 1, 3),
    record("pg-4", "Careers at Shield", "/careers", "Careers", "PUBLISHED", "Fatima Al Rashid", 12, 4),
    record("pg-5", "Newsroom", "/newsroom", "Media", "PUBLISHED", "Daniel Okafor", 2, 5),
    record("pg-6", "Investor Relations", "/investors", "Corporate", "DRAFT", "Arjun Mehta", 7, 6),
    record("pg-7", "Compliance & Ethics", "/compliance", "Legal", "PUBLISHED", "Marcus Lin", 22, 7),
    record("pg-8", "Privacy Policy", "/privacy", "Legal", "PUBLISHED", "Marcus Lin", 30, 8),
    record("pg-9", "Terms of Use", "/terms", "Legal", "ARCHIVED", "Marcus Lin", 96, 9),
    record("pg-10", "Contact Shield Global", "/contact", "Corporate", "PUBLISHED", "Fatima Al Rashid", 3, 10),
  ],
  services: [
    record("sv-1", "Integrated Facility Management", "End-to-end FM programmes", "Operations", "PUBLISHED", "Arjun Mehta", 5, 1),
    record("sv-2", "Manned Guarding & Protection", "Licensed security workforce", "Security", "PUBLISHED", "Fatima Al Rashid", 2, 2),
    record("sv-3", "Electronic Security Systems", "CCTV, access, integration", "Security", "PUBLISHED", "Daniel Okafor", 11, 3),
    record("sv-4", "Risk & Threat Advisory", "Corporate risk consulting", "Advisory", "DRAFT", "Arjun Mehta", 1, 4),
    record("sv-5", "Logistics & Supply Chain", "Cross-border movement", "Logistics", "PUBLISHED", "Marcus Lin", 18, 5),
    record("sv-6", "Manpower Outsourcing", "Skilled workforce supply", "Operations", "PUBLISHED", "Fatima Al Rashid", 9, 6),
    record("sv-7", "Technical Maintenance", "MEP and asset upkeep", "Operations", "DRAFT", "Daniel Okafor", 4, 7),
    record("sv-8", "Cash Management Solutions", "Secure valuables transit", "Security", "PUBLISHED", "Arjun Mehta", 26, 8),
  ],
  companies: [
    record("co-1", "Shield Security Services LLC", "Dubai, UAE", "Security", "PUBLISHED", "Arjun Mehta", 6, 1),
    record("co-2", "Shield Facilities Management", "Abu Dhabi, UAE", "Facilities", "PUBLISHED", "Fatima Al Rashid", 3, 2),
    record("co-3", "Shield Logistics International", "Jebel Ali, UAE", "Logistics", "PUBLISHED", "Marcus Lin", 14, 3),
    record("co-4", "Shield Technologies FZE", "Sharjah, UAE", "Technology", "PUBLISHED", "Daniel Okafor", 8, 4),
    record("co-5", "Shield Manpower Solutions", "Riyadh, KSA", "Staffing", "DRAFT", "Fatima Al Rashid", 2, 5),
    record("co-6", "Shield Global UK Ltd", "London, UK", "Security", "PUBLISHED", "Daniel Okafor", 19, 6),
    record("co-7", "Shield Global India Pvt Ltd", "Mumbai, India", "Operations", "PUBLISHED", "Priya Nair", 10, 7),
    record("co-8", "Shield Marine Services", "Fujairah, UAE", "Marine", "PUBLISHED", "Arjun Mehta", 33, 8),
    record("co-9", "Shield Energy Support", "Doha, Qatar", "Energy", "DRAFT", "Marcus Lin", 5, 9),
    record("co-10", "Shield Aviation Support", "Muscat, Oman", "Aviation", "PUBLISHED", "Arjun Mehta", 44, 10),
    record("co-11", "Shield Retail Protection", "Singapore", "Retail", "PUBLISHED", "Marcus Lin", 51, 11),
    record("co-12", "Shield Ventures Holding", "Dubai, UAE", "Corporate", "ARCHIVED", "Arjun Mehta", 120, 12),
  ],
  timeline: [
    record("tl-1", "2004 — Foundation in Dubai", "First security contract awarded", "Milestone", "PUBLISHED", "Arjun Mehta", 40, 1),
    record("tl-2", "2009 — Facilities division launched", "Expansion into integrated FM", "Milestone", "PUBLISHED", "Fatima Al Rashid", 40, 2),
    record("tl-3", "2013 — 5,000 employees", "Workforce milestone", "Milestone", "PUBLISHED", "Marcus Lin", 39, 3),
    record("tl-4", "2017 — UK entry", "Shield Global UK incorporated", "Expansion", "PUBLISHED", "Daniel Okafor", 21, 4),
    record("tl-5", "2021 — Technology division", "Electronic security business", "Expansion", "PUBLISHED", "Arjun Mehta", 15, 5),
    record("tl-6", "2024 — 12 group companies", "Consolidated group structure", "Milestone", "PUBLISHED", "Fatima Al Rashid", 7, 6),
    record("tl-7", "2026 — Asia-Pacific expansion", "Singapore + Malaysia rollout", "Expansion", "DRAFT", "Arjun Mehta", 1, 7),
  ],
  achievements: [
    record("ac-1", "ISO 9001:2015 Certification", "Quality management", "Certification", "PUBLISHED", "Fatima Al Rashid", 12, 1),
    record("ac-2", "ISO 45001 Occupational Safety", "Safety management", "Certification", "PUBLISHED", "Fatima Al Rashid", 12, 2),
    record("ac-3", "Gulf Security Excellence Award", "2025 winner", "Award", "PUBLISHED", "Arjun Mehta", 5, 3),
    record("ac-4", "Great Place to Work — UAE", "2025 certified", "Recognition", "PUBLISHED", "Daniel Okafor", 20, 4),
    record("ac-5", "SIRA Grade A Rating", "Regulatory rating", "Rating", "PUBLISHED", "Marcus Lin", 28, 5),
    record("ac-6", "Zero Lost-Time Incidents", "18 million man-hours", "Milestone", "DRAFT", "Arjun Mehta", 2, 6),
  ],
  testimonials: [
    record("ts-1", "Emaar Properties", "\"Consistently exceptional service standards.\"", "Real Estate", "PUBLISHED", "Fatima Al Rashid", 3, 1),
    record("ts-2", "DP World", "\"A dependable partner across all terminals.\"", "Logistics", "PUBLISHED", "Arjun Mehta", 9, 2),
    record("ts-3", "Majid Al Futtaim", "\"Their teams are an extension of ours.\"", "Retail", "PUBLISHED", "Daniel Okafor", 1, 3),
    record("ts-4", "ADNOC", "\"Uncompromising on safety and compliance.\"", "Energy", "DRAFT", "Marcus Lin", 4, 4),
    record("ts-5", "Jumeirah Group", "\"Discreet, professional, always on time.\"", "Hospitality", "PUBLISHED", "Fatima Al Rashid", 16, 5),
    record("ts-6", "Standard Chartered", "\"Rigorous controls and reporting.\"", "Banking", "PUBLISHED", "Arjun Mehta", 34, 6),
  ],
  partners: [
    record("pt-1", "Genetec", "Security platform partner", "Technology", "PUBLISHED", "Daniel Okafor", 6, 1),
    record("pt-2", "Axis Communications", "Certified integrator", "Technology", "PUBLISHED", "Daniel Okafor", 6, 2),
    record("pt-3", "Honeywell", "Building systems partner", "Technology", "PUBLISHED", "Marcus Lin", 24, 3),
    record("pt-4", "Bosch Security", "Distribution partner", "Technology", "PUBLISHED", "Arjun Mehta", 30, 4),
    record("pt-5", "G4S Consulting", "Advisory alliance", "Advisory", "DRAFT", "Fatima Al Rashid", 2, 5),
    record("pt-6", "Siemens Smart Infrastructure", "Automation partner", "Technology", "PUBLISHED", "Daniel Okafor", 13, 6),
    record("pt-7", "SIRA", "Regulatory affiliation", "Regulatory", "PUBLISHED", "Arjun Mehta", 47, 7),
  ],
  "global-presence": [
    record("gp-1", "United Arab Emirates", "Headquarters — Dubai", "Middle East", "PUBLISHED", "Arjun Mehta", 4, 1),
    record("gp-2", "Kingdom of Saudi Arabia", "Regional office — Riyadh", "Middle East", "PUBLISHED", "Fatima Al Rashid", 8, 2),
    record("gp-3", "Qatar", "Operations — Doha", "Middle East", "PUBLISHED", "Marcus Lin", 17, 3),
    record("gp-4", "Oman", "Operations — Muscat", "Middle East", "PUBLISHED", "Marcus Lin", 17, 4),
    record("gp-5", "United Kingdom", "Regional office — London", "Europe", "PUBLISHED", "Daniel Okafor", 11, 5),
    record("gp-6", "India", "Delivery centre — Mumbai", "Asia", "PUBLISHED", "Priya Nair", 9, 6),
    record("gp-7", "Singapore", "Representative office", "Asia", "DRAFT", "Arjun Mehta", 1, 7),
    record("gp-8", "Kenya", "Emerging market office — Nairobi", "Africa", "ARCHIVED", "Marcus Lin", 88, 8),
  ],
};

export const kpis: KpiMetric[] = [
  { id: "pages", label: "Total Pages", value: 24, change: 14.3, changeLabel: "+3 this month", supporting: "Across 6 site sections", icon: "FileText" },
  { id: "published", label: "Published Content", value: 18, change: 5.9, changeLabel: "+1 this week", supporting: "Live on shieldglobalgroup.com", icon: "CheckCircle2" },
  { id: "drafts", label: "Draft Content", value: 6, change: -12.5, changeLabel: "-1 vs last month", supporting: "Awaiting editorial review", icon: "PencilLine" },
  { id: "services", label: "Services", value: 8, change: 0, changeLabel: "No change", supporting: "3 categories", icon: "Layers" },
  { id: "companies", label: "Group Companies", value: 12, change: 9.1, changeLabel: "+1 this quarter", supporting: "7 countries", icon: "Building2" },
  { id: "enquiries", label: "Contact Enquiries", value: 42, change: 23.5, changeLabel: "+8 this month", supporting: "12 awaiting response", icon: "Mail" },
  { id: "media", label: "Media Assets", value: 156, change: 6.8, changeLabel: "+10 this month", supporting: "2.4 GB of storage used", icon: "Images" },
  { id: "users", label: "Admin Users", value: 5, change: 25, changeLabel: "+1 this month", supporting: "3 active today", icon: "Users" },
];

export const activitySeries: ActivityPoint[] = Array.from({ length: 30 }, (_, i) => {
  const index = 29 - i;
  return {
    date: daysAgo(index).slice(0, 10),
    published: 2 + ((index * 7) % 5),
    updated: 4 + ((index * 3) % 7),
    drafted: 1 + ((index * 5) % 4),
  };
});

export const distribution: DistributionSlice[] = [
  { name: "Pages", value: 24 },
  { name: "Services", value: 8 },
  { name: "Companies", value: 12 },
  { name: "Testimonials", value: 6 },
  { name: "Partners", value: 7 },
  { name: "Media", value: 156 },
];

export const contentStatusBreakdown = [
  { label: "Published", value: 75, count: 42, tone: "success" as const },
  { label: "Draft", value: 20, count: 11, tone: "warning" as const },
  { label: "Archived", value: 5, count: 3, tone: "neutral" as const },
];

export const recentActivity: ActivityItem[] = [
  { id: "af-1", user: "Arjun Mehta", action: "updated", contentType: "Home", target: "Homepage Hero", timestamp: daysAgo(0, 5), tone: "info" },
  { id: "af-2", user: "Fatima Al Rashid", action: "added", contentType: "Testimonial", target: "Majid Al Futtaim", timestamp: daysAgo(0, 4), tone: "success" },
  { id: "af-3", user: "Daniel Okafor", action: "updated", contentType: "Company", target: "Shield Technologies FZE", timestamp: daysAgo(1, 16), tone: "info" },
  { id: "af-4", user: "System", action: "received", contentType: "Enquiry", target: "Emaar Properties — RFP", timestamp: daysAgo(1, 11), tone: "warning" },
  { id: "af-5", user: "Daniel Okafor", action: "replaced", contentType: "Partner", target: "Genetec logo", timestamp: daysAgo(2, 9), tone: "neutral" },
  { id: "af-6", user: "Marcus Lin", action: "published", contentType: "Page", target: "Compliance & Ethics", timestamp: daysAgo(3, 13), tone: "success" },
];

export const enquiries: Enquiry[] = [
  {
    id: "enq-1001",
    name: "Sarah Whitfield",
    email: "s.whitfield@emaar.com",
    phone: "+971 50 224 8891",
    company: "Emaar Properties",
    subject: "Integrated FM proposal for Downtown portfolio",
    message:
      "We are shortlisting partners for an integrated facility management programme across 14 assets in Downtown Dubai. Could you share capability credentials and available references?",
    status: "NEW",
    createdAt: daysAgo(0, 4),
    notes: [],
    history: [{ id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(0, 4), by: "Website" }],
  },
  {
    id: "enq-1002",
    name: "Omar Haddad",
    email: "omar.haddad@dpworld.com",
    phone: "+971 4 881 4400",
    company: "DP World",
    subject: "Manned guarding extension — Jebel Ali",
    message: "Requesting a quotation to extend manned guarding coverage to two additional terminal gates.",
    status: "IN_PROGRESS",
    createdAt: daysAgo(1, 9),
    notes: [{ id: "n1", author: "Fatima Al Rashid", body: "Commercial team preparing revised rate card.", createdAt: daysAgo(1, 12) }],
    history: [
      { id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(1, 9), by: "Website" },
      { id: "h2", label: "Status changed to IN PROGRESS", at: daysAgo(1, 10), by: "Fatima Al Rashid" },
    ],
  },
  {
    id: "enq-1003",
    name: "Ananya Krishnan",
    email: "ananya.k@mafgroup.com",
    phone: "+971 4 294 9000",
    company: "Majid Al Futtaim",
    subject: "Retail loss prevention programme",
    message: "Looking for a loss prevention pilot across three malls in Q4.",
    status: "CONTACTED",
    createdAt: daysAgo(3, 15),
    notes: [{ id: "n1", author: "Arjun Mehta", body: "Intro call completed. Sending pilot scope Monday.", createdAt: daysAgo(2, 10) }],
    history: [
      { id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(3, 15), by: "Website" },
      { id: "h2", label: "Marked as contacted", at: daysAgo(2, 10), by: "Arjun Mehta" },
    ],
  },
  {
    id: "enq-1004",
    name: "James Alderton",
    email: "j.alderton@stanchart.com",
    phone: "+44 20 7885 8888",
    company: "Standard Chartered",
    subject: "Cash-in-transit tender clarification",
    message: "Two clarification points on the tender documentation regarding insurance limits.",
    status: "CLOSED",
    createdAt: daysAgo(9, 11),
    notes: [{ id: "n1", author: "Marcus Lin", body: "Clarifications issued; tender submitted.", createdAt: daysAgo(7, 9) }],
    history: [
      { id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(9, 11), by: "Website" },
      { id: "h2", label: "Closed after tender submission", at: daysAgo(7, 9), by: "Marcus Lin" },
    ],
  },
  {
    id: "enq-1005",
    name: "Crypto Growth Team",
    email: "promo@fastleads.biz",
    phone: "+1 202 555 0116",
    company: "FastLeads",
    subject: "Guaranteed traffic offer",
    message: "Increase your traffic 10x with our SEO package.",
    status: "SPAM",
    createdAt: daysAgo(5, 2),
    notes: [],
    history: [{ id: "h1", label: "Flagged as spam", at: daysAgo(5, 3), by: "Daniel Okafor" }],
  },
  {
    id: "enq-1006",
    name: "Layla Farouk",
    email: "layla.farouk@adnoc.ae",
    phone: "+971 2 707 0000",
    company: "ADNOC",
    subject: "HSE audit support for offshore facilities",
    message: "Do you provide HSE audit and compliance support for offshore operations?",
    status: "NEW",
    createdAt: daysAgo(2, 8),
    notes: [],
    history: [{ id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(2, 8), by: "Website" }],
  },
  {
    id: "enq-1007",
    name: "Peter Nakamura",
    email: "p.nakamura@jumeirah.com",
    phone: "+971 4 366 5000",
    company: "Jumeirah Group",
    subject: "Event security for hospitality season",
    message: "Seasonal event security resourcing for six properties, November through February.",
    status: "IN_PROGRESS",
    createdAt: daysAgo(4, 13),
    notes: [],
    history: [{ id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(4, 13), by: "Website" }],
  },
  {
    id: "enq-1008",
    name: "Grace Mwangi",
    email: "grace.mwangi@safaricom.co.ke",
    phone: "+254 20 427 2000",
    company: "Safaricom",
    subject: "Regional partnership enquiry",
    message: "Exploring a regional security partnership across East Africa.",
    status: "NEW",
    createdAt: daysAgo(6, 7),
    notes: [],
    history: [{ id: "h1", label: "Enquiry submitted via /contact", at: daysAgo(6, 7), by: "Website" }],
  },
];

const mediaSeed: Array<[string, MediaAsset["type"], string, number, string]> = [
  ["hero-headquarters-dubai.jpg", "IMAGE", "jpg", 842, "1920x1080"],
  ["group-leadership-portrait.jpg", "IMAGE", "jpg", 615, "1600x1067"],
  ["shield-global-logo-primary.svg", "LOGO", "svg", 24, "512x512"],
  ["shield-global-logo-mono.svg", "LOGO", "svg", 21, "512x512"],
  ["corporate-brochure-2026.pdf", "DOCUMENT", "pdf", 3480, ""],
  ["iso-9001-certificate.pdf", "DOCUMENT", "pdf", 780, ""],
  ["operations-showreel.mp4", "VIDEO", "mp4", 24600, "1920x1080"],
  ["facility-management-team.jpg", "IMAGE", "jpg", 720, "1600x900"],
  ["control-room-night.jpg", "IMAGE", "jpg", 910, "1920x1280"],
  ["partner-genetec-logo.png", "LOGO", "png", 38, "400x160"],
  ["partner-axis-logo.png", "LOGO", "png", 41, "400x160"],
  ["sustainability-report.pdf", "DOCUMENT", "pdf", 5120, ""],
  ["logistics-fleet-aerial.jpg", "IMAGE", "jpg", 1180, "2400x1350"],
  ["client-testimonial-clip.mp4", "VIDEO", "mp4", 18400, "1280x720"],
  ["office-london-exterior.jpg", "IMAGE", "jpg", 690, "1600x1067"],
  ["career-fair-2026.jpg", "IMAGE", "jpg", 540, "1400x933"],
];

export const media: MediaAsset[] = mediaSeed.map(([name, type, ext, sizeKb, dims], i) => ({
  id: `md-${i + 1}`,
  name,
  type,
  extension: ext,
  sizeKb,
  url: `https://cdn.shieldglobalgroup.com/media/${name}`,
  uploadedAt: daysAgo(i * 2 + 1, 12),
  uploadedBy: i % 3 === 0 ? "Arjun Mehta" : i % 3 === 1 ? "Fatima Al Rashid" : "Daniel Okafor",
  altText: name.replace(/[-_]/g, " ").replace(/\.\w+$/, ""),
  ...(dims ? { dimensions: dims } : {}),
}));

export const seoEntries: SeoEntry[] = [
  {
    id: "seo-1",
    page: "Homepage",
    path: "/",
    metaTitle: "Shield Global Group | Integrated Security & Facility Services",
    metaDescription:
      "Shield Global Group delivers integrated security, facility management and logistics services across the Middle East, Europe and Asia.",
    slug: "",
    canonicalUrl: "https://www.shieldglobalgroup.com/",
    ogTitle: "Shield Global Group",
    ogDescription: "Integrated security, facility management and logistics for global enterprise.",
    ogImage: "https://cdn.shieldglobalgroup.com/media/hero-headquarters-dubai.jpg",
    robots: "index,follow",
    updatedAt: daysAgo(3),
  },
  {
    id: "seo-2",
    page: "About",
    path: "/about",
    metaTitle: "About Shield Global Group",
    metaDescription: "Two decades of protecting people, assets and operations across 7 countries.",
    slug: "about",
    canonicalUrl: "https://www.shieldglobalgroup.com/about",
    ogTitle: "About Shield Global Group",
    ogDescription: "Our story, values and governance.",
    ogImage: "https://cdn.shieldglobalgroup.com/media/group-leadership-portrait.jpg",
    robots: "index,follow",
    updatedAt: daysAgo(11),
  },
  {
    id: "seo-3",
    page: "Services",
    path: "/services",
    metaTitle: "Services",
    metaDescription: "Security, facility management, logistics and advisory services.",
    slug: "services",
    canonicalUrl: "https://www.shieldglobalgroup.com/services",
    ogTitle: "Shield Global Services",
    ogDescription: "Eight service lines under one accountable group.",
    ogImage: "https://cdn.shieldglobalgroup.com/media/control-room-night.jpg",
    robots: "index,follow",
    updatedAt: daysAgo(6),
  },
  {
    id: "seo-4",
    page: "Careers",
    path: "/careers",
    metaTitle: "Careers at Shield Global Group",
    metaDescription: "Join a workforce of more than 12,000 professionals across seven countries.",
    slug: "careers",
    canonicalUrl: "https://www.shieldglobalgroup.com/careers",
    ogTitle: "Careers",
    ogDescription: "Build your career with Shield Global Group.",
    ogImage: "https://cdn.shieldglobalgroup.com/media/career-fair-2026.jpg",
    robots: "index,follow",
    updatedAt: daysAgo(19),
  },
  {
    id: "seo-5",
    page: "Contact",
    path: "/contact",
    metaTitle: "Contact",
    metaDescription: "Talk to our team.",
    slug: "contact",
    canonicalUrl: "https://www.shieldglobalgroup.com/contact",
    ogTitle: "Contact Shield Global Group",
    ogDescription: "Offices in Dubai, Riyadh, Doha, London, Mumbai and Singapore.",
    ogImage: "https://cdn.shieldglobalgroup.com/media/office-london-exterior.jpg",
    robots: "index,follow",
    updatedAt: daysAgo(2),
  },
];

const auditSeed: Array<[string, AuditLog["action"], string, string, number, string]> = [
  ["Arjun Mehta", "UPDATE", "Home", "Homepage Hero", 0, "94.204.18.22"],
  ["Fatima Al Rashid", "CREATE", "Testimonials", "Majid Al Futtaim", 0, "94.204.18.31"],
  ["Daniel Okafor", "PUBLISH", "Pages", "Newsroom", 1, "81.129.44.10"],
  ["Marcus Lin", "DELETE", "Media", "old-brochure-2019.pdf", 2, "203.116.9.4"],
  ["Arjun Mehta", "LOGIN", "Auth", "Session started", 0, "94.204.18.22"],
  ["Priya Nair", "UPDATE", "Companies", "Shield Global India Pvt Ltd", 3, "49.36.180.7"],
  ["Fatima Al Rashid", "UNPUBLISH", "Services", "Technical Maintenance", 4, "94.204.18.31"],
  ["Daniel Okafor", "UPDATE", "SEO", "/services", 5, "81.129.44.10"],
  ["Marcus Lin", "LOGOUT", "Auth", "Session ended", 6, "203.116.9.4"],
  ["Arjun Mehta", "CREATE", "Users", "Priya Nair", 12, "94.204.18.22"],
  ["Fatima Al Rashid", "UPDATE", "Settings", "Contact details", 14, "94.204.18.31"],
  ["Daniel Okafor", "PUBLISH", "Partners", "Siemens Smart Infrastructure", 13, "81.129.44.10"],
];

export const auditLogs: AuditLog[] = auditSeed.map(([user, action, module, rec, ago, ip], i) => ({
  id: `log-${1000 + i}`,
  user,
  action,
  module,
  record: rec,
  createdAt: daysAgo(ago, 8 + (i % 10)),
  ipAddress: ip,
  oldValue:
    action === "UPDATE" || action === "UNPUBLISH"
      ? { status: action === "UNPUBLISH" ? "PUBLISHED" : "DRAFT", title: rec }
      : null,
  newValue:
    action === "DELETE"
      ? null
      : { status: action === "PUBLISH" ? "PUBLISHED" : action === "UNPUBLISH" ? "DRAFT" : "UPDATED", title: rec },
}));

export const siteSettings: SiteSettings = {
  general: {
    websiteName: "Shield Global Group",
    tagline: "Protecting people, assets and operations worldwide.",
    contactEmail: "info@shieldglobalgroup.com",
    phone: "+971 4 512 8800",
    address: "Level 22, Shield Tower, Sheikh Zayed Road, Dubai, UAE",
  },
  social: {
    linkedin: "https://linkedin.com/company/shield-global-group",
    facebook: "https://facebook.com/shieldglobalgroup",
    instagram: "https://instagram.com/shieldglobalgroup",
    youtube: "https://youtube.com/@shieldglobalgroup",
    twitter: "https://x.com/shieldglobal",
  },
  contact: {
    contactEmail: "enquiries@shieldglobalgroup.com",
    contactPhone: "+971 4 512 8801",
    officeAddress: "Level 22, Shield Tower, Sheikh Zayed Road, Dubai, UAE",
  },
  system: {
    maintenanceMode: false,
    defaultLanguage: "en-GB",
    timezone: "Asia/Dubai",
  },
};

export const health: HealthStatus[] = [
  { id: "frontend", label: "Frontend Status", value: "Operational", state: "operational" },
  { id: "api", label: "API Status", value: "Connected", state: "operational" },
  { id: "database", label: "Database Status", value: "Connected", state: "operational" },
  { id: "storage", label: "Media Storage", value: "Operational", state: "operational" },
  { id: "backup", label: "Last Backup", value: "Today, 03:00 AM", state: "operational" },
];

export const notifications: NotificationItem[] = [
  { id: "nt-1", title: "New contact enquiry received", description: "Emaar Properties — Integrated FM proposal", at: daysAgo(0, 4), read: false, type: "enquiry" },
  { id: "nt-2", title: "Homepage content was updated", description: "Hero section updated by Arjun Mehta", at: daysAgo(0, 5), read: false, type: "content" },
  { id: "nt-3", title: "New testimonial added", description: "Majid Al Futtaim testimonial published", at: daysAgo(1, 10), read: false, type: "content" },
  { id: "nt-4", title: "Media upload completed", description: "12 assets uploaded to Media Library", at: daysAgo(2, 16), read: true, type: "media" },
  { id: "nt-5", title: "New admin user invited", description: "Priya Nair — Editor role", at: daysAgo(12, 9), read: true, type: "user" },
];
