package com.ukeess.bootstrap.web.service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.TransactionRequest;
import com.ukeess.bootstrap.web.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "6znmrtbzhbd6pkcf",
            "w34rjsxcr56r2hc5",
            "b282132e93ad53bf990e1468138d3b5c"
    );

    public void transfer(PaymentRequest paymentRequest) {
        TransactionRequest request = new TransactionRequest()
                .amount(paymentRequest.getAmount())
                .paymentMethodNonce("123")
                .options()
                .submitForSettlement(true)
                .done();
    }
}
