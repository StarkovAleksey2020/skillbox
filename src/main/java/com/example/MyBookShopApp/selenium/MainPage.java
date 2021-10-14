package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

    private String url = "https://localhost:8085";
    private WebDriver driver;
    private static WebDriverWait driverWait;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public MainPage callPage() {
        driver.get(url);
        return this;
    }

    public MainPage pause() throws InterruptedException {
        Thread.sleep(1000);
        return this;
    }

    public MainPage setUpSearchToken(String token) {
        WebElement element = driver.findElement(By.id("query"));
        element.sendKeys(token);
        return this;
    }

    public MainPage submit() {
        WebElement element = driver.findElement(By.id("search"));
        element.submit();
        return this;
    }

    public MainPage submitGenres() {
        WebElement element = driver.findElement(By.id("pointGenres"));
        element.submit();
        return this;
    }

    public MainPage submitElement(String elementId) {
        WebElement element = driver.findElement(By.id(elementId));
        element.submit();
        return this;
    }

    public MainPage waitElement(String elementId) {
        driverWait = new WebDriverWait(driver, 20);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));
        return this;
    }
}
