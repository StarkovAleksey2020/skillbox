package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MainPageSeleniumTests {

    private static WebDriver driver;

    @BeforeAll
    static void setup() {
        try {

            String filePath = "/home/alex/DISTR/selenium/chromedriver";
            System.setProperty("webdriver.chrome.driver", filePath);

            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--kiosk");
            options.setAcceptInsecureCerts(true);
            options.addArguments("test-type");

            String[] switches = {"--ignore-certificate-errors"};

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("chrome.switches", Arrays.asList(switches));
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            driver = new ChromeDriver(capabilities);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://localhost:8085");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            System.out.println("PAGE SOURCE : \n" + driver.getPageSource());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    void testMainPageSearchByQuery() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .waitElement("query")
                .setUpSearchToken("Randy")
                .pause()
                .waitElement("search")
                .submit()
                .pause();
        assertTrue(driver.getPageSource().contains("Randy and the Mob"));
    }

    @Test
    void testNavigateGenres() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointGenres")));

        driver.findElement(By.id("pointGenres")).click();

        assertTrue(driver.getPageSource().contains("Easy reading"));
    }

    @Test
    void testNavigateRecent() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointNew")));

        driver.findElement(By.id("pointNew")).click();

        assertTrue(driver.getPageSource().contains("from"));
    }

    @Test
    void testNavigatePopular() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointPopular")));

        driver.findElement(By.id("pointPopular")).click();

        assertTrue(driver.getPageSource().contains("Popular"));
    }

    @Test
    void testNavigateAuthors() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointAuthors")));

        driver.findElement(By.id("pointAuthors")).click();

        assertTrue(driver.getPageSource().contains("Authors"));
    }

    @Test
    void testNavigateBook() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointMain")));

        driver.findElement(By.id("pointMain")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div[1]/div[2]/div[1]/div/div/div[1]/div/div/div/strong/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/div[2]/div[1]/div/div/div[1]/div/div/div/strong/a")).click();

        assertTrue(driver.getPageSource().contains("Randy and the Mob"));
    }

    @Test
    void testNavigateTag() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointMain")));

        driver.findElement(By.id("pointMain")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div[2]/div[1]/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[1]/a")).click();

        assertTrue(driver.getPageSource().contains("Randy and the Mob"));
    }

    @Test
    void testNavigateGenresItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointGenres")));

        driver.findElement(By.id("pointGenres")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div/div[1]/div[5]/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div/div/div[1]/div[5]/a")).click();

        assertTrue(driver.getPageSource().contains("Ploy"));
    }

    @Test
    void testNavigateRecentItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointNew")));

        driver.findElement(By.id("pointNew")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/main/div/div[2]/div[1]/div[2]/strong/a")));
        driver.findElement(By.xpath("/html/body/div[1]/div/main/div/div[2]/div[1]/div[2]/strong/a")).click();

        assertTrue(driver.getPageSource().contains("Glengarry Glen Ross"));
    }

    @Test
    void testNavigatePopularItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointPopular")));

        driver.findElement(By.id("pointPopular")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div[2]/div[1]/div[1]/a/div[1]/span")));
        driver.findElement(By.xpath("/html/body/div/div/main/div/div[2]/div[1]/div[1]/a/div[1]/span")).click();

        assertTrue(driver.getPageSource().contains("Exit Smiling"));
    }

    @Test
    void testNavigateAuthorsItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointAuthors")));

        driver.findElement(By.id("pointAuthors")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div/div[2]/div/div[1]/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div/div/div[2]/div/div[1]/a")).click();

        assertTrue(driver.getPageSource().contains("Emerald Forest, The"));
    }

    @Test
    void testNavigateAuthorsItemBook() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("pointAuthors")));

        driver.findElement(By.id("pointAuthors")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div/div[2]/div/div[1]/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div/div/div[2]/div/div[1]/a")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div/div[1]/div[2]/strong/a")));
        driver.findElement(By.xpath("/html/body/div/div/main/div/div/div[1]/div[2]/strong/a")).click();

        assertTrue(driver.getPageSource().contains("Anabelle Baud"));
    }

    @Test
    void testSignIn() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/header/div[1]/div/div/div[3]/div[1]/a[3]")));

        driver.findElement(By.xpath("/html/body/header/div[1]/div/div/div[3]/div[1]/a[3]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/main/form/div/div[1]/div[2]/div/div[2]/label")));
        driver.findElement(By.xpath("/html/body/div/div[2]/main/form/div/div[1]/div[2]/div/div[2]/label")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mail\"]")));
        driver.findElement(By.xpath("//*[@id=\"mail\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"mail\"]")).sendKeys("test1@example.com");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"sendauth\"]")));
        driver.findElement(By.xpath("//*[@id=\"sendauth\"]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mailcode\"]")));
        driver.findElement(By.xpath("//*[@id=\"mailcode\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"mailcode\"]")).sendKeys("1234567");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"toComeInMail\"]")));
        driver.findElement(By.xpath("//*[@id=\"toComeInMail\"]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/div[1]/div/span")));
        assertTrue(driver.getPageSource().contains("Непрочитанные"));

    }

    @Test
    void testSignOut() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/header/div[1]/div/div/div[3]/div[1]/a[3]")));

        driver.findElement(By.xpath("/html/body/header/div[1]/div/div/div[3]/div[1]/a[3]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/main/form/div/div[1]/div[2]/div/div[2]/label")));
        driver.findElement(By.xpath("/html/body/div/div[2]/main/form/div/div[1]/div[2]/div/div[2]/label")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mail\"]")));
        driver.findElement(By.xpath("//*[@id=\"mail\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"mail\"]")).sendKeys("test1@example.com");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"sendauth\"]")));
        driver.findElement(By.xpath("//*[@id=\"sendauth\"]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mailcode\"]")));
        driver.findElement(By.xpath("//*[@id=\"mailcode\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"mailcode\"]")).sendKeys("1234567");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"toComeInMail\"]")));
        driver.findElement(By.xpath("//*[@id=\"toComeInMail\"]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/header/div[1]/div/div/div[3]/div/a[5]")));
        driver.findElement(By.xpath("/html/body/header/div[1]/div/div/div[3]/div/a[5]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/main/form/div/div[1]/div[1]/label")));

        assertTrue(driver.getPageSource().contains("Enter your e-mail or phone"));

    }
}