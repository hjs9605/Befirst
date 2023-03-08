package com.example.befirst2;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Sleeper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class brtest  implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception{
        process();
    }


    private WebDriver driver;
    private WebDriver driver_spare;
    java.time.Duration Second(long sec){
        return java.time.Duration.ofSeconds(sec);
    }
    private static final Sleeper _Sleeper = Sleeper.SYSTEM_SLEEPER;
    private static final String url_ct_login = "https://app.catchtable.co.kr/ct/login";
    private static final String url_ct_kakaoLogin = "https://accounts.kakao.com/login/?continue=https%3A%2F%2Fkauth.kakao.com%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26redirect_uri%3Dhttps%253A%252F%252Fapp.catchtable.co.kr%252Fct%252Foauth%252Fkakao_callback%26state%3Dcatchtable%26through_account%3Dtrue%26client_id%3Db119ec349e6088e0cd54141180e07bbb#login";
    private static final String brseo_id_kakao = "sbl1998@naver.com";
    private static final String brseo_password_kakao = "Hjs220801@*";
    public void process() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);


        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sbl\\IdeaProjects\\BeFirst2\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        driver = new ChromeDriver(options);
        driver_spare = new ChromeDriver(options); // 상세보기 페이지 ?

        //브라우저 선택
        driver.get(url_ct_login);
        driver.findElement(By.className("__kakao")).click();

        //_Sleeper.sleep(Second(1));

        driver_spare.get(url_ct_kakaoLogin);
        driver_spare.findElement(By.id("loginKey--1")).sendKeys("");
        
        //driver.findElement(By.xpath("//*[@id=\"main\"]/section/div/div/div[1]/button")).click(); // xpath는 너무 불안정한듯
//        driver.close();    //탭 닫기
//        driver.quit();    //브라우저 닫기
    }
}


