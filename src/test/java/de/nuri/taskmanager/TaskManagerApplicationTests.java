package de.nuri.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		String email = registerUser("testuser");
	}
	
	@Test
	void shouldLoginUser() throws Exception {
		String email = registerUser("login");
		
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
		String userAEmail = registerUser("userA");
		String userBEmail = registerUser("userB");
		
		String userAToken = login(userAEmail);
		String userBToken = login(userBEmail);
		
		int taskId = createTask(userAToken);
		
		mockMvc.perform(get("/tasks/" + taskId)
				.header("Authorization", "Bearer " + userBToken))
		       .andExpect(status().isNotFound());
		
	}
	
	@Test
	void shouldRejectDuplicationEmail() throws Exception {
		String email = registerUser("duplicate");
		
		String json = """
		              {
		                  "username": "duplicate",
		                  "email": "%s",
		                  "password": "123456"
		              }
		              """.formatted(email);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json").content(json))
		       .andExpect(status().isConflict());
	}
	
	@Test
	void shouldRejectedInvalidPassword() throws Exception {
		String email = registerUser("invalidPassword");
		
		String json = """
		              {
		                 "email": "%s",
		                 "password": "wrongPassword"
		              }
		              """.formatted(email);
		
		mockMvc.perform(post("/auth/login")
				.contentType("application/json")
				.content(json))
		       .andExpect(status().isUnauthorized());
	}
	
	@Test
	void shouldDeleteOwnTask() throws Exception {
		String email  = registerUser("deleteTask");
		String token  = login(email);
		int    taskId = createTask(token);
		
		mockMvc.perform(delete("/tasks/" + taskId)
				.header("Authorization", "Bearer " + token))
		       .andExpect(status().isOk());
		
		mockMvc.perform(get("/tasks/" + taskId)
				.header("Authorization", "" +
						"Bearer " + token))
		       .andExpect(status().isNotFound());
	}
	
	@Test
	void shouldUpdateOwnTask() throws Exception {
		String email  = registerUser("updateTask");
		String token  = login(email);
		int    taskId = createTask(token);
		
		String json = """
		              {
		              "title": "new title",
		              "description": "new description"
		              }
		              """;
		
		mockMvc.perform(put("/tasks/" + taskId)
				.header("Authorization", "Bearer " + token)
				.contentType("application/json")
				.content(json))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.title").value("new title"))
		       .andExpect(jsonPath("$.description").value("new description"));
	}
	
	@Test
	void shouldToggleTaskDone() throws Exception {
		String email  = registerUser("shouldToggle");
		String token  = login(email);
		int    taskId = createTask(token);
		
		mockMvc.perform(patch("/tasks/" + taskId + "/done")
				.header("Authorization", "Bearer " + token))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.done").value(true));
		
		mockMvc.perform(patch("/tasks/" + taskId + "/done")
				.header("Authorization", "Bearer " + token))
		       .andExpect(jsonPath("$.done").value(false));
	}
	
	private String registerUser(String username) throws Exception {
		String email = username + System.currentTimeMillis() + "@test.de";
		String json = """
		              {
		                  "username": "%s",
		                  "email": "%s",
		                  "password": "123456"
		              }
		              """.formatted(username, email);
		
		mockMvc.perform(post("/auth/register")
				.contentType("application/json")
				.content(json)).andExpect(status().isOk());
		
		return email;
	}
	
	private String login(String email) throws Exception {
		
		String json = """
		              {
		                  "email":"%s",
		                  "password":"123456"
		              }
		              """.formatted(email);
		
		String response = mockMvc.perform(post("/auth/login")
				.contentType("application/json")
				.content(json))
		                         .andExpect(status().isOk())
		                         .andReturn()
		                         .getResponse()
		                         .getContentAsString();
		
		return objectMapper
				.readTree(response)
				.get("token")
				.asText();
	}
	
	private int createTask(String token) throws Exception {
		
		String json = """
		              {
		                  "title":"Test Task",
		                  "description":"Test Description"
		              }
		              """;
		
		String response = mockMvc.perform(post("/tasks")
				.header("Authorization", "Bearer " + token)
				.contentType("application/json")
				.content(json))
		                         .andExpect(status().isOk())
		                         .andReturn()
		                         .getResponse()
		                         .getContentAsString();
		
		return objectMapper
				.readTree(response)
				.get("id")
				.asInt();
	}
}

