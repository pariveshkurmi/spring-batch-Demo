/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.batch.configuration;

import java.util.ArrayList;
import java.util.List;

import io.spring.batch.components.CustomRetryableException;
import io.spring.batch.components.CustomSkipListener;
import io.spring.batch.components.ProcessorService;
import io.spring.batch.components.RetryItemProcessor;
import io.spring.batch.components.RetryItemWriter;
import io.spring.batch.policy.CrestRetryPolicy;
import io.spring.batch.policy.CrestSkipPolicy;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.StringUtils;

/**
 * @author Michael Minella
 */
@Configuration
@EnableBatchProcessing
@EnableRetry
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public ListItemReader reader() {

		List<String> items = new ArrayList<>();

		for(int i = 0; i < 100; i++) {
			items.add(String.valueOf(i));
		}

		ListItemReader<String> reader = new ListItemReader(items);

		return reader;
	}

	@Bean
	public RetryItemProcessor processor(/* @Value("#{jobParameters['retry']}") *//* String retry */) {
		RetryItemProcessor processor = new RetryItemProcessor();
		//ProcessorService processorService = new ProcessorService();
		//String retry ="processor";
		//processorService.setRetry(StringUtils.hasText(retry) && retry.equalsIgnoreCase("processor"));

		return processor;
	}

	@Bean
	public RetryItemWriter writer(/* @Value("#{jobParameters['retry']}")String retry */) {
		//System.out.println("Retry valuessssssss"+retry);
		RetryItemWriter writer = new RetryItemWriter();

		/*
		 * writer.setRetry(StringUtils.hasText(retry) &&
		 * retry.equalsIgnoreCase("writer"));
		 */

		return writer;
	}
	
	@Bean
	@StepScope
	public RetryPolicy crestRetryPolicy() {
		System.out.println("Catch inside Retry policy");
		return new CrestRetryPolicy();
	}

	@Bean
	@StepScope
	public SkipPolicy crestSkipPolicy() {
		System.out.println("Catch inside Skip policy");
		return new CrestSkipPolicy();
	}


	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step")
				.<String, String>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				//.retry(CustomRetryableException.class)
				//.retryLimit(15)
				.skipPolicy(crestSkipPolicy())
				//.retry(CustomRetryableException.class)
				//.retryLimit(15)
				//.skip(CustomRetryableException.class)
				//.skipLimit(8)
				.listener(new CustomSkipListener())
				.build();
	}

	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}
}
