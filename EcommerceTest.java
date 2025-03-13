import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class EcommerceTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver(); // ChromeDriver path is now managed by system environment variables
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("file:///path/to/your/html/file.html"); // Update with the correct local file path
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

    @Test(priority = 3)
    public void updateCartAndVerifyTotal() {
        WebElement quantityInput = driver.findElement(By.xpath("//div[@id='cart']//input[@type='number']"));
        quantityInput.clear();
        quantityInput.sendKeys("2");

        WebElement grandTotal = driver.findElement(By.id("cart-total"));
        Assert.assertTrue(grandTotal.getText().contains("Rs. 160000"), "Grand total should be updated.");
    }

    @Test(priority = 4)
    public void removeProductAndVerifyTotal() {
        driver.findElement(By.xpath("//div[@id='cart']//button[text()='Remove']")).click();
        WebElement grandTotal = driver.findElement(By.id("cart-total"));
        Assert.assertTrue(grandTotal.getText().contains("Rs. 0"), "Grand total should be Rs. 0 after removal.");
    }

    @Test(priority = 5)
    public void completeCheckout() {
        driver.findElement(By.xpath("//button[text()='Checkout']")).click();
        
        WebElement nameInput = driver.findElement(By.id("billingName"));
        WebElement addressInput = driver.findElement(By.id("billingAddress"));
        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        
        nameInput.sendKeys("John Doe");
        addressInput.sendKeys("123 Test Street, India");
        paymentMethod.sendKeys("Credit Card");

        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        
        WebElement successMessage = driver.findElement(By.id("orderSuccess"));
        Assert.assertTrue(successMessage.isDisplayed(), "Order should be placed successfully.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
