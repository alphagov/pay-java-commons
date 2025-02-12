package uk.gov.service.payments.commons.queue.model;

import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.List;
import java.util.stream.Collectors;

public class QueueMessage {

    private String messageId;
    private String receiptHandle;
    private String messageBody;

    private QueueMessage(String messageId, String receiptHandle, String messageBody) {
        this.messageId = messageId;
        this.receiptHandle = receiptHandle;
        this.messageBody = messageBody;
    }

    private QueueMessage(String messageId, String messageBody) {
        this(messageId, null, messageBody);
    }

    public static List<QueueMessage> of(ReceiveMessageResponse receiveMessageResult) {

        return receiveMessageResult.messages()
                .stream()
                .map(c -> new QueueMessage(c.messageId(), c.receiptHandle(), c.body()))
                .collect(Collectors.toList());
    }

    public static QueueMessage of(SendMessageResponse sendMessageResult, String messageBody) {
        return new QueueMessage(sendMessageResult.messageId(), messageBody);
    }

    public String getMessageId() {
        return messageId;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
