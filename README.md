# PixelForge Studio Pro v4.0

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36)
![License](https://img.shields.io/badge/License-MIT-green)

**PixelForge Studio Pro** is a professional-grade image-to-ASCII art converter built with JavaFX. It features a high-performance rendering engine designed for real-time creativity and ultra-HD exports.

## 🚀 Key Features

- **🌈 Full-Color ASCII Art**: Toggle between classic Matrix green and vibrant, per-character coloring based on original image data.
- **⚡ Zero-Lag Engine**: 
    - **Multi-threaded Rendering**: Heavy processing is offloaded to background threads so the UI never freezes.
    - **Smart Debouncing**: Resolution updates only trigger when you stop moving the slider, saving resources.
- **📱 Adaptive Viewport**: The art automatically scales to fit your window size. Resize the app, and the art adapts instantly!
- **💎 Ultra-HD Export**: Download feature triggers a dedicated high-res render pass, ensuring your saved PNG matches the **original image resolution** exactly.
- **🎨 Modern Pro UI**: A sleek, dark-themed interface with smooth animations and interactive feedback.

## 🛠️ Tech Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 17
- **Rendering**: Multi-threaded Canvas Engine
- **Build Tool**: Maven

## 🏁 Getting Started

### Prerequisites

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or higher.
- [Apache Maven](https://maven.apache.org/download.cgi).

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/vasu-devs/PixelForge.git
   cd PixelForge
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

### Running the App
```bash
mvn javafx:run
```

## 📖 Usage Instructions

1. **Upload**: Click the green "Upload" button to select a photo.
2. **Interact**: Use the **Resolution** slider to change detail levels. The UI will remain smooth during adjustments.
3. **Customize**: Toggle **Color Mode** for vibrant results.
4. **Export**: Click the blue "Download" button to save a high-res PNG of your masterpiece at the original photo's dimensions.

---
Built with 💚 and performance in mind by Vasu-Devs
