package com.kklimkovtests;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import java.io.IOException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@Owner("KKlimkov")
@Layer("monitor")
@Feature("MS4D RT Monitor")

@TestMethodOrder(OrderAnnotation.class)
public class TestMonitor {


    @DisplayName("Запуск монитора из панели задач")
    @Test
    @Story("Launch Monitor")
    @Tags({@Tag("Monitor"),@Tag("Process")})
    @Order(1)
    public void Launch() throws InterruptedException {
        MonitorSteps.LaunchRoot();
        MonitorSteps.LaunchTray();
    }

        @DisplayName("Проверка на существование процессов")
        @Test
        @Story("CheckProcess")
        @Tags({@Tag("Monitor"),@Tag("Process")})
        @Order(2)
        public void LaunchMPLCALL() throws InterruptedException, IOException {
            MonitorSteps.StopAllProcess();
            MonitorSteps.GetProcessExistence("mplc", false);
            MonitorSteps.GetProcessExistence("nginx", false);
            MonitorSteps.GetProcessExistence("node_ms4d", false);
            MonitorSteps.StartAllProcess();
            MonitorSteps.GetProcessExistence("mplc", true);
            MonitorSteps.GetProcessExistence("nginx", true);
            MonitorSteps.GetProcessExistence("node_ms4d", true);
            MonitorSteps.ReopenWindowMonitor();
        }

        @DisplayName("Запуск отдельных процессов")
        @Test
        @Story("CheckSeparateProcess")
        @Tags({@Tag("Monitor"),@Tag("Process")})
        @Order(3)
        public void LaunchOnlymplc() throws InterruptedException, IOException {
            MonitorSteps.ClickToMonitorElement("mplc",500);
            MonitorSteps.ClickToMonitorElement("Остановить процесс",5000);
            MonitorSteps.GetProcessExistence("mplc", false);
            MonitorSteps.GetProcessExistence("nginx", true);
            MonitorSteps.GetProcessExistence("node_ms4d", true);
            MonitorSteps.ClickToMonitorElement("Запустить процесс",5000);
            MonitorSteps.GetProcessExistence("mplc", true);
            MonitorSteps.GetProcessExistence("nginx", true);
            MonitorSteps.GetProcessExistence("node_ms4d", true);
        }

    @DisplayName("Импорт проектов")
    @Test
    @Story("ImportProject")
    @Tags({@Tag("Monitor"),@Tag("Import")})
    @Order(4)
    public void ImportProject() throws InterruptedException, IOException {
        MonitorSteps.StopAllProcess();
        MonitorSteps.ClickToMonitorElement("Импорт проекта",2000);
        MonitorSteps.ChoiceImportProject();
        MonitorSteps.ClickToMonitorElement("Импортировать",1000);
        MonitorSteps.StartAllProcess();
        MonitorSteps.ReopenWindowMonitor();
        MonitorSteps.ClickToMonitorElement("Ventilation (АРМ 1)",1000);
    }

    @DisplayName("Запуск клиента")
    @Test
    @Story("LaunchClient")
    @Tags({@Tag("Monitor"),@Tag("Embedded Client")})
    @Order(5)
    public void StartClient() throws InterruptedException, IOException {
        MonitorSteps.ClickToMonitorElement("Запустить клиент",7000);
        MonitorSteps.GetProcessExistence("MasterSCADA4DClient", true);
        MonitorSteps.ClickToMonitorElement("Закрыть",2000);
        MonitorSteps.GetProcessExistence("MasterSCADA4DClient", false);
    }

    @DisplayName("Удаление проекта")
    @Test
    @Story("LaunchClient")
    @Tags({@Tag("Monitor"),@Tag("DelProject")})
    @Order(6)
    public void DelProject() throws InterruptedException, IOException {
        MonitorSteps.StopAllProcess();
        MonitorSteps.ClickToMonitorElement("Удалить проект",1000);
        MonitorSteps.ClickToMonitorElement("Удалить проект",3000);
        MonitorSteps.ClickToMonitorElement("Удалить",5000);
        MonitorSteps.CheckElementExistence("Другие файлы",0,"Нет кнопки Другие файлы");
        MonitorSteps.StartAllProcess();
        //MonitorSteps.ReopenWindowMonitor();
        MonitorSteps.CheckElementExistence("Ventilation (АРМ 1)",0, "Удаление не сработало");
    }

}
