package com.example.befirst2;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class jstest implements ApplicationRunner {

    private WebDriver _WebDriver;
    private WebDriverWait _WebDriverWait;
    private static final Sleeper _Sleeper = Sleeper.SYSTEM_SLEEPER;
    private static final String URL_ct_login = "https://app.catchtable.co.kr/ct/login";
    private static final String URL_ct_main = "https://app.catchtable.co.kr/";
    private static final String JISOO_ChromeDriverPath = "/Users/jisoo/Desktop/Study/befirst2/src/main/resources/chromedriver";
    private static final String BRSEO_id_kakao = "sbl1998@naver.com";
    private static final String BRSEO_password_kakao = "Hjs220801@*";
    private static final String TargetRestaurant = "우정초밥";
    @Override
    public void run(ApplicationArguments args) throws Exception {
        process();
    }

    java.time.Duration Second(long sec){
        return java.time.Duration.ofSeconds(sec);
    }

    public void process() throws InterruptedException{
        ChromeDriverSetting();

        //로그인 윈도우 켜기
        _WebDriver.get(URL_ct_login);

        // 현재 윈도우를 메인으로 저장
        String mainWindow = _WebDriver.getWindowHandle();

        //카카오로 로그인하기
        _WebDriver.findElement(By.className("__kakao")).click();

        WebDriverWait _WebDriverWait = new WebDriverWait(_WebDriver, Second(2));
        //팝업창 하나라도 더 뜰 때까지 기다리기
        _WebDriverWait.until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> windows = _WebDriver.getWindowHandles();
        List<String> windowList = new ArrayList<>(windows);

        //로그인 페이지로 전환
        setLoginWindow(windowList, 1);

        login(mainWindow, _WebDriverWait);
        _Sleeper.sleep(Second(5));

        //이상한 팝업 있는지 체크
        checkPopup(_WebDriverWait);

        //검색
        _WebDriver.findElement(By.className("keyword-search")).click();
        _WebDriverWait.until(ExpectedConditions.urlToBe("https://app.catchtable.co.kr/ct/search/total"));

        _WebDriver.findElement(By.xpath("//*[@id=\"header\"]/div/form/input")).sendKeys(TargetRestaurant);
        _WebDriverWait.until(ExpectedConditions.elementToBeClickable(_WebDriver.findElement(By.className("searched-keyword-list-item"))));
        _WebDriver.findElement(By.className("searched-keyword-list-item")).click();

    }

    private void login(String mainWindow, WebDriverWait _WebDriverWait) {
        // 로그인 입력창이 클릭 가능할때 까지(be enabled) 명시적 대기
        _WebDriverWait.until(ExpectedConditions.elementToBeClickable(_WebDriver.findElement(By.id("loginKey--1"))));

        _WebDriver.findElement(By.id("loginKey--1")).sendKeys(BRSEO_id_kakao);
        _WebDriver.findElement(By.id("password--2")).sendKeys(BRSEO_password_kakao);

        _WebDriver.findElement(By.className("btn_g")).click();

        //메인 창으로 돌아가기
        _WebDriver.switchTo().window(mainWindow);

        //로그인 완료된 페이지로 변화되기까지 기다리기
//        _WebDriverWait.until(ExpectedConditions.urlToBe(URL_ct_main));
    }

    private void setLoginWindow(List<String> windowList, int i) throws InterruptedException{
        _WebDriver.switchTo().window(windowList.get(i));
//        _Sleeper.sleep(Second(2));
        List<WebElement> elements = _WebDriver.findElements(By.id("loginKey--1"));
        while(elements.isEmpty()){
            _WebDriver.switchTo().window(windowList.get(i +1));
            elements =  _WebDriver.findElements(By.id("loginKey--1"));
            i = i +1;
        }
    }

    private void checkPopup(WebDriverWait _WebDriverWait) {
        List<WebElement> trashElements = _WebDriver.findElements(By.className("btn-close"));
        if(!trashElements.isEmpty()){
            _WebDriver.findElement(By.className("btn-close")).click();
            _WebDriverWait.until(ExpectedConditions.elementToBeClickable(_WebDriver.findElement(By.className("btn-today-close"))));
            _WebDriver.findElement(By.className("btn-today-close")).click();
        }
    }

    private void ChromeDriverSetting() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--remote-allow-origins=*");
        System.setProperty("webdriver.chrome.driver", JISOO_ChromeDriverPath);
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        _WebDriver = new ChromeDriver(options);
    }

}
