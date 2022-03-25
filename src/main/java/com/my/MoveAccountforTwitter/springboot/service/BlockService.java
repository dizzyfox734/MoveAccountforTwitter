package com.my.MoveAccountforTwitter.springboot.service;

import com.my.MoveAccountforTwitter.springboot.dto.BlockDto;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockService {

    private List<BlockDto> blockList;


    public List<BlockDto> fileConvert(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".")+1);

        blockList = new ArrayList<BlockDto>();

        InputStreamReader isr = new InputStreamReader(file.getInputStream());
        if(ext.equalsIgnoreCase("js")) {
            readFileAddList(isr);
        } else {
        }

        return blockList;

    }

    public void readFileAddList(InputStreamReader isr) throws IOException {
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null) {
            if(line.contains("accountId")) {
                String tmp[] = line.split("\"");
                BlockDto block = new BlockDto();

                block.setId(tmp[3]);
                line = br.readLine();
                tmp = line.split("\"");
                block.setLink(tmp[3]);

                blockList.add(block);
            }
        }

    }



}
