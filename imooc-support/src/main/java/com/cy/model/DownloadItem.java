package com.cy.model;

import lombok.Data;

import java.io.File;

/**
 * 下载项
 *
 * @author CY
 */
@Data
public class DownloadItem {

    private String type;

    private File outputFile;

    private String url;

    private String fileName;
}
