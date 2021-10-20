package com.kklimkovtests;

import io.appium.java_client.windows.WindowsDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import java.util.List;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MonitorSteps {

    public static WindowsDriver driver2 = null;

    @Step("Запуск драйвера root")
    public static void LaunchRoot() throws InterruptedException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Windows");
        cap.setCapability("deviceName", "WindowsPC");
        cap.setCapability("app", "Root");
        cap.setCapability("ms:waitForAppLaunch", "10");
        try {
            driver2 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Step("Запуск Monitor из tray")
    public static void LaunchTray() throws InterruptedException {
        Actions actionProvider = new Actions(driver2);
        driver2.findElementByName("Шеврон уведомления").click();
        Thread.sleep(500);
        assertTrue(driver2.findElements(By.name("MasterSCADA 4D RT 1_2")).size() > 0);
        WebElement MonitorTest1 = driver2.findElementByName("MasterSCADA 4D RT 1_2");
        actionProvider.doubleClick(MonitorTest1).perform();
        Thread.sleep(500);
    }

    @Step("Остановить все процессы")
    public static void StopAllProcess() throws InterruptedException {
        List<WebElement> elements = driver2.findElements(By.name("ВСЕ"));
        System.out.println(elements);
        elements.get(1).click();
        Thread.sleep(5000);
    }

    @Step("Запустить все процессы все процессы")
    public static void StartAllProcess() throws InterruptedException {
        List<WebElement> elements = driver2.findElements(By.name("ВСЕ"));
        System.out.println(elements);
        elements.get(0).click();
        Thread.sleep(5000);
    }

    @Step("Проверка на существование процесса")
    public static void GetProcessExistence(String ProcessName,Boolean Ex) throws IOException {
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
            System.out.println(line);
            if (Ex) {
                assertTrue(!line.contains("Задачи, отвечающие заданным критериям, отсутствуют"));
            } else
            {assertTrue(line.contains("Задачи, отвечающие заданным критериям, отсутствуют"));};
        }
    }

    @Step("Переоткрыть окно монитора")
    public static void ReopenWindowMonitor() throws InterruptedException {
        driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
        Thread.sleep(1000);
    }

    @Step("Клик на элемент окна монитора")
    public static void ClickToMonitorElement(String NameElement, Integer Waiting) throws InterruptedException {
        driver2.findElementByName(NameElement).click();
        Thread.sleep(Waiting);
    }

    @Step("Выбор проекта для импорта")
    public static void ChoiceImportProject() throws InterruptedException {
        /*driver2.findElementByClassName("UIProperty").click();
        Thread.sleep(1000);
        driver2.findElementByName("Открыть").click();
        Thread.sleep(1000);*/
        driver2.findElementByClassName("Edit").sendKeys("V");
        Thread.sleep(1000);
        driver2.findElementByAccessibilityId("ListViewSubItem-0").click();
        Thread.sleep(1000);
        driver2.findElementByName("Открыть").click();
        Thread.sleep(1000);
    }

    @Step("Проверка на существование элементов")
    public static void CheckElementExistence(String Whatfind, Integer AccSize, String AssertMessage) throws InterruptedException {
        Boolean DelNotWork = driver2.findElementsByName(Whatfind).size()>AccSize;
        System.out.println(driver2.findElementsByName(Whatfind).size());
        assertFalse(DelNotWork, AssertMessage);
        Thread.sleep(2000);
        /*if (DelNotWork) {
            System.out.println(driver2.findElementsByName("Удалить").size());
            driver2.findElementByName(" Отмена ").click();
            Thread.sleep(2000);
            StartAllProcess();
            driver2.findElementByAccessibilityId("ReopenAmongButtons").click();
            Thread.sleep(2000);
            List<WebElement> elements2 = driver2.findElements(By.name("ВСЕ"));
            StartAllProcess();
            Thread.sleep(3000);
            assertFalse(DelNotWork, "Кнопка Удалить не работает. Выход нажатием Отмена");
        };
        */
    }

}
