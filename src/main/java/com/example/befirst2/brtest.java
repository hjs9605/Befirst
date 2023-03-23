package com.example.befirst2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.openqa.selenium.support.ui.WebDriverWait; // 명시적 대기 사용

import java.util.Set;
import java.util.Scanner;

@Component
public class brtest  implements CommandLineRunner {

    /**
     * @implNote
     * 현재 웹드라이버가 관리하는 윈도우 외의 모든 윈도우를 닫는다.
     */
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

    /**
     * @implNote 
     * 오늘 날짜 클릭 후 다시 타겟 날짜 클릭
     */
    public void RefreshCatchTable(){
        _WebDriver.findElement(By.xpath("//button[text() = '오늘']")).click();
        _WebDriver.findElement(By.xpath(GetCalenderXPath(_TargetRow, _TargetCol))).click();
    }

    /**
     * @implNote
     * 예약하고싶은 날짜의 행 열 입력
     */
    public String GetCalenderXPath(int row, int col){
        return String.format("/html/body/div[4]/div[3]/div/div[1]/div[1]/div/div/div/div/div/div/div[2]/div/div[1]/div/div[1]/div/div[2]/div/div[%d]/div[%d]/div/div", row+1, col);
    }

    @Override
    public void run(String... args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        // 자꾸 행열 입력해야해서 화났음
        boolean debugging = true;

        if(debugging){
            _TargetRow = 5;
            _TargetCol = 6;
            _TargetTime = "오후 7:00";
        }
        else {
            System.out.println("캘린더 행 입력 : ");
            _TargetRow = scanner.nextInt();
            System.out.println("캘린더 열 입력 : ");
            _TargetCol = scanner.nextInt();
            System.out.println("원하는 시간 입력 **오전 7:15** ");
            _TargetTime = scanner.nextLine();
        }
        process();
    }

    //region Params
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
    private int _TargetRow;
    private int _TargetCol;
    private String _TargetTime;
    //endregion

    public void process() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // 모든 페이지를 로드하냐 마냐 에 대한 설정인듯?
        options.addArguments("--remote-allow-origins=*"); // 원격 접속 허용
        System.setProperty("webdriver.chrome.driver", BRSEO_ChromeDriverDirectory);

        _WebDriver = new ChromeDriver(options);
        _WebDriverWait = new WebDriverWait(_WebDriver, Second(100));

        //브라우저 선택
        _WebDriver.get(URL_ct_login);
        mainWindow = _WebDriver.getWindowHandle();

        // 팝업창 다 닫깅
        closePopup();
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("__kakao")));
        _WebDriver.findElement(By.className("__kakao")).click();


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
        
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginKey--1")));
        _WebDriver.findElement(By.id("loginKey--1")).sendKeys(BRSEO_id_kakao);
        _WebDriver.findElement(By.id("password--2")).sendKeys(BRSEO_password_kakao);
        _WebDriver.findElement(By.className("btn_g")).click();
        _WebDriver.switchTo().window(mainWindow);
        _Sleeper.sleep(Second(3));

        try{
            _WebDriver.findElement(By.className("btn-close")).click();
            _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-today-close")));
            _WebDriver.findElement(By.className("btn-today-close")).click();
        }
        catch(NoSuchElementException e) {

        }
        finally{
            _WebDriver.findElement(By.className("keyword-search")).click();
        }

        _WebDriverWait.until(ExpectedConditions.urlToBe("https://app.catchtable.co.kr/ct/search/total"));
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"header\"]/div/form/input")));
        _WebDriver.findElement(By.xpath("//*[@id=\"header\"]/div/form/input")).sendKeys(TargetRestaurant);
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("searched-keyword-list-item")));
        _WebDriver.findElement(By.className("searched-keyword-list-item")).click();
        _WebDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-reservation")));

        // TODO: 2023-03-16 예약하기나 예약가능날짜찾기 버튼을 눌러서 time slot unavailable을 찾을 수 있는 지 검사

        _WebDriver.findElement(By.className("btn-reservation")).click();
        _Sleeper.sleep(Second(3));
        _WebDriver.findElement(By.xpath(GetCalenderXPath(_TargetRow, _TargetCol))).click();

        boolean refreshing = true;
        while(refreshing) {
            try {
                _WebDriver.findElement(By.xpath("//*[contains(text(), '예약이 모두 마감되었습니다.') or contains(text(), '온라인 예약을 받지 않는 날입니다.') or contains(text(), '빈자리 알림 신청') or contains(text(), '휴무일입니다.')]"));
                System.out.println("큰일이야");
                RefreshCatchTable();

            } catch (NoSuchElementException e) {
                System.out.println("테스트");
            }

            try {
                _WebDriver.findElement(By.xpath("//*[contains(text(), '예약 오픈 전입니다.')]"));
                RefreshCatchTable();
            } catch (NoSuchElementException e) {
                System.out.println("테스트");
            }

            try {
                _WebDriver.findElement(By.xpath(String.format("//span[contains(text(), %s)]", _TargetTime))).click();
                System.out.println("테스트");
            } catch (NoSuchElementException e) {

            }

        }
    }
}


