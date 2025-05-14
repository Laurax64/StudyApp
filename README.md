# Under Development
### Design
- [x] [Figma](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=1-3&t=Z2gDVT6f44Ki0b7z-1)
- [x] [Material Theme Builder](https://www.figma.com/community/plugin/1034969338659738588/material-theme-builder)
- [x] [Material 3 Guidelines](https://developer.android.com/design/ui?hl=en)
- [x] [Previews](https://developer.android.com/develop/ui/compose/tooling/previews)

### Canonical layouts
- [ ] [Feed](https://m3.material.io/foundations/layout/canonical-layouts/overview#963d7d99-4f04-4685-b7bd-57a89607b514)
- [x] [List-detail](https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail)
- [ ] [Supporting pane](https://m3.material.io/foundations/layout/canonical-layouts/overview#b01f6399-a0d3-4fd8-b78b-78a9ab663482)
### Search 
- [ ] [Searchbar](https://developer.android.com/develop/ui/compose/components/search-bar)
### Widget 
- [ ] [Glance](https://developer.android.com/codelabs/glance?hl=en#0)
### Images
- [x] [Photo picker](https://developer.android.com/training/data-storage/shared/photopicker) to let the user pick an image for the subtopic
- [x] [AsyncImage](https://developer.android.com/develop/ui/compose/graphics/images/loading) composable to load images from the file storage
### Accessibility
- [ ] [TalkBack](https://developer.android.com/guide/topics/ui/accessibility/testing#talkback)
### Navigation 
- [x] [Navigation controller](https://developer.android.com/guide/navigation/navcontroller)
- [x] [Navigation graph](https://developer.android.com/guide/navigation/design)
- [x] [Navigation to destinations](https://developer.android.com/guide/navigation/use-graph/navigate)
- [x] [NavigationSuiteScaffold](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation)
- [x] [Nested graph](https://developer.android.com/guide/navigation/design/nested-graphs)
### Dependency injection 
- [ ] Assisted and non-assisted dependency injection
 - [x] [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=en)
 - [ ] [Dagger](https://developer.android.com/training/dependency-injection/dagger-android#assisted-injection)
### Data Storage 
- [x] [Room](https://developer.android.com/training/data-storage/room?hl=en)
### States
- [ ] ...
### App Architecture
<img width="500" alt="image" src="https://github.com/user-attachments/assets/79ca51cb-ae7a-42e8-ada4-c085367edba1" />

[Single Sources of Truth](https://developer.android.com/topic/architecture#single-source-of-truth) 
* Each data type has a SSOT assigned to it.
* Only the SSOT can modify or mutate the data.
* The SSOT exposes the data using an immutable type, and to modify the data, the SSOT exposes functions or receives events.

[Unidirectional Data Flow](https://developer.android.com/topic/architecture#unidirectional-data-flow)
* State flows in only one direction.
* The events that modify the data flow in the opposite direction.

### Play Feature Delivery
- [ ] 
[Modularization](https://developer.android.com/topic/modularization)
- [ ]
### Code Style 
- [ ] ...
### Testing
- [ ] ...
