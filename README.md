# FastuteIO Tutorials

Welcome to the FastuteIO Tutorials repository! This repository contains complete, working code examples for all FastuteIO articles.

## 📚 Available Tutorials

### Java 21 Virtual Threads
**Location**: `/java-21-virtual-threads`  
**Article**: [Mastering Java 21 Virtual Threads](https://fastute.io/j21-vthreads-final.html)  
**Topics Covered**:
- Basic virtual thread creation
- Virtual thread executors
- Exception handling patterns
- Scalability demonstrations
- Performance benchmarks

**Quick Start**:
```bash
cd java-21-virtual-threads
mvn clean test
```

## 🎯 About This Repository

This repository follows the Baeldung model of providing complete, runnable code examples for every tutorial article published on FastuteIO. Each tutorial includes:

- ✅ **Complete Maven/Gradle projects** - Ready to build and run
- ✅ **Comprehensive unit tests** - Verify all examples work correctly
- ✅ **Detailed README files** - Step-by-step instructions
- ✅ **Best practices** - Production-ready code patterns
- ✅ **Well-documented code** - Clear explanations and comments

## 🚀 Getting Started

### Prerequisites

- **Java 21+** (for Java 21 tutorials)
- **Maven 3.6+** or **Gradle 7+**
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Clone the Repository

```bash
git clone git@github.com:darthapps/fastute-io-tutorials.git
cd fastute-io-tutorials
```

### Navigate to a Tutorial

```bash
cd java-21-virtual-threads
```

### Build and Run

```bash
# Build the project
mvn clean install

# Run tests
mvn test

# Run a specific example
mvn exec:java -Dexec.mainClass="io.fastute.tutorials.virtualthreads.BasicVirtualThreadExample"
```

## 📖 Tutorial Structure

Each tutorial follows this structure:

```
tutorial-name/
├── pom.xml                          # Maven configuration
├── README.md                        # Tutorial-specific documentation
├── src/
│   ├── main/
│   │   └── java/                    # Example code
│   │       └── io/fastute/tutorials/
│   └── test/
│       └── java/                    # Unit tests
│           └── io/fastute/tutorials/
```

## 🤝 Contributing

We welcome contributions! If you find issues or want to improve examples:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Make your changes with tests
4. Commit your changes (`git commit -am 'Add improvement'`)
5. Push to the branch (`git push origin feature/improvement`)
6. Open a Pull Request

### Contribution Guidelines

- All code must compile and pass tests
- Follow existing code style and conventions
- Add unit tests for new examples
- Update README files as needed
- Keep examples simple and focused

## 📝 License

This tutorial code is provided for educational purposes. Feel free to use, modify, and distribute.

## 🔗 Links

- **FastuteIO Website**: [https://fastute.io](https://fastute.io)
- **Twitter/X**: [@fastuteio](https://twitter.com/fastuteio)
- **GitHub**: [darthapps/fastute-io-tutorials](https://github.com/darthapps/fastute-io-tutorials)

## 💬 Support

- **Issues**: Open an issue in this repository
- **Questions**: Comment on the related article
- **Twitter**: Tweet us [@fastuteio](https://twitter.com/fastuteio)

---

**Happy Learning! 🚀**

*Maintained by the FastuteIO Team*
