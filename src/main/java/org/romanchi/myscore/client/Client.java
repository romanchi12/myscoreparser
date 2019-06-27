package org.romanchi.myscore.client;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.romanchi.myscore.model.dtos.MatchDTO;
import org.romanchi.myscore.model.entities.Match;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class Client {
    private final static Logger logger = Logger.getLogger(Client.class.getName());

    private WebDriver driver = new ChromeDriver();

    @Value("${overlay}")
    private String overlay;
    @Value("${openedMatchWindowOverlay}")
    private String openedMatchWindowOverlay;
    @Value("${mainUrl}")
    private String mainUrl;
    @Value("${loadMoreMatches}")
    private String loadMoreMatches;
    @Value("${timeout}")
    private Integer timeout;
    @Value("${matchRow}")
    private String matchRow;
    @Value("${scoreItem}")
    private String scoreItem;
    @Value("${homeTeamName}")
    private String homeTeamName;
    @Value("${hostTeamName}")
    private String hostTeamName;
    @Value("${matchDate}")
    private String matchDate;
    @Value("${advertisingBlock}")
    private String advertisingBlock;
    @Value("${advertisingBlockJS}")
    private String advertisingBlockJS;

    private String mainWindowHandle;

    public void qwe() {
        driver.get(mainUrl);
        loadMoreMatches();
    }

    public boolean isElementExistsBy(By by){
        return driver.findElements(by).size() > 0;
    }

    public void loadMoreMatches(){
        if(!driver.getCurrentUrl().equals(mainUrl)){
            driver.get(mainUrl);
        }
        waitLoading();
        if(isElementExistsBy(By.cssSelector(loadMoreMatches))){
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            WebElement element = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(loadMoreMatches)));
            element.click();
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(overlay))));
            logger.info("Loaded");
            return;
        }
        logger.info("All matches loaded");
        logger.info("Starting parsing");
        parsePage();
    }
    private void waitLoading(){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.not(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(overlay))));
    }
    private void wailLoadingOpenedMatchWindow(){
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector(openedMatchWindowOverlay))));
    }
    private void removeAdBlock(){
        if(isElementExistsBy(By.cssSelector(advertisingBlock))){
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            WebElement adBlockElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(advertisingBlock)));
            JavascriptExecutor js;
            if (driver instanceof JavascriptExecutor) {
                js = (JavascriptExecutor) driver;
                js.executeScript("return document.getElementsByClassName('" + advertisingBlockJS + "')[0].remove();");
            }
        }
    }

    public void parsePage(){
        if(!driver.getCurrentUrl().endsWith("results/")){
            driver.get(mainUrl);
            waitLoading();
        }
        removeAdBlock();
        mainWindowHandle = driver.getWindowHandle();
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        List<WebElement> matches = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(matchRow)));
        for (WebElement matchElement:matches) {
            matchElement.click();
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            String handleToSwitch = null;
            for(String handle:driver.getWindowHandles()){
                if(!handle.equals(mainWindowHandle)){
                    handleToSwitch = handle;
                }
            }
            driver.switchTo().window(handleToSwitch);
            wailLoadingOpenedMatchWindow();
            List<WebElement> scores = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(scoreItem)));
            MatchDTO.MatchDTOBuilder matchBuilder = MatchDTO.builder();
            if(scores.size() >= 2){
                try {
                    matchBuilder.score1(Integer.valueOf(scores.get(0).getText()));
                    matchBuilder.score2(Integer.valueOf(scores.get(1).getText()));
                }catch (NumberFormatException ignored){}
            }
            WebElement homeTeamNameElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(homeTeamName)));
            String homeTeam = homeTeamNameElement.getText();
            WebElement hostTeamNameElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(hostTeamName)));
            String hostTeam = hostTeamNameElement.getText();
            matchBuilder.team1(homeTeam);
            matchBuilder.team2(hostTeam);
            WebElement matchDateElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(matchDate)));
            String matchDateText = matchDateElement.getText();
            String league = "england";
            matchBuilder.date(matchDateText);
            matchBuilder.league(league);
            MatchDTO matchDTO = matchBuilder.build();
            logger.info(matchDTO.toString());
            driver.close();
            wait.until(ExpectedConditions.numberOfWindowsToBe(1));
            driver.switchTo().window(mainWindowHandle);
        }
    }
}
