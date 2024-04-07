import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.*;

/**
 * This work is done by:
 * C14544 Ralf Animägi ralfanimagi@gmail.com
 * B911205 Elisabet Hein elisabet.hein@ut.ee
 */
public class BasicTest extends TestHelper {


    private String username = "123";
    private String password = "123";

    // Verify that the title of the page is "ST Online Store"
    @Test
    public void titleExistsTest() {
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle);
    }

    // Verify that user can log in and log out successfully
    @Test
    public void loginLogoutTest() {
        login(username, password);
        WebElement adminMenu = driver.findElement(By.id("menu"));
        WebElement firstMenuItem = adminMenu.findElement(By.cssSelector("li:first-child"));
        assertEquals("Admin", firstMenuItem.getText());
        logout();
    }

    // Verify that logging in with false password displays an error message
    @Test
    public void loginFalsePassword() {
        login(username, "1234");
        WebElement error = driver.findElement(By.id("notice"));
        assertEquals("Invalid user/password combination", error.getText());
    }

    // Verify that user can register successfully (erasing data after)
    @Test
    public void registerAnAccount() {
        register("test", "test");
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User test was successfully created.", notice.getText());
        deleteAccount("test");
    }

    // Negative test 1: Verify we cannot create a user with missing input
    @Test
    public void registerAnAccountWithMissingField(){
        register("222", "");
        assertTrue(isElementPresent(By.id("error_explanation")));
    }
    // Verify that user can be deleted successfully
    @Test
    public void deleteAnAccount() {
        register("test", "test");
        deleteAccount("test");
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User was successfully deleted.", notice.getText());
    }

    // Verify that products can be added under the category "Books"
    @Test
    public void addProductsBooks() {
        login(username, password);
        addProductshelp("Test Product", "Test Description", "Books", "10");
        assertTrue(findProduct("Test Product"));
        deleteProduct("Test Product");
    }

    // Verify that products can be added under the category "Sunglasses"
    @Test
    public void addProductsSunglasses() {
        login(username, password);
        addProductshelp("Test Product 2", "Test Description", "Sunglasses", "10");
        assertTrue(findProduct("Test Product 2"));
        deleteProduct("Test Product 2");
    }

    // Verify that products can be added under the category "Other"
    @Test
    public void addProductsOther() {
        login(username, password);
        addProductshelp("Test Product 3", "Test Description", "Other", "10");
        assertTrue(findProduct("Test Product 3"));
        deleteProduct("Test Product 3");
    }

    // Negative test 2: Verify that adding a product with a negative price displays an error message
    @Test
    public void addProductsNegativePrice() {
        login(username, password);
        addProductshelp("Test Product 4", "Test Description", "Books", "-10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }

    // Negative test 3: Verify that adding an already existing product displays an error message
    @Test
    public void addAlreadyExistingProduct() {
        login(username, password);
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        addProductshelp("Test Product Existing", "Test Description", "Other", "10");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
        deleteProduct("Test Product Existing");
    }

    // Negative test 4: Verify that adding a product with empty fields displays an error message
    @Test
    public void addProductsWithEmptyFields() {
        login(username, password);
        addProductshelp("", "123", "Other", "123");
        WebElement error = driver.findElement(By.id("error_explanation"));
        assertNotNull(error);
    }

    // Verify that a product can be edited successfully
    @Test
    public void editProduct() {
        login(username, password);
        addProductshelp("Test Product 5", "Test Description", "Books", "10");
        editProducthelp("Test Product 5", "Test Product 6", "Test Description 2", "Books", "10");
        assertTrue(findProduct("Test Product 6"));
        deleteProduct("Test Product 6");
    }

    // Verify that a product can be deleted successfully
    @Test
    public void deleteProduct() {
        login(username, password);
        addProductshelp("Test Product 7", "Test Description", "Books", "10");
        deleteProduct("Test Product 7");
        assertFalse(findProduct("Test Product 7"));
    }

    // FAIL 1 - Verify that adding a product with a large price still adds the product
    @Test
    public void addProductLargePrice() {
        login(username, password);
        addProductshelp("Test Product 8", "Test Description", "Books", "10000000");
        assertTrue(findProduct("Test Product 8"));
    }

    // End User tests
    // Verify that products can be added to the cart
    @Test
    public void addProductsToCart() {
        addToCart("B45593 Sunglasses");
        assertTrue(isElementPresent(By.id("checkout_button")));
    }

    // Verify that the quantity of a product can be increased in the cart
    @Test
    public void increaseProductQuantity() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        assertEquals(getQuantity("B45593 Sunglasses"), "2×");
    }

    // Verify that the quantity of a product can be decreased in the cart
    @Test
    public void decreaseProductQuantity() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        decreaseQuantity("B45593 Sunglasses");
        assertEquals(getQuantity("B45593 Sunglasses"), "2×");
    }

    // Verify that a product can be deleted from the cart
    @Test
    public void deleteProductFromCart() {
        addToCart("B45593 Sunglasses");
        deleteItem("B45593 Sunglasses");
        assertFalse(isElementPresent(By.id("checkout_button")));
    }

    // Verify that multiple products can be deleted from the cart
    @Test
    public void deleteMultipleProductFromCart() {
        addToCart("B45593 Sunglasses");
        addToCart("Sunglasses 2AR");
        deleteItem("B45593 Sunglasses");
        assertEquals(getCartLength(), 1);
    }

    // Verify that the cart can be emptied successfully
    @Test
    public void deleteCart() {
        addToCart("B45593 Sunglasses");
        addToCart("Sunglasses 2AR");
        emptyCart();
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Cart successfully deleted.", notice.getText());
    }

    // FAIL 2: Verify that items can be purchased successfully with correct price
    @Test
    public void purchaseItems() {
        addToCart("B45593 Sunglasses");
        increaseQuantity("B45593 Sunglasses");
        checkout("name", "address", "email.com", "Credit card");
        assertEquals(driver.findElement(By.className("total_cell")).getText(), "€52.00");
    }

    // Negative test 5: Verify that items cannot be purchased with missing fields
    @Test
    public void purchaseItemsWithMissingFields() {
        addToCart("B45593 Sunglasses");
        checkout("", "", "", "Credit card");
        assertTrue(isElementPresent(By.id("error_explanation")));
    }

    // Verify that search functionality works correctly
    @Test
    public void search() {
        search("sun");
        assertTrue(iterOverItems("sun"));
    }

    // Verify that selecting the category "Books" displays books
    @Test
    public void selectBooks() {
        goToPage("Books");
        assertTrue(checkCategories("Books"));
    }

    // Verify that selecting the category "Sunglasses" displays sunglasses
    @Test
    public void selectSunglasses() {
        goToPage("Sunglasses");
        assertTrue(checkCategories("Sunglasses"));
    }

    // FAIL 3: Verify that selecting the category "Other" displays other products
    @Test
    public void selectOther() {
        goToPage("Other");
        assertTrue(checkCategories("Other"));
    }
}
