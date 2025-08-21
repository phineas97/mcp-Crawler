package com.example.crawler.implement;

import com.example.crawler.Config.Info;
import com.example.crawler.Service.ToutiaoService;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToutiaoImpl implements ToutiaoService {

    String k = "";
    String maintext = "";
    String title = "";

    public void setUp(WebDriver driver) {
        // 设置WebDriver路径
        System.setProperty("webdriver.chrome.driver", "D:\\Drivers\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        Proxy proxy = new Proxy();
        proxy.setSocksProxy("10c8cee.b5.gladns.com:2377"); // 替换为您的代理 IP 和端口
        options.setProxy(proxy);
        options.addArguments("--disable-extensions");  // 禁用浏览器扩展。
        options.addArguments("--headless");  // 启用无头模式（不显示浏览器界面）
        options.addArguments("window-size=1920x3000");    // 指定浏览器分辨率
        options.addArguments("start-maximized");    // 启动时最大化浏览器窗口
        options.addArguments("--disable-gpu");    // 禁用GPU硬件加速。
        options.addArguments("--disable-popup-blocking");    // 禁用弹出窗口拦截器。
        options.addArguments("--no-sandbox");    // 彻底停用沙箱，这在非桌面环境中运行Chrome时非常有用
        options.addArguments("--disable-web-security");    // 禁用网页安全性功能，用于解决跨域问题
        options.addArguments("--ignore-certificate-errors");    // 忽略SSL证书错误
        options.addArguments("--incognito");    // 以隐身模式启动浏览器
        options.addArguments("--disable-javascript");    // 禁用JavaScript
        options.addArguments("--disable-plugins");    // 禁用插件
        options.addArguments("--disable-images");    // 禁用图像
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "--disable-blink-features=AutomationControlled",
        "--incognito");
        // 初始化WebDriver
        driver = new ChromeDriver(options);
    }

    @Override
    public Info ExtractToutiao(String url) throws IOException {
        WebDriver driver = new ChromeDriver();
        setUp(driver);
        try {
            // 打开目标页面
            driver.get(url);

            // 显式等待
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            // 额外等待 5 秒，允许 JavaScript 执行
            Thread.sleep(5000);

            // 获取并打印
            ExtractTitle(driver);
            ExtractMaintext(wait);
            ExtractKeywords(wait);

        } catch (Exception e) {
            System.out.println("发生错误：" + e.getMessage());
        } finally {
            // 关闭浏览器
            driver.quit();
        }

        Info info = new Info();
        info.setTitle(title);
        info.setMaintext(maintext);
        info.setKeywords(k);
        return info;

    }
    private void ExtractTitle(WebDriver driver) {
        title = driver.getTitle();
        if (title == null || title.isEmpty()) {
            title = "无标题";
        }
    }

    private void ExtractMaintext(WebDriverWait wait) {
        WebElement articleElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("article.syl-article-base.syl-page-article.tt-article-content.syl-device-pc")));
        List<WebElement> paragraph = articleElement.findElements(By.tagName("p"));
        List<String>content= new ArrayList<>();
        for(WebElement p : paragraph) {
            content.add(p.getText().trim());
        }
        maintext = String.join("\n", content);
        if (maintext.isEmpty()) {
            maintext = "无正文内容";
        }
    }

    private void ExtractKeywords(WebDriverWait wait) {
        WebElement keywordElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("meta[name='keywords']")));
        k = keywordElement.getAttribute("content").trim();
        if (k.isEmpty()){
            k = "无关键词";
        }
    }

}