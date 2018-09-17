package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LinZhiQ
 * @date 2018/9/12 21:31
 */

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
   public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count){
       if(productId == null || count == null){
           return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
       }

       Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
       if(cart == null){
           Cart cartItem = new Cart();
           cartItem.setQuantity(count);
           cartItem.setChecked(Const.Cart.CHECKED);
           cartItem.setProductId(productId);
           cartItem.setUserId(userId);
           cartMapper.insert(cartItem);
       }else{
           count = cart.getQuantity() + count;
           cart.setQuantity(count);
           cartMapper.updateByPrimaryKeySelective(cart);
       }
       CartVo cartVo = this.getCartVoLimit(userId);
       return ServerResponse.createBySuccess(cartVo);
   }

   private CartVo getCartVoLimit(Integer userId){
       CartVo cartVo = new CartVo();
       List<Cart> cartList = cartMapper.selectCartByUserId(userId);
       List<CartProductVo> cartProductVoList = Lists.newArrayList();
       BigDecimal cartTotalPrice = new BigDecimal("0");

       if(CollectionUtils.isNotEmpty(cartList)){
           for(Cart item : cartList){
               CartProductVo cartProductVo = new CartProductVo();
               cartProductVo.setId(item.getId());
               cartProductVo.setUserId(userId);
               cartProductVo.setProductId(item.getProductId());

               Product product = productMapper.selectByPrimaryKey(item.getProductId());
               if(product != null){
                   cartProductVo.setProductMainImage(product.getMainImage());
                   cartProductVo.setProductName(product.getName());
                   cartProductVo.setProductSubTitle(product.getSubtitle());
                   cartProductVo.setProductStatus(product.getStatus());
                   cartProductVo.setProductPrice(product.getPrice());
                   cartProductVo.setProductStock(product.getStock());

                   int buyLimitCount = 0;
                   if(product.getStock() >= item.getQuantity()){
                       cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        buyLimitCount = item.getQuantity();
                   }else{
                       buyLimitCount = product.getStock();
                       cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                       // 更新购物车中数量
                       Cart cartForQuantity = new Cart();
                       cartForQuantity.setId(item.getId());
                       cartForQuantity.setQuantity(buyLimitCount);
                       cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                   }
                   cartProductVo.setQuantity(buyLimitCount);
                   cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));

                   cartProductVo.setProductChecked(item.getChecked());

               }
               if(item.getChecked() == Const.Cart.CHECKED){
                   cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
               }
               cartProductVoList.add(cartProductVo);
           }
       }
       cartVo.setCartTotalProce(cartTotalPrice);
       cartVo.setCartProductVoList(cartProductVoList);
       cartVo.setAllChecked(this.getAllCheckedStatus(userId));
       cartVo.setImageHost(PropertiesUtil.getProperty(Const.FTP_SERVER_HTTP_PREFIX));
       return cartVo;
   }

   @Override
   public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);

   }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId, Integer checked){
        cartMapper.checkedOrUnCheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return  ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

   private boolean getAllCheckedStatus(Integer userId){
       if(userId == null){
           return false;
       }
       return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
   }
}
