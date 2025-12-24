# PixelForge Studio

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36)
![License](https://img.shields.io/badge/License-MIT-green)

**PixelForge Studio** is a high-performance, aesthetically pleasing image-to-ASCII art converter built with JavaFX. It transforms your images into detailed ASCII representations with a sleek "Matrix-style" dark interface.

## 🚀 Features

- **High-Quality Conversion**: Uses a detailed character density map for superior image fidelity.
- **Aspect Ratio Correction**: Automatically adjusts for the non-square nature of terminal fonts.
- **Matrix Theme**: Immersive dark mode with neon green text.
- **Adjustable Resolution**: Control the output detail with a real-time slider.
- **Download Art**: Save your ASCII creations as high-quality `.png` files for sharing.
- **Cross-Platform**: Runs anywhere Java is supported.

## 🛠️ Tech Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 17
- **Build Tool**: Maven
- **Packaging**: Maven Shade Plugin (Fat JAR)

## 🏁 Getting Started

### Prerequisites

- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/downloads/#java17) or higher.
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

## 跑 Usage

### Running with Maven
You can run the application directly using the JavaFX Maven plugin:
```bash
mvn javafx:run
```

### Running the Executable JAR
After building, a standalone JAR file is generated in the `target` directory:
```bash
java -jar target/pixelforge-1.0-SNAPSHOT.jar
```

### How to Use
1. Click the **Upload** button to select an image (JPG, PNG, BMP, GIF).
2. Use the **Resolution** slider to adjust the detail level in real-time.
3. Click the **Download** button to save your masterpiece as a PNG image.
4. View the generated ASCII art in the main display area.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
