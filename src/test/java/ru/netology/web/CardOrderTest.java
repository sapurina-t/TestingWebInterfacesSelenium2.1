package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {

    WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendTheForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendWithAHyphen() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Имя Фамилия-Фамилия");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenInitialsLatin() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Name Surname");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenInitialsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenNotEnoughDigits() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+7901234567");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenMoreDigits() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+790123456789");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenPhoneEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldTrowUpErrorWhenCheckboxEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79012345678");
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim();
        assertEquals(expected, actual);
    }
}
