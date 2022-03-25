package com.my.MoveAccountforTwitter.springboot.controller;

import com.my.MoveAccountforTwitter.springboot.dto.BlockDto;
import com.my.MoveAccountforTwitter.springboot.service.BlockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BlockController {

    private static String clientId, clientSecret;

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        clientId = env.getProperty("twitter.clientId");
        clientSecret = env.getProperty("twitter.clientSecret");
    }


    @Autowired
    private BlockService blockService;

    @PostMapping("/fileLoad")
    public String getFile(@CookieValue(value = "token", required = false) String token, @CookieValue(value = "tokensc", required = false) String tokensc, @CookieValue(value = "id", required = false) String id, @RequestParam("blocklist") MultipartFile blockFile) throws Exception {


        List<BlockDto> blockList = new ArrayList<BlockDto>();

        try {
            blockList = blockService.fileConvert(blockFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for(int i=0; i<blockList.size(); i++) {
            if(sendBlockRequest(token, tokensc, id, blockList.get(i).getId()) == true) {
                blockList.remove(i);
                i--;
            }
        }


        if(blockList.isEmpty()) {
            return "Complete ALL Block";
        } else {
            return "Fail For\n" + blockList.toString();
        }
    }

    private boolean sendBlockRequest(String token, String tokensc, String id, String targetID) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.twitter.com/2/users/" + id + "/blocking";

        // Create Header
        HeaderController headgenerator = new HeaderController(clientId, clientSecret, token, tokensc);
        String header = headgenerator.generateHeader("POST", url);
        HttpHeaders httpheader = new HttpHeaders();
        httpheader.setContentType(MediaType.APPLICATION_JSON);
        httpheader.add("Authorization", header);


        // Create Body
        String body = "{\"target_user_id\": \"" + targetID + "\"}";

        //
        HttpEntity<String> httpEntity = new HttpEntity<String>(body, httpheader);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        return responseEntity.getBody().contains("true");
    }




}
