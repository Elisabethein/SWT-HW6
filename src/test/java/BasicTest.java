import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.*;

public class BasicTest extends TestHelper {


    private String username = "123";
    private String password = "123";


    @Test
    public void titleExistsTest() {
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }


    @Test
    public void loginLogoutTest() {
        login(username, password);

        WebElement adminMenu = driver.findElement(By.id("menu"));
        WebElement firstMenuItem = adminMenu.findElement(By.cssSelector("li:first-child"));
        assertEquals("Admin", firstMenuItem.getText());

        logout();
    }


    @Test
    public void loginFalsePassword() {
        login(username, "1234");
        WebElement error = driver.findElement(By.id("notice"));
        assertEquals("Invalid user/password combination", error.getText());
    }

    @Test
    public void registerAndDeleteAnAccount() {
        register("test", "test");
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User test was successfully created.", notice.getText());
        deleteAccount("test");
    }

    @Test
    public void addProductsBooks(){
        login(username, password);
        addProductshelp("Test Product", "Test Description", "Books", "10");
        assertTrue(findProduct("Test Product"));
        deleteProduct("Test Product");
    }
    @Test
    public void addProductsSunglasses(){
        login(username, password);
        addProductshelp("Test Product 2", "Test Description", "Sunglasses", "10");
        assertTrue(findProduct("Test Product 2"));
        deleteProduct("Test Product 2");
    }
    @Test
    public void addProductsOther(){
        login(username, password);
        addProductshelp("Test Product 3", "Test Description", "Other", "10");
        assertTrue(findProduct("Test Product 3"));
        deleteProduct("Test Product 3");
    }
    @Test
    public void addProductsNegativePrice(){
        login(username, password);
        addProductshelp("Test Product 4", "Test Description", "Books", "-10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }
    @Test
    public void addAlreadyExistingProduct(){
        login(username, password);
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
        deleteProduct("Test Product Existing");
    }
    @Test
    public void addProductsWithEmptyFields(){
        login(username, password);
        addProductshelp("", "123", "Other", "123");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }

    @Test
    public void editProduct(){
        login(username, password);
        addProductshelp("Test Product 5", "Test Description", "Books", "10");
        editProducthelp("Test Product 5", "Test Product 6", "Test Description 2", "Books", "10");
        assertTrue(findProduct("Test Product 6"));
        deleteProduct("Test Product 6");
    }

    @Test
    public void deleteProduct(){
        login(username, password);
        addProductshelp("Test Product 7", "Test Description", "Books", "10");
        deleteProduct("Test Product 7");
        assertFalse(findProduct("Test Product 7"));
    }

    // FAIL 1 - too large price
    @Test
    public void addProductLargePrice() {
        login(username, password);
        addProductshelp("Test Product 8", "Test Description", "Books", "10000000");
        assertTrue(findProduct("Test Product 8"));
    }
    
}
