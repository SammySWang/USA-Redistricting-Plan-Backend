package com.national.national;

//import com.national.national.handler.JobHandler.loadPlans;

import com.national.national.handler.JobHandler;
import com.national.national.model.Job;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;



//import static com.national.national.controller.JobController.loadPlans;

@SpringBootApplication
public class NationalServerApplication {



	public static void main(String[] args) {

		SpringApplication.run(NationalServerApplication.class, args);
		System.out.println("Hello, I'm server");
		System.out.println("Start loading plans...");
		ArrayList<String> states = new ArrayList<>(Arrays.asList("MD"));//MD, VA, WA
		//System.out.println(states);
		for(String state: states) {
			JobHandler.loadPlans(state);
			//JobHandler.loadEnactedPlans(state);
		}
//		Job job = new Job("MD");
//		job.getBoxAndWhiskerPlot("BVAP");

	}

}
