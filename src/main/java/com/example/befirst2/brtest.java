package com.example.befirst2;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.openqa.selenium.support.ui.WebDriverWait; // 명시적 대기 사용

import java.lang.annotation.Target;
import java.time.Duration;
import java.util.Set;

@Component
public class brtest  implements CommandLineRunner {

    public void closePopup(){
        Set<String> windows = _WebDriver.getWindowHandles();
        for(String window : windows){
            if(!window.contentEquals(mainWindow)){
                _WebDriver.switchTo().window(window).close();
            }
        }
        mainWindow = _WebDriver.getWindowHandle();
    }

    /**
     * @implNote
     * 현재 Set을 순회하며 현재 메인 페이지가 아닌 페이지(임의의 팝업)로 전환, 전환 시 true  *팝업창이 여러개일 때 한계가 있음.
     */
    public boolean handlePopup(){
        Set<String> windows = _WebDriver.getWindowHandles();
        for(String window : windows){
            if(!window.contentEquals(mainWindow)){
                _WebDriver.switchTo().window(window);
                return true;
            }
        }
        return false;
    }


    @Override
    public void run(String... args) throws Exception{
        process();
    }


    private WebDriver _WebDriver;
    private WebDriverWait _WebDriverWait;
    java.time.Duration Second(long sec){
        return java.time.Duration.ofSeconds(sec);
    }
    private static final Sleeper _Sleeper = Sleeper.SYSTEM_SLEEPER;
    private static final String URL_ct_login = "https://app.catchtable.co.kr/ct/login";
    private static final String URL_ct_main = "https://app.catchtable.co.kr/";
    private static final String BRSEO_id_kakao = "sbl1998@naver.com";
    private static final String BRSEO_password_kakao = "Hjs220801@*";
    private static final String BRSEO_ChromeDriverDirectory ="src/main/resources/chromedriver_brseo.exe";
    private static final String TargetRestaurant = "우정초밥";
    private String mainWindow;




    public void process() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // 모든 페이지를 로드하냐 마냐 에 대한 설정인듯?
        options.addArguments("--remote-allow-origins=*"); // 원격 접속 허용
        System.setProperty("webdriver.chrome.driver", BRSEO_ChromeDriverDirectory);


        _WebDriver = new ChromeDriver(options);
        // TODO Duration
        _WebDriverWait = new WebDriverWait(_WebDriver, Second(100));
        //driver_spare = new ChromeDriver(options); // 웹을 하나 더 컨트롤해야한다면...

        //브라우저 선택
        _WebDriver.get(URL_ct_login);
        mainWindow = _WebDriver.getWindowHandle();

        // 팝업창 다 닫깅
        closePopup();

        _WebDriverWait.until(ExpectedConditions.elementToBeClickable(_WebDriver.findElement(By.className("__kakao"))));

        _WebDriver.findElement(By.className("__kakao")).click();

        // 명시적 대기 구현  *너무 복잡하게한거같기도하고..

        boolean check = handlePopup();
        while(!check){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            check = handlePopup();
        }

        // 이제부터 카카오로그인 팝업창

        // 로그인 입력창이 클릭 가능할때 까지(be enabled) 명시적 대기
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginKey--1")));
        _WebDriver.findElement(By.id("loginKey--1")).sendKeys(BRSEO_id_kakao);
        _WebDriver.findElement(By.id("password--2")).sendKeys(BRSEO_password_kakao);

        _WebDriver.findElement(By.className("btn_g")).click();


        _WebDriver.switchTo().window(mainWindow);
        //_WebDriverWait.until(ExpectedConditions.urlToBe(URL_ct_main));


        _Sleeper.sleep(Second(4));

        try{
            _WebDriver.findElement(By.className("btn-close")).click();
            _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-today-close")));
            _WebDriver.findElement(By.className("btn-today-close")).click();
        }
        catch(NoSuchElementException e){

        }


        _WebDriver.findElement(By.className("keyword-search")).click();
        _WebDriverWait.until(ExpectedConditions.urlToBe("https://app.catchtable.co.kr/ct/search/total"));

        _WebDriver.findElement(By.xpath("//*[@id=\"header\"]/div/form/input")).sendKeys(TargetRestaurant);

        _Sleeper.sleep(Second(1));

        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("searched-keyword-list-item")));
        _WebDriver.findElement(By.className("searched-keyword-list-item")).click();

        //_WebDriver.findElement(By.)
        // TODO

//        driver.close();    //탭 닫기
//        driver.quit();    //브라우저 닫기
    }
}


