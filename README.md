# GiftCard8-Assesment

🚀 Modern Android Todo App with Jetpack Compose, Clean Architecture & Real API Integration

A production-ready Android application showcasing modern development practices with Jetpack Compose, MVVM architecture, Room database, Retrofit API integration, and offline-first design. Features real-time task management with DummyJSON API, secure authentication, and responsive Material Design 3 UI.

 Technologies: Kotlin • Jetpack Compose • Hilt • Room • Retrofit • Clean Architecture

 **Repository Topics/Tags**

android
kotlin
jetpack-compose
clean-architecture
mvvm
hilt
room-database
retrofit
material-design
dummyjson
offline-first
technical-assessment
todo-app
modern-android
coroutines
flow

### 🔐 **Authentication**
- Secure JWT-based login with DummyJSON API
- Persistent user sessions with encrypted storage
- Automatic token refresh and validation
- Seamless logout functionality

### ✅ **Task Management**
- Create, read, update, and delete tasks
- Real-time task completion toggling
- Optimistic UI updates for instant feedback
- Task sorting by creation date

### 🌐 **Network & Offline Support**
- Offline-first architecture with Room database
- Automatic data synchronization when online
- Network state monitoring and error handling
- Graceful degradation without internet

### 🎨 **Modern UI/UX**
- Material Design 3 components
- Responsive layouts for all screen sizes
- Dark/Light theme support
- Smooth animations and transitions
- Loading states and progress indicators
- Toast notifications for user feedback

## 🏛️ Architecture Overview

This project implements **Clean Architecture** principles with clear separation of concerns:

 **Project Structure**

app/src/main/java/com/todoapp/
├── 🎨 presentation/           # UI Layer
│   ├── auth/                  # Authentication screens
│   ├── tasks/                 # Task management screens
│   ├── navigation/            # Navigation setup
│   ├── theme/                 # App theming
│   └── common/                # Shared UI components
├── 🏢 domain/                 # Business Logic
│   ├── model/                 # Domain entities
│   ├── repository/            # Repository interfaces
│             
├── 💾 data/                   # Data Layer
│   ├── local/                 # Room database
│   ├── remote/                # Retrofit API
│   └── repository/            # Repository implementations
├── 🔧 di/                     # Dependency Injection
└── 🛠️ utils/                  # Utilities & Helpers
