---
name: Serene Study
colors:
  surface: '#fbf8ff'
  surface-dim: '#d7d8f0'
  surface-bright: '#fbf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f2ff'
  surface-container: '#ececff'
  surface-container-high: '#e5e6fe'
  surface-container-highest: '#dfe1f8'
  on-surface: '#181b2b'
  on-surface-variant: '#464652'
  inverse-surface: '#2c2f41'
  inverse-on-surface: '#f0efff'
  outline: '#767683'
  outline-variant: '#c7c5d4'
  surface-tint: '#4e54b3'
  primary: '#4248a6'
  on-primary: '#ffffff'
  primary-container: '#5b61c0'
  on-primary-container: '#eceaff'
  inverse-primary: '#bfc2ff'
  secondary: '#5c5d71'
  on-secondary: '#ffffff'
  secondary-container: '#dedef6'
  on-secondary-container: '#606175'
  tertiary: '#8a3b0d'
  on-tertiary: '#ffffff'
  tertiary-container: '#a95224'
  on-tertiary-container: '#ffe8df'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e0e0ff'
  primary-fixed-dim: '#bfc2ff'
  on-primary-fixed: '#01006e'
  on-primary-fixed-variant: '#363b99'
  secondary-fixed: '#e1e0f8'
  secondary-fixed-dim: '#c5c4dc'
  on-secondary-fixed: '#191a2b'
  on-secondary-fixed-variant: '#444558'
  tertiary-fixed: '#ffdbcc'
  tertiary-fixed-dim: '#ffb694'
  on-tertiary-fixed: '#351000'
  on-tertiary-fixed-variant: '#7a2f01'
  background: '#fbf8ff'
  on-background: '#181b2b'
  surface-variant: '#dfe1f8'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 57px
    fontWeight: '700'
    lineHeight: 64px
  display-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 45px
    fontWeight: '700'
    lineHeight: 52px
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 32px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  title-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  title-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system translates academic rigor and contemplative self-study into an ergonomic, ultra-refined Android Material 3 experience. Built for long-session question banks, review intervals, and deep cognitive focus, the interface deliberately eliminates sensory clutter, visual noise, and aggressive gamification.

### Visual Philosophy
- **Restrained & Intellectual:** Anchored in a muted violet-blue spectrum that evokes psychological calm, clarity, and prolonged focus without ocular fatigue.
- **Tonal Depth over Harsh Boundaries:** Layers are separated through subtle chromatic tonal shifts, delicate micro-borders, and low-glare surface containers rather than stark black lines or intrusive drop shadows.
- **Material 3 Expressive & Native Ergonomics:** Follows Android’s native rounded geometry, adaptive containers, and responsive touch boundaries (minimum 48×48dp targets) optimized for fluid one-handed thumb interaction.

## Colors

The color architecture is built around an authentic, WCAG AAA/AA compliant dual-theme implementation of Material Design 3. Primary brand hues and semantic states balance saturation to guarantee visual hierarchy across daylight and night study modes.

### Dual-Theme Token Architecture

#### 1. Light Theme (Daylight Study)
- **Primary Tokens:**
  - `primary`: `#5B61C0` — Calm, non-glare indigo for primary actions, floating triggers, and key indicators.
  - `on-primary`: `#FFFFFF` — Clear, crisp glyph and label contrast.
  - `primary-container`: `#ECEEFA` — Soft periwinkle fill for secondary chips and active state containers.
  - `on-primary-container`: `#1D2163` — Deep accessible purple text over light containers.
- **Surface & Background Tiers:**
  - `background`: `#F7F8FC` — Slight cool-tinted canvas to prevent pure-white brightness fatigue.
  - `surface`: `#FFFFFF` — Crisp canvas for elevated cards, modal sheets, and dialogue tiles.
  - `surface-container`: `#F0F2FA` — Muted level-1 container backing (options, table tracks).
  - `surface-container-high`: `#E8ECF7` — Level-2 structural container (search bars, header blocks).
  - `outline`: `#767683` — High-contrast functional dividers and idle control bounds.
  - `outline-variant`: `#E3E5EE` — Ultra-delicate 1dp card borders and card separators.
- **Text & Content Contrast:**
  - `on-surface` (Primary): `#1E2132` — Deep blue-slate; eliminates black harshness.
  - `on-surface-variant` (Secondary): `#595E73` — Balanced subtitle and explanation text.
  - Tertiary / Caption: `#8A90A6` — Inactive counters, timestamps, and input placeholders.
- **Semantic Accents:**
  - Success / Correct (`mint`): `#3D8C66` text/icon on `#E6F3EB` container.
  - Habit / Streak (`peach`): `#D97746` text/icon on `#FDEEE7` container.
  - Warning / Conflict (`amber`): `#C27A23` text/icon on `#FDF2E2` container.
  - Error / Incorrect (`coral`): `#D04848` text/icon on `#FCECEC` container.

#### 2. Dark Theme (Night & OLED Low-Light Mode)
- **Primary Tokens:**
  - `primary`: `#A8ADF8` — Desaturated, high-luminance lavender for high legibility on dark surfaces.
  - `on-primary`: `#171B67` — High-contrast dark core for button labels.
  - `primary-container`: `#4248A7` — Mid-tone container backing for active question pills.
  - `on-primary-container`: `#E0E2FF` — Soft tinted white for container text.
- **Surface & Background Tiers:**
  - `background`: `#13151F` — Rich, comfortable navy-black canvas preventing pure `#000000` clipping.
  - `surface`: `#1A1D2B` — Slightly lifted surface for item cards and navigation sheets.
  - `surface-container`: `#222638` — Recessed container tone for option buttons and test rows.
  - `surface-container-high`: `#2B3046` — High-emphasis dark container for toolbars and chips.
  - `outline`: `#8D92A6` — Subtle structural guidance lines.
  - `outline-variant`: `#2F344A` — Non-distracting 1dp card boundaries.
- **Text & Content Contrast:**
  - `on-surface` (Primary): `#EFF1F8` — Soft milky white with zero screen halation.
  - `on-surface-variant` (Secondary): `#A3A8BE` — Clean secondary legibility for reading question prompts.
  - Tertiary / Caption: `#6E748B` — Subdued metadata and tags.
- **Semantic Accents:**
  - Success / Correct (`mint`): `#6ECFA0` text/icon on `#173B28` container.
  - Warning / Ambiguity (`amber`): `#F0A858` text/icon on `#3D270E` container.
  - Error / Incorrect (`coral`): `#F87979` text/icon on `#421818` container.
  - Info / Review (`azure`): `#92B5F8` text/icon on `#1B2E52` container.

## Typography

The typographic scale uses **Plus Jakarta Sans** across all levels to synthesize geometric balance with open, humanist aperture counters. This ensures maximum scanability when rendering lengthy exam stems, analytical explanations, and multi-line option cards.

### Typographic Applications
- **Exam Question Stems:** Use `body-lg` (18px/28px) or `title-lg` (18px/26px) with relaxed vertical rhythm to facilitate frictionless comprehension without horizontal fatigue.
- **Multiple-Choice Options:** Standardized at `body-md` (15px/22px). Maintains crisp clarity while retaining space for inline math tokens and sub-labels.
- **Analytical Metrics & Explanations:** Key summary badges and statistics employ `headline-sm` or `headline-md` for numerical mastery scores, while explanatory analysis returns to `body-md` in `on-surface-variant`.
- **System Badges & Chips:** Use `label-md` and `label-sm` in semi-bold weight for status pills, category taxonomies, and difficulty tiers.

## Layout & Spacing

Spacing is anchored strictly to an 8dp baseline grid with 4dp micro-increments, conforming to Android Material 3 spatial logic.

### Form Factors & Adaptation
- **Mobile (< 600dp):** Fluid single-column layout with 16dp (`1rem`) outer canvas margins and 12dp–16dp gap between question cards. Content expands to full safe display width. The bottom app bar remains fixed at 80dp baseline height above system insets.
- **Foldables & Tablets (≥ 600dp):** Transition to a dual-pane layout (`ListDetailPaneScaffold`). Margin scales to 24dp with a 16dp gutter separating the question navigator panel from the active workspace. Bottom navigation elevates into an adaptive leading `NavigationRail`.
- **Touch Bounds:** Interactive elements (checkboxes, radio targets, chip taps) must maintain a minimum bounding box of 48×48dp regardless of visible shape dimensions.

## Elevation & Depth

Visual hierarchy is communicated via **tonal surface stacking** complemented by **subtle ambient shadows** in light mode and **luminescence stepping** in dark mode.

### Elevation Hierarchy
- **Level 0 (App Canvas):** `background` (`#F7F8FC` / `#13151F`). Flush foundation for the entire viewport.
- **Level 1 (Card & Module Containers):** `surface` or `surface-container`. Bound by a 1dp stroke of `outline-variant`. In light mode, uses an ambient diffused shadow: `0px 2px 8px -2px rgba(30, 33, 50, 0.04)`. In dark mode, no shadow is applied; separation is driven solely by tonal stepping (`#1A1D2B` over `#13151F`).
- **Level 2 (Popovers, Bottom Sheets & Dialogs):** Modals elevate using `surface` accompanied by `0px 8px 24px -4px rgba(30, 33, 50, 0.12)` in light mode, and a soft rim outline `outline-variant` in dark mode.
- **Level 3 (Floating Action Button):** The primary FAB uses a branded chromatic bloom in light mode: `0px 6px 16px 0px rgba(91, 97, 192, 0.28)`. In dark mode, it uses pure surface container luminance with zero blur artifacting.

## Shapes

The design system adopts a rounded shape language (Level 3) that aligns with Material 3 Expressive guidelines, offering pill-forward curvatures for touch targets and containers.

- **Small (0.5rem / 8dp):** Checkbox boxes, progress bar caps, tooltip containers, and micro-metric badges.
- **Medium (1.5rem / 24dp):** Text input fields, contextual toast overlays, and segmented option groups.
- **Large (2rem / 32dp):** Question option cards, bottom sheet sheets, stat summary blocks, and dialog cards.
- **Pill (Full curve / 9999px):** Primary interactive action buttons, filter chips, category pills, and navigation pill indicators.

## Components

### Buttons
- **Primary Filled Button:** Full pill radius (9999px), minimum height 48dp.
  - *Light Mode:* `#5B61C0` fill, `#FFFFFF` text. Pressed state shifts to `rgba(91, 97, 192, 0.9)`.
  - *Dark Mode:* `#A8ADF8` fill, `#171B67` text.
- **Tonal (Secondary) Button:** Full pill radius.
  - *Light Mode:* `#ECEEFA` fill, `#5B61C0` text.
  - *Dark Mode:* `#4248A7` fill, `#E0E2FF` text.
- **Outlined Button:** Full pill radius. Transparent fill with 1dp `outline-variant` border. Text inherits `on-surface`.

### Question Option Cards
- **Idle State:** 16dp rounded radius, 1dp `outline-variant` border. Fill is `surface` (light: `#FFFFFF`, dark: `#1A1D2B`). Text is `on-surface`.
- **Selected State:** Border width increases to 1.5dp with primary tint (`#5B61C0` / `#A8ADF8`). Fill transitions to `primary-container` (light: `#ECEEFA`, dark: `#2A2F50`). Leading indicator radio adopts solid primary fill.
- **Correct State (Review Mode):**
  - *Light Mode:* Fill `#E6F3EB`, border 1.5dp `#3D8C66`, text `#1E2132`, badge/check glyph `#3D8C66`.
  - *Dark Mode:* Fill `#173B28`, border 1.5dp `#6ECFA0`, text `#EFF1F8`, badge/check glyph `#6ECFA0`.
- **Incorrect State (Review Mode):**
  - *Light Mode:* Fill `#FCECEC`, border 1.5dp `#D04848`, text `#1E2132`, badge/cross glyph `#D04848`.
  - *Dark Mode:* Fill `#421818`, border 1.5dp `#F87979`, text `#EFF1F8`, badge/cross glyph `#F87979`.

### Chips & Badges
- **Filter & Category Chips:** 32dp height, full pill radius. Idle state is unselected with 1dp `outline-variant` and `on-surface-variant` text. Active state transitions to `primary-container` with primary brand label.
- **Status Tags (Streak & Review Badges):** 24dp height, 6dp radius. Streak counters use peach tokens; confidence levels utilize mint (high) and amber (medium).

### Inputs & Form Fields
- **Outlined Text Field:** 12dp corner radius. Idle state: 1dp `outline-variant` border with `surface` background. Focus state: 2dp primary color stroke (`#5B61C0` / `#A8ADF8`). Placeholder copy rendered in `on-surface-variant`.

### Progress Indicators
- **Linear Progress Track:** 6dp height, rounded end caps. Track background uses `surface-container-high` (`#E8ECF7` / `#2B3046`). Indicator bar uses primary brand color (`#5B61C0` / `#A8ADF8`) or mint accent (`#3D8C66` / `#6ECFA0`) for completed sessions.

### Navigation
- **Navigation Bar:** 80dp height, docked to viewport bottom. Background matches `surface` with zero divider line. Active destination is highlighted via a 64×32dp pill indicator filled with `primary-container` housing the active primary icon.