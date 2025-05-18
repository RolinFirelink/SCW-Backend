package com.rolin.orangesmart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rolin.orangesmart.enums.GanjuwEnum;
import com.rolin.orangesmart.exception.errorEnum.CrawlerErrorEnum;
import com.rolin.orangesmart.model.attachment.entity.Attachment;
import com.rolin.orangesmart.model.common.dto.PageDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.crawler.bo.CnhnbPriceBO;
import com.rolin.orangesmart.model.crawler.bo.CnhnbProcurementBO;
import com.rolin.orangesmart.model.crawler.bo.GanjuwBO;
import com.rolin.orangesmart.model.crawler.bo.WeatherBO;
import com.rolin.orangesmart.model.crawler.entity.CnhnbPrice;
import com.rolin.orangesmart.model.crawler.entity.CnhnbProcurement;
import com.rolin.orangesmart.model.crawler.entity.Ganjuw;
import com.rolin.orangesmart.model.crawler.entity.Weather;
import com.rolin.orangesmart.model.crawler.vo.*;
import com.rolin.orangesmart.service.fish.PostCommonService;
import com.rolin.orangesmart.util.*;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 00:28
 */
@Service
public class CrawlerService {

  @Resource
  private GanjuwService ganjuwService;
  @Resource
  private CnhnbProcurementService cnhnbProcurementService;
  @Resource
  private CnhnbPriceService cnhnbPriceService;
  @Resource
  private WeatherService weatherService;
  @Resource
  private SmartIdentifyService smartIdentifyService;
  @Resource
  private PostCommonService postCommonService;
  @Resource
  private DownloadService downloadService;
  @Resource
  private AttachmentService attachmentService;

  public RestTemplate restTemplate = new RestTemplate();

  public static final String GANJUW_DOMAIN = "https://gj.nxin.com/";

  private static final Set<String> PROVINCES = new HashSet<>(Arrays.asList(
      "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
      "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
      "北京", "天津", "上海", "重庆",
      "内蒙古", "广西", "西藏", "宁夏", "新疆",
      "香港", "澳门"
  ));

  @Transactional
  public void crawlerganjuw() {

    // 行业快讯
    new Thread(() -> execute("https://gj.nxin.com/information/ganjuService?type=139", GanjuwEnum.NEWS.getCode())).start();
    // 政策法规
    new Thread(() -> execute("https://gj.nxin.com/information/ganjuService?type=142", GanjuwEnum.LAWS.getCode())).start();
    // 行情咨询
    new Thread(() -> execute("https://gj.nxin.com/information/ganjuService?type=153", GanjuwEnum.MARKET.getCode())).start();
    // 种植技术
    new Thread(() -> execute("https://gj.nxin.com/information/ganjuService?type=140", GanjuwEnum.TECH.getCode())).start();

    // 柑橘头条
    ChromeDriver chromeDriver = ChromeUtil.getChromeDriver();
    try {
      login(chromeDriver);
      chromeDriver.get("https://gj.nxin.com/");
      WebElement element = chromeDriver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div/ul"));
      List<WebElement> lis = element.findElements(By.cssSelector("li"));
      List<Ganjuw> ganjuws = new ArrayList<>();
      for (WebElement li : lis) {
        WebElement a = li.findElement(By.cssSelector("a"));
        WebElement span = li.findElement(By.cssSelector("a span"));
        String href = a.getAttribute("href");
        String title = span.getText();
        ganjuws.add(new Ganjuw(GanjuwEnum.HEAD.getCode(), href, title));
      }
      for (Ganjuw ganjuw : ganjuws) {
        chromeDriver.get(ganjuw.getDetailUrl());
        WebElement element2 = chromeDriver.findElement(By.xpath("/html/body/div[2]/div"));
        String outerHtml = element2.getAttribute("outerHTML");
        System.out.println(outerHtml);
        ganjuw.setDetail(outerHtml);
      }
      ganjuwService.batchSaveOrUpdate(ganjuws);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      chromeDriver.close();
    }
  }

  private void login(ChromeDriver chromeDriver) {
    //登录
    chromeDriver.get("https://gj.nxin.com/login");
    WebElement account = chromeDriver.findElement(By.xpath("//*[@id=\"username\"]"));
    account.sendKeys("18319714791");
    WebElement password = chromeDriver.findElement(By.xpath("//*[@id=\"password\"]"));
    password.sendKeys("feigeA.5200....");
    WebElement loginButton = chromeDriver.findElement(By.xpath("//*[@id=\"loginBox\"]/div/p[4]/button"));
    loginButton.click();
    sleep(1000);
  }

  private void execute(String url, String type) {
    ChromeDriver chromeDriver = ChromeUtil.getChromeDriver();
    try {
      login(chromeDriver);
      chromeDriver.get(url);
      WebElement button = chromeDriver.findElement(By.xpath("/html/body/div[3]/div/div[4]"));
      sleep(1000);
      for (int i = 0; i < 10; i++) {
        sleep(500);
        button.click();
      }
      WebElement listAppend = chromeDriver.findElement(By.id("listAppend"));
      List<WebElement> lis = listAppend.findElements(By.cssSelector("li"));
      List<Ganjuw> ganjuws = new ArrayList<>();
      for (WebElement li : lis) {
        WebElement img = li.findElement(By.cssSelector("a div div img"));
        WebElement href = li.findElement(By.cssSelector("a"));
        WebElement title = li.findElement(By.cssSelector("a div h3"));
        WebElement time = li.findElements(By.cssSelector("a div div span")).get(0);
        WebElement author = li.findElements(By.cssSelector("a div div span")).get(1);
        String src = img.getAttribute("src");
        System.out.println(src);
        String href2 = href.getAttribute("href");
        System.out.println(href2);
        String titleText = title.getText();
        System.out.println(titleText);
        String timeText = time.getText();
        System.out.println(timeText);
        String authorText = author.getText();
        System.out.println(authorText);
        if (StringUtils.isEmpty(src)) {
          // 没有图片则使用默认图片
          src = "https://rolin-typora.oss-cn-guangzhou.aliyuncs.com/20250317193026.png";
        }
        ganjuws.add(new Ganjuw(src, type, href2, titleText, timeText, authorText, null, 0L));
      }
      for (Ganjuw ganjuw : ganjuws) {
        chromeDriver.get(ganjuw.getDetailUrl());
        WebElement element = chromeDriver.findElement(By.xpath("/html/body/div[2]/div"));
        String outerHtml = element.getAttribute("outerHTML");
        System.out.println(outerHtml);
        ganjuw.setDetail(outerHtml);
      }
      ganjuwService.batchSaveOrUpdate(ganjuws);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      chromeDriver.close();
    }
  }

  private void sleep(int i) {
    try {
      Thread.sleep(i);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public PageInfo<GanjuwVO> page(PageDTO pageDTO, String type,String keyword) {
    LambdaQueryWrapper<Ganjuw> queryWrapper = ganjuwService.getLambdaQueryWrapper();
    queryWrapper.eq(CheckUtil.isNotEmpty(type), Ganjuw::getType, type);
    queryWrapper.like(CheckUtil.isNotEmpty(keyword), Ganjuw::getTitle, keyword);
    List<Ganjuw> ganjuws = ganjuwService.list(queryWrapper);
    ganjuws.sort((o1, o2) -> (int) (o2.getClickNums() - o1.getClickNums()));
    PageInfo<Ganjuw> pageInfo = new PageInfo<>(pageDTO.getPageNum(), pageDTO.getPageSize(), ganjuws);
    PageInfo<GanjuwVO> convert = pageInfo.convert(Ganjuw::toVo);
    return convert;
  }

  public void cnhnbProcurement() {
    ChromeDriver chromeDriver = ChromeUtil.getChromeDriver();
    try {
      chromeDriver.get("https://www.cnhnb.com/purchase/ganju/");
      for (int i = 0; i < 30; i++) {
        WebElement tbody = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div[2]/div/div[3]/div[1]/div[2]/table/tbody"));
        List<WebElement> as = tbody.findElements(By.cssSelector("a"));
        List<CnhnbProcurement> cnhnbProcurements = new ArrayList<>();
        for (WebElement a : as) {
          List<WebElement> tds = a.findElements(By.cssSelector("tr td"));
          if (tds.size() != 7) {
            continue;
          }
          WebElement type = tds.get(0);
          WebElement purchaseNum = tds.get(1);
          WebElement address = tds.get(2);
          WebElement purchaser = tds.get(3);
          WebElement updateTime = tds.get(4);
          WebElement level = tds.get(5);
          CnhnbProcurement cnhnbProcurement = new CnhnbProcurement(type.findElement(By.cssSelector("span")).getText(),
              purchaseNum.findElement(By.cssSelector("span")).getText(),
              address.findElement(By.cssSelector("span")).getText(),
              purchaser.findElement(By.cssSelector("span")).getText(),
              updateTime.findElement(By.cssSelector("span")).getText(),
              level.findElement(By.cssSelector("span")).getText(),
              a.getAttribute("href"));
          System.out.println(cnhnbProcurement);
          cnhnbProcurements.add(cnhnbProcurement);
        }
        cnhnbProcurementService.batchSaveOrUpdate(cnhnbProcurements);
        WebElement button = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div[2]/div/div[3]/div[1]/div[3]/button[2]"));
        button.click();
        sleep(2000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      chromeDriver.close();
    }
  }

  public List<ProcurementEchartsVO> getProcurementMap() {
    List<CnhnbProcurement> list = cnhnbProcurementService.list();
    List<String> addressList = ConvertUtil.listToList(list, CnhnbProcurement::getAddress);

    // 省份 -> (省value, 城市统计)
    Map<String, ProcurementEchartsVO> provinceMap = new HashMap<>();

    // 直辖市 -> 全部区县
    Map<String, List<String>> directCityToDistricts = new HashMap<>();
    directCityToDistricts.put("北京", List.of("东城区", "西城区", "朝阳区", "丰台区", "石景山区", "海淀区",
        "门头沟区", "房山区", "通州区", "顺义区", "昌平区", "大兴区", "怀柔区", "平谷区", "密云区", "延庆区"));
    directCityToDistricts.put("上海", List.of("黄浦区", "徐汇区", "长宁区", "静安区", "普陀区", "虹口区", "杨浦区",
        "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "奉贤区", "崇明区"));
    directCityToDistricts.put("天津", List.of("和平区", "河东区", "河西区", "南开区", "河北区", "红桥区",
        "东丽区", "西青区", "津南区", "北辰区", "武清区", "宝坻区", "滨海新区", "宁河区", "静海区", "蓟州区"));
    directCityToDistricts.put("重庆", List.of("万州区", "涪陵区", "渝中区", "大渡口区", "江北区", "沙坪坝区",
        "九龙坡区", "南岸区", "北碚区", "綦江区", "大足区", "渝北区", "巴南区", "黔江区", "长寿区",
        "江津区", "合川区", "永川区", "南川区", "璧山区", "铜梁区", "潼南区", "荣昌区", "开州区",
        "梁平区", "武隆区", "城口县", "丰都县", "垫江县", "忠县", "云阳县", "奉节县", "巫山县",
        "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县"));

    Set<String> directCities = directCityToDistricts.keySet();

    int nationwideCount = 0; // 统计 "全国" 出现的次数

    for (String address : addressList) {
      if ("全国".equals(address)) {
        nationwideCount++; // 记录全国出现的次数
        continue;
      }

      String province;
      String city;

      if (directCities.contains(address)) {
        // 直辖市
        province = address;
        city = address;
      } else if (address.length() >= 3) {
        province = address.substring(0, 2); // 省份
        city = address.substring(2) + "市"; // 城市
      } else {
        continue; // 数据格式不正确，跳过
      }

      // 获取省份对象
      ProcurementEchartsVO provinceVO = provinceMap.computeIfAbsent(province, k -> {
        ProcurementEchartsVO vo = new ProcurementEchartsVO();
        vo.setProvince(province);
        vo.setValue(0);
        vo.setCitys(new ArrayList<>());
        return vo;
      });

      // 省份value +1
      provinceVO.setValue(provinceVO.getValue() + 1);

      // 处理城市数据
      List<ProcurementCityVO> cityList = provinceVO.getCitys();
      Optional<ProcurementCityVO> cityOptional = cityList.stream()
          .filter(c -> c.getCity().equals(city))
          .findFirst();

      if (cityOptional.isPresent()) {
        cityOptional.get().setValue(cityOptional.get().getValue() + 1);
      } else {
        ProcurementCityVO newCity = new ProcurementCityVO();
        newCity.setCity(city);
        newCity.setValue(1);
        cityList.add(newCity);
      }

      // 处理直辖市的区县数据
      if (directCities.contains(province)) {
        List<String> districts = directCityToDistricts.get(province);
        for (String district : districts) {
          Optional<ProcurementCityVO> districtOpt = cityList.stream()
              .filter(c -> c.getCity().equals(district))
              .findFirst();
          if (districtOpt.isPresent()) {
            districtOpt.get().setValue(districtOpt.get().getValue() + 1);
          } else {
            ProcurementCityVO newDistrict = new ProcurementCityVO();
            newDistrict.setCity(district);
            newDistrict.setValue(1);
            cityList.add(newDistrict);
          }
        }
      }
    }

    // 处理 "全国" 影响的所有数据
    if (nationwideCount > 0) {
      for (ProcurementEchartsVO provinceVO : provinceMap.values()) {
        // 省份数据 + 全国出现的次数
        provinceVO.setValue(provinceVO.getValue() + nationwideCount);

        for (ProcurementCityVO cityVO : provinceVO.getCitys()) {
          // 所有城市数据 + 全国出现的次数
          cityVO.setValue(cityVO.getValue() + nationwideCount);
        }
      }
    }

    ArrayList<ProcurementEchartsVO> vos = new ArrayList<>(provinceMap.values());
    for (ProcurementEchartsVO vo : vos) {
      List<ProcurementCityVO> citys = vo.getCitys();
      int sum = 0;
      for (ProcurementCityVO city : citys) {
        sum+=city.getValue();
      }
      vo.setValue(sum);
    }
    return vos;
  }

//  public List<ProcurementEchartsVO> getProcurementMap() {
//    Map<String, Integer> map = new HashMap<>();
//    List<CnhnbProcurement> list = cnhnbProcurementService.list();
//    ConvertUtil.listToList(list, item -> {
//      String address = item.getAddress();
//      // 大于2则必然不为全国
//      if (address.length() > 2) {
//        // 先处理省级的内容
//        String province = address.substring(0, 2);
//        map.put(province, map.getOrDefault(province, 0) + 1);
//        // 再处理地级市的内容
//        String city = address.substring(0, 4);
//        map.put(city, map.getOrDefault(province, 0) + 1);
//      } else {
//        // 其他情况说明不为直辖市或者是为全国
//        map.put(address, map.getOrDefault(address, 0) + 1);
//      }
//      return item;
//    });
//    return map;
//  }

  public Map<String, Integer> countLocations(List<String> data) {
    List<String> provinces = Arrays.asList(
        "内蒙古", "黑龙江",
        "新疆", "宁夏", "西藏", "广西",
        "北京", "天津", "上海", "重庆",
        "河北", "山西", "辽宁", "吉林",
        "江苏", "浙江", "安徽", "福建", "江西", "山东",
        "河南", "湖北", "湖南", "广东", "海南",
        "四川", "贵州", "云南", "陕西", "甘肃", "青海",
        "台湾", "香港", "澳门"
    );

    Set<String> municipalities = new HashSet<>(Arrays.asList("北京", "上海", "天津", "重庆"));

    Map<String, Integer> result = new HashMap<>();

    for (String item : data) {
      if ("全国".equals(item)) {
        continue;
      }

      if (municipalities.contains(item)) {
        result.put(item, result.getOrDefault(item, 0) + 1);
      } else {
        String province = null;
        for (String p : provinces) {
          if (item.startsWith(p)) {
            province = p;
            break;
          }
        }

        if (province != null) {
          result.put(province, result.getOrDefault(province, 0) + 1);
          result.put(item, result.getOrDefault(item, 0) + 1);
        }
      }
    }

    return result;
  }

  public PageInfo<CnhnbProcurementVO> cnhnbProcurementPage(PageDTO pageDTO, String keyword) {
    LambdaQueryWrapper<CnhnbProcurement> queryWrapper = cnhnbProcurementService.getLambdaQueryWrapper();
    if (CheckUtil.isNotEmpty(keyword)) {
      queryWrapper.like(CnhnbProcurement::getName, keyword)
          .or().like(CnhnbProcurement::getAddress, keyword)
          .or().like(CnhnbProcurement::getPurchaser, keyword)
          .or().like(CnhnbProcurement::getLevel, keyword);
    }
    queryWrapper.orderByDesc(CnhnbProcurement::getUpdateDate);
    PageInfo<CnhnbProcurement> page = cnhnbProcurementService.page(pageDTO.getPageNum(), pageDTO.getPageSize(), queryWrapper);
    return page.convert(CnhnbProcurement::toVo);
  }

  public void cnhnbPrice() {
    ChromeDriver chromeDriver = ChromeUtil.getChromeDriver();
    chromeDriver.get("https://www.cnhnb.com/hangqing/?k=%E6%9F%91%E6%A9%98");
    try {
      for (int i = 0; i < 30; i++) {
        List<WebElement> lis = chromeDriver.findElements(By.className("market-list-item"));
        List<CnhnbPrice> cnhnbPrices = new ArrayList<>();
        for (WebElement li : lis) {
          WebElement a = li.findElement(By.cssSelector("a"));
          String timeText = a.findElement(By.className("time")).getText();
          Date time = DateUtil.string2date(timeText, DateUtil.DATE_ZH_DEFAULT);
          CnhnbPrice cnhnbPrice = new CnhnbPrice(
              time,
              a.findElement(By.className("product")).getText(),
              a.findElement(By.className("place")).getText(),
              a.findElement(By.className("price")).getText(),
              a.getAttribute("href"));
          System.out.println(cnhnbPrice);
          cnhnbPrices.add(cnhnbPrice);
        }
        cnhnbPriceService.saveBatch(cnhnbPrices);
        WebElement button = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div[2]/div[1]/div[3]/div/div[1]/div/div[2]/div/button[2]"));
        button.click();
        sleep(3000);
      }
    } catch (Exception e) {
      // 解决验证码问题
//      WebElement element = chromeDriver.findElement(By.xpath("//*[@id=\"verify\"]/div[1]/div"));
//      Point point = element.getLocation();
//      Dimension size = element.getSize();
//      File screenshotFile = chromeDriver.getScreenshotAs(OutputType.FILE);
//      BufferedImage fullImg = readFile(screenshotFile);
//
//      Rectangle rect = new Rectangle(point.getX(), point.getY(), size.getWidth(), size.getHeight());
//
//      BufferedImage elementImg = fullImg.getSubimage(rect.x, rect.y, rect.width, rect.height);
//      try {
//        ImageIO.write(elementImg, "png", new File("D:\\vertifyPic\\element_screenshot.png"));
//      } catch (IOException ex) {
//        throw new RuntimeException(ex);
//      }
    }
    chromeDriver.close();
  }

  private void cnhnbLogin(ChromeDriver chromeDriver) {
    chromeDriver.get("https://www.cnhnb.com/hangqing/?k=%E6%9F%91%E6%A9%98");
    WebElement login = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div[1]/div[1]/div/div[1]/div[2]/div[1]"));
    login.click();
    sleep(3000);
    WebDriver frame = chromeDriver.switchTo().frame(0);
    WebElement passwordLogin = frame.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div/div[1]/div[1]/div[3]"));
    passwordLogin.click();
    WebElement account = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div/div[1]/div[2]/form[2]/div[1]/input"));
    account.sendKeys("18319714791");
    WebElement password = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div/div[1]/div[2]/form[2]/div[2]/input"));
    password.sendKeys("xue13680178322");
    WebElement agreeButton = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div/div[1]/div[2]/div[2]/i"));
    agreeButton.click();
    WebElement loginBtn = chromeDriver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div/div/div[1]/div[2]/form[2]/button"));
    loginBtn.click();
    sleep(3000);
  }

  private BufferedImage readFile(File screenshotFile) {
    try {
      return ImageIO.read(screenshotFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public GanjuwVO get(Long id) {
    Ganjuw ganjuw = ganjuwService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(ganjuw);
    ganjuw.setClickNums(ganjuw.getClickNums() + 1);
    ganjuwService.updateById(ganjuw);
    return ganjuw.toVo();
  }

  public PageInfo<CnhnbPriceVO> cnhnbPricePage(PageDTO pageDTO, String keyword) {
    LambdaQueryWrapper<CnhnbPrice> queryWrapper = cnhnbPriceService.getLambdaQueryWrapper();
    if (CheckUtil.isNotEmpty(keyword)) {
      queryWrapper.like(CnhnbPrice::getType, keyword).or()
          .like(CnhnbPrice::getAddress, keyword);
    }
    queryWrapper.orderByDesc(CnhnbPrice::getTime);
    PageInfo<CnhnbPrice> page = cnhnbPriceService.page(pageDTO.getPageNum(), pageDTO.getPageSize(), queryWrapper);
    return page.convert(CnhnbPrice::toVo);
  }

  public void weather() {
    ChromeDriver chromeDriver = ChromeUtil.getChromeDriver();
    try {
      chromeDriver.get("http://www.tqyb.com.cn/");
      sleep(5000);
      Weather weather = new Weather();
      WebElement maxTemp = chromeDriver.findElement(By.id("temp-max"));
      WebElement minTemp = chromeDriver.findElement(By.id("temp-min"));
      weather.setMaxTemperature(maxTemp.getText());
      weather.setMinTemperature(minTemp.getText());

      WebElement rainBtn = chromeDriver.findElement(By.xpath("//*[@id=\"column01\"]/div/div[1]/div[1]/ul/li[2]"));
      rainBtn.click();
      sleep(1000);
      WebElement maxRain = chromeDriver.findElement(By.id("rain-max"));
      WebElement minRain = chromeDriver.findElement(By.id("rain-min"));
      weather.setMaxRainfall(maxRain.getText());
      weather.setMinRainfall(minRain.getText());

      WebElement windBtn = chromeDriver.findElement(By.xpath("//*[@id=\"column01\"]/div/div[1]/div[1]/ul/li[3]"));
      windBtn.click();
      sleep(1000);
      WebElement maxWind = chromeDriver.findElement(By.id("wind-max"));
      WebElement minWind = chromeDriver.findElement(By.id("wind-min"));
      weather.setMaxSpeed(maxWind.getText());
      weather.setMinSpeed(minWind.getText());

      WebElement aqi = chromeDriver.findElement(By.id("aqi-val"));
      WebElement level = chromeDriver.findElement(By.id("aqi-level"));
      WebElement pri = chromeDriver.findElement(By.id("primary-p"));
      WebElement pm = chromeDriver.findElement(By.id("aqi-pm25"));
      WebElement temp = chromeDriver.findElement(By.xpath("//*[@id=\"gzobts-module\"]/div/div/div[2]/div[2]"));
      WebElement windSpeed = chromeDriver.findElement(By.xpath("//*[@id=\"gzobts-module\"]/div/div/div[3]/div[2]"));
      WebElement windDirection = chromeDriver.findElement(By.xpath("//*[@id=\"gzobts-module\"]/div/div/div[3]/div[3]"));
      WebElement shidu = chromeDriver.findElement(By.id("shidu"));
      WebElement rainfall = chromeDriver.findElement(By.xpath("//*[@id=\"gzobts-module\"]/div/div/div[5]/div[3]"));
      WebElement shortContent = chromeDriver.findElement(By.xpath("//*[@id=\"gzzt-module\"]/div/div[2]/div/div[2]/div[2]"));

      weather.setAqi(aqi.getText());
      weather.setLevel(level.getText());
      weather.setPollutants(pri.getText());
      weather.setPm(pm.getText());
      weather.setTemperature(temp.getText());
      weather.setWindLevel(windSpeed.getText());
      weather.setWindDirection(windDirection.getText());
      weather.setHumidity(shidu.getText());
      weather.setHourRainfall(rainfall.getText());
      weather.setShortReport(shortContent.getText());

      String s = "现有以下的天气相关数据,请你以你的角度,给出今日种植柑橘的建议,包括种植柑橘需要注意的细节、可能多发的疾病等内容,下面是天气数据:" + weather;
      String url = "http://localhost:5000/kimichat";

      Map<String, String> requestBody = new HashMap<>();
      requestBody.put("message", s);

      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody);

      try {
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        String message = StringEscapeUtils.unescapeJava(response);
        weather.setAdvice(message);
        System.out.println(message);
      } catch (Exception e) {
        e.printStackTrace();
      }

      System.out.println(weather);
      weatherService.save(weather);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      chromeDriver.close();
    }
  }

  public WeatherVO getWeather() {
    LambdaQueryWrapper<Weather> queryWrapper = weatherService.getLambdaQueryWrapper();
    queryWrapper.orderByDesc(Weather::getCreateDate);
    List<Weather> weathers = weatherService.list(queryWrapper);
    return weathers.getFirst().toVo();
  }

  public Boolean updateGanjuw(GanjuwBO ganjuwBO) {
    Ganjuw ganjuw = ganjuwService.getById(ganjuwBO.getId());
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(ganjuw);
    String imgUrl = ganjuwBO.getHeadImg();
    if(!imgUrl.contains("http")){
      Attachment attachment = attachmentService.getById(Long.valueOf(imgUrl));
      String path = attachment.getPathName();
      File file = new File(path);
      String headImg = postCommonService.uploadPostImageReturnUrl(new File[]{file});
      ganjuwBO.setHeadImg(headImg);
    }
    return ganjuwService.updateById(ganjuwBO.toEntity());
  }

  public Boolean addGanjuw(GanjuwBO ganjuwBO) {
    LambdaQueryWrapper<Ganjuw> queryWrapper = ganjuwService.getLambdaQueryWrapper();
    queryWrapper.eq(Ganjuw::getTitle, ganjuwBO.getTitle());
    CrawlerErrorEnum.CRAWLER_EXIST_ERROR.isTrue(ganjuwService.exists(queryWrapper));
    Attachment attachment = attachmentService.getById(Long.valueOf(ganjuwBO.getHeadImg()));
    String path = attachment.getPathName();
    File file = new File(path);
    String headImg = postCommonService.uploadPostImageReturnUrl(new File[]{file});
    ganjuwBO.setHeadImg(headImg);
    Ganjuw entity = ganjuwBO.toEntity();
    LambdaQueryWrapper<Ganjuw> wrapper = ganjuwService.getLambdaQueryWrapper();
    wrapper.orderByDesc(Ganjuw::getClickNums) // 按clickNums降序排序
        .last("LIMIT 1"); // 只取第一条数据
    Ganjuw ganjuw = ganjuwService.getOne(wrapper);
    entity.setClickNums(ganjuw.getClickNums()+1);
    return ganjuwService.save(entity);
  }

  public Boolean deleteGanjuw(Long id) {
    Ganjuw ganjuw = ganjuwService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(ganjuw);
    return ganjuwService.deleteById(id);
  }

  public Boolean updateCnhnb(CnhnbProcurementBO cnhnbProcurementBO) {
    CnhnbProcurement cnhnbProcurement = cnhnbProcurementService.getById(cnhnbProcurementBO.getId());
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbProcurement);
    return cnhnbProcurementService.updateById(cnhnbProcurementBO.toEntity());
  }

  public Boolean addCnhnb(CnhnbProcurementBO cnhnbProcurementBO) {
    return cnhnbProcurementService.save(cnhnbProcurementBO.toEntity());
  }

  public Boolean deleteCnhnb(Long id) {
    CnhnbProcurement cnhnbProcurement = cnhnbProcurementService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbProcurement);
    return cnhnbProcurementService.deleteById(id);
  }

  public Boolean updatePrice(CnhnbPriceBO cnhnbPriceBO) {
    CnhnbPrice cnhnbPrice = cnhnbPriceService.getById(cnhnbPriceBO.getId());
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbPrice);
    return cnhnbPriceService.updateById(cnhnbPriceBO.toEntity());
  }

  public Boolean addPrice(CnhnbPriceBO cnhnbPriceBO) {
    return cnhnbPriceService.save(cnhnbPriceBO.toEntity());
  }

  public Boolean deletePrice(Long id) {
    CnhnbPrice cnhnbPrice = cnhnbPriceService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbPrice);
    return cnhnbPriceService.deleteById(id);
  }

  public Boolean updateWeather(WeatherBO weatherBO) {
    Weather weather = weatherService.getById(weatherBO.getId());
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(weather);
    return weatherService.updateById(weatherBO.toEntity());
  }

  public Boolean addWeather(WeatherBO weatherBO) {
    return weatherService.save(weatherBO.toEntity());
  }

  public Boolean deleteWeather(Long id) {
    Weather weather = weatherService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(weather);
    return weatherService.deleteById(id);
  }

  public PageInfo<WeatherVO> weatherPage(PageDTO pageDTO) {
    PageInfo<Weather> page = weatherService.page(pageDTO.getPageNum(), pageDTO.getPageSize(), weatherService.getLambdaQueryWrapper());
    return page.convert(Weather::toVo);
  }

  public Boolean weatherIdentify(Long id) {
    Weather weather = weatherService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(weather);
    String advice = smartIdentifyService.identifyWeather(weather);
    weather.setAdvice(advice);
    return weatherService.updateById(weather);
  }

  public CnhnbProcurementVO getCnhnb(Long id) {
    CnhnbProcurement cnhnbProcurement = cnhnbProcurementService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbProcurement);
    return cnhnbProcurement.toVo();
  }

  public CnhnbPriceVO getPrice(Long id) {
    CnhnbPrice cnhnbPrice = cnhnbPriceService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(cnhnbPrice);
    return cnhnbPrice.toVo();
  }

  public WeatherVO getWeatherById(Long id) {
    Weather weather = weatherService.getById(id);
    CrawlerErrorEnum.CRAWLER_NOT_EXIST_ERROR.isNull(weather);
    return weather.toVo();
  }

  public Map<String, Integer> getPriceMap() {
    Map<String, List<CnhnbPriceVO>> map = new HashMap<>();
    Map<String, Integer> result = new HashMap<>();
    List<CnhnbPriceVO> cnhnbPrices = ConvertUtil.listToList(cnhnbPriceService.list(), CnhnbPrice::toVo);
    List<String> provinces = List.copyOf(PROVINCES);
    for (String province : provinces) {
      map.put(province,new ArrayList<>());
    }
    for (CnhnbPriceVO cnhnbPrice : cnhnbPrices) {
      String address = cnhnbPrice.getAddress();
      // 判断是否存在省份
      for (String province : provinces) {
        if(address.contains(province)){
//          List<CnhnbPriceVO> cnhnbPriceVOS = map.get(province);
//          cnhnbPriceVOS.add(cnhnbPrice);
          result.put(province,result.getOrDefault(province,0)+1);
          break;
        }
      }
    }
//    for (Map.Entry<String, List<CnhnbPriceVO>> entry : map.entrySet()) {
//      result.put(entry.getKey(),entry.getValue().size());
//    }
    return result;
  }

  public Map<String, Integer> countAreas(List<String> areas) {
    Map<String, Integer> result = new HashMap<>();

    for (String area : areas) {
      // 解析省份
      String province = parseProvince(area);
      if (province == null) {
        continue; // 无法解析省份则跳过
      }

      int provinceLen = province.length();
      String remaining = area.substring(provinceLen);

      boolean isMunicipality = isMunicipality(province);

      // 解析市和区县
      String[] cityAndCounty = parseCityAndCounty(remaining, province, isMunicipality);
      String cityFullName = cityAndCounty[0];
      String county = cityAndCounty[1];

      // 统计省级
      incrementKey(result, province);

      // 统计市级（省+市简称）
      if (!cityFullName.isEmpty()) {
        String shortCity = getShortCityName(cityFullName);
        String cityKey = province + shortCity;
        incrementKey(result, cityKey);
      }

      // 统计区县级
      if (!county.isEmpty()) {
        String countyKey = isMunicipality ?
            province + county : // 直辖市格式：省+区县
            getShortCityName(cityFullName) + county; // 非直辖市格式：市简称+区县
        incrementKey(result, countyKey);
      }
    }

    return result;
  }

  private String parseProvince(String input) {
    int maxLen = 0;
    String foundProvince = null;
    for (String province : PROVINCES) {
      if (input.startsWith(province) && province.length() > maxLen) {
        maxLen = province.length();
        foundProvince = province;
      }
    }
    return foundProvince;
  }

  private boolean isMunicipality(String province) {
    return "北京".equals(province) || "天津".equals(province) ||
        "上海".equals(province) || "重庆".equals(province);
  }

  private String[] parseCityAndCounty(String remaining, String province, boolean isMunicipality) {
    String[] res = new String[2];
    if (isMunicipality) {
      res[0] = province; // 市名等于省名
      // 处理"市XX区"情况
      res[1] = remaining.startsWith("市") ? remaining.substring(1) : remaining;
    } else {
      Matcher matcher = Pattern.compile("^(.*?市|.*?自治州|.*?地区|.*?盟)(.*)")
          .matcher(remaining);
      if (matcher.find()) {
        res[0] = matcher.group(1); // 市全称
        res[1] = matcher.group(2); // 区县
      } else {
        res[0] = "";
        res[1] = remaining;
      }
    }
    return res;
  }

  private String getShortCityName(String cityFullName) {
    if (cityFullName.endsWith("市")) {
      return cityFullName.substring(0, cityFullName.length() - 1);
    } else if (cityFullName.endsWith("自治州")) {
      return cityFullName.substring(0, cityFullName.length() - 3);
    } else if (cityFullName.endsWith("地区")) {
      return cityFullName.substring(0, cityFullName.length() - 2);
    } else if (cityFullName.endsWith("盟")) {
      return cityFullName.substring(0, cityFullName.length() - 1);
    }
    return cityFullName;
  }

  private void incrementKey(Map<String, Integer> map, String key) {
    map.put(key, map.getOrDefault(key, 0) + 1);
  }

  public Boolean getSevenDays() {
    List<CnhnbPrice> result = new ArrayList<>();
    List<CnhnbPrice> list = cnhnbPriceService.list();
    List<CnhnbPrice> list1 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list2 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list3 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list4 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list5 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list6 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    List<CnhnbPrice> list7 = list.stream().map(CnhnbPrice::clone).collect(Collectors.toList());
    setTime(list1,0);
    setTime(list2,1);
    setTime(list3,2);
    setTime(list4,3);
    setTime(list5,4);
    setTime(list6,5);
    setTime(list7,6);
    result.addAll(list1);
    result.addAll(list2);
    result.addAll(list3);
    result.addAll(list4);
    result.addAll(list5);
    result.addAll(list6);
    result.addAll(list7);
    Random r = new Random();
    DecimalFormat df = new DecimalFormat("0.00");
    // 全部设置id为null
    ConvertUtil.listToList(result,item -> {
      item = EntityUtil.setIdNull(item);
      double randomValue = r.nextDouble() * 3;
      String formattedValue = df.format(randomValue);
      item.setPrice(formattedValue+"元/斤");
      return item;
    });
    return cnhnbPriceService.saveBatch(result);
  }

  private void setTime(List<CnhnbPrice> list1, int index) {
    for (int i = 0; i < index; i++) {
      for (CnhnbPrice cnhnbPrice : list1) {
        cnhnbPrice.setTime(DateUtil.getLastDay(cnhnbPrice.getTime()));
      }
    }
  }
}
