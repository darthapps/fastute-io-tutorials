# Java 21 Virtual Threads Tutorial

Complete working examples for the [Mastering Java 21 Virtual Threads](https://fastute.io/j21-vthreads-final.html) article.

## 📋 Prerequisites

- **Java 21 or later** (required for virtual threads)
- **Maven 3.6+**
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone git@github.com:darthapps/fastute-io-tutorials.git
cd fastute-io-tutorials/java-21-virtual-threads
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Examples

```bash
# Basic Virtual Thread Examples
mvn exec:java -Dexec.mainClass="io.fastute.tutorials.virtualthreads.BasicVirtualThreadExample"

# Virtual Thread Executor Examples
mvn exec:java -Dexec.mainClass="io.fastute.tutorials.virtualthreads.VirtualThreadExecutorExample"

# Exception Handling Examples
mvn exec:java -Dexec.mainClass="io.fastute.tutorials.virtualthreads.ExceptionHandlingExample"

# Scalability Demonstration
mvn exec:java -Dexec.mainClass="io.fastute.tutorials.virtualthreads.ScalabilityDemo"
```

### 4. Run the Tests

```bash
mvn test
```

## 📚 What's Included

### Example Classes

#### 1. **BasicVirtualThreadExample.java**
Demonstrates the fundamentals of virtual threads:
- Creating platform threads (traditional approach)
- Creating virtual threads using `Thread.startVirtualThread()`
- Creating named virtual threads using `Thread.Builder`
- Memory comparison between platform and virtual threads

**Key Learning Points:**
- Virtual threads are lightweight and JVM-managed
- Simple API for creating virtual threads
- Backward compatible with existing Thread API

#### 2. **VirtualThreadExecutorExample.java**
Shows how to use ExecutorService with virtual threads:
- Creating a virtual thread executor
- Submitting multiple tasks
- Comparing platform thread pools vs virtual thread executors
- Proper executor shutdown

**Key Learning Points:**
- `Executors.newVirtualThreadPerTaskExecutor()` for unlimited concurrency
- No need to limit thread pool size with virtual threads
- Automatic resource management with try-with-resources

#### 3. **ExceptionHandlingExample.java**
Covers exception handling patterns:
- Try-catch blocks in virtual threads
- Uncaught exception handlers
- Resource cleanup with try-finally
- Thread interruption handling

**Key Learning Points:**
- Virtual threads handle exceptions like platform threads
- Proper exception handling prevents silent failures
- Always clean up resources in finally blocks

#### 4. **ScalabilityDemo.java**
Demonstrates the scalability advantages:
- Creating thousands of virtual threads
- Comparing performance with platform thread pools
- Memory efficiency measurements
- Concurrent execution patterns

**Key Learning Points:**
- Virtual threads enable massive concurrency
- Much lower memory footprint than platform threads
- Ideal for I/O-bound workloads

## 🧪 Unit Tests

Comprehensive test coverage for all examples:

- **BasicVirtualThreadExampleTest**: Tests virtual thread creation and characteristics
- **VirtualThreadExecutorExampleTest**: Tests executor behavior and scalability
- **ExceptionHandlingExampleTest**: Tests exception handling patterns
- **ScalabilityDemoTest**: Tests performance and memory efficiency

### Running Specific Tests

```bash
# Run a specific test class
mvn test -Dtest=BasicVirtualThreadExampleTest

# Run a specific test method
mvn test -Dtest=BasicVirtualThreadExampleTest#testCreateVirtualThread

# Run all tests with verbose output
mvn test -X
```

## 📊 Performance Benchmarks

The `ScalabilityDemo` class includes performance comparisons:

### Expected Results (approximate):

| Metric | Platform Threads (Pool of 100) | Virtual Threads |
|--------|-------------------------------|-----------------|
| 1,000 tasks (100ms I/O each) | ~1,000ms | ~100-200ms |
| Memory for 10,000 threads | ~10 GB | ~100-500 MB |
| Thread creation time | ~1-2 ms per thread | ~1-10 μs per thread |

**Note**: Actual results vary based on hardware and JVM configuration.

## 🎯 Key Concepts Demonstrated

### 1. Virtual Thread Creation

```java
// Simple creation
Thread vt = Thread.startVirtualThread(() -> {
    System.out.println("Hello from virtual thread!");
});
vt.join();

// With builder
Thread vt2 = Thread.builder()
    .virtual()
    .name("my-thread", 1)
    .start(() -> {
        // Your code here
    });
```

### 2. Virtual Thread Executor

```java
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10000; i++) {
        executor.submit(() -> {
            // Handle I/O-bound task
        });
    }
} // Auto-shutdown
```

### 3. Exception Handling

```java
Thread vt = Thread.startVirtualThread(() -> {
    try {
        // Your code
    } catch (Exception e) {
        // Handle exception
    } finally {
        // Cleanup
    }
});
```

## 🔍 Troubleshooting

### Java Version Issues

If you get compilation errors, ensure you're using Java 21+:

```bash
java -version
# Should show: openjdk version "21" or higher

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/jdk-21
```

### Maven Issues

If Maven can't find Java 21:

```bash
# Use Maven wrapper with specific Java version
./mvnw clean install -Djava.home=/path/to/jdk-21
```

### IDE Setup

**IntelliJ IDEA:**
1. File → Project Structure → Project SDK → Select JDK 21
2. File → Settings → Build, Execution, Deployment → Compiler → Java Compiler → Set to 21

**Eclipse:**
1. Window → Preferences → Java → Installed JREs → Add JDK 21
2. Right-click project → Properties → Java Compiler → Set to 21

**VS Code:**
1. Install "Extension Pack for Java"
2. Set `java.configuration.runtimes` in settings.json to point to JDK 21

## 📖 Related Resources

- [FastuteIO Article: Mastering Java 21 Virtual Threads](https://fastute.io/j21-vthreads-final.html)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [Project Loom Documentation](https://wiki.openjdk.org/display/loom)
- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnotes.html)

## 🤝 Contributing

Found an issue or want to improve the examples? Contributions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -am 'Add improvement'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Open a Pull Request

## 📝 License

This tutorial code is provided as-is for educational purposes. Feel free to use, modify, and distribute.

## 💬 Questions?

- Visit [FastuteIO](https://fastute.io) for more tutorials
- Follow us on [X/Twitter](https://twitter.com/fastuteio)
- Open an issue in this repository

---

**Happy Learning! 🚀**

*Part of the FastuteIO Tutorial Series*
