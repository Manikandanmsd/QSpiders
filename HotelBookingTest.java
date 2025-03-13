package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class HotelBookingTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver(); // No need to set system property if ChromeDriver is in PATH
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("file:///path/to/your/local/index.html");
    }

    @Test(priority = 1)
    public void userLoginAndBooking() {
        driver.findElement(By.id("user-username")).sendKeys("testuser");
        driver.findElement(By.id("user-password")).sendKeys("password");
        driver.findElement(By.xpath("//button[contains(text(),'Login as User')]")).click();
        
        Assert.assertTrue(driver.findElement(By.id("userDashboard")).isDisplayed(), "User Dashboard not displayed");
        
        WebElement searchBox = driver.findElement(By.id("searchHotel"));
        searchBox.sendKeys("Taj Hotel");
        driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();
        
        driver.findElement(By.xpath("//button[contains(text(),'Book Now')]")).click();
        
        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("john@example.com");
        driver.findElement(By.id("phone")).sendKeys("9876543210");
        driver.findElement(By.id("date")).sendKeys("2025-03-15");
        driver.findElement(By.id("days")).sendKeys("3");
        driver.findElement(By.xpath("//button[contains(text(),'Confirm Booking')]")).click();
        
        Assert.assertTrue(driver.findElement(By.id("bookingMessage")).getText().contains("Booking Successful"), "Booking failed");
    }

    @Test(priority = 2)
    public void adminLoginAndCancelBooking() {
        driver.findElement(By.xpath("//button[contains(text(),'Logout')]")).click();
        
        driver.findElement(By.id("admin-username")).sendKeys("admin1");
        driver.findElement(By.id("admin-password")).sendKeys("admin@123");
        driver.findElement(By.xpath("//button[contains(text(),'Login as Admin')]")).click();
        
        Assert.assertTrue(driver.findElement(By.id("adminDashboard")).isDisplayed(), "Admin Dashboard not displayed");
        
        WebElement bookingTable = driver.findElement(By.id("bookingTable"));
        Assert.assertTrue(bookingTable.getText().contains("John Doe"), "Booking not found");
        
        driver.findElement(By.xpath("//tr[td[contains(text(),'John Doe')]]//button[contains(text(),'Cancel')]")).click();
        
        Assert.assertFalse(bookingTable.getText().contains("John Doe"), "Booking was not cancelled");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
