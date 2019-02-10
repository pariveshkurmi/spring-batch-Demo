package io.spring.batch.policy;

import java.util.logging.Logger;

import org.apache.log4j.spi.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import io.spring.batch.components.CustomRetryableException;

public class CrestSkipPolicy implements SkipPolicy {

	//private static final Logger logger = LoggerFactory.getLogger("badRecordLogger");
	 
    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
    	System.out.println("Skip Plocyyyyyyyyy entry.............................");
        if (exception instanceof NullPointerException) {
            return false;
        } else if (exception instanceof CustomRetryableException && skipCount <= 15) {
        	System.out.println("Skip Plocyyyyyyyyy entry");
        	//CustomRetryableException ffpe = (CustomRetryableException) exception;
			/*
			 * StringBuilder errorMessage = new StringBuilder();
			 * errorMessage.append("An error occured while processing the " +
			 * ffpe.getMessage() + " line of the file. Below was the faulty " + "input.\n");
			 * errorMessage.append(ffpe.getInput() + "\n"); logger.error("{}",
			 * errorMessage.toString());
			 */
            return true;
        } else {
            return false;
        }
    }

}
