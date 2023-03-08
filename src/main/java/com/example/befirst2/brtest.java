package com.example.befirst2;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
    private WebDriver driver_detail;
    private static final String url_ct_login = "https://app.catchtable.co.kr/ct/login";
    public void process() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);


        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sbl\\IdeaProjects\\BeFirst2\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        driver = new ChromeDriver(options);
        driver_detail = new ChromeDriver(options); // 상세보기 페이지 ?


        //브라우저 선택
        driver.get(url_ct_login);
        driver.findElement(By.xpath("//*[@id=\"main\"]/section/div/div/div[1]/button")).click();
//        driver.close();    //탭 닫기
//        driver.quit();    //브라우저 닫기
    }
}


