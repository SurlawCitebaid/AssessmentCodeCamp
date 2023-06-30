package Tests;

import io.restassured.internal.common.assertion.Assertion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PizzaTests {
    WebDriver driver;

    @BeforeEach
    public void setUpDriverandOpenBrowser(){
        setUpDriver();
        driver.get("https://d3ovkzfkbrwp1z.cloudfront.net/#/");
    }
    @Test
    public void getWebsiteTest(){
        driver.get("https://d3ovkzfkbrwp1z.cloudfront.net/#/");
    }

    @Test
    public void testMandatoryErrorMsgsTest(){

        WebElement contactButton = driver.findElement(By.cssSelector("[aria-label=contact]"));
        contactButton.findElement(By.className("v-btn__content")).click();

        //Plan to make a class for form
        WebElement submitButton = driver.findElement(By.cssSelector("[aria-label=submit]"));
        submitButton.findElement(By.className("v-btn__content")).click();

        boolean wasFound = driver.findElement(By.id("forename-err")).isDisplayed();
        if(wasFound){
            System.out.println("Found it");
        }

        String foreNameErrorText = driver.findElement(By.id("forename-err")).getText();
        String emailErrorText = driver.findElement(By.id("email-err")).getText();
        String messageErrorText = driver.findElement(By.id("message-err")).getText();

        Assertions.assertEquals("Forename is required",foreNameErrorText);
        Assertions.assertEquals("Email is required",emailErrorText);
        Assertions.assertEquals("Message is required",messageErrorText);

        //Check through the fields to make sure they don't exist anymore
        driver.findElement(By.id("forename")).sendKeys("Dan");
        Assertions.assertEquals(false,isElementFound("forename-err"));

        driver.findElement(By.id("email")).sendKeys("dan@abc.com");
        Assertions.assertEquals(false,isElementFound("email-err"));

        driver.findElement(By.id("message")).sendKeys("Nice Pizza");
        Assertions.assertEquals(false,isElementFound("message-err"));
    }

    @Test
    public void menuItemRatingsTest(){
        WebElement contactButton = driver.findElement(By.cssSelector("[aria-label=menu]"));
        contactButton.findElement(By.className("v-btn__content")).click();

        //new WebDriverWait(driver, Duration.ofSeconds(2));

        //CLick on drinks tab
        List<WebElement> tabElements = driver.findElements(By.cssSelector("[role=tab]"));
        tabElements.get(2).click();

        //Do this so it doesn't find the other 0 rated item in the other tab
        WebElement drinksTab = driver.findElement(By.className("drinks-tab"));
        //WebElement drink = driver.findElement(By.cssSelector(".drink"));
        WebElement starRating = drinksTab.findElement(By.cssSelector(".rating-0"));

        try{
            starRating.findElement(By.cssSelector("[aria-label='Rating 3 of 5']")).click();
        }
        catch (ElementNotInteractableException error){
            //Item not interactable you can't click on it
        }

        //Not sure if I had to grab it again or the webelement updates dynamically
//        WebElement drinksTab2 = driver.findElement(By.className("drinks-tab"));
//        WebElement drink2 = driver.findElement(By.cssSelector(".drink"));
        WebElement starRating2 = drinksTab.findElement(By.cssSelector(".rating-0"));


        //Before and after comparison of the same element because we find by rating of 0 in the drinks tab
        //Wanted to compare the drink name was same that it had not changed and it was still 0
        Assertions.assertEquals(starRating2,starRating);
    }

    @AfterEach
    public void quitDriver(){
        //driver.quit();
    }

    private boolean isElementFound(String errorID){
        //write a function for if the element is found
        try{
            driver.findElement(By.id(errorID)).getText();
        }
        catch (NoSuchElementException error){
            return false;
        }
        return true;
    }

    private void setUpDriver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }
    //
}
