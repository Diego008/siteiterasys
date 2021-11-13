package site;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class buscaSeleniumPuro {
    String url = "https://iterasys.com.br";

    private static WebDriver driver;
    private By botaoSignOut = By.cssSelector("a.foto.dropdown-toggle img");

    @BeforeAll
    public static void iniciar(){
        System.setProperty("webdriver.chrome.driver", "drivers/chrome/95/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void finalizar(){
        driver.quit();
    }

    @Test
    public void buscaCurso(){
        driver.get(url);
        driver.findElement(By.id("searchtext")).sendKeys("Mantis" + Keys.ENTER);
        driver.findElement(By.cssSelector("span.comprar")).click();
        Assert.assertEquals(driver.findElement(By.cssSelector("span.item-title")).getText(), "Mantis");
        Assert.assertEquals("R$ 59,99", driver.findElement(By.cssSelector("span.new-price")).getText());
    }

    //Massa de Teste
    @ParameterizedTest
    @CsvFileSource(resources = "/massaLogin.csv", numLinesToSkip = 1, delimiter = ';')
    public void massaTesteLogin(String teste, String email, String senha, String resultado){
        driver.get(url);
        System.out.println("Teste: " +teste);

        driver.findElement(By.cssSelector("li.active.login_header")).click();
        driver.findElement(By.xpath("//div[@class='container login']/div/div/form/div[1]/input")).sendKeys(email);
        driver.findElement(By.xpath("//div[@class='container login']/div/div/form/div[2]/input")).sendKeys(senha);

        driver.findElement(By.id("btn_login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5);

        if(resultado.equals("positivo")){
            Assert.assertTrue(true);
            wait.until(ExpectedConditions.visibilityOfElementLocated(botaoSignOut));
            driver.findElement(botaoSignOut).click();
            // driver.findElement(By.cssSelector("a.foto.dropdown-toggle img")).click();
            driver.findElement(By.cssSelector("li.li_logout a")).click();
        }else{
            Assert.assertEquals(driver.findElement(By.xpath("//div[@class='alert alert-danger alert-block']/h4")).getText(), "Dados de acesso incorretos!");
        }

    }

}
