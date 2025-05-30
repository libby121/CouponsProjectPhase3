package com.example.demo.service;
import com.example.demo.entity.Category;
import com.example.demo.entity.Coupon;
import com.example.demo.entity.Customer;
import com.example.demo.exceptions.*;
import com.example.demo.model.CustomerDTO;
import java.util.List;

public interface CustomerService {


      String verify(String username, String password);

      CustomerDTO register(String username,
                                String password, String email);
      Coupon purchaseCoupon(int coupId) throws
            CustomerDoesnotExistException, CouponOutOfStockException,
            PurchaseDuplicationException, couponExpiredException,
            CouponDoesnotExistException, NoSuchCouponException,
            noSuchCartException;



      void cancelOrder(int coupId) throws
            CustomerDoesnotExistException,
            CouponDoesnotExistException,
            NoSuchCouponException, noSuchCartException,
            PurchaseDuplicationException, CouponOutOfStockException,
            couponExpiredException;
      List<Coupon> getCustomerCoupons();

      List<Coupon> getCustomerCouponsBYCategory(Category cat) ;

      List<Coupon> getCustomerCouponsUpToPrice(double maxprice);

      Customer getCustomerDetails() throws CustomerDoesnotExistException;

      List<Coupon> getAllCoupons();


      Coupon getOneCoupon(int coupId) throws NoSuchCouponException;

      boolean login(String email, String password) throws loginException ;
}
