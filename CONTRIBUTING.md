## Contributing

Thank you for your interest in contributing to the Balanced Android.

If you are opening a new issue or submitting a pull request, go for it! Don't be afraid that it's a dumb idea or a duplicate of another issue or an unwanted change or whatever, especially if this is your first time participating with us. We're glad to have you! :^)

### Project Setup

balanced-android and balanced-android-test have been primarily built and tested with ADT(Eclipse).

#### Prerequisites

##### Install the plug-in.
Go to Help -> Install New Software...

Select "Juno - http://download.eclipse.org/releases/juno" for the "Work with" field
Expand "General Purpose tools"
Select "m2e - Maven Integration for Eclipse"

##### Import the projects

Open ADT. Go to File -> Import -> General -> Existing Projects into Workspace.

Select the folder containg both balanced-android and balanced-android-example.


### Building the Projects

balanced-android should be Run As "Maven Install" which builds the library into a jar file for use in projects.

balanced-android-test should be Run As "Android JUnit Test". It should reference the jar file built into the target folder of balanced-android. Running this test suite requires a configured and running Android emulator. This is configurable from Window -> Android Virtual Device Manager.