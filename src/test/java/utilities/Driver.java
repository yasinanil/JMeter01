package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class Driver {

    // ThreadLocal to maintain a separate WebDriver instance per thread
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private Driver() {
    }

    public static WebDriver getDriver() {
        // If no WebDriver instance is assigned to the current thread, create a new one
        if (driverThread.get() == null) {
            String browser = ConfigReader.getProperty("browser");

            switch (browser) {
                case "edge":
                    driverThread.set(new EdgeDriver());
                    break;
                case "firefox":
                    driverThread.set(new FirefoxDriver());
                    break;
                case "headless":
                    driverThread.set(new ChromeDriver(new ChromeOptions().addArguments("--headless")));
                    break;
                default:
                    driverThread.set(new ChromeDriver());
            }

            // WebDriver configuration common for all instances
            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

        // Return the WebDriver instance specific to the current thread
        return driverThread.get();
    }


    public static WebDriver getRemoteDriver() {
        // If no WebDriver instance is assigned to the current thread, create a new one
        if (driverThread.get() == null) {

            // Configure desired capabilities for remote WebDriver.
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("browserName", "chrome"); // Specify Chrome as the browser.
            capabilities.setCapability("platformName", "Windows 11"); // Set the platform to Windows 11.

            try {
                driverThread.set(new RemoteWebDriver(new URL("http://localhost:4444"), capabilities));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            // WebDriver configuration common for all instances
            getDriver().manage().window().maximize();
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

        // Return the WebDriver instance specific to the current thread
        return driverThread.get();
    }

    public static void closeDriver() {
        // Quit and remove WebDriver instance for the current thread
        if (driverThread.get() != null) {
            try {
                Thread.sleep(3000); // Optional sleep, can be removed if not needed
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            driverThread.get().quit();
            driverThread.remove(); // Remove instance to prevent memory leaks
        }
    }


}
