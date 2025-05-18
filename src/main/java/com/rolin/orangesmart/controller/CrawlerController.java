package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.model.common.dto.PageDTO;
import com.rolin.orangesmart.model.common.dto.ResponseDTO;
import com.rolin.orangesmart.model.common.info.PageInfo;
import com.rolin.orangesmart.model.crawler.bo.CnhnbPriceBO;
import com.rolin.orangesmart.model.crawler.bo.CnhnbProcurementBO;
import com.rolin.orangesmart.model.crawler.bo.GanjuwBO;
import com.rolin.orangesmart.model.crawler.bo.WeatherBO;
import com.rolin.orangesmart.model.crawler.vo.*;
import com.rolin.orangesmart.service.CrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author: Rolin
 * Date: 2025/2/22
 * Time: 00:26
 */
@Tag(name = "爬虫管理")
@RestController
@RequestMapping("/crawler")
public class CrawlerController {

  @Resource
  private CrawlerService crawlerService;

  @Async
  @Operation(description = "爬取柑橘网的柑橘新闻内容")
  @GetMapping("/ganjuw")
  public void crawlerganjuw() {
    crawlerService.crawlerganjuw();
  }

  @Operation(description = "分页获取柑橘网的柑橘新闻内容")
  @GetMapping("/ganjuwPage")
  public ResponseDTO<PageInfo<GanjuwVO>> ganjuwPage(
      PageDTO pageDTO,
      @Parameter(description = "数据类型") @RequestParam(required = false) String type,
      @Parameter(description = "查询关键字") @RequestParam(required = false) String keyword
      ) {
    return ResponseDTO.ok(crawlerService.page(pageDTO,type,keyword));
  }

  @Operation(description = "根据id获取柑橘网的柑橘新闻内容")
  @GetMapping("/ganjuw/{id}")
  public ResponseDTO<GanjuwVO> getGanjuw(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.get(id));
  }

  @Operation(description = "修改柑橘网的柑橘新闻内容")
  @PostMapping("/ganjuw/update")
  public ResponseDTO<Boolean> update(@RequestBody GanjuwBO ganjuwBO) {
    return ResponseDTO.ok(crawlerService.updateGanjuw(ganjuwBO));
  }

  @Operation(description = "新增柑橘网的柑橘新闻内容")
  @PostMapping("/ganjuw/save")
  public ResponseDTO<Boolean> add(@RequestBody GanjuwBO ganjuwBO) {
    return ResponseDTO.ok(crawlerService.addGanjuw(ganjuwBO));
  }

  @Operation(description = "删除柑橘网的柑橘新闻内容")
  @DeleteMapping("/ganjuw/delete/{id}")
  public ResponseDTO<Boolean> delete(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.deleteGanjuw(id));
  }

  @Async
  @Operation(description = "爬取惠农网的采购信息")
  @GetMapping("/cnhnbProcurement")
  public void cnhnbProcurement() {
    crawlerService.cnhnbProcurement();
  }

  @Operation(description = "获取惠农网的采购统计信息")
  @GetMapping("/getProcurementMap")
  public ResponseDTO<List<ProcurementEchartsVO>> getProcurementMap() {
    return ResponseDTO.ok(crawlerService.getProcurementMap());
  }

  @Operation(description = "生成七天的测试价格数据")
  @GetMapping("/getSevenDays")
  public ResponseDTO<Boolean> getSevenDays() {
    return ResponseDTO.ok(crawlerService.getSevenDays());
  }

  @Operation(description = "获取惠农网的价格统计信息")
  @GetMapping("/getPriceMap")
  public ResponseDTO<Map<String, Integer>> getPriceMap() {
    return ResponseDTO.ok(crawlerService.getPriceMap());
  }

  @Operation(description = "分页获取惠农网的采购信息")
  @GetMapping("/cnhnbProcurementPage")
  public ResponseDTO<PageInfo<CnhnbProcurementVO>> cnhnbProcurementPage(
      PageDTO pageDTO,
      @Parameter(description = "查询关键字") @RequestParam(required = false) String keyword
  ) {
    return ResponseDTO.ok(crawlerService.cnhnbProcurementPage(pageDTO,keyword));
  }

  @Operation(description = "根据id获取惠农网的采购信息")
  @GetMapping("/cnhnbProcurement/{id}")
  public ResponseDTO<CnhnbProcurementVO> cnhnbProcurementPage(
      @PathVariable Long id
  ) {
    return ResponseDTO.ok(crawlerService.getCnhnb(id));
  }

  @Operation(description = "修改惠农网的柑橘新闻内容")
  @PostMapping("/cnhnb/update")
  public ResponseDTO<Boolean> updateCnhnb(@RequestBody CnhnbProcurementBO cnhnbProcurementBO) {
    return ResponseDTO.ok(crawlerService.updateCnhnb(cnhnbProcurementBO));
  }

  @Operation(description = "新增惠农网的柑橘新闻内容")
  @PostMapping("/cnhnb/save")
  public ResponseDTO<Boolean> addCnhnb(@RequestBody CnhnbProcurementBO cnhnbProcurementBO) {
    return ResponseDTO.ok(crawlerService.addCnhnb(cnhnbProcurementBO));
  }

  @Operation(description = "删除惠农网的柑橘新闻内容")
  @DeleteMapping("/cnhnb/delete/{id}")
  public ResponseDTO<Boolean> deleteCnhnb(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.deleteCnhnb(id));
  }

  @Async
  @Operation(description = "爬取惠农网的价格信息")
  @GetMapping("/cnhnbPrice")
  public void cnhnbPrice() {
    crawlerService.cnhnbPrice();
  }

  @Operation(description = "分页获取惠农网的价格信息")
  @GetMapping("/cnhnbPricePage")
  public ResponseDTO<PageInfo<CnhnbPriceVO>> cnhnbPricePage(
      PageDTO pageDTO,
      @Parameter(description = "查询关键字") @RequestParam(required = false) String keyword
  ) {
    return ResponseDTO.ok(crawlerService.cnhnbPricePage(pageDTO,keyword));
  }

  @Operation(description = "修改惠农网的柑橘价格新闻内容")
  @PostMapping("/price/update")
  public ResponseDTO<Boolean> updatePrice(@RequestBody CnhnbPriceBO cnhnbPriceBO) {
    return ResponseDTO.ok(crawlerService.updatePrice(cnhnbPriceBO));
  }

  @Operation(description = "新增惠农网的柑橘价格新闻内容")
  @PostMapping("/price/save")
  public ResponseDTO<Boolean> addPrice(@RequestBody CnhnbPriceBO cnhnbPriceBO) {
    return ResponseDTO.ok(crawlerService.addPrice(cnhnbPriceBO));
  }

  @Operation(description = "删除惠农网的柑橘价格新闻内容")
  @DeleteMapping("/price/delete/{id}")
  public ResponseDTO<Boolean> deletePrice(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.deletePrice(id));
  }

  @Operation(description = "根据id获取惠农网柑橘价格新闻内容")
  @GetMapping("/price/{id}")
  public ResponseDTO<CnhnbPriceVO> getPrice(
      @PathVariable Long id
  ) {
    return ResponseDTO.ok(crawlerService.getPrice(id));
  }

  @Async
  @Operation(description = "爬取当期气象局的天气信息")
  @GetMapping("/weather")
  public void weather() {
    crawlerService.weather();
  }

  @Operation(description = "分页获取当期气象局的天气信息")
  @GetMapping("/weather/page")
  public ResponseDTO<PageInfo<WeatherVO>> weatherPage(
      PageDTO pageDTO
  ) {
    return ResponseDTO.ok(crawlerService.weatherPage(pageDTO));
  }

  @Operation(description = "重新获取天气信息的柑橘种植建议")
  @GetMapping("/weather/identify/{id}")
  public ResponseDTO<Boolean> weatherIdentify(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.weatherIdentify(id));
  }

  @Operation(description = "根据id获取天气信息")
  @GetMapping("/weather/{id}")
  public ResponseDTO<WeatherVO> getWeather(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.getWeatherById(id));
  }

  @Operation(description = "修改当期气象局的天气信息")
  @PostMapping("/weather/update")
  public ResponseDTO<Boolean> updateWeather(@RequestBody WeatherBO weatherBO) {
    return ResponseDTO.ok(crawlerService.updateWeather(weatherBO));
  }

  @Operation(description = "新增当期气象局的天气信息")
  @PostMapping("/weather/save")
  public ResponseDTO<Boolean> addWeather(@RequestBody WeatherBO weatherBO) {
    return ResponseDTO.ok(crawlerService.addWeather(weatherBO));
  }

  @Operation(description = "删除当期气象局的天气信息")
  @DeleteMapping("/weather/delete/{id}")
  public ResponseDTO<Boolean> deleteWeather(@PathVariable Long id) {
    return ResponseDTO.ok(crawlerService.deleteWeather(id));
  }

  @Operation(description = "获取最新的天气信息")
  @GetMapping("/getWeather")
  public ResponseDTO<WeatherVO> getWeather() {
    return ResponseDTO.ok(crawlerService.getWeather());
  }
}
