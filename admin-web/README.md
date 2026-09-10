# Shield CMS Pro

Build a complete, production-quality Admin Panel frontend for Shield Global Group.

This is the administrative CMS for the existing Shield Global Group corporate website. The public website already exists separately. Do NOT redesign or modify the public website. This task is ONLY to build the admin panel frontend.

The admin panel will later connect to a Java Spring Boot REST API + PostgreSQL backend, so structure the frontend cleanly for future API integration.

TECH STACK

Use:

React

TypeScript

Vite

Tailwind CSS

shadcn/ui

Lucide React icons

React Router

TanStack Query for future API integration

React Hook Form

Zod for validation

Use reusable components and clean TypeScript architecture.

Do not use mock backend APIs that make the project difficult to connect to Spring Boot later. For now, use realistic local/mock data only where required to demonstrate the UI.

1. ADMIN PANEL BRANDING

Brand the dashboard as:

Shield Global Group

Admin portal name:

Shield Global CMS

The design should feel:

Enterprise

Corporate

Premium

Modern

Clean

Professional

Minimal but visually rich

Suitable for a real client-facing CMS

Follow the visual identity of the Shield Global website as much as possible, especially its corporate feel, typography, spacing, and general visual language.

Do NOT create a generic colorful SaaS dashboard.

Avoid excessive gradients, excessive glassmorphism, cartoon illustrations, or unnecessary decorative elements.

Use subtle shadows, borders, cards, whitespace and strong information hierarchy.

2. APPLICATION STRUCTURE

Create the following structure:

/admin/login

/admin/dashboard

/admin/home

/admin/pages

/admin/services

/admin/companies

/admin/timeline

/admin/achievements

/admin/testimonials

/admin/partners

/admin/global-presence

/admin/media

/admin/enquiries

/admin/seo

/admin/users

/admin/settings

/admin/audit-logs

3. LOGIN PAGE

Create a professional admin login page.

Layout:

Left side:

Shield Global branding

Short statement about the CMS

Corporate visual treatment

Right side:

Login card

Login fields:

Email

Password

Remember me

Show/hide password

Actions:

Login

Forgot password

Include:

Form validation

Error state

Loading state

Invalid credentials state

Disabled button while submitting

Example branding:

"Shield Global CMS"

"Manage your website content, media and business information from one secure platform."

Do not implement real authentication yet.

Create a clean authentication service abstraction so Spring Security/JWT can be connected later.

4. MAIN ADMIN LAYOUT

After login, use a permanent admin layout.

Structure:

Sidebar
+
Top Header
+
Main Content
+
Responsive mobile navigation

Desktop:

Sidebar on left.

Top header across the content area.

Main content with responsive max-width.

5. SIDEBAR

Create a professional collapsible sidebar.

Header:

Shield Global logo
Shield Global CMS

Navigation:

MAIN

Dashboard

CONTENT

Home
Pages
Services
Group Companies
Journey Timeline
Achievements
Testimonials
Partners
Global Presence

MANAGEMENT

Media Library
Contact Enquiries

SETTINGS

SEO Management
Users & Roles
Site Settings
Audit Logs

Sidebar behavior:

Active route highlighted

Icons for every menu item

Hover states

Collapsed mode

Expanded mode

Tooltips when collapsed

Smooth transitions

Mobile drawer

Logout button at bottom

Use Lucide icons.

Do not use emojis as navigation icons.

6. TOP HEADER

Create a professional header containing:

Left:

Sidebar toggle

Breadcrumb

Right:

Search

Notifications

User avatar

Admin name

Role

Profile dropdown

Profile dropdown:

My Profile

Account Settings

Change Password

Logout

Notification dropdown:

Show example notifications such as:

"New contact enquiry received"

"Homepage content was updated"

"New testimonial added"

"Media upload completed"

Include notification count.

7. GLOBAL SEARCH

Implement a UI for global admin search.

Search should be able to search across:

Pages

Services

Companies

Testimonials

Partners

Media

Enquiries

For now use mock/local data.

Create:

Search overlay

Keyboard-friendly interaction

Search results grouped by content type

Recent searches

Empty state

Structure it so a backend search API can easily replace the local implementation later.

8. DASHBOARD

Create a complete enterprise CMS dashboard.

Page title:

"Dashboard"

Subtitle:

"Overview of your Shield Global Group website."

Top-right:

Date range selector

Refresh button

9. KPI CARDS

Create dashboard cards for:

Total Pages

Example:
24

Show:
+3 this month

Published Content

Example:
18

Draft Content

Example:
6

Services

Example:
8

Group Companies

Example:
12

Contact Enquiries

Example:
42

Media Assets

Example:
156

Admin Users

Example:
5

Cards should include:

Icon

Main number

Label

Percentage/change indicator

Small supporting text

Use realistic mock data.

10. CONTENT ACTIVITY CHART

Create a professional analytics section.

Title:

"Content Activity"

Display a line/area chart showing:

Published

Updated

Drafted

Over the last 30 days.

Use Recharts.

Include:

Date range selector

Hover tooltip

Legend

Keep the chart clean and corporate.

11. WEBSITE CONTENT DISTRIBUTION

Create a chart showing content distribution:

Pages

Services

Companies

Testimonials

Partners

Media

Use an appropriate chart such as donut/pie chart.

Show totals clearly.

12. RECENT ACTIVITY

Create a "Recent Activity" card.

Examples:

"Homepage Hero updated"

"New testimonial added"

"Company profile updated"

"New contact enquiry received"

"Partner logo replaced"

Each activity should show:

User

Action

Content type

Timestamp

Use subtle status indicators.

Add:

"View all activity"

13. RECENT CONTACT ENQUIRIES

Create a table/card displaying latest enquiries.

Columns:

Name

Company

Email

Subject

Status

Date

Action

Statuses:

NEW
IN PROGRESS
CONTACTED
CLOSED
SPAM

Use status badges.

Add:

"View all enquiries"

14. CONTENT STATUS

Create a section showing:

Published
Draft
Archived

Use cards/progress indicators.

Example:

Published — 75%
Draft — 20%
Archived — 5%

15. QUICK ACTIONS

Create a Quick Actions section.

Actions:

Add Page

Add Service

Add Company

Add Testimonial

Upload Media

View Enquiries

Each action should be represented as a professional button/card.

16. WEBSITE HEALTH

Create a small "Website Health" section.

Display:

Frontend Status — Operational

API Status — Connected (mock)

Database Status — Connected (mock)

Media Storage — Operational

Last Backup — Today, 03:00 AM

Use green/amber/red status indicators.

Clearly structure this so real Spring Boot health endpoints can replace mock values later.

17. CONTENT MANAGEMENT PAGES

Create placeholder but polished management screens for:

Home

Pages

Services

Group Companies

Journey Timeline

Achievements

Testimonials

Partners

Global Presence

Each management page should have:

Page title

Description

Search

Filters

Status filter

Sort

Add button

Table/list

Pagination

Row actions

Edit

Delete

Publish/unpublish

Empty state

Loading state

Do NOT make these pages plain empty placeholders.

Use realistic sample data so the CMS feels complete.

18. MEDIA LIBRARY

Create a professional media management interface.

Features:

Grid view

List view

Search

Filter by type

Upload button

Drag-and-drop upload area

Image preview

File details

File size

File type

Upload date

Alt text

Delete

Copy URL

Filters:

Images
Videos
Documents
Logos

Create upload progress UI.

Structure the upload service so it can later connect to S3/Cloudinary through Spring Boot.

19. CONTACT ENQUIRIES

Create a complete enquiries management page.

Table:

Name
Email
Phone
Company
Subject
Status
Date
Actions

Filters:

New

In Progress

Contacted

Closed

Spam

Search by:

Name

Email

Company

Clicking an enquiry should open a detailed panel/page containing:

Contact information
Message
Submission date
Status
Internal notes
Activity history

Actions:

Mark as contacted
Change status
Add note
Delete
Mark spam

20. SEO MANAGEMENT

Create an SEO management screen.

Allow editing:

Page title

Meta title

Meta description

URL slug

Canonical URL

Open Graph title

Open Graph description

Open Graph image

Robots setting

Show an SEO preview card.

Include character counters for:

Meta title
Meta description

Add validation warnings.

21. USERS & ROLES

Create user management UI.

Table:

Name
Email
Role
Status
Last Login
Created Date
Actions

Roles:

SUPER ADMIN
ADMIN
EDITOR

Features:

Add user

Edit user

Disable user

Delete user

Reset password

Change role

Create role/permission UI.

Permissions:

Pages
Services
Companies
Media
Enquiries
SEO
Users
Settings
Audit Logs

Do not implement real permission enforcement yet, but structure the frontend around permissions.

22. AUDIT LOGS

Create a professional audit log screen.

Columns:

User
Action
Module
Record
Date
IP Address

Examples:

CREATE
UPDATE
DELETE
PUBLISH
UNPUBLISH
LOGIN
LOGOUT

Include:

Search

User filter

Action filter

Module filter

Date filter

Click a log to see details including old value and new value.

23. SITE SETTINGS

Create settings sections:

General

Website name

Contact email

Phone

Address

Social Media

LinkedIn

Facebook

Instagram

YouTube

X/Twitter

Contact

Contact email

Contact phone

Office address

System

Maintenance mode

Default language

Timezone

Use tabs or cards.

24. PROFILE

Create a profile page for the logged-in administrator.

Fields:

Name

Email

Phone

Profile image

Role

Actions:

Update profile
Change password

25. UI STATES

Every important page must include polished:

Loading state

Skeleton loader

Empty state

Error state

Success toast

Delete confirmation dialog

Unsaved changes warning

Form validation

Saving state

Publishing state

Use shadcn/ui components.

26. RESPONSIVE DESIGN

The admin panel must work on:

Desktop
Laptop
Tablet
Mobile

On mobile:

Sidebar becomes drawer

Tables become responsive cards where necessary

Header adapts

Charts resize

Forms become single column

Do not simply shrink desktop layouts.

27. DESIGN SYSTEM

Create reusable components:

AdminLayout
Sidebar
Header
Breadcrumbs
PageHeader
StatCard
DataTable
StatusBadge
SearchBar
FilterBar
EmptyState
LoadingSkeleton
ConfirmDialog
FormSection
FileUploader
ActivityFeed
NotificationDropdown
UserMenu
ChartCard

Create a consistent design system.

28. TOASTS

Use toast notifications for:

Saved successfully
Updated successfully
Deleted successfully
Published successfully
Unpublished successfully
Upload completed
Error occurred

29. DARK MODE

Include a light/dark mode toggle.

The default should be light mode.

Dark mode should still maintain a professional corporate appearance.

Do not make dark mode overly neon or futuristic.

30. ACCESSIBILITY

Follow accessibility best practices:

Keyboard navigation

Proper labels

Focus states

ARIA where necessary

Sufficient contrast

Screen-reader-friendly controls

Accessible dialogs

Accessible dropdowns

31. FUTURE SPRING BOOT INTEGRATION

VERY IMPORTANT:

Architect the frontend so the mock data can later be replaced by REST APIs without rewriting components.

Create an API layer such as:

src/
api/
authApi.ts
dashboardApi.ts
pagesApi.ts
servicesApi.ts
companiesApi.ts
testimonialsApi.ts
partnersApi.ts
mediaApi.ts
enquiriesApi.ts
seoApi.ts
usersApi.ts
settingsApi.ts
auditApi.ts

Create a centralized API client.

Example future base URL:

VITE_API_BASE_URL

Use TanStack Query for server state.

Do not hardcode API URLs inside UI components.

32. AUTHENTICATION ARCHITECTURE

Prepare the frontend for:

POST /api/admin/auth/login

GET /api/admin/auth/me

POST /api/admin/auth/refresh

POST /api/admin/auth/logout

Use an authentication context/store.

Prepare for:

Access token
Refresh token
Protected routes
Automatic logout
Unauthorized handling

Do not put authentication logic directly into page components.

33. PROTECTED ROUTES

All /admin/* routes except /admin/login must be protected.

Unauthenticated users should be redirected to:

/admin/login

Create an authentication guard that currently works with mock authentication but can later use Spring Security JWT.

34. CODE QUALITY

Use:

Strong TypeScript typing

Reusable components

No unnecessary duplication

Clean folder structure

Proper separation of UI and business logic

No giant components

No hardcoded API calls inside components

No inline mock data scattered throughout the application

Suggested structure:

src/

components/
admin/
ui/

layouts/
AdminLayout.tsx

pages/
admin/
Login.tsx
Dashboard.tsx
Home.tsx
Pages.tsx
Services.tsx
Companies.tsx
Timeline.tsx
Achievements.tsx
Testimonials.tsx
Partners.tsx
GlobalPresence.tsx
Media.tsx
Enquiries.tsx
SEO.tsx
Users.tsx
Settings.tsx
AuditLogs.tsx

api/

hooks/

types/

lib/

routes/

35. IMPORTANT RESTRICTIONS

Do NOT:

Modify the public Shield Global website

Redesign the existing public website

Build the Spring Boot backend yet

Connect to a real database yet

Use Firebase

Use Supabase as the backend

Use a BaaS instead of preparing for Spring Boot

Hardcode API URLs

Put all dashboard code in one file

Create a generic template-looking dashboard

The goal is to create a professional CMS frontend ready to connect to a Java Spring Boot backend.

36. FINAL QUALITY REQUIREMENT

Before finishing:

Make every admin route accessible.

Make sidebar navigation functional.

Make login flow work with mock authentication.

Make protected routes work.

Make dashboard charts render.

Make tables searchable/filterable.

Make forms visually complete.

Make dialogs and dropdowns functional.

Make responsive layouts work.

Make loading/error/empty states available.

Make light/dark mode functional.

Ensure there are no broken links or console errors.

Ensure the application builds successfully.

Keep the architecture ready for Spring Boot REST API integration.

The result should look like a real enterprise CMS that could be handed to a client, not a prototype or simple dashboard template.

This project was built with [Lovable](https://lovable.dev).

## Build with Lovable

Continue developing this project in the [Lovable editor](https://lovable.dev/projects/2571505d-fcb9-45ca-9bb6-53edd2a2bcc8).

- **Ship faster**: describe what you want to build and Lovable handles the code.
- **Stay in sync**: every change made in Lovable is committed straight to this repository.
- **Full ownership**: this code is yours. Push to `main` on GitHub and your changes sync back into Lovable, ready for your next prompt.

## Development

Prefer working locally? You need Node.js and npm — [install with nvm](https://github.com/nvm-sh/nvm#installing-and-updating).

```sh
git clone <this-repository-url>
cd <repository-name>
npm i
npm run dev
```
