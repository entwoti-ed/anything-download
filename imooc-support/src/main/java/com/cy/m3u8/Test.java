package com.cy.m3u8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iheartradio.m3u8.*;
import com.iheartradio.m3u8.data.Playlist;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException, ParseException, PlaylistException {
        FileInputStream stream = new FileInputStream("C:\\Users\\dnydi\\Desktop\\imooc.m3u8");
        PlaylistParser parser = new PlaylistParser(stream, Format.EXT_M3U, Encoding.UTF_8);
        Playlist playlist = parser.parse();
        String s = new ObjectMapper().writeValueAsString(playlist);
        System.out.println(s);
    }
}
