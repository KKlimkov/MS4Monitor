import org.junit.jupiter.api.*;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class TestMonitor {

    public static WindowsDriver driver1 = null;
    public static WindowsDriver driver2 = null;

    public static String GetProcessExistence(String ProcessName, Boolean Ex) throws IOException {
        String err;
        String line = "";
        System.out.println("Start Search Process:  " + ProcessName);
        Process p1 = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq " + ProcessName + ".exe\"");
        InputStream stderr = p1.getErrorStream();
        InputStream stdout = p1.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, "Cp866"));
        BufferedReader error = new BufferedReader(new InputStreamReader(stderr, "Cp866"));
        String Out = null;
        while ((err = error.readLine()) != null) {
            System.out.println(err);
        }
        while ((line = reader.readLine()) != null) {
            Out = line;
            System.out.println(line);
            if (Ex) {
                assertTrue(!line.contains("Задачи, отвечающие заданным критериям, отсутствуют"));
            } else
            {assertTrue(line.contains("Задачи, отвечающие заданным критериям, отсутствуют"));};
        }

        return Out;
    }

    @BeforeAll
    public static void setUp() {
/*
        DesiredCapabilities cap1 = new DesiredCapabilities();
        cap1.setCapability("platformName", "Windows");
        cap1.setCapability("deviceName", "WindowsPC");
        cap1.setCapability("app", "C:\\\\Program Files\\\\MPSSoft\\\\MasterSCADA 4D RT 1.2\\\\MS4DMonitor.exe");
        cap1.setCapability("ms:waitForAppLaunch", "1000");
        try {
            driver1 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap1);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/
        DesiredCapabilities cap2 = new DesiredCapabilities();
        cap2.setCapability("platformName", "Windows");
        cap2.setCapability("deviceName", "WindowsPC");
        cap2.setCapability("app", "Root");
        try {
            driver2 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap2);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @DisplayName("MonitorExistence")
    @Test
    @Tag("Monitor")
    @Order(1)
    public void Launch() throws InterruptedException {
        Actions actionProvider = new Actions(driver2);
        driver2.findElementByName("Шеврон уведомления").click();
        Thread.sleep(500);
        assertTrue(driver2.findElements(By.name("MasterSCADA 4D RT 1_2")).size() > 0);
        WebElement MonitorTest1 = driver2.findElementByName("MasterSCADA 4D RT 1_2");
        actionProvider.doubleClick(MonitorTest1).perform();
        Thread.sleep(500);
    }

    @DisplayName("MonitorExistence")
    @Test
    @Tag("Monitor")
    @Order(2)
    public void LaunchMPLCALL() throws InterruptedException, IOException {
        List<WebElement> elements = driver2.findElements(By.name("ВСЕ"));
        System.out.println(elements);
        elements.get(1).click();
        Thread.sleep(5000);
        GetProcessExistence("mplc", false);
        GetProcessExistence("nginx", false);
        GetProcessExistence("node_ms4d", false);
        elements.get(0).click();
        Thread.sleep(5000);
        GetProcessExistence("mplc", true);
        GetProcessExistence("nginx", true);
        GetProcessExistence("node_ms4d", true);
    }

    @DisplayName("MPLCLaunch")
    @Test
    @Tag("Monitor")
    @Order(3)
    public void LaunchOnlymplc() throws InterruptedException, IOException {
        driver2.findElementByName("mplc").click();
        Thread.sleep(500);
        driver2.findElementByName("Остановить процесс").click();
        Thread.sleep(5000);
        GetProcessExistence("mplc", false);
        GetProcessExistence("nginx", true);
        GetProcessExistence("node_ms4d", true);

        driver2.findElementByName("Запустить процесс").click();
        Thread.sleep(5000);
        GetProcessExistence("mplc", true);
        GetProcessExistence("nginx", true);
        GetProcessExistence("node_ms4d", true);
    }

    @DisplayName("ImportLaunch")
    @Test
    @Tag("Monitor")
    @Order(4)
    public void ImportProject() throws InterruptedException, IOException {
        List<WebElement> elements = driver2.findElements(By.name("ВСЕ"));
        System.out.println(elements);
        elements.get(1).click();
        Thread.sleep(5000);
        driver2.findElementByName("Импорт проекта").click();
        Thread.sleep(1000);
        driver2.findElementByClassName("UIProperty").click();
        Thread.sleep(1000);
        driver2.findElementByName("Открыть").click();
        Thread.sleep(1000);
        driver2.findElementByName("Импортировать").click();
        Thread.sleep(1000);
        elements.get(0).click();
        Thread.sleep(5000);
        driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
        Thread.sleep(1000);
        driver2.findElementByName("Ventilation (АРМ 1)").click();


    }


    @DisplayName("StartClient")
    @Test
    @Tag("Monitor")
    @Order(5)
    public void StartClient() throws InterruptedException, IOException {
        driver2.findElementByName("Запустить клиент").click();
        Thread.sleep(5000);
        GetProcessExistence("MasterSCADA4DClient", true);
        driver2.findElementByName("Закрыть").click();
        Thread.sleep(1000);
        GetProcessExistence("MasterSCADA4DClient", false);
    }


    @DisplayName("DelProject")
    @Test
    @Tag("Monitor")
    @Order(6)
    public void DelProject() throws InterruptedException, IOException {
        List<WebElement> elements = driver2.findElements(By.name("ВСЕ"));
        System.out.println(elements);
        elements.get(1).click();
        Thread.sleep(5000);
        WebElement DelProject = driver2.findElementByName("Удалить проект");
        DelProject.click();
        DelProject.click();
        Thread.sleep(3000);
        driver2.findElementByName("Удалить").click();
        Thread.sleep(3000);
        Boolean DelNotWork = driver2.findElementsByName("Удалить").size()>1;
        if (DelNotWork) {
            System.out.println(driver2.findElementsByName("Удалить").size());
            driver2.findElementByName(" Отмена ").click();
            Thread.sleep(2000);
            elements.get(0).click();
            Thread.sleep(3000);
            driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
            Thread.sleep(2000);
            List<WebElement> elements2 = driver2.findElements(By.name("ВСЕ"));
            elements2.get(0).click();
            Thread.sleep(3000);
            assertFalse(DelNotWork, "Кнопка Удалить не работает. Выход нажатием Отмена");
        };
        Thread.sleep(2000);
        elements.get(0).click();
        Thread.sleep(3000);
        driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
        Thread.sleep(1000);
        assertTrue(driver2.findElementsByName("Ventilation (АРМ 1)").size() == 0);
        //driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
    }

}
