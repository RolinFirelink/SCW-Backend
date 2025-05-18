package com.rolin.orangesmart.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 00:23
 */
public class ChromeUtil {
  public static ChromeDriver getChromeDriver() {
    // 本地测试，本地需配置同版本的chromedriver和Chrome，测试时保持Chrome网页运行
    // chrome会自动更新，需注意保持版本不变
    // 快速入门教学：https://blog.csdn.net/chenjxj123/article/details/121802904
//    System.setProperty("webdriver.chrome.driver", "/usr/local/chromeDriver/chromedriver");
    System.setProperty("webdriver.chrome.driver", "D:\\programmingNoDelete\\chromedriver\\chromedriver.exe");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
//    options.addArguments("--headless");
    ChromeDriver driver = new ChromeDriver(options);
    driver.manage().window().setSize(new Dimension(1366, 768));
    return driver;
  }

}
