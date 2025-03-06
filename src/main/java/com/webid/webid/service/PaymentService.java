package com.webid.webid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webid.webid.model.Payment;
import com.webid.webid.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Creates a new Payment record.
     */
    public Payment createPayment(Long userID, Long itemID, String auctionType, float itemPrice, 
                                 float expeditedShipping, float expeditedShippingCost, int shippingDays) {
        Payment payment = Payment.create(userID, itemID, auctionType, itemPrice, expeditedShipping, expeditedShippingCost, shippingDays);
        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all Payment records.
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Retrieves a Payment record by its ID.
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    /**
     * Updates an existing Payment record.
     */
    public Payment updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(existingPayment -> {
            existingPayment.setUserID(updatedPayment.getUserID());
            existingPayment.setItemID(updatedPayment.getItemID());
            existingPayment.setAuctionType(updatedPayment.getAuctionType());
            existingPayment.setItemPrice(updatedPayment.getItemPrice());
            existingPayment.setExpeditedShipping(updatedPayment.getExpeditedShipping());
            existingPayment.setExpeditedShippingCost(updatedPayment.getExpeditedShippingCost());
            existingPayment.setShippingDays(updatedPayment.getShippingDays());
            // Optionally update totalCost if needed.
            return paymentRepository.save(existingPayment);
        }).orElse(null);
    }

    /**
     * Deletes a Payment record by its ID.
     */
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
