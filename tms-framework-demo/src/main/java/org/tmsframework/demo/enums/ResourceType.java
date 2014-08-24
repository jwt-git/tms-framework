package org.tmsframework.demo.enums;

public enum ResourceType {

    PROVINCE("province", "省份"),
    CITY("city", "城市");

    private String name;
    private String desc;

    private ResourceType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}

