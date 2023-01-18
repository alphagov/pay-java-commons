package uk.gov.service.payments.commons.queue.sqs;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import uk.gov.service.payments.commons.queue.exception.QueueException;
import uk.gov.service.payments.commons.queue.model.QueueMessage;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqsQueueServiceTest {

    private static final String QUEUE_URL = "http://queue-url";
    private static final String MESSAGE = "{chargeId: 123}";
    private static final String MESSAGE_ATTRIBUTE_NAME = "All";

    @Mock
    private AmazonSQS mockSqsClient;
    @Mock
    private Appender<ILoggingEvent> mockAppender;

    private ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

    private SqsQueueService sqsQueueService;

    @Before
    public void setUp() {
        sqsQueueService = new SqsQueueService(mockSqsClient, 20, 10);

        Logger root = (Logger) LoggerFactory.getLogger(SqsQueueService.class);
        root.setLevel(Level.INFO);
        root.addAppender(mockAppender);
    }

    @Test
    public void shouldSendMessageToQueueSuccessfully() throws QueueException {
        SendMessageResult sendMessageResult = new SendMessageResult();
        sendMessageResult.setMessageId("test-message-id");
        SendMessageRequest sendMessageRequest = new SendMessageRequest(QUEUE_URL, MESSAGE);
        
        when(mockSqsClient.sendMessage(sendMessageRequest)).thenReturn(sendMessageResult);

        QueueMessage message = sqsQueueService.sendMessage(QUEUE_URL, MESSAGE);
        assertEquals("test-message-id", message.getMessageId());

        verify(mockAppender, times(1)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> logEvents = loggingEventArgumentCaptor.getAllValues();

        assertThat(logEvents.stream().anyMatch(e -> e.getFormattedMessage().contains("Message sent to SQS queue - {MessageId: test-message-id")), is(true));
    }

    @Test
    public void shouldSendMessageWithDelayToQueueSuccessfully() throws QueueException {
        SendMessageResult sendMessageResult = new SendMessageResult();
        sendMessageResult.setMessageId("test-message-id");
        SendMessageRequest sendMessageRequest = new SendMessageRequest(QUEUE_URL, MESSAGE).withDelaySeconds(2);

        when(mockSqsClient.sendMessage(sendMessageRequest)).thenReturn(sendMessageResult);

        QueueMessage message = sqsQueueService.sendMessage(QUEUE_URL, MESSAGE, 2);
        assertEquals("test-message-id", message.getMessageId());

        verify(mockAppender, times(1)).doAppend(loggingEventArgumentCaptor.capture());
        List<LoggingEvent> logEvents = loggingEventArgumentCaptor.getAllValues();

        assertThat(logEvents.stream().anyMatch(e -> e.getFormattedMessage().contains("Message sent to SQS queue - {MessageId: test-message-id")), is(true));
    }

    @Test(expected = QueueException.class)
    public void shouldThrowExceptionIfMessageIsNotSentToQueue() throws QueueException {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(QUEUE_URL, MESSAGE);
        when(mockSqsClient.sendMessage(sendMessageRequest)).thenThrow(AmazonSQSException.class);

        sqsQueueService.sendMessage(QUEUE_URL, MESSAGE);
    }

    @Test
    public void shouldReceiveMessagesFromQueueSuccessfully() throws QueueException {
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();
        Message message = new Message();
        message.setMessageId("test-message-id");
        message.setReceiptHandle("test-receipt-handle");
        message.setBody("test-message-body");
        receiveMessageResult.getMessages().add(message);

        when(mockSqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);

        List<QueueMessage> queueMessages = sqsQueueService.receiveMessages(QUEUE_URL, MESSAGE_ATTRIBUTE_NAME);
        Assert.assertThat(queueMessages.size(), is(1));
        Assert.assertThat(queueMessages.get(0).getMessageId(), is("test-message-id"));
        Assert.assertThat(queueMessages.get(0).getReceiptHandle(), is("test-receipt-handle"));
        Assert.assertThat(queueMessages.get(0).getMessageBody(), is("test-message-body"));
    }

    @Test
    public void shouldReturnEmptyListWhenReceiveDoesNotReturnAnyMessages() throws QueueException {
        ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();
        when(mockSqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);

        List<QueueMessage> queueMessages = sqsQueueService.receiveMessages(QUEUE_URL, MESSAGE_ATTRIBUTE_NAME);
        assertTrue(queueMessages.isEmpty());
    }

    @Test(expected = QueueException.class)
    public void shouldThrowExceptionIfMessageCannotBeReceivedFromQueue() throws QueueException {
        when(mockSqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenThrow(AmazonSQSException.class);

        sqsQueueService.receiveMessages(QUEUE_URL, MESSAGE_ATTRIBUTE_NAME);
    }
}
