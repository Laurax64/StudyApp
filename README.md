# Under Development
## User Interface
### Design
[Figma](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=1-3&t=Z2gDVT6f44Ki0b7z-1)
   
[Material 3 Guidelines](https://developer.android.com/design/ui?hl=en)

[Previews](https://developer.android.com/develop/ui/compose/tooling/previews)
### Adaptive layout
Layout for different screen sizes 
### Navigation 
The type-safe navigation has been implemented by creating a [navigation controller](https://developer.android.com/guide/navigation/navcontroller) and [navigation graph](https://developer.android.com/guide/navigation/design) and then, using the navigation graph, to [navigate to destinations](https://developer.android.com/guide/navigation/use-graph/navigate).
### [Searchbar](https://developer.android.com/develop/ui/compose/components/search-bar)
### Widget 
[Glance](https://developer.android.com/codelabs/glance?hl=en#0)
### Images
A [photo picker](https://developer.android.com/training/data-storage/shared/photopicker) has been used to let the user pick an image for the subtopic.
The [AsyncImage](https://developer.android.com/develop/ui/compose/graphics/images/loading) composable has been used to load the images from the file storage.
### Accessibility
Content descriptions for non-decorative icons and images have been added so that users can use the app with [TalkBack](https://developer.android.com/guide/topics/ui/accessibility/testing#talkback).
## Dependency injection 
For assisted and non-assisted dependency injection, [Dagger](https://developer.android.com/training/dependency-injection/dagger-android#assisted-injection) including [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=en) has been used.
## Data Storage 
[Room](https://developer.android.com/training/data-storage/room?hl=en)
## States
## App Architecture
<img width="500" alt="image" src="https://github.com/user-attachments/assets/79ca51cb-ae7a-42e8-ada4-c085367edba1" />

[Single Sources of Truth](https://developer.android.com/topic/architecture#single-source-of-truth) 
* Each data type has a SSOT assigned to it.
* Only the SSOT can modify or mutate the data.
* The SSOT exposes the data using an immutable type, and to modify the data, the SSOT exposes functions or receives events.

[Unidirectional Data Flow](https://developer.android.com/topic/architecture#unidirectional-data-flow)
* State flows in only one direction.
* The events that modify the data flow in the opposite direction.

## Code Style 
...
## Testing
...
