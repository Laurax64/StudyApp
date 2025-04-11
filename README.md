# Under Development
## UI Design
* [Figma](https://www.figma.com/files/team/1227960222597739086/recents-and-sharing?fuid=1227960214312908777)
* [Material 3 Guidelines](https://developer.android.com/design/ui?hl=en)
* [Previews](https://developer.android.com/develop/ui/compose/tooling/previews)
## Navigation 
* [Navigation controller](https://developer.android.com/guide/navigation/navcontroller)
* ...
## Dependency injection 
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=en)
## Data Storage 
* [Room](https://developer.android.com/training/data-storage/room?hl=en)
## States
## App Architecture
* [Single source of truth](https://developer.android.com/topic/architecture#single-source-of-truth)
  * Each data type has a SSOT assigned to it.
  * Only the SSOT can modify or mutate the data.
  * The SSOT exposes the data using an immutable type, and to modify the data, the SSOT exposes functions or receives events.
* [Unidirectional Data Flow](https://developer.android.com/topic/architecture#unidirectional-data-flow)
  * State flows in only one direction.
  * The events that modify the data flow in the opposite direction.
## Widget 
* [Glance](https://developer.android.com/codelabs/glance?hl=en#0)
