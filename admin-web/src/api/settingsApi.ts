import { apiRequest } from "@/lib/api-client";
import { siteSettings } from "@/lib/mock-data";
import type { SiteSettings } from "@/types/admin";

/** Spring Boot mapping: /api/admin/settings */
export const settingsApi = {
  get(): Promise<SiteSettings> {
    return apiRequest<SiteSettings>("/settings", { mockResolver: () => siteSettings });
  },

  update(section: keyof SiteSettings, input: Partial<SiteSettings[keyof SiteSettings]>) {
    return apiRequest<SiteSettings>(`/settings/${section}`, {
      method: "PUT",
      body: input,
      mockDelay: 500,
      mockResolver: () => {
        Object.assign(siteSettings[section], input);
        return siteSettings;
      },
    });
  },
};
