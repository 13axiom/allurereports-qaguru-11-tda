package qa.guru.allure;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.step;

public class StepsTest {

    private static final String REPOSITORY = "eroshenkoam/allure-example";
    private static final int ISSUE_NUMBER = 68;

    @Test
    @Owner("dimtok")
    @Severity(SeverityLevel.NORMAL)
    @Feature("обучение на репе " + REPOSITORY)
    @Story("вкладка Issue")
    @DisplayName("проверяем что существует Issue с номером " + ISSUE_NUMBER)
    public void testLambdaSteps() {
        Allure.parameter("REPOSITORY", "eroshenkoam/allure-example");
        Allure.parameter("ISSUE_NUMBER", "68");

        SelenideLogger.addListener("allure", new AllureSelenide());

        step("открываем главную страницу", () -> {
            open("https://github.com");
        });
        step("ищем репоизторий " + REPOSITORY, () -> {
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(REPOSITORY);
            $(".header-search-input").submit();
        });
        step("открываем репозиторий " + REPOSITORY, () -> {
            $(By.linkText("eroshenkoam/allure-example")).click();
        });
        step("переходим в таб Issue", () -> {
            $(By.partialLinkText("Issues")).click();
            addAttachment("Page Source", "text/html", WebDriverRunner.source(), "html");
        });
        step("проверяем что существует Issue с номером " + ISSUE_NUMBER, () -> {
            $(withText("#68")).should(Condition.exist);
        });
    }

    @Test
    public void testAnnotatedSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        //вот это одинаковое для 10 тестов
        WebSteps steps = new WebSteps();
        steps.openMainPage();
        steps.searchForRepository(REPOSITORY);
        steps.openRepository(REPOSITORY);

        //вот отсюда разное
        steps.openIssueTab();
        steps.shouldSeeIssueWithNumber(ISSUE_NUMBER);

        steps.takeScreenshot();
    }

}
