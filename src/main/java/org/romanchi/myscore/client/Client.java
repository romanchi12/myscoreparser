package org.romanchi.myscore.client;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.romanchi.myscore.model.dtos.MatchDTO;
import org.romanchi.myscore.model.dtos.PlayerDTO;
import org.romanchi.myscore.model.entities.Match;
import org.romanchi.myscore.model.entities.Player;
import org.romanchi.myscore.services.MatchService;
import org.romanchi.myscore.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class Client {


    private final PlayerService playerService;
    private final MatchService matchService;

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
    @Value("${playersTab}")
    private String playersTab;
    @Value("${tableWithPlayers}")
    private String tableWithPlayers;
    @Value("${homePlayer}")
    private String homePlayer;
    @Value("${hostPlayer}")
    private String hostPlayer;
    @Value("${playerFullName}")
    private String playerFullName;
    @Value("${playerBirthday}")
    private String playerBirthday;
    @Value("${injuries}")
    private String injuries;
    @Value("${teamName}")
    private String teamName;

    private String mainWindowHandle;
    private String matchWindowHandle;
    private String playerWindowHandle;

    @Autowired
    public Client(MatchService matchService, PlayerService playerService) {
        this.matchService = matchService;
        this.playerService = playerService;
    }

    public boolean isElementExistsBy(By by){
        return driver.findElements(by).size() > 0;
    }

    public boolean hasMoreMatches(){
        return isElementExistsBy(By.cssSelector(loadMoreMatches));
    }

    public void parse() throws ParseException {
        loadMoreMatches();
        while(hasMoreMatches()){
            loadMoreMatches();
        }
        parsePage();
    }

    public void loadMoreMatches() {
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

    public Player parsePlayer(WebElement player) throws ParseException {
        String id = player.getAttribute("onclick");
        Optional<Player> playerOptional = playerService.findById(id);
        if(playerOptional.isPresent()){
            return playerOptional.get();
        }
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        player.click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));
        Set<String> windowHandles = driver.getWindowHandles();
        for(String handle:windowHandles){
            if(!handle.equals(mainWindowHandle) && !handle.equals(matchWindowHandle)){
                playerWindowHandle = handle;
            }
        }
        driver.switchTo().window(playerWindowHandle);
        WebElement playerFullNameElement =
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(playerFullName)));
        String playerFullNameElementText = playerFullNameElement.getText();
        String playerBirthdayText = null;
        if(isElementExistsBy(By.cssSelector(playerBirthday))){
            WebElement playerBirthdayElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(playerBirthday)));
            String playerBithdayRaw = playerBirthdayElement.getText();
            playerBirthdayText = playerBithdayRaw.substring(playerBithdayRaw.indexOf("(") + 1, playerBithdayRaw.length() - 1);
        }
        Integer injuriesAmount = 0;
        if(isElementExistsBy(By.cssSelector(injuries))){
            injuriesAmount = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector(injuries))).size();
        }
        PlayerDTO playerDTO = PlayerDTO.builder()
                .id(id)
                .name(playerFullNameElementText)
                .birthdate(playerBirthdayText)
                .wins(0)
                .draws(0)
                .injuryAmount(injuriesAmount)
                .allGames(0)
                .build();
        Player playerReady = playerService.findById(id)
                .orElse(playerDTO.toEntity());

        logger.info(new PlayerDTO(playerReady).toString());
        driver.close();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        driver.switchTo().window(matchWindowHandle);
        return playerReady;
    }


    public void parsePage() throws ParseException {
        logger.info("Starting parsing...");
        if(!driver.getCurrentUrl().endsWith("results/")){
            driver.get(mainUrl);
            waitLoading();
        }
        removeAdBlock();
        mainWindowHandle = driver.getWindowHandle();
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        List<WebElement> matches = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(matchRow)));
        for (WebElement matchElement:matches.stream().skip(332).collect(Collectors.toList())) {
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
            matchWindowHandle = driver.getWindowHandle();
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
            Match match = matchDTO.toEntity();
            logger.info(matchDTO.toString());
            WebElement playersTabElement = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(playersTab)));
            playersTabElement.click();
            wailLoadingOpenedMatchWindow();
            List<WebElement> trs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector(tableWithPlayers)));
            List<WebElement> positions = trs.subList(1,12);
            List<Player> playerList = new ArrayList<>();
            for(WebElement position:positions){

                WebElement homeTeamPlayer = position.findElement(By.cssSelector(homePlayer));
                Player homePlayer = parsePlayer(homeTeamPlayer);
                homePlayer.setTeamName(homeTeam);
                switch (match.getHomeTeamStatus()){
                    case WIN:{
                        homePlayer.setWins(homePlayer.getWins() + 1);
                        break;
                    }
                    case DRAW:{
                        homePlayer.setDraws(homePlayer.getDraws() + 1);
                        break;
                    }
                    case LOOSE:
                }
                homePlayer.setAllGames(homePlayer.getAllGames() + 1);

                playerList.add(homePlayer);

                WebElement hostTeamPlayer = position.findElement(By.cssSelector(hostPlayer));
                Player hostPlayer = parsePlayer(hostTeamPlayer);
                hostPlayer.setTeamName(hostTeam);
                switch (match.getHomeTeamStatus()){
                    case LOOSE:{
                        hostPlayer.setWins(hostPlayer.getWins() + 1);
                        break;
                    }
                    case DRAW:{
                        hostPlayer.setDraws(hostPlayer.getDraws() + 1);
                        break;
                    }
                }
                hostPlayer.setAllGames(hostPlayer.getAllGames() + 1);

                playerList.add(hostPlayer);
            }

            List<Player> savedPlayers = playerService.saveAll(playerList);
            match.setPlayers(savedPlayers);
            matchService.save(match);
            driver.close();
            wait.until(ExpectedConditions.numberOfWindowsToBe(1));
            driver.switchTo().window(mainWindowHandle);
        }
    }
}
