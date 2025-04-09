package uk.gov.service.payments.commons.queue.exception;

public class QueueException extends Exception {

    public QueueException(){
        
    }
    
    public QueueException(String message) {
        super(message);
    }
}
