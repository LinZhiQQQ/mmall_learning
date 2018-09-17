package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LinZhiQ
 * @date 2018/9/12 21:45
 */
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalProce;

    private Boolean allChecked;

    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalProce() {
        return cartTotalProce;
    }

    public void setCartTotalProce(BigDecimal cartTotalProce) {
        this.cartTotalProce = cartTotalProce;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
