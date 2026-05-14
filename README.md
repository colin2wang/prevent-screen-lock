# Prevent Screen Lock

A Java SWT application that prevents your computer screen from locking by simulating periodic Scroll Lock key presses.

## Features

- **Automatic Key Press Simulation**: Periodically presses the Scroll Lock key to keep your session active
- **Configurable Interval**: Set custom time intervals (in seconds) between key presses
- **Smart State Management**: Intelligently checks Scroll Lock state and ensures it remains OFF after each operation
- **Real-time Log Display**: Built-in log viewer with adjustable log levels and dual output (console + UI)
- **User-Friendly Interface**: Clean tabbed interface with intuitive start/stop controls
- **Non-blocking Architecture**: Uses separate threads for key simulation and log updates to maintain UI responsiveness

## How It Works

The application prevents screen lock through intelligent key state management:

1. **State Detection**: Checks the current state of the Scroll Lock key
2. **Conditional Action**: 
   - If Scroll Lock is ON: Waits 2 seconds, then presses it to turn OFF
   - If Scroll Lock is OFF: Presses it once to momentarily activate and immediately deactivate
3. **State Guarantee**: Ensures Scroll Lock always ends in OFF state
4. **Periodic Execution**: Repeats this process at configurable intervals (default: 60 seconds)

This approach simulates user activity without interfering with your typing or workflow, as the Scroll Lock state is always returned to OFF.

## Requirements

- **Java**: JDK 8 or higher (tested with JDK 17.0.17)
- **Operating System**: Windows (tested on Windows 25H2)
- **Build Tool**: Maven 3.x (for building from source)

## Building the Project

This is a Maven project. To build:

```bash
mvn clean package
```

## Running the Application

After building, run the application using:

```bash
java -jar target/prevent-screen-lock.jar
```

Or run directly from your IDE by executing the `PSLMainUI` class.

## Usage

1. **Launch the application** - The main window will appear
2. **Configure interval** - Enter the desired time interval in seconds (default: 60)
3. **Click "Open"** - Start the automatic key press simulation
4. **Click "Stop"** - Stop the simulation when no longer needed

### Log Window

The application features a sophisticated real-time log viewer that provides:

- **Dual Output**: Logs are displayed both in the console and the UI window
- **Dynamic Log Level Control**: Adjust log level on-the-fly (ERROR, WARN, INFO, DEBUG, TRACE)
- **Toggle Logging**: Enable/disable log capture without restarting
- **Log Management**: Clear log content with a single click
- **Auto-scroll**: New logs automatically appear in real-time
- **Thread-Safe Updates**: Uses non-blocking polling to ensure smooth UI performance

## Project Structure

```
src/main/java/com/colin/
├── swt/
│   ├── PSLMainUI.java          # Main application entry point
│   ├── Constants.java           # Application constants
│   ├── composite/
│   │   ├── KeyPressComposite.java    # Key press control panel
│   │   ├── LogWindow.java            # Log display component
│   │   └── LogWindowAppender.java    # Custom Logback appender
│   └── dialog/
│       └── BrowserDialog.java        # Browser dialog (utility)
└── util/
    └── UserActionUtil.java     # User action simulation utilities
```

## Technical Details

### Technologies Used

- **SWT (Standard Widget Toolkit)**: Native GUI toolkit for Windows
- **Java AWT Robot**: Low-level input event simulation for keyboard actions
- **Logback 1.5.25**: High-performance logging framework
- **SLF4J**: Logging facade for flexible log implementation
- **Maven**: Dependency management and build automation

### Architecture

The application uses a multi-threaded architecture:

1. **UI Thread**: Handles all SWT widget interactions and updates
2. **Key Press Thread**: Runs independently to simulate periodic key presses
3. **Log Timer Thread**: Polls log cache every 100ms for new messages
4. **Log Cache**: Thread-safe `BlockingDeque` buffers up to 500 log messages

### Key Components

- **PSLMainUI**: Application entry point and main window controller
- **KeyPressComposite**: Manages key press simulation with state-aware logic
- **UserActionUtil**: Utility class wrapping Java Robot for keyboard/mouse actions
- **LogWindow**: Real-time log display with dynamic level control
- **LogWindowAppender**: Custom Logback appender implementing `UnsynchronizedAppenderBase<ILoggingEvent>`
- **Constants**: Centralized configuration constants

## Configuration

### Default Settings

- **Sleep Interval**: 60 seconds (configurable via UI)
- **Initial Log Level**: INFO
- **Logs Enabled**: Yes (toggle via UI checkbox)
- **Log Cache Size**: 500 messages maximum
- **Log Poll Interval**: 100ms

### Modifying Defaults

You can change default values in:
- `Constants.java`: Application-wide constants
- `logback.xml`: Logging configuration
- UI controls: Runtime adjustments (no restart needed)

## Important Notes

### Thread Safety
- The application runs key simulation in a separate thread to avoid blocking the UI
- Log updates use non-blocking `poll()` operations to prevent timer thread suspension
- All UI updates are executed via `Display.syncExec()` for thread safety

### Scroll Lock Behavior
- Scroll Lock state is checked before each operation to maintain consistency
- The application guarantees Scroll Lock ends in OFF state to avoid interfering with normal usage
- If Scroll Lock is accidentally turned ON, it will be automatically corrected within 2 seconds

### Error Handling
- All exceptions are logged using SLF4J for easy troubleshooting
- Invalid sleep interval values fall back to default (60 seconds)
- Encoding errors in log appender are silently handled to prevent infinite loops

### Performance
- Minimal CPU usage due to efficient sleep-based timing
- Log cache prevents memory issues with bounded queue (500 messages)
- Non-blocking design ensures responsive UI even during heavy logging

## License

This project is open source and available under the MIT License.

## Author

Developed by Colin

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
