package uk.gov.service.payments.logging;

/**
 * This file needs to be kept in sync with its Node.js consociate
 * @see <a href="https://github.com/alphagov/pay-js-commons/blob/master/src/logging-keys/index.js">https://github.com/alphagov/pay-js-commons/blob/master/src/logging-keys/index.js</a>
 */
public interface LoggingKeys {

    /**
     * "card", "Direct Debit"
     */
    String PAYMENT_TYPE = "payment_type";

    /**
     * "payment", "mandate", "refund"
     */
    String RESOURCE_TYPE = "resource_type";

    /**
     * "sandbox", "Worldpay", "Smartpay", "ePDQ", "Stripe", "GoCardless"
     */
    String PROVIDER = "provider";

    /**
     * The digital wallet used for a payment
     * Value must be a value of the WalletType enum
     * @see <a href="https://github.com/alphagov/pay-connector/blob/master/src/main/java/uk/gov/pay/connector/wallets/WalletType.java">WalletType</a>
     */
    String WALLET = "wallet";

    /**
     * The type of a gateway account
     * Value must be a value of the GatewayAccountEntity.Type enum
     * @see <a href="https://github.com/alphagov/pay-connector/blob/master/src/main/java/uk/gov/pay/connector/gatewayaccount/model/GatewayAccountEntity.java">GatewayAccountEntity</a>
     */
    String GATEWAY_ACCOUNT_TYPE = "gateway_account_type";

    /**
     * The type of operation being performed with a gateway for a card payment
     * Value must be a value of the OperationType enum
     * @see <a href="https://github.com/alphagov/pay-connector/blob/master/src/main/java/uk/gov/pay/connector/paymentprocessor/model/OperationType.java">OperationType</a>
     */
    String GATEWAY_CARD_OPERATION = "gateway_card_operation";

    /**
     * The amount of a payment in pence
     */
    String AMOUNT = "amount";
 
    /**
     * Mandate external id
     */
    String MANDATE_EXTERNAL_ID = "mandate_external_id";

    /**
     * The PSP's identifier for a payment
     */
    String PROVIDER_PAYMENT_ID = "provider_payment_id";

    /**
     * The ID a provider gives to an event (e.g. one in a notification)
     */
    String PROVIDER_EVENT_ID = "provider_event_id";

    /**
     * The reference a partner service assigns to a payment, mandate etc.
     */
    String SERVICE_PAYMENT_REFERENCE = "service_reference";

    /**
     * The ID GOV.UK Pay gives to a gateway account
     */
    String GATEWAY_ACCOUNT_ID = "gateway_account_id";

    /**
     * The ID of an event emitted to ledger
     */
    String LEDGER_EVENT_ID = "ledger_event_id";

    /**
     * The type of an event emitted to ledger
     */
    String LEDGER_EVENT_TYPE = "ledger_event_type";

    /**
     * The type of an internal event recorded by Direct Debit
     * Value must be a value from the GovUkPayEventType enum
     * @see <a href="https://github.com/alphagov/pay-direct-debit-connector/blob/master/src/main/java/uk/gov/pay/directdebit/events/model/GovUkPayEventType.java">GovUkPayEventType</a>
     */
    String DIRECT_DEBIT_INTERNAL_EVENT_TYPE = "direct_debit_internal_event_type";

    /**
     * The current (or new if transitioning) internal state of a payment, mandate etc.
     */
    String CURRENT_INTERNAL_STATE = "current_internal_state";

    /**
     * The previous internal state of a payment, mandate etc. when transitioning
     */
    String PREVIOUS_INTERNAL_STATUS = "previous_internal_status";

    /**
     * The last event (status) that Worldpay recorded for a payment, refund etc.
     */
    String WORLDPAY_LAST_EVENT = "worldpay_last_event";

    /**
     * The result code (status) that Smartpay recorded for a payment, refund etc.
     */
    String SMARTPAY_RESULT_CODE = "smartpay_result_code";

    /**
     * The status code that ePDQ recorded for a payment, refund etc.
     */
    String EPDQ_STATUS = "epdq_status";

    /**
     * The status that Stripe recorded for a payment, refund etc.
     */
    String STRIPE_STATUS = "stripe_status";

    /**
     * The action that GoCardless recorded for a payment, mandate etc.
     */
    String GOCARDLESS_PAYMENT_ACTION = "gocardless_action";

    /**
     * The HTTP status we sent to a client
     */
    String HTTP_STATUS = "status_code";

    /**
     * The HTTP method for a request
     */
    String METHOD = "method";

    /**
     * The URL for a request
     */
    String URL = "url";

    /**
     * The time taken for the server to respond to a request
     */
    String RESPONSE_TIME = "response_time";

    /**
     * The HTTP status code we received from a remote server (e.g. a payment provider)
     */
    String REMOTE_HTTP_STATUS = "remote_http_status";

    /**
     * The Internet Protocol (IP) address of the client that sent the request.
     */
    String REMOTE_ADDRESS = "remote_address";

    /**
     * AWS error code
     */
    String AWS_ERROR_CODE = "aws_error_code";

    /**
     * Payment External Id
     */
    String PAYMENT_EXTERNAL_ID = "payment_external_id";

    /**
     * Refund External Id
     */
    String REFUND_EXTERNAL_ID = "refund_external_id";

    /**
     * Secure Token
     */
    String SECURE_TOKEN = "secure_token";

    /**
     * Logging key that maps to the http X-Request-Id header
     */
    String MDC_REQUEST_ID_KEY = "x_request_id";

    /**
     * User external id
     */
    String USER_EXTERNAL_ID = "user_external_id";

    /**
     * A service's external id
     */
    String SERVICE_EXTERNAL_ID = "service_external_id";

    /**
     * The error returned by a payment gateway.
     */
    String GATEWAY_ERROR = "gateway_error";

    /**
     * The id of a payout with the gateway (Stripe specific)
     */
    String GATEWAY_PAYOUT_ID = "gateway_payout_id";

    /**
     * The id of the connect account in Stripe 
     */
    String CONNECT_ACCOUNT_ID = "stripe_connect_account_id";

    /**
     * The id of the Stripe event notification
     */
    String STRIPE_EVENT_ID = "stripe_event_id";

    /**
     * The id of a dispute from gateway (Stripe specific)
     */
    String GATEWAY_DISPUTE_ID = "gateway_dispute_id";

    /**
     * Dispute external Id
     */
    String DISPUTE_EXTERNAL_ID = "dispute_external_id";

    /**
     * Agreement external ID
     */
    String AGREEMENT_EXTERNAL_ID = "agreement_external_id";

    /**
     * The authorisation mode for a payment
     */
    String AUTHORISATION_MODE = "authorisation_mode";

    /**
     * Payment instrument external ID
     */
    String PAYMENT_INSTRUMENT_EXTERNAL_ID = "payment_instrument_external_id";

}
