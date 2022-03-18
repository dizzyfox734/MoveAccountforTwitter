package com.my.MoveAccountforTwitter.springboot.dto;

public class BlockDto {
    private String id;
    private String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "blocking {" + "accountId=" + id + ", userLink=" + link + "}";
    }
}
