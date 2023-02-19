package com.avega.task;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderEaseApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

	protected MockMvc mvc;
	protected ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext webApplicationContext;

	protected void setUp() {
		objectMapper = new ObjectMapper();
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

}