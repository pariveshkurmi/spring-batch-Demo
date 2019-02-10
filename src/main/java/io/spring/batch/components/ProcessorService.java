package io.spring.batch.components;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class ProcessorService {

	private boolean retry = false;
	private int attemptCount = 0;

	@Retryable(value = { CustomRetryableException.class }, maxAttempts = 3)
	public String processData(String item) throws CustomRetryableException {
		System.out.println("processing item " + item);
		if (item.equalsIgnoreCase("42")) {
			attemptCount++;
			System.out.println("Inside retry condition................ ");
			/*
			 * if(attemptCount >= 5) { System.out.println("Success!"); retry = false; return
			 * String.valueOf(Integer.valueOf(item) * -1); } else {
			 */
			System.out.println("Processing of item " + item + " failed");
			throw new CustomRetryableException("Process failed.  Attempt:" + attemptCount);
			// }
		} else {
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}
}
