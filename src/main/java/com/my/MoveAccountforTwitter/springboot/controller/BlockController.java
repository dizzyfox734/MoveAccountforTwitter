package com.my.MoveAccountforTwitter.springboot.controller;

import com.my.MoveAccountforTwitter.springboot.dto.BlockDto;
import com.my.MoveAccountforTwitter.springboot.service.BlockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping("/fileLoad")
    public String getFile(@RequestParam("blocklist")  MultipartFile blockFile) throws Exception {

        List<BlockDto> fail = null;

        try {
            fail = blockService.fileConvert(blockFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if(fail == null) {
            return "Complete Block";
        } else {
            return fail.toString();
        }
    }
}
