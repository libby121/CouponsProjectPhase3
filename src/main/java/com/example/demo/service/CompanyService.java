package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.Coupon;
import com.example.demo.exceptions.*;
import com.example.demo.model.CompanyDTO;
import com.example.demo.model.CouponDTO;

import java.util.List;

public interface CompanyService {

//      CompanyDTO register(CompanyDTO u);
      Coupon getOneCoupon(int coupId) throws CouponDoesnotExistException;

     List<Coupon> getCouponsByCategory(Category cat) ;

     void addCoupon(CouponDTO coup) throws CouponExistsException, CouponDateSetException,
            CompanyDoesNotExistException, CouponOutOfStockException;
     List<CouponDTO> getCompanyCoupons();

     void updateCoupon(CouponDTO coupon)
            throws unchangeableCouponCompanyId, CouponDateSetException,
            CouponOutOfStockException ;

     void deleteCoupon(int Couponid) throws CompanyDoesNotExistException;

     List<Coupon> getCouponUpToMaxPrice(double maxPrice);

     Company getCompanyDetails() throws CompanyDoesNotExistException;


}
