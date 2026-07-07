package de.nuri.taskmanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagerApplicationTests {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void shouldRegisterUser() throws Exception {
		String email = "testuser" + System.currentTimeMillis() + "@test.de";
		String json = """
		              {
		                  "username": "testuser",
		                  "email": "%s",
		                  "password": "123456"
		              }
		              """.formatted(email);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json")
				.content(json)).andExpect(status().isOk());
		
	}
	
	@Test
	void shouldLoginUser() throws Exception {
		String email = "login" + System.currentTimeMillis() + "@test.de";
		String registerJson = """
		                      {
		                       "username": "testuser",
		                       "email": "%s",
		                       "password": "123456"
		                      }
		                      """.formatted(email);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json")
				.content(registerJson))
		       .andExpect(status().isOk());
		
		String loginJson = """
		                   {
		                   "email": "%s",
		                   "password": "123456"
		                   }
		                   """.formatted(email);
		
		mockMvc.perform(post("/auth/login")
				.contentType("application/json")
				.content(loginJson))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.token").isNotEmpty());
	}
	
	@Test
	void shouldNotAllowUserToAccessOtherUsersTasks() throws Exception {
		String userAEmail = "userA" + System.currentTimeMillis() + "@test.de";
		String userBEmail = "userA" + System.currentTimeMillis() + "@test.de";
		
		String userARegisterJson = """
		                           {
		                           "username": "userA",
		                           "email": "%s",
		                           "password": "123456"
		                           }
		                           """.formatted(userAEmail);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json")
				.content(userAEmail)).andExpect(status().isOk());
		
		String userBRegisterJson = """
		                           {
		                           "username": "userB",
		                           "email": "%s",
		                           "password": "123456"
		                           }
		                           """.formatted(userBEmail);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json")
				.content(userBEmail)).andExpect(status().isOk());
		
		String userALoginJson = """
		                        {
		                        "email": "%s",
		                        "password": "123456"
		                        }
		                        """.formatted(userAEmail);
		
		String userALoginResponse = mockMvc.perform(post("/auth/login")
				.contentType("application/json")
				.content(userALoginJson)).andExpect(status().isOk())
		                                   .andReturn().getResponse()
		                                   .getContentAsString();
		
		String userBLoginJson = """
		                        {
		                            "email":"%s",
		                            "password":"123456"
		                        }
		                        """.formatted(userBEmail);
		
		String userBLoginResponse = mockMvc.perform(post("/auth/login")
				.contentType("application/json")
				.content(userBLoginJson)).andExpect(status().isOk())
		                                   .andReturn()
		                                   .getResponse()
		                                   .getContentAsString();
		
		JsonNode userATokenJson = objectMapper.readTree(userALoginResponse);
		String   userAToken     = userATokenJson.get("token").asText();
		
		JsonNode userBTokenJson = objectMapper.readTree(userBLoginResponse);
		String   userBToken     = userBTokenJson.get("token").asText();
		
		String taskJson = """
		                  {
		                      "title":"Private Task",
		                      "description":"Only user A should see this"
		                  }
		                  """;
		
		String createdTaskResponse = mockMvc.perform(post("/tasks").header("Authorization", "Bearer " + userAToken)
		                                                           .contentType("application/json")
		                                                           .content(taskJson))
		                                    .andExpect(status().isOk())
		                                    .andExpect(status().isOk())
		                                    .andReturn()
		                                    .getResponse()
		                                    .getContentAsString();
		
		JsonNode taskNode = objectMapper.readTree(createdTaskResponse);
		int      taskId   = taskNode.get("id").asInt();
		
		mockMvc.perform(get("/tasks/" + taskId)
				.header("Authorization", "Bearer " + userBToken))
		       .andExpect(status().isNotFound());
		
	}
	
}
