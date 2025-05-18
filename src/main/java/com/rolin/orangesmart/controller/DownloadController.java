package com.rolin.orangesmart.controller;

import com.rolin.orangesmart.service.DownloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunys
 * 2021年2月19日
 */
@Tag(name = "附件下载接口")
@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @Operation(description = "根据eId下载附件")
    @GetMapping("/{eId}")
    public void download(
            @Parameter(description = "附件encryptedId")
            @PathVariable String eId, HttpServletResponse response) {
        downloadService.download(eId, response);
    }

    @Operation(description = "根据Id下载附件")
    @GetMapping("byId/{id}")
    public void download(
            @Parameter(description = "附件encryptedId")
            @PathVariable Long id, HttpServletResponse response) {
        downloadService.download(id, response);
    }

}