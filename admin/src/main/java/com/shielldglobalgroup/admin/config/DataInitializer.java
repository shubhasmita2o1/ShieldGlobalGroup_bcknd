package com.shielldglobalgroup.admin.config;

import com.shielldglobalgroup.admin.entity.ContentBlock;
import com.shielldglobalgroup.admin.entity.ContentListItem;
import com.shielldglobalgroup.admin.entity.HeroSlide;
import com.shielldglobalgroup.admin.entity.MapLocation;
import com.shielldglobalgroup.admin.repository.ContentBlockRepository;
import com.shielldglobalgroup.admin.repository.ContentListItemRepository;
import com.shielldglobalgroup.admin.repository.HeroSlideRepository;
import com.shielldglobalgroup.admin.repository.MapLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ContentBlockRepository blockRepo;
    private final ContentListItemRepository listRepo;
    private final HeroSlideRepository heroRepo;
    private final MapLocationRepository mapLocationRepo;

    @Override
    public void run(String... args) {
        if (blockRepo.count() == 0) seedContentBlocks();
        if (listRepo.count() == 0) seedListItems();
        if (heroRepo.count() == 0) seedHeroSlides();
        if (mapLocationRepo.count() == 0) seedMapLocations();
        System.out.println("✅ Seed data loaded successfully");
    }

    // ══════════════════════════════════════════════════════
    // CONTENT BLOCKS
    // ══════════════════════════════════════════════════════
    private void seedContentBlocks() {
        List<ContentBlock> blocks = List.of(

            // ══════════════════════════════════════════════
            // HOME PAGE — index.html
            // ══════════════════════════════════════════════

            // ── Hero Slide 1 ──────────────────────────────
            block("home", "hero_slide_1", "welcome_text",
                "Welcome to"),
            block("home", "hero_slide_1", "title",
                "Shield Global"),
            block("home", "hero_slide_1", "group_label",
                "Group"),
            block("home", "hero_slide_1", "pill_text",
                "Connecting talent, technology & entertainment"),
            block("home", "hero_slide_1", "video_path",
                "Assets/HV1.mp4"),

            // ── Hero Slide 2 ──────────────────────────────
            block("home", "hero_slide_2", "title",
                "Shield Global Hr Solutions"),
            block("home", "hero_slide_2", "subtitle",
                "Recruiting Manpower from Asia, Africa and Europe"),
            block("home", "hero_slide_2", "video_path",
                "Assets/HV2.mp4"),

            // ── Hero Slide 3 ──────────────────────────────
            block("home", "hero_slide_3", "title",
                "Shield Workforce"),
            block("home", "hero_slide_3", "subtitle",
                "Connecting talent with precision"),
            block("home", "hero_slide_3", "video_path",
                "Assets/HV3.mp4"),

            // ── Hero Slide 4 ──────────────────────────────
            block("home", "hero_slide_4", "title",
                "InfiCorp Technology"),
            block("home", "hero_slide_4", "subtitle",
                "AI-Powered Industrial Automation Software"),
            block("home", "hero_slide_4", "video_path",
                "Assets/HV4.mp4"),

            // ── Hero Slide 5 ──────────────────────────────
            block("home", "hero_slide_5", "title",
                "CineGlare Entertainment"),
            block("home", "hero_slide_5", "subtitle",
                "Ad films, Corporate Films, Event & Celebrity Management"),
            block("home", "hero_slide_5", "video_path",
                "Assets/HV5.mp4"),

            // ── Hero Tabs (slides 2-5 bottom tabs) ────────
            block("home", "hero_tabs", "tab_1",
                "Global Manpower\nRecruitment"),
            block("home", "hero_tabs", "tab_2",
                "Staffing & Workforce\nSolutions"),
            block("home", "hero_tabs", "tab_3",
                "AI-Powered\nIndustrial Automation"),
            block("home", "hero_tabs", "tab_4",
                "Media &\nEntertainment"),

            // ── About Section — Company Logos ─────────────
            block("home", "logos", "logo_1_src",
                "Assets/1.png"),
            block("home", "logos", "logo_1_alt",
                "Shield Global Hr Solutions"),
            block("home", "logos", "logo_2_src",
                "Assets/2.png"),
            block("home", "logos", "logo_2_alt",
                "Shield Workforce"),
            block("home", "logos", "logo_3_src",
                "Assets/3.png"),
            block("home", "logos", "logo_3_alt",
                "InfiCorp Technology"),
            block("home", "logos", "logo_4_src",
                "Assets/4.png"),
            block("home", "logos", "logo_4_alt",
                "CineGlare Brands & Entertainment"),

            // ── About Intro Paragraphs ─────────────────────
            block("home", "about_intro", "para_1",
                "Shield Global Group is a diversified global business group delivering " +
                "integrated solutions across Human Resources, Technology, and Media & " +
                "Entertainment. Backed by a growing network of international associations, " +
                "strategic partners, and a broad global clientele, the group operates through " +
                "its core companies — Shield Global HR Solutions, InfiCorp Technology, " +
                "and Cineglare Entertainment."),
            block("home", "about_intro", "para_2",
                "We provide end-to-end capabilities including global talent solutions, " +
                "AI-powered industrial automation, and creative brand & entertainment services. " +
                "Our collaborative approach, combined with industry expertise and global " +
                "partnerships, enables us to support organizations across diverse sectors " +
                "and geographies. Shield Global Group is committed to driving innovation, " +
                "operational excellence, and sustainable growth in an increasingly connected " +
                "global economy."),

            // ── Timeline Section ───────────────────────────
            block("home", "timeline", "section_label",
                "OUR JOURNEY"),
            block("home", "timeline", "heading",
                "From 2009 to Today"),

            // Timeline Card 1 — 2009
            block("home", "timeline_1", "year",   "2009"),
            block("home", "timeline_1", "number", "01"),
            block("home", "timeline_1", "name",   "Vijay Infotech"),
            block("home", "timeline_1", "sub",    "ICA Vizag"),
            block("home", "timeline_1", "desc",
                "The foundation — Training, Staffing & Recruitment services launched " +
                "in Visakhapatnam, planting the seed of a global enterprise."),
            block("home", "timeline_1", "tag",
                "Training, Staffing & Recruitment"),
            block("home", "timeline_1", "is_new", "false"),

            // Timeline Card 2 — 2016
            block("home", "timeline_2", "year",   "2016"),
            block("home", "timeline_2", "number", "02"),
            block("home", "timeline_2", "name",
                "Shield Global HR Solutions"),
            block("home", "timeline_2", "sub",
                "Overseas Manpower Solutions"),
            block("home", "timeline_2", "desc",
                "Expanding globally — connecting talent across Asia, Africa and the " +
                "Middle East with end-to-end overseas recruitment."),
            block("home", "timeline_2", "tag",    "Overseas Manpower"),
            block("home", "timeline_2", "is_new", "false"),

            // Timeline Card 3 — 2017
            block("home", "timeline_3", "year",   "2017"),
            block("home", "timeline_3", "number", "03"),
            block("home", "timeline_3", "name",   "MEA Licence Secured"),
            block("home", "timeline_3", "sub",
                "Shield Global HR Solutions"),
            block("home", "timeline_3", "desc",
                "Official recognition — Government of India MEA recruitment licence " +
                "obtained, enabling full global operations."),
            block("home", "timeline_3", "tag",    "Govt. Licence"),
            block("home", "timeline_3", "is_new", "false"),

            // Timeline Card 4 — 2020
            block("home", "timeline_4", "year",   "2020"),
            block("home", "timeline_4", "number", "04"),
            block("home", "timeline_4", "name",   "InfiCorp Technology"),
            block("home", "timeline_4", "sub",    "AI & Industrial Automation"),
            block("home", "timeline_4", "desc",
                "Tech vertical launched — AI-powered industrial automation solutions " +
                "for clients across manufacturing and enterprise sectors."),
            block("home", "timeline_4", "tag",    "Technology"),
            block("home", "timeline_4", "is_new", "false"),

            // Timeline Card 5 — 2023
            block("home", "timeline_5", "year",   "2023"),
            block("home", "timeline_5", "number", "05"),
            block("home", "timeline_5", "name",   "Shield Global Mgmt. LLC"),
            block("home", "timeline_5", "sub",    "Dubai, UAE"),
            block("home", "timeline_5", "desc",
                "Going global — Shield Global Management Consultancies LLC established " +
                "in Dubai, anchoring the group's Middle East presence."),
            block("home", "timeline_5", "tag",    "Dubai Entity"),
            block("home", "timeline_5", "is_new", "false"),

            // Timeline Card 6 — 2025
            block("home", "timeline_6", "year",   "2025"),
            block("home", "timeline_6", "number", "06"),
            block("home", "timeline_6", "name",   "Cineglare"),
            block("home", "timeline_6", "sub",
                "Entertainment, Media & Brands"),
            block("home", "timeline_6", "desc",
                "Creative arm established — brand storytelling, media production and " +
                "entertainment services for global clients."),
            block("home", "timeline_6", "tag",    "Entertainment"),
            block("home", "timeline_6", "is_new", "false"),

            // Timeline Card 7 — 2026
            block("home", "timeline_7", "year",   "2026"),
            block("home", "timeline_7", "number", "07"),
            block("home", "timeline_7", "name",   "Shield Workforce Pvt Ltd"),
            block("home", "timeline_7", "sub",
                "Staffing & Workforce Solutions"),
            block("home", "timeline_7", "desc",
                "Lorem ipsum dolor sit amet consectetur adipisicing elit. Deleniti " +
                "tenetur voluptatum quidem ipsam architecto cum. Illo veritatis labore facili"),
            block("home", "timeline_7", "tag",    "Staffing & Workforce"),
            block("home", "timeline_7", "is_new", "false"),

            // Timeline Card 8 — 2026 NEW
            block("home", "timeline_8", "year",   "2026 · NEW"),
            block("home", "timeline_8", "number", "08"),
            block("home", "timeline_8", "name",   "Shield Global Group"),
            block("home", "timeline_8", "sub",    "Unified Global Brand"),
            block("home", "timeline_8", "desc",
                "All verticals unified — HR, Technology and Entertainment operating " +
                "under one global brand with international reach."),
            block("home", "timeline_8", "tag",    "Group Launch"),
            block("home", "timeline_8", "is_new", "true"),

            // ── Showreel ───────────────────────────────────
            block("home", "showreel", "video_path",
                "Assets/compReel.mp4"),
            block("home", "showreel", "pdf_path",
                "Assets/ShieldGlobal_CompanyProfile.pdf"),
            block("home", "showreel", "pdf_btn_label",
                "COMPANY PROFILE - PDF"),

            // ── Global Network ─────────────────────────────
            block("home", "global_network", "label",
                "Global network & Associations"),

            // ══════════════════════════════════════════════
            // ABOUT PAGE — aboutus.html
            // ══════════════════════════════════════════════

            // ── Hero ───────────────────────────────────────
            block("about", "hero", "video_path",
                "Assets/HV2.mp4"),

            // ── Hero Tabs ──────────────────────────────────
            block("about", "hero_tabs", "tab_1",
                "Global Manpower\nRecruitment"),
            block("about", "hero_tabs", "tab_2",
                "Staffing & Workforce\nSolutions"),
            block("about", "hero_tabs", "tab_3",
                "AI-Powered\nIndustrial Automation"),
            block("about", "hero_tabs", "tab_4",
                "Media &\nEntertainment"),

            // ── Group Identity ─────────────────────────────
            block("about", "group_identity", "eyebrow",
                "Shield Global Group · The Power of One"),
            block("about", "group_identity", "heading",
                "Group Identity"),
            block("about", "group_identity", "intro",
                "At Shield Global Group – Power of One, we have redefined the conglomerate " +
                "model by uniting human potential, technological intelligence, and creative " +
                "storytelling into a single, synchronised ecosystem. We operate under the " +
                "philosophy of The Power of One — providing a 360-degree growth engine that " +
                "eliminates the need for fragmented vendors."),
            block("about", "group_identity", "tagline",
                "Talent > Intelligence > Impact – One Identity."),

            // ── Pillars ────────────────────────────────────
            block("about", "gi_pillar_1", "title",
                "The Human Engine"),
            block("about", "gi_pillar_1", "body",
                "We build the foundation of success by bridging the gap between global demand " +
                "and local talent. Through Overseas Recruitment & HR Services, we connect " +
                "world-class professionals with international opportunities, while our Payroll " +
                "Management & Indian Outsourcing divisions streamline the complexities of " +
                "global workforce management with localized precision and compliance."),

            block("about", "gi_pillar_2", "title",
                "The Digital Brain"),
            block("about", "gi_pillar_2", "body",
                "In an era defined by data, we provide the tools to lead rather than follow. " +
                "We deploy AI-Powered Automation to transform legacy processes into high-speed, " +
                "intelligent workflows and utilize Data Intelligence to turn raw information " +
                "into actionable strategies that predict trends and optimize performance."),

            block("about", "gi_pillar_3", "title",
                "The Creative Soul"),
            block("about", "gi_pillar_3", "body",
                "Every business needs a voice that resonates. We craft the narratives that " +
                "define market leaders through high-impact Ad & Corporate Films—ranging from " +
                "30-second sparks to deep-dive brand stories. Our Events & Celebrity Management " +
                "team completes the circle, creating immersive experiences and aligning brands " +
                "with influential voices to dominate the cultural conversation."),

            // ── Founder's Message ──────────────────────────
            block("about", "founders", "heading",
                "Founder's Message"),
            block("about", "founders", "para_1",
                "At Shield Global Group, our vision is to build a diversified and future-ready " +
                "conglomerate delivering integrated solutions across multiple industries, " +
                "grounded in trust, professionalism, and innovation. As a group, we collectively " +
                "offer services in Overseas Recruitment, Indian Staffing, HR Services, Industrial " +
                "Automation, Data Intelligence, and Commercial Advertisement & Event Management, " +
                "enabling us to support organisations through comprehensive and value-driven solutions."),
            block("about", "founders", "para_2",
                "Our objective is to create long-term partnerships by connecting global talent, " +
                "strengthening workforce capabilities, driving technology-led transformation, and " +
                "delivering impactful brand experiences. We remain committed to sustainable growth, " +
                "operational excellence, and fostering enduring relationships with our clients and " +
                "stakeholders as we continue to expand our footprint and contribute to business " +
                "success across sectors."),
            block("about", "founders", "signature",
                "— Founder, Shield Global Group"),
            block("about", "founders", "image_path",
                "Assets/founder.jpg"),

            // ── Sustainability Section ─────────────────────
            block("about", "sustainability", "heading",
                "SUSTAINABILITY & ESG INITIATIVES"),
            block("about", "sustainability", "card_tl",
                "Ethical Recruitment & Workforce Welfare"),
            block("about", "sustainability", "card_tr",
                "Energy Efficient Automation"),
            block("about", "sustainability", "card_bl",
                "Community Skill Development"),
            block("about", "sustainability", "card_br",
                "Bal Samriddhi Yojana"),
            block("about", "sustainability", "logo_path",
                "Assets/ab_logo.jpg"),

            // ── ESG — Ethical Recruitment ──────────────────
            block("about", "esg_ethical", "title",
                "Ethical Recruitment & Workforce Welfare"),
            block("about", "esg_ethical", "body",
                "At Shield Global Group, we follow the highest standards of ethical recruitment " +
                "and workforce welfare across our global network. We ensure fair, transparent, " +
                "and responsible hiring practices that protect candidates while delivering " +
                "reliable talent solutions worldwide."),
            block("about", "esg_ethical", "principles_heading",
                "Our Principles"),
            block("about", "esg_ethical", "act_heading",
                "We Act differently"),
            block("about", "esg_ethical", "commitment_heading",
                "Our Commitment"),
            block("about", "esg_ethical", "commitment",
                "Through integrity, transparency, and care, we build a sustainable global " +
                "workforce ecosystem that benefits both businesses and professionals."),
            block("about", "esg_ethical", "image_path",
                "Assets/ab1.png"),

            // ── ESG — Energy Automation ────────────────────
            block("about", "esg_energy", "title",
                "Energy Efficient Automation"),
            block("about", "esg_energy", "body",
                "At InfiCorp Technology, part of Shield Global Group, we develop SaaS-based " +
                "industrial automation software focused on improving energy efficiency and " +
                "supporting ESG goals. Our solutions help industries optimize operations, reduce " +
                "energy consumption, and enable sustainable digital transformation."),
            block("about", "esg_energy", "commitment",
                "Our Commitment: Delivering smart automation software that enhances operational " +
                "efficiency while supporting sustainable and responsible industrial growth."),
            block("about", "esg_energy", "image_path",
                "Assets/ab2.png"),

            // ── ESG — Community Skill Development ──────────
            block("about", "esg_community", "title",
                "Community Skill Development"),
            block("about", "esg_community", "body",
                "Shield Global Group empowers communities through free technical training and " +
                "certification programs. We focus on building industry-relevant skills, enhancing " +
                "employability, and creating pathways for sustainable career growth."),
            block("about", "esg_community", "initiative_heading",
                "Our Initiative"),
            block("about", "esg_community", "initiative_body",
                "We provide structured training designed to bridge the gap between talent " +
                "and industry requirements."),
            block("about", "esg_community", "impact_heading",
                "Our Impact"),
            block("about", "esg_community", "commitment_heading",
                "Our Commitment"),
            block("about", "esg_community", "commitment",
                "Shield Global Group builds a skilled talent pool through community skill " +
                "development, supporting job seekers and promoting inclusive economic growth."),
            block("about", "esg_community", "image_path",
                "Assets/ab3.png"),

            // ── ESG — Bal Samriddhi Yojana ─────────────────
            block("about", "esg_bal", "title",
                "Bal Samriddhi Yojana"),
            block("about", "esg_bal", "body_1",
                "Starting June 2026, Shield Global Group introduces Bal Samriddhi Yojana, " +
                "a new initiative aimed at supporting the education and career development " +
                "of employees' children."),
            block("about", "esg_bal", "body_2",
                "This program is designed to encourage long-term growth and provide meaningful " +
                "assistance for both sons and daughters of our workforce. Under this initiative, " +
                "eligible employees will receive structured support from the employer towards " +
                "their child's educational and skill development journey. The program reflects " +
                "our commitment to employee welfare and strengthening family well-being."),
            block("about", "esg_bal", "objectives_heading",
                "Key Objectives"),
            block("about", "esg_bal", "body_3",
                "Through Bal Samriddhi Yojana, Shield Global Group aims to invest in the " +
                "future generation while reinforcing a culture of care, responsibility, " +
                "and sustainable development."),
            block("about", "esg_bal", "image_path",
                "Assets/ab4.png"),

            // ── Corporate Governance ───────────────────────
            block("about", "corp_gov", "heading",
                "Corporate Governance"),
            block("about", "corp_gov", "body_1",
                "Shield Global Group is committed to maintaining the highest standards of " +
                "corporate governance, ensuring transparency, accountability, and ethical " +
                "business practices across all its operations. Our governance framework is " +
                "built on integrity, compliance, and responsible decision-making, supporting " +
                "sustainable growth and long-term stakeholder value."),
            block("about", "corp_gov", "body_2",
                "We adhere to applicable legal and regulatory requirements while promoting fair " +
                "employment practices, transparent client engagement, and responsible financial " +
                "management. The Group emphasizes strong internal controls, risk management, " +
                "and data confidentiality across its companies —"),
            block("about", "corp_gov", "body_3",
                "Our leadership encourages a culture of professionalism, ethical conduct, and " +
                "operational excellence, ensuring that all business activities align with industry " +
                "standards and corporate responsibility. Through effective governance, we aim to " +
                "build trust with clients, partners, employees, and stakeholders while " +
                "strengthening our position as a reliable and responsible conglomerate."),
            block("about", "corp_gov", "image_path",
                "Assets/ab5.jpg"),

            // ══════════════════════════════════════════════
            // SERVICE PAGES
            // ══════════════════════════════════════════════

            // ── Global Manpower — service.html ─────────────
            block("svc_manpower", "banner", "title",
                "Global Manpower Recruitment"),
            block("svc_manpower", "banner", "image_path",
                "Assets/s1.png"),
            block("svc_manpower", "intro", "company_name",
                "SHIELD GLOBAL HR SOLUTIONS"),
            block("svc_manpower", "intro", "website_url",
                "#"),
            block("svc_manpower", "intro", "visit_btn_label",
                "Click here to Visit main website"),
            block("svc_manpower", "intro", "para",
                "Shield Global HR Solutions is a leading international manpower recruitment " +
                "company, delivering end-to-end workforce solutions to employers across Asia, " +
                "Africa, Europe and Canada. With a strong sourcing presence in 21 countries, " +
                "we specialize in identifying, screening, and deploying skilled, semi-skilled, " +
                "and unskilled manpower tailored to diverse industry needs."),
            block("svc_manpower", "coverage", "card_title",
                "Global Recruitment coverage"),
            block("svc_manpower", "recruitment_services", "card_title",
                "Our Recruitment Services"),
            block("svc_manpower", "recruitment_services", "sub_heading",
                "Technical & Non Technical Hiring"),
            block("svc_manpower", "industries", "card_title",
                "Industries We Service"),
            block("svc_manpower", "why_us", "card_title",
                "Why Choose Shield Global HR Solutions"),
            block("svc_manpower", "commitment", "heading",
                "Our Commitment"),
            block("svc_manpower", "commitment", "body",
                "At Shield Global HR Solutions, we are committed to connecting global employers " +
                "with reliable manpower. Our focus is on quality recruitment, timely deployment, " +
                "and long-term partnerships that support business growth across borders."),
            block("svc_manpower", "commitment", "tagline",
                "Connecting Talent Across 21 Countries, Delivering Workforce Worldwide."),

            // ── Staffing & Workforce — sws.html ────────────
            block("svc_staffing", "banner", "title",
                "Staffing & Workforce Solutions"),
            block("svc_staffing", "banner", "image_path",
                "Assets/s2.png"),
            block("svc_staffing", "intro", "company_name",
                "SHIELD WORKFORCE LLP"),
            block("svc_staffing", "intro", "website_url",
                "#"),
            block("svc_staffing", "intro", "visit_btn_label",
                "Click here to Visit main website"),
            block("svc_staffing", "intro", "para",
                "We provide end-to-end manpower staffing solutions including recruitment, " +
                "deployment, payroll management, statutory compliance, and workforce " +
                "administration. Our services ensure compliant, flexible, and efficient " +
                "workforce management tailored to business needs."),
            block("svc_staffing", "staffing_services", "card_title",
                "Our Staffing services includes:"),
            block("svc_staffing", "vendorship", "card_title",
                "What we perform in Vendorship"),
            block("svc_staffing", "industries", "card_title",
                "Industries We Service"),
            block("svc_staffing", "why_us", "heading",
                "Why Choose Us – Shield Workforce LLP"),
            block("svc_staffing", "commitment", "heading",
                "Our Commitment"),
            block("svc_staffing", "commitment", "body",
                "With a legacy of trust, experienced professionals, and industry-focused " +
                "expertise, Shield Workforce LLP is committed to delivering dependable staffing " +
                "solutions that support business growth and workforce efficiency."),

            // ── AI Automation — apa.html ───────────────────
            block("svc_ai", "banner", "title",
                "AI Powered Automation"),
            block("svc_ai", "banner", "image_path",
                "Assets/s3.png"),
            block("svc_ai", "intro", "company_name",
                "INFICORP TECHNOLOGY"),
            block("svc_ai", "intro", "website_url",
                "#"),
            block("svc_ai", "intro", "visit_btn_label",
                "Click here to Visit main website"),
            block("svc_ai", "intro", "para",
                "We provide end-to-end manpower staffing solutions including recruitment, " +
                "deployment, payroll management, statutory compliance, and workforce " +
                "administration. Our services ensure compliant, flexible, and efficient " +
                "workforce management tailored to business needs."),
            block("svc_ai", "industrial", "card_title",
                "Industrial Automation Software"),
            block("svc_ai", "testing", "card_title",
                "Automated Software Testing"),
            block("svc_ai", "commitment", "heading",
                "Our Commitment"),
            block("svc_ai", "commitment", "body",
                "With a strong legacy of trust, experienced professionals, and industry-focused " +
                "expertise, InfiCorp Technology is committed to delivering reliable automation " +
                "solutions that drive business growth, enhance operational efficiency, and enable " +
                "effective data management."),

            // ── Media & Entertainment — mne.html ───────────
            block("svc_media", "banner", "title",
                "Media & Entertainment"),
            block("svc_media", "banner", "image_path",
                "Assets/s4.png"),
            block("svc_media", "intro", "company_name",
                "CineGlare Entertainment"),
            block("svc_media", "intro", "website_url",
                "#"),
            block("svc_media", "intro", "visit_btn_label",
                "Click here to Visit main website"),
            block("svc_media", "intro", "para",
                "Cineglare Entertainment, part of Shield Global Group, provides end-to-end " +
                "media and entertainment solutions including corporate film production, corporate " +
                "branding, celebrity management, event management, and advertisement video " +
                "creation. With a creative and professional approach, Cineglare Entertainment " +
                "supports organizations in enhancing brand presence and delivering impactful " +
                "visual communication."),
            block("svc_media", "film", "card_title",
                "Film Shoot & Video Creation"),
            block("svc_media", "events", "card_title",
                "Events we cover"),
            block("svc_media", "commitment", "heading",
                "Our Commitment"),
            block("svc_media", "commitment", "body_1",
                "At Cineglare Entertainment, we are committed to delivering creative, " +
                "high-quality, and impactful media solutions that enhance brand presence and " +
                "audience engagement. We focus on professional execution, innovative " +
                "storytelling, and timely delivery across corporate films, branding, events, " +
                "celebrity management, and advertisement video production."),
            block("svc_media", "commitment", "body_2",
                "Our goal is to provide end-to-end entertainment and media services that align " +
                "with client objectives while ensuring excellence, creativity, and reliability " +
                "in every project."),

            // ══════════════════════════════════════════════
            // CONTACT PAGE — contact.html
            // ══════════════════════════════════════════════
            block("contact", "hero", "eyebrow",
                "Get in touch"),
            block("contact", "hero", "title",
                "Contact Us"),
            block("contact", "hero", "video_path",
                "Assets/contactvd.mp4"),
            block("contact", "address", "tag",
                "HEADQUARTER"),
            block("contact", "address", "company",
                "Shield Global Group"),
            block("contact", "address", "line_1",
                "104, Hinal Residency, Dahanukarwadi Junction,"),
            block("contact", "address", "line_2",
                "Kandivali West, Mumbai – 400 067"),
            block("contact", "address", "phone",
                "Tel: +22 28678678 | +91"),
            block("contact", "address", "email",
                "info@shieldglobalindia.com"),
            block("contact", "address", "map_embed_url",
                "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3767.714791605458" +
                "!2d72.83471497583994!3d19.207655547824988!2m3!1f0!2f0!3f0!3m2!1i1024" +
                "!2i768!4f13.1!3m3!1m2!1s0x3be7b7bbaf9d9cd5%3A0x2ced5051619c2559" +
                "!2sSHIELD%20GLOBAL!5e0!3m2!1sen!2sin!4v1778222460615!5m2!1sen!2sin"),
            block("contact", "form", "title",
                "Send us a message"),
            block("contact", "form", "subtitle",
                "We'll get back to you within 24 hours."),
            block("contact", "form", "consent_text",
                "I accept that Shield Global Group will process my personal data " +
                "for the purpose of handling my request."),
            block("contact", "form", "submit_btn",
                "SEND MESSAGE"),
            block("contact", "form", "success_msg",
                "Thank you! We'll be in touch soon."),

            // ══════════════════════════════════════════════
            // GROUP OF COMPANIES — groupofcompanies.html
            // ══════════════════════════════════════════════
            block("group", "company_1", "name",
                "Shield Global HR Solutions LLP"),
            block("group", "company_1", "url",
                "https://www.shieldglobal.in/"),
            block("group", "company_2", "name",
                "Shield Workforce LLP"),
            block("group", "company_2", "url",
                "#"),
            block("group", "company_3", "name",
                "InfiCorp Solution Pvt Ltd."),
            block("group", "company_3", "url",
                "https://www.inficorpgroup.com/"),
            block("group", "company_4", "name",
                "CineGlare Entertainment"),
            block("group", "company_4", "url",
                "#"),

            // ══════════════════════════════════════════════
            // SHARED — navbar + footer (all pages)
            // ══════════════════════════════════════════════
            block("shared", "navbar", "logo_path",
                "Assets/logo.png"),
            block("shared", "navbar", "lang_en", "English"),
            block("shared", "navbar", "lang_ar", "Arabic"),
            block("shared", "navbar", "lang_fr", "French"),
            block("shared", "navbar", "lang_hi", "Hindi"),
            block("shared", "navbar", "lang_ru", "Russian"),

            block("shared", "footer", "heading_companies",
                "Group of Companies"),
            block("shared", "footer", "company_1",
                "Shield Global HR Solutions LLP"),
            block("shared", "footer", "company_2",
                "Shield Workforce LLP"),
            block("shared", "footer", "company_3",
                "InfiCorp Solution Pvt Ltd."),
            block("shared", "footer", "company_4",
                "CineGlare Entertainment"),
            block("shared", "footer", "heading_links",
                "Quick Links"),
            block("shared", "footer", "copyright",
                "Copyright © 2026 Shield Global Group")
        );

        blockRepo.saveAll(blocks);
        System.out.println("✅ Content blocks seeded: " + blocks.size() + " rows");
    }

    // ══════════════════════════════════════════════════════
    // HERO SLIDES
    // ══════════════════════════════════════════════════════
    private void seedHeroSlides() {
        List<HeroSlide> slides = List.of(
            slide(1, "Shield Global",
                "Group",
                "Assets/HV1.mp4", null, "intro"),
            slide(2, "Shield Global Hr Solutions",
                "Recruiting Manpower from Asia, Africa and Europe",
                "Assets/HV2.mp4",
                "Global Manpower\nRecruitment", "service"),
            slide(3, "Shield Workforce",
                "Connecting talent with precision",
                "Assets/HV3.mp4",
                "Staffing & Workforce\nSolutions", "service"),
            slide(4, "InfiCorp Technology",
                "AI-Powered Industrial Automation Software",
                "Assets/HV4.mp4",
                "AI-Powered\nIndustrial Automation", "service"),
            slide(5, "CineGlare Entertainment",
                "Ad films, Corporate Films, Event & Celebrity Management",
                "Assets/HV5.mp4",
                "Media &\nEntertainment", "service")
        );
        heroRepo.saveAll(slides);
        System.out.println("✅ Hero slides seeded: " + slides.size() + " rows");
    }

    // ══════════════════════════════════════════════════════
    // MAP PINS — updated coordinates from latest index.html
    // ══════════════════════════════════════════════════════
    private void seedMapLocations() {
        List<MapLocation> locations = List.of(
            // South Asia — offices
            loc("Mumbai", "India", 19.0760, 72.8777, "South Asia", "office", 1),
            loc("Kolkata", "India", 22.5726, 88.3639, "South Asia", "office", 2),
            loc("Bangalore", "India", 12.9716, 77.5946, "South Asia", "office", 3),
            loc("Kathmandu", "Nepal", 27.7172, 85.3240, "South Asia", "office", 4),
            loc("Dhaka", "Bangladesh", 23.8103, 90.4125, "South Asia", "local-recruitment", 5),
            loc("Colombo", "Sri Lanka", 6.9271, 79.8612, "South Asia", "local-recruitment", 6),

            // Southeast Asia
            loc("Singapore", "Singapore", 1.3521, 103.8198, "Southeast Asia", "office", 7),
            loc("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, "Southeast Asia", "local-recruitment", 8),
            loc("Jakarta", "Indonesia", -6.2088, 106.8456, "Southeast Asia", "local-recruitment", 9),
            loc("Ho Chi Minh City", "Vietnam", 10.8231, 106.6297, "Southeast Asia", "local-recruitment", 10),
            loc("Yangon", "Myanmar", 16.8661, 96.1951, "Southeast Asia", "recruitment-associate", 11),
            loc("Bangkok", "Thailand", 13.7563, 100.5018, "Southeast Asia", "local-recruitment", 12),

            // Middle East
            loc("Dubai", "UAE", 25.2048, 55.2708, "Middle East", "office", 13),
            loc("Doha", "Qatar", 25.2854, 51.5310, "Middle East", "office", 14),
            loc("Kuwait City", "Kuwait", 29.3759, 47.9774, "Middle East", "recruitment-associate", 15),
            loc("Riyadh", "Saudi Arabia", 24.7136, 46.6753, "Middle East", "local-recruitment", 16),
            loc("Muscat", "Oman", 23.5880, 58.3829, "Middle East", "recruitment-associate", 17),
            loc("Manama", "Bahrain", 26.2285, 50.5860, "Middle East", "recruitment-associate", 18),

            // Africa
            loc("Cairo", "Egypt", 30.0444, 31.2357, "Africa", "local-recruitment", 19),
            loc("Tunis", "Tunisia", 36.8065, 10.1815, "Africa", "recruitment-associate", 20),
            loc("Casablanca", "Morocco", 33.5731, -7.5898, "Africa", "recruitment-associate", 21),
            loc("Nairobi", "Kenya", -1.2921, 36.8219, "Africa", "local-recruitment", 22),
            loc("Kampala", "Uganda", 0.3476, 32.5825, "Africa", "recruitment-associate", 23),
            loc("Accra", "Ghana", 5.6037, -0.1870, "Africa", "local-recruitment", 24),
            loc("Addis Ababa", "Ethiopia", 9.0320, 38.7469, "Africa", "local-recruitment", 25),
            loc("Lagos", "Nigeria", 6.5244, 3.3792, "Africa", "local-recruitment", 26),
            loc("Johannesburg", "South Africa", -26.2041, 28.0473, "Africa", "office", 27),

            // Europe
            loc("Athens", "Greece", 37.9838, 23.7275, "Europe", "recruitment-associate", 28),
            loc("Istanbul", "Turkey", 41.0082, 28.9784, "Europe", "local-recruitment", 29),
            loc("London", "United Kingdom", 51.5074, -0.1278, "Europe", "office", 30),
            loc("Warsaw", "Poland", 52.2297, 21.0122, "Europe", "recruitment-associate", 31),

            // North America
            loc("Toronto", "Canada", 43.6532, -79.3832, "North America", "office", 32)
        );
        mapLocationRepo.saveAll(locations);
        System.out.println("✅ Map locations seeded: " + locations.size() + " rows");
    }

    private void seedListItems() {

        // ── ABOUT — ESG Ethical Recruitment ───────────────
        saveList("about", "esg_ethical", "principles", List.of(
            "Transparent hiring with no hidden costs",
            "Compliance with international labor laws",
            "Equal opportunity and non-discriminatory practices",
            "Verified job offers and employer credentials",
            "Zero tolerance for unethical recruitment"
        ));
        saveList("about", "esg_ethical", "act_differently", List.of(
            "Pre-deployment briefing and documentation support",
            "Visa, travel, and onboarding assistance",
            "Accommodation and workplace guidance",
            "Grievance support and continuous communication",
            "Safe work environment collaboration with employers"
        ));

        // ── ABOUT — ESG Energy Automation ─────────────────
        saveList("about", "esg_energy", "features", List.of(
            "Real-time energy monitoring and analytics",
            "Process optimization to reduce power usage",
            "Cloud-based SaaS model minimizing hardware footprint",
            "Predictive insights for efficient equipment performance",
            "Data-driven reporting for ESG compliance"
        ));

        // ── ABOUT — ESG Community Skill Development ────────
        saveList("about", "esg_community", "initiative", List.of(
            "Free technical skill development programs",
            "Industry-oriented curriculum and practical learning",
            "Certification to enhance job readiness",
            "Guidance for international and domestic job opportunities",
            "Career counseling and interview preparation"
        ));
        saveList("about", "esg_community", "impact", List.of(
            "Supporting candidates in upgrading skills",
            "Promoting equal access to career opportunities",
            "Strengthening workforce readiness for global industries",
            "Encouraging sustainable community development"
        ));

        // ── ABOUT — ESG Bal Samriddhi ──────────────────────
        saveList("about", "esg_bal", "objectives", List.of(
            "Support for children's education and career development",
            "Inclusive benefits for both sons and daughters",
            "Encouraging long-term academic and skill growth",
            "Strengthening employee engagement and family support"
        ));

        // ── ABOUT — Corporate Governance — companies ───────
        saveList("about", "corp_gov", "companies", List.of(
            "Shield Global HR Solutions",
            "Shield Workforce Solutions",
            "InfiCorp Technology",
            "Cineglare Entertainment"
        ));

        // ── SERVICE MANPOWER ───────────────────────────────
        saveList("svc_manpower", "coverage", "countries", List.of(
            "GCC Countries",
            "Canada",
            "European Countries",
            "Singapore",
            "Malaysia",
            "Russia"
        ));
        saveList("svc_manpower", "recruitment_services", "list", List.of(
            "Bulk Hiring – Blue Collars",
            "Ethical Recruitment",
            "Global Executive Search",
            "Project-Based Recruitment"
        ));
        saveList("svc_manpower", "industries", "list", List.of(
            "Oil & Gas & Energy",
            "Construction & Infrastructure",
            "Facility Management",
            "Hospitality",
            "Heavy Engineering",
            "Logistics & Warehousing",
            "Healthcare",
            "IT & Technology",
            "Marine & Shipyard"
        ));
        saveList("svc_manpower", "why_us", "reasons", List.of(
            "Connecting Jobseekers from 21 countries worldwide",
            "Expertise of Major Government & PPP Project",
            "Drive Interview with team of recruiter according to sector",
            "Strong global talent database from Asia, Africa & Europe",
            "Fast turnaround for Blue bulk recruitment",
            "Compliance-driven international hiring process",
            "End-to-end documentation & mobilization support",
            "Dedicated client relationship management Team",
            "Follow IRIS or Employer guideline in ethical recruitment"
        ));

        // ── SERVICE STAFFING ───────────────────────────────
        saveList("svc_staffing", "staffing_services", "list", List.of(
            "Recruitment & Talent Sourcing",
            "Deployment & Onboarding",
            "Payroll Management",
            "Statutory Compliance Management",
            "Contract Staffing / Third-Party Payroll",
            "Employee Lifecycle Management",
            "Workforce Administration",
            "Skill Training & Development"
        ));
        saveList("svc_staffing", "vendorship", "list", List.of(
            "Ensure timely hiring as per client requirement",
            "Maintain employee records and documentation",
            "Process salaries accurately and on time",
            "Deposit statutory contributions (PF, ESIC, etc.)",
            "Ensure compliance with labour laws",
            "Manage attendance and leave records",
            "Provide replacement for attrition",
            "Coordinate with client HR and operations",
            "Handle employee grievances",
            "Manage exit and final settlement"
        ));
        saveList("svc_staffing", "industries", "list", List.of(
            "Oil & Gas & Energy",
            "Manufacturing",
            "Retail & Distribution",
            "Telecom",
            "Banking, Finance, and Insurance",
            "E-Governance",
            "Hospitality",
            "Healthcare",
            "IT & Technology",
            "E-commerce & Logistic"
        ));
        saveList("svc_staffing", "why_us", "reasons", List.of(
            "Backed by 10 Years of Global Recruitment Legacy.",
            "Exclusive Industry Experience & Qualified team",
            "Proven Recruitment Track record of many prestigious projects globally.",
            "Technical Support in planning and estimating workforce requirements " +
                "and related costs for a project.",
            "An exclusive team of accounting & Tax professionals, monitored by CA/CWA, " +
                "managing statutory compliances and payroll operations efficiently.",
            "Our Training & Development (T&D) Department provides role-specific training " +
                "to each employee in line with client requirements prior to onboarding at the site"
        ));

        // ── SERVICE AI — exact text with trailing commas ───
        saveList("svc_ai", "industrial", "list", List.of(
            "SCADA,",
            "PLC,",
            "HMI,",
            "MES,",
            "IIoT Platforms,",
            "Predictive Maintenance,",
            "Energy Management,",
            "Process Automation, and",
            "ERP Integration Software."
        ));
        saveList("svc_ai", "testing", "list", List.of(
            "Unit Testing,",
            "Integration Testing,",
            "Functional Testing,",
            "Regression Testing,",
            "UI Testing,",
            "API Testing,",
            "Performance & Load Testing,",
            "Security Testing, and",
            "End-to-End Testing."
        ));

        // ── SERVICE MEDIA ──────────────────────────────────
        saveList("svc_media", "film", "list", List.of(
            "Corporate Introduction",
            "Corporate Services video",
            "Product Advertisement video",
            "CSR videos",
            "Government Awareness video",
            "Short Films",
            "Music videos",
            "Cultural & Travel Promotion video",
            "Political promotion video",
            "Sports Live Coverage"
        ));
        saveList("svc_media", "events", "list", List.of(
            "Corporate Events",
            "Product Launch Events",
            "Award Ceremonies",
            "Exhibition & Trade Shows",
            "Brand Promotion Events",
            "Celebrity & Entertainment Events",
            "Press Conferences & Media Events",
            "Wedding & Social Events",
            "Cultural Events & Festivals",
            "Training & Workshop Events",
            "Sports & Outdoor Events"
        ));

        System.out.println("✅ List items seeded");
    }

    // ══════════════════════════════════════════════════════
    // HELPER METHODS
    // ══════════════════════════════════════════════════════
    private ContentBlock block(String pageKey, String sectionKey,
                                String field, String value) {
        ContentBlock b = new ContentBlock();
        b.setPageKey(pageKey);
        b.setSectionKey(sectionKey);
        b.setField(field);
        b.setValue(value);
        return b;
    }

    private HeroSlide slide(int order, String title, String subtitle,
                             String videoPath, String tabLabel,
                             String slideType) {
        HeroSlide s = new HeroSlide();
        s.setSlideOrder(order);
        s.setTitle(title);
        s.setSubtitle(subtitle);
        s.setVideoPath(videoPath);
        s.setTabLabel(tabLabel);
        s.setSlideType(slideType);
        return s;
    }

    private MapLocation loc(String name, String country, double lat, double lng,
                            String region, String kind, int order) {
        MapLocation m = new MapLocation();
        m.setName(name);
        m.setCountry(country);
        m.setLatitude(lat);
        m.setLongitude(lng);
        m.setRegion(region);
        m.setKind(kind);
        m.setIsActive(true);
        m.setDisplayOrder(order);
        return m;
    }

    private void saveList(String pageKey, String sectionKey,
                           String listType, List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            ContentListItem item = new ContentListItem();
            item.setPageKey(pageKey);
            item.setSectionKey(sectionKey);
            item.setListType(listType);
            item.setOrderIndex(i + 1);
            item.setItemText(items.get(i));
            listRepo.save(item);
        }
    }
}