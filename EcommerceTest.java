package testing;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Assess1 
{
	WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver(); // ChromeDriver path is now managed by system environment variables
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("file:///D:\\Prodapt/ecom.html"); // Update with the correct local file path
    }

    @Test(priority = 1)
    public void searchAndAddProduct() {
        WebElement searchInput = driver.findElement(By.id("searchInput"));
        searchInput.sendKeys("Laptop");

        driver.findElement(By.xpath("//button[text()='Search']")).click();

        WebElement product = driver.findElement(By.xpath("//div[@data-name='Laptop']"));
        Assert.assertTrue(product.isDisplayed(), "Laptop should be visible after search.");

        product.findElement(By.xpath(".//button[text()='Add to Cart']")).click();
    }

    @Test(priority = 2)
    public void verifyProductInCart() {
        WebElement cartItem = driver.findElement(By.xpath("//div[@id='cart']//p[contains(text(),'Laptop')]"));
        Assert.assertTrue(cartItem.isDisplayed(), "Laptop should be in the cart.");
    }

    @Test(priority = 3) // Ensure this runs after adding products
    public void updateCartAndVerifyTotal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Locate quantity input field (modify XPath if needed)
        WebElement quantityInput = driver.findElement(By.xpath("//div[@id='cart']//input[@type='number']"));
        
        // Clear and update quantity
        quantityInput.clear();
        quantityInput.sendKeys("2");
        
        // Trigger update (simulate Enter key to ensure JavaScript triggers total update)
        quantityInput.sendKeys(Keys.RETURN);

        // Wait until Grand Total updates
        WebElement grandTotal = driver.findElement(By.id("cart-total"));
        boolean isTotalUpdated = wait.until(ExpectedConditions.textToBePresentInElement(grandTotal, "Grand Total: Rs."));

        // Verify Grand Total is updated dynamically
        Assert.assertTrue(isTotalUpdated, "Grand total should be updated dynamically.");
    }

    @Test(priority = 5)
    public void removeProductAndVerifyTotal() {
        driver.findElement(By.xpath("//div[@id='cart']//button[text()='Remove']")).click();
        WebElement grandTotal = driver.findElement(By.id("cart-total"));
        Assert.assertTrue(grandTotal.getText().contains("Rs. 0"), "Grand total should be Rs. 0 after removal.");
    }

    @Test(priority = 4) // Ensures checkout happens before product removal
    public void completeCheckout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click on Checkout button
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Checkout']")));
        checkoutButton.click();

        // Handle unexpected alert (e.g., "Proceeding to checkout...")
        try {
            wait.until(ExpectedConditions.alertIsPresent()); // Wait for alert
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert Text: " + alert.getText()); // Log the alert message
            alert.accept(); // Click "OK" to dismiss
        } catch (NoAlertPresentException e) {
            System.out.println("No alert found, proceeding...");
        }

        // Fill out checkout details
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billingName")));
        WebElement addressInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billingAddress")));
        WebElement paymentMethod = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("paymentMethod")));

        nameInput.sendKeys("John Doe");
        addressInput.sendKeys("123 Test Street, India");
        paymentMethod.sendKeys("Credit Card");

        // Click "Place Order" button
        WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']")));
        placeOrderButton.click();

        // Handle the success alert (e.g., "Order Placed Successfully!")
        try {
            wait.until(ExpectedConditions.alertIsPresent()); // Wait for alert
            Alert orderAlert = driver.switchTo().alert();
            System.out.println("Order Alert Text: " + orderAlert.getText()); // Log the message
            orderAlert.accept(); // Click "OK" to dismiss
        } catch (NoAlertPresentException e) {
            System.out.println("No order alert found, proceeding...");
        }

        // Verify success message appears
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderSuccess")));
        Assert.assertTrue(successMessage.isDisplayed(), "Order should be placed successfully.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
