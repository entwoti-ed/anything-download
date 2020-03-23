package com.cy;

import com.cy.htmlparser.DownloadListParser;
import com.cy.model.DownloadItem;

import java.io.IOException;
import java.util.List;

public class ImoocSupport {

    public static void main(String[] args) {
        List<DownloadItem> parse = null;
        try {
            parse = DownloadListParser.parse("https://www.imooc.com/learn/1163");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(parse);
    }
}
