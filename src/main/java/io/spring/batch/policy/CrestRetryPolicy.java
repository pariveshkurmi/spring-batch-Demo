package io.spring.batch.policy;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;

import io.spring.batch.components.CustomRetryableException;

public class CrestRetryPolicy implements RetryPolicy{

	@Override
	public boolean canRetry(RetryContext context) {
		System.out.println("Retry Plocyyyyyyyyy entry");
		Throwable throwable = context.getLastThrowable();
		if(context.getRetryCount() == 15 && throwable instanceof CustomRetryableException) {
			System.out.println("Retrying "+context.getRetryCount());
			return false;
		}
		if(throwable instanceof CustomRetryableException) {
			return true;
		}
		return false;
	}

	@Override
	public RetryContext open(RetryContext parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close(RetryContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerThrowable(RetryContext context, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

}
