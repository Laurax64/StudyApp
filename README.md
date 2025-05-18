# Under Development

# Design 
Currently, I am making major updates to the design because of the M3 expressive update.
### [Figma design file](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=1-3&t=Z2gDVT6f44Ki0b7z-1) :arrows_counterclockwise:
### [Material theme builder](https://www.figma.com/community/plugin/1034969338659738588/material-theme-builder) 
### [Material 3 guidelines](https://developer.android.com/design/ui?hl=en) 
### [Material 3 Expressive](https://m3.material.io/blog/building-with-m3-expressive) :arrows_counterclockwise:
### [Previews](https://developer.android.com/develop/ui/compose/tooling/previews)

# UI Components

## Actions
### Button groups :arrows_counterclockwise:
### Buttons 
### Extended FAB
### FAB menu  :arrows_counterclockwise:
### Floating action buttons  :arrows_counterclockwise:
### Icon buttons  :arrows_counterclockwise:
### Segmented button  :arrows_counterclockwise:
### Split buttons  :arrows_counterclockwise:

## Communication
### Badges  :arrows_counterclockwise:
### Loading indicator  :arrows_counterclockwise:
### Progress indicators
### Snackbar  :arrows_counterclockwise:
### Tooltips  :arrows_counterclockwise:

## Containment
### Bottom sheets  :arrows_counterclockwise:
### Cards  :arrows_counterclockwise:
### Carousel  :arrows_counterclockwise:
### Dialogs
### Divider
### Lists 
### Side sheets  :arrows_counterclockwise:

## Navigation 
### App bars
### Navigation bar
### Navigation drawer :arrows_counterclockwise:
### Navigation rail 
### Tabs  :arrows_counterclockwise:
### Toolbar  :arrows_counterclockwise:

## Selection
### Checkbox 
### Chips
### Date pickers  :arrows_counterclockwise:
### Menus 
### Radio button  :arrows_counterclockwise:
### Sliders  :arrows_counterclockwise:
### Switch  :arrows_counterclockwise:
### Timer pickers  :arrows_counterclockwise:
## Text inputs
### Search 
### Text fields

## Launcher Icon
<img alt="image" width="50"  src = docs/images/study-app-ic_launcher_round.png>

## Custom Splash Screen

<img width="200" alt="image" src = docs/images/study-app-splash-screen-light.png>  <img width="200" alt="image" src = docs/images/study-app-splash-screen-dark.png>

## Adaptive Layouts 
### Canonical Layouts  :arrows_counterclockwise:
#### [Feed](https://m3.material.io/foundations/layout/canonical-layouts/overview#963d7d99-4f04-4685-b7bd-57a89607b514) :arrows_counterclockwise:
#### [List-detail](https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail)
#### [Supporting pane](https://m3.material.io/foundations/layout/canonical-layouts/overview#b01f6399-a0d3-4fd8-b78b-78a9ab663482) :arrows_counterclockwise:

## Widget 
### [Glance](https://developer.android.com/codelabs/glance?hl=en#0) :arrows_counterclockwise:
## Images
### [Photo picker](https://developer.android.com/training/data-storage/shared/photopicker) 
Photo picker to let the user pick an image for the subtopic
### [AsyncImage](https://developer.android.com/develop/ui/compose/graphics/images/loading)
AsyncImage composable to load images from the file storage
## Accessibility
### [TalkBack](https://developer.android.com/guide/topics/ui/accessibility/testing#talkback) :arrows_counterclockwise:

## Navigation 
 -  [Navigation controller](https://developer.android.com/guide/navigation/navcontroller)
 -  [Navigation graph](https://developer.android.com/guide/navigation/design)
 -  [Navigation to destinations](https://developer.android.com/guide/navigation/use-graph/navigate)
 -  [NavigationSuiteScaffold](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation)
 -  [Nested graph](https://developer.android.com/guide/navigation/design/nested-graphs)
## Dependency Injection 
### [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=en)
For non-assisted dependency injection
### [Dagger](https://developer.android.com/training/dependency-injection/dagger-android#assisted-injection) 
For assisted dependency injection :arrows_counterclockwise:
## Data Storage 
[Room](https://developer.android.com/training/data-storage/room?hl=en)
## States :arrows_counterclockwise:

## App Architecture
<img width="600" alt="image" src = docs/images/architecture.png>

### [Single Sources of Truth](https://developer.android.com/topic/architecture#single-source-of-truth) 
* Each data type has a SSOT assigned to it.
* Only the SSOT can modify or mutate the data.
* The SSOT exposes the data using an immutable type, and to modify the data, the SSOT exposes functions or receives events.

### [Unidirectional Data Flow](https://developer.android.com/topic/architecture#unidirectional-data-flow)
* State flows in only one direction.
* The events that modify the data flow in the opposite direction.
  
### [Modularization](https://developer.android.com/topic/modularization) :arrows_counterclockwise:

## [Play Feature Delivery](https://developer.android.com/guide/playcore/feature-delivery) :arrows_counterclockwise:


## Code Style :arrows_counterclockwise:

## Testing :arrows_counterclockwise:
