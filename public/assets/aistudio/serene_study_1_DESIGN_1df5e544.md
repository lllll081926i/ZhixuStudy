---
name: Serene Study
colors:
  surface: '#FFFFFF'
  surface-dim: '#d6d8f2'
  surface-bright: '#fbf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f2ff'
  surface-container: '#ececff'
  surface-container-high: '#e4e7ff'
  surface-container-highest: '#dee1fb'
  on-surface: '#161b2d'
  on-surface-variant: '#7C8298'
  inverse-surface: '#2b2f43'
  inverse-on-surface: '#efefff'
  outline: '#767683'
  outline-variant: '#E3E5EE'
  surface-tint: '#4e55b2'
  primary: '#4950ad'
  on-primary: '#ffffff'
  primary-container: '#6269c8'
  on-primary-container: '#fbf7ff'
  inverse-primary: '#bec2ff'
  secondary: '#5c5d71'
  on-secondary: '#ffffff'
  secondary-container: '#e1e0f8'
  on-secondary-container: '#626377'
  tertiary: '#67574f'
  on-tertiary: '#ffffff'
  tertiary-container: '#806f67'
  on-tertiary-container: '#fff8f5'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e0e0ff'
  primary-fixed-dim: '#bec2ff'
  on-primary-fixed: '#00016d'
  on-primary-fixed-variant: '#353c99'
  secondary-fixed: '#e1e0f8'
  secondary-fixed-dim: '#c5c4dc'
  on-secondary-fixed: '#191a2b'
  on-secondary-fixed-variant: '#444558'
  tertiary-fixed: '#f5ded4'
  tertiary-fixed-dim: '#d8c2b9'
  on-tertiary-fixed: '#251913'
  on-tertiary-fixed-variant: '#52443d'
  background: '#F6F7FB'
  on-background: '#161b2d'
  surface-variant: '#dee1fb'
  mint-accent: '#E6F3EB'
  mint-text: '#216C4B'
  peach-text: '#8F4829'
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
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
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

This design system translates the thoughtful, academic rhythm of deliberate practice into an ergonomic, modern Android experience. Rooted in the principles of Material 3 and Material You, the visual language balances utilitarian precision with welcoming softness.

### Brand Personality & Core Mood
- **Serene & Focused:** The interface avoids noisy gamification and sensory overload. Long study sessions require a calm cognitive canvas with restrained color distribution and low-glare surfaces.
- **Systematic & Trustworthy:** Built around strict data verification, confidence scoring, and clean question hierarchies, every layout conveys meticulous order and dependable logic.
- **Approachable & Human:** Flat geometric vector illustrations greet users at milestones, empty states, and onboarding junctures, preventing the utility from feeling stark or mechanical.

### Design Movement
The interface employs **Modern Material 3** augmented by **Material You tonal harmony**:
- Large corner radii (12dp–16dp on cards, pill-shaped chips and buttons) create an ergonomic touch environment.
- Surface nesting relies on chromatic shifts (subtle purple-tinted slates and whites) rather than high-contrast structural borders or heavy drop shadows.
- Visual rhythm follows strict 4dp/8dp spatial increments, providing native alignment for high-density academic and data-table views.

## Colors

The color palette centers on a soft periwinkle-indigo primary hue supported by calming pastel container fills. The system avoids visual fatigue by keeping high-chroma applications strictly limited to interactive priorities (primary buttons, active tabs, progress milestones).

### Color Roles & Meaning
- **Primary (`#6269C8`):** The intellectual anchor. Used for primary interactive actions, high-emphasis selections, and active indicator states.
- **Secondary Container (`#E8E7FF`):** Tonal complement. Houses selected states, secondary button backgrounds, and card chips within list elements.
- **Peach Accent (`#FFE8DE`):** Warm spotlight tone. Reserved for contextual highlight cards, active campaign notifications, review reminders, and warm illustration accents.
- **Mint Accent (`#E6F3EB`):** Affirmative status tone. Communicates verified imports, accurate questions during review, and completed mastery metrics.
- **Neutral Foreground (`#24283B`):** Deep indigo-tinted slate for high-contrast legibility, preventing the eye strain of harsh `#000000`.
- **Surface & Background (`#FFFFFF` / `#F6F7FB`):** Soft, off-white canvas with a cool undertone to separate interactive sheets and cards from application backing.

## Typography

The type system utilizes Plus Jakarta Sans to harmonize modern geometric clarity with open letterforms suitable for prolonged reading on mobile screens. 

### Scale Applications
- **Stem/Question Titles (`body-lg` / `title-lg`):** Sized between 17sp and 20sp equivalent (18px baseline with 28px line height) to accommodate complex exam questions, mathematical fragments, and inline code terms without horizontal cramping.
- **Interface Copy & Choices (`body-md` / `title-md`):** Fixed around 15–16px with relaxed line height for rapid scanability during time-constrained mock tests.
- **Metadata & Chips (`label-sm` / `label-md`):** Formatted in medium weight for parsing tabular mapping columns, confidence tags, and status counters.

## Layout & Spacing

Layout geometry follows an atomic 4dp and 8dp spatial grid aligned with Android platform standards.

### Canvas Structure
- **Mobile Handset (< 600dp):** Fluid single-column layout with 16dp (`1rem`) outer canvas margins. Components stretch horizontally across safe display boundaries. Bottom navigation stays anchored to the device baseline, padded above the system gesture bar.
- **Tablet & Foldables (≥ 600dp):** Content shifts to a dual-pane structure (`ListDetailPaneScaffold`). Margin scales to 24dp–32dp with a 16dp gutter separating list navigation from item preview and detail inspector panels. Bottom navigation transforms into an adaptive leading `NavigationRail`.
- **Touch Ergonomics:** All touch targets must honor an absolute minimum bounding area of 48×48dp, even when underlying label chips or icon graphics measure smaller.

## Elevation & Depth

Visual separation relies primarily on **tonal stepping** and **low-contrast outlines** rather than opaque, dramatic drop shadows.

### Surface Tiers
- **Tier 0 (Background Canvas - `#F6F7FB`):** Base structural canvas across full viewport areas.
- **Tier 1 (Surface Neutral - `#FFFFFF`):** Base cards, review options, and question blocks. Bordered by a delicate 1dp stroke of `#E3E5EE` with no shadow.
- **Tier 2 (Elevated Sheets & Floating Dialogs):** Modal bottom sheets, action bar menus, and dropdown menus utilize a micro-ambient shadow: `0px 8px 24px -4px rgba(36, 40, 59, 0.08)` coupled with the container fill to lift comfortably above base items.
- **Tier 3 (Floating Action Button):** Features an intentional soft bloom: `0px 6px 16px 0px rgba(98, 105, 200, 0.28)`, creating recognizable emphasis for the bottom-right import trigger.

## Shapes

The design uses a rounded physical geometry (Level 2) aligned with Material 3 expressive tokens:
- **Base Components (0.5rem / 8dp):** Small badges, text input outlines, toast containers, and mini metrics.
- **Containers & Options (1rem / 16dp):** Question option cards, bottom sheet top edges, review tiles, and dialog windows.
- **Action Controls & Chips (Pill / Full Curve):** Primary buttons, floating filter chips, status badges, and segmented toggles use full 9999px pill curves to optimize one-handed tap affordance.

## Components

### Buttons & Selection
- **Primary Button:** Pill-shaped, `#6269C8` background with `#FFFFFF` text. Minimum height 48dp with 24dp horizontal padding.
- **Secondary (Tonal) Button:** Pill-shaped, `#E8E7FF` container with `#6269C8` text. Used for secondary navigation, step rollbacks, and contextual filters.
- **Outlined Button:** 1dp `#E3E5EE` border, transparent background, `#24283B` label for minor inline actions.

### Cards & Question Items
- **Question Option Card:** Full-width rounded card (16dp radius), `#FFFFFF` fill with 1dp `#E3E5EE` outline. When selected, transitions to `#E8E7FF` container fill and a 1.5dp `#6269C8` active border. 
- **Feedback States:** In review mode, correct selections morph to `#E6F3EB` (mint) with `#216C4B` text; errors highlight in a delicate muted coral.
- **Question Tile Item:** Stacked vertical list item with 16dp padding, holding question type chips, truncation limiters, and clean status indicators.

### Chips & Tags
- **Filter Chips:** 32dp height, fully rounded pill. Unselected state is bordered transparent or neutral `#E3E5EE`; selected state adopts `#6269C8` with white typography.
- **Status Tags (Confidence & Duplicate Flags):** Compact rounded pill (8dp radius), 24dp height, using semantic backgrounds (`#FFE8DE` for conflicts/warnings, `#E6F3EB` for valid items).

### Form Inputs
- **Outlined Text Field:** 12dp rounded corner radius, `#E3E5EE` border in idle state, transitioning to 2dp `#6269C8` on active focus. Supporting text sits 4dp below the field in `#7C8298`.

### Navigation
- **Navigation Bar:** Fixed 80dp baseline bar, `#FFFFFF` container with no divider line; active destination sits inside an elongated `#E8E7FF` pill indicator with a `#6269C8` icon.