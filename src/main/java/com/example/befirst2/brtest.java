package com.example.befirst2;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.openqa.selenium.support.ui.WebDriverWait; // 명시적 대기 사용

import java.lang.annotation.Target;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Component
public class brtest  implements CommandLineRunner {

    public void closePopup(WebDriver wd, String mainWindowHandle){
        Set<String> windows = wd.getWindowHandles();
        for(String window : windows){
            if(!window.contentEquals(mainWindowHandle)){
                wd.switchTo().window(window).close();
            }
        }
        mainWindow = _WebDriver.getWindowHandle();
    }

    /**
     * @implNote
     * 현재 Set을 순회하며 현재 메인 페이지가 아닌 페이지(임의의 팝업)로 전환, 전환 시 true  *팝업창이 여러개일 때 한계가 있음.
     * @param wd 사용중인 웹드라이버
     * @param mainWindowHandle 현재 메인 페이지
     */
    public boolean handlePopup(WebDriver wd, String mainWindowHandle){
        Set<String> windows = wd.getWindowHandles();
        for(String window : windows){
            if(!window.contentEquals(mainWindowHandle)){
                wd.switchTo().window(window);
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
    private static final String URL_ct_main = "https://app.catchtable.co.kr";
    private static final String BRSEO_id_kakao = "sbl1998@naver.com";
    private static final String BRSEO_password_kakao = "Hjs220801@*";
    private static final String BRSEO_ChromeDriverDirectory ="C:\\Users\\sbl\\IdeaProjects\\BeFirst2\\chromedriver.exe";
    private static final String TargetRestaurant = "우정분식";
    private String mainWindow;




    public void process() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // 모든 페이지를 로드하냐 마냐 에 대한 설정인듯?
        options.addArguments("--remote-allow-origins=*"); // 원격 접속 허용
        System.setProperty("webdriver.chrome.driver", BRSEO_ChromeDriverDirectory);


        _WebDriver = new ChromeDriver(options);
        // TODO Duration
        _WebDriverWait = new WebDriverWait(_WebDriver, Duration.ofSeconds(10));
        //driver_spare = new ChromeDriver(options); // 웹을 하나 더 컨트롤해야한다면...

        //브라우저 선택
        _WebDriver.get(URL_ct_login);

        // 팝업창 다 닫깅
        closePopup(_WebDriver, _WebDriver.getWindowHandle());

        _WebDriver.findElement(By.className("__kakao")).click();

        // 카카오 로그인 버튼을 눌렀을 때, 팝업창이 열릴 때 까지 대기하는 기대 시간
        _Sleeper.sleep(Second(2));

        // 명시적 대기 구현  *너무 복잡하게한거같기도하고..
        boolean check =handlePopup(_WebDriver, _WebDriver.getWindowHandle());
        while(!check){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            check = handlePopup(_WebDriver, _WebDriver.getWindowHandle());
        }

        // 이제부터 카카오로그인 팝업창

        // 로그인 입력창이 클릭 가능할때 까지(be enabled) 명시적 대기
        _WebDriverWait.until(ExpectedConditions.elementToBeClickable(_WebDriver.findElement(By.id("loginKey--1"))));
        
        _WebDriver.findElement(By.id("loginKey--1")).sendKeys(BRSEO_id_kakao);
        _WebDriver.findElement(By.id("password--2")).sendKeys(BRSEO_password_kakao);

        _WebDriver.findElement(By.className("btn_g")).click();




        _WebDriverWait.until(ExpectedConditions.urlToBe(URL_ct_main));

        // 로그인 이후 메인페이지
        // 하나가 확실하겟지?

        mainWindow = _WebDriver.getWindowHandle();
        _WebDriver.switchTo().window(mainWindow);

        _WebDriver.findElement(By.className("keyword-search")).sendKeys(TargetRestaurant);
        // TODO








//        driver.close();    //탭 닫기
//        driver.quit();    //브라우저 닫기
    }
}


