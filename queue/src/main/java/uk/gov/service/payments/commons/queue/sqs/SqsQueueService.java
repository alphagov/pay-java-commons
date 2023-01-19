package uk.gov.service.payments.commons.queue.sqs;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.service.payments.commons.queue.exception.QueueException;
import uk.gov.service.payments.commons.queue.model.QueueMessage;

import java.util.List;

public class SqsQueueService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AmazonSQS sqsClient;

    private final int messageMaximumWaitTimeInSeconds;
    private final int messageMaximumBatchSize;
    
    public SqsQueueService(AmazonSQS sqsClient, int messageMaximumWaitTimeInSeconds, int messageMaximumBatchSize) {
        this.sqsClient = sqsClient;
        this.messageMaximumWaitTimeInSeconds = messageMaximumWaitTimeInSeconds;
        this.messageMaximumBatchSize = messageMaximumBatchSize;
    }

    public QueueMessage sendMessage(String queueUrl, String messageBody) throws QueueException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, messageBody);
        return sendMessage(sendMessageRequest);
    }

    public QueueMessage sendMessage(String queueUrl, String messageBody, int delayInSeconds) throws QueueException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, messageBody).withDelaySeconds(delayInSeconds);
        return sendMessage(sendMessageRequest);
    }
    
    private QueueMessage sendMessage(SendMessageRequest sendMessageRequest) throws QueueException {
        try {
            SendMessageResult sendMessageResult = sqsClient.sendMessage(sendMessageRequest);

            logger.info("Message sent to SQS queue - {}", sendMessageResult);
            return QueueMessage.of(sendMessageResult, sendMessageRequest.getMessageBody());
        } catch (AmazonSQSException | UnsupportedOperationException e) {
            logger.error("Failed sending message to SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }
    
    public List<QueueMessage> receiveMessages(String queueUrl, String messageAttributeName) throws QueueException {
        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
            receiveMessageRequest
                    .withMessageAttributeNames(messageAttributeName)
                    .withWaitTimeSeconds(messageMaximumWaitTimeInSeconds)
                    .withMaxNumberOfMessages(messageMaximumBatchSize);

            ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);

            return QueueMessage.of(receiveMessageResult);
        } catch (AmazonSQSException | UnsupportedOperationException e) {
            logger.error("Failed to receive messages from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }

    public DeleteMessageResult deleteMessage(String queueUrl, String messageReceiptHandle) throws QueueException {
        try {
            return sqsClient.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
        } catch (AmazonSQSException | UnsupportedOperationException e) {
            logger.error("Failed to delete message from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        } catch (AmazonServiceException e) {
            logger.error("Failed to delete message from SQS queue - [errorMessage={}] [awsErrorCode={}]", e.getMessage(), e.getErrorCode());
            String errorMessage = String.format("%s [%s]", e.getMessage(), e.getErrorCode());
            throw new QueueException(errorMessage);
        }
    }

    public ChangeMessageVisibilityResult deferMessage(String queueUrl, String messageReceiptHandle, int timeoutInSeconds) throws QueueException {
        try {
            ChangeMessageVisibilityRequest changeMessageVisibilityRequest = new ChangeMessageVisibilityRequest(
                    queueUrl,
                    messageReceiptHandle,
                    timeoutInSeconds);

            return sqsClient.changeMessageVisibility(changeMessageVisibilityRequest);
        } catch (AmazonSQSException | UnsupportedOperationException e) {
            logger.error("Failed to defer message from SQS queue - {}", e.getMessage());
            throw new QueueException(e.getMessage());
        }
    }
}
