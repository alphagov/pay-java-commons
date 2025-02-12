package uk.gov.service.payments.commons.queue.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityRequest;
import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;
import uk.gov.service.payments.commons.queue.exception.QueueException;
import uk.gov.service.payments.commons.queue.model.QueueMessage;

import java.util.List;

public class SqsQueueService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SqsClient sqsClient;

    private final int messageMaximumWaitTimeInSeconds;
    private final int messageMaximumBatchSize;
    
    public SqsQueueService(SqsClient sqsClient, int messageMaximumWaitTimeInSeconds, int messageMaximumBatchSize) {
        this.sqsClient = sqsClient;
        this.messageMaximumWaitTimeInSeconds = messageMaximumWaitTimeInSeconds;
        this.messageMaximumBatchSize = messageMaximumBatchSize;
    }

    public QueueMessage sendMessage(String queueUrl, String messageBody) throws QueueException {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        try {
            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
            return QueueMessage.of(sendMessageResponse, messageBody);
        } catch (SqsException e) {
            throw new QueueException(e.getMessage());
        }
    }


    public QueueMessage sendMessage(String queueUrl, String messageBody, int delayInSeconds) throws QueueException {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .delaySeconds(delayInSeconds)
                .build();
        return sendMessage(sendMessageRequest);
    }
    
    private QueueMessage sendMessage(SendMessageRequest sendMessageRequest) throws QueueException {
        try {
            SendMessageResponse sendMessageResult = sqsClient.sendMessage(sendMessageRequest);

            logger.info("Message sent to SQS queue - {}", sendMessageResult);
            return QueueMessage.of(sendMessageResult, sendMessageRequest.messageBody());
        } catch (SqsException | UnsupportedOperationException e) {
            logger.error("Failed sending message to SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }
    
    public List<QueueMessage> receiveMessages(String queueUrl, String messageAttributeName) throws QueueException {
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageAttributeNames(messageAttributeName)
                    .waitTimeSeconds(messageMaximumWaitTimeInSeconds)
                    .maxNumberOfMessages(messageMaximumBatchSize)
                    .build();

            ReceiveMessageResponse receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);

            return QueueMessage.of(receiveMessageResult);
        } catch (SqsException | UnsupportedOperationException e) {
            logger.error("Failed to receive messages from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }

    public DeleteMessageResponse deleteMessage(String queueUrl, String messageReceiptHandle) throws QueueException {
        try {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(messageReceiptHandle)
                    .build();
            return sqsClient.deleteMessage(deleteMessageRequest);
        } catch (SqsException | UnsupportedOperationException e) {
            logger.error("Failed to delete message from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        } catch (AwsServiceException e) {
            logger.error("Failed to delete message from SQS queue - [errorMessage={}] [awsErrorCode={}]", e.getMessage(), e.awsErrorDetails().errorCode());
            String errorMessage = String.format("%s [%s]", e.getMessage(), e.awsErrorDetails().errorCode());
            throw new QueueException(errorMessage);
        }
    }

    public ChangeMessageVisibilityResponse deferMessage(String queueUrl, String messageReceiptHandle, int timeoutInSeconds) throws QueueException {
        try {
            ChangeMessageVisibilityRequest changeVisibilityRequest = ChangeMessageVisibilityRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(messageReceiptHandle)
                    .visibilityTimeout(timeoutInSeconds)
                    .build();

            return sqsClient.changeMessageVisibility(changeVisibilityRequest);
        } catch (SqsException | UnsupportedOperationException e) {
            logger.error("Failed to defer message from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }
}
