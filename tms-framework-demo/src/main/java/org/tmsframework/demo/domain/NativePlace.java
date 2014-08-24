package org.tmsframework.demo.domain;

import java.io.Serializable;

/**
 * 籍贯Domain
 * 
 * @author zhengdd
 * @version $Id: NativePlace.java,v 0.1 2010-6-23 下午03:35:34 zhengdd Exp $
 */
public class NativePlace implements Serializable {

    private static final long serialVersionUID = -108931802982496964L;

    private String            province;
    private String            city;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
