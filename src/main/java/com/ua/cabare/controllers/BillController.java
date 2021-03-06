package com.ua.cabare.controllers;

import static com.ua.cabare.domain.Response.BILL;
import static com.ua.cabare.domain.Response.BILL_LIST;
import static com.ua.cabare.domain.Response.BILL_PRICE;
import static com.ua.cabare.domain.Response.STATUS;

import com.ua.cabare.domain.Money;
import com.ua.cabare.domain.PayStatus;
import com.ua.cabare.domain.Response;
import com.ua.cabare.models.Bill;
import com.ua.cabare.models.Discount;
import com.ua.cabare.models.OrderItem;
import com.ua.cabare.services.BillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

  @Autowired
  private BillService billService;
  @Autowired
  private Response response;

  public void setBillService(BillService billService) {
    this.billService = billService;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  @RequestMapping(value = "/open", method = RequestMethod.PUT)
  public Response openBill(@RequestBody Bill bill) {
    try {
      bill = billService.openBill(bill);
      response.put(BILL, bill);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public Response updateBill(@RequestBody Bill bill) {
    try {
      Bill updatedBill = billService.updateBill(bill);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/add/orderitems", method = RequestMethod.PUT)
  public Response addOrder(@RequestParam long billId, @RequestParam List<OrderItem> orderItems) {
    try {
      Bill bill = billService.updateBill(billId, orderItems);
      response.put(BILL, bill);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/add/payment", method = RequestMethod.PUT)
  public Response addPayment(@RequestParam long billId, @RequestParam Money income) {
    try {
      Bill bill = billService.addPayment(billId, income);
      response.put(BILL, bill);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/preclose", method = RequestMethod.POST)
  public Response closeBill(@RequestParam Long id, @RequestParam Discount discount) {
    try {
      Bill bill = billService.preCloseBill(id, discount);
      response.put(BILL, bill);
      Money billPrice = bill.getBillPrice();
      response.put(BILL_PRICE, billPrice);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/payoff", method = RequestMethod.POST)
  public Response payOff(@RequestParam("bill_id") Long billId) {
    try {
      billService.payOff(billId);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/opened")
  public Response getOpened() {
    try {
      response.put(BILL_LIST, billService.getOpened());
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/all/opened")
  public Response getOpenedAll() {
    try {
      response.put(BILL_LIST, billService.getOpenedAll());
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }

  @RequestMapping(value = "/all/{paystatus}")
  public Response getBillsByPayStatus(@PathVariable(name = "paystatus") PayStatus payStatus) {
    try {
      List<Bill> bills = billService.getBillsByPayStatus(payStatus);
      response.put(BILL_LIST, bills);
    } catch (Exception ex) {
      response.put(STATUS, ex.getMessage());
    }
    return response;
  }
}
