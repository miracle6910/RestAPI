package testgroup;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {

    private static final String url = "http://94.198.50.185:7081/api/users";
    private static String sessionId = null;
    private static final RestTemplate restTemplate = new RestTemplate();

    private static Integer result;

    public static void main(String[] args) {

        ResponseEntity<String> getAllUsersResponse = restTemplate.getForEntity(url, String.class);
        HttpHeaders headers = getAllUsersResponse.getHeaders();
        String setCookie = Objects.requireNonNull(headers.get("Set-Cookie")).get(0);
        sessionId = setCookie;

        System.out.println("Session ID: " + sessionId);
        System.out.println("Список пользователей: " + getAllUsersResponse.getBody());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", sessionId);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        //POST
        Map<String, Object> postAddUser = new HashMap<>();
        postAddUser.put("id", 3);
        postAddUser.put("name", "James");
        postAddUser.put("lastName", "Brown");
        postAddUser.put("age", 30);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(postAddUser, requestHeaders);

        ResponseEntity<String> postResponse = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println("Первая часть кода: " + postResponse.getBody());
        //PUT
        Map<String, Object> putUpdateUser = new HashMap<>();
        putUpdateUser.put("id", 3);
        putUpdateUser.put("name", "Thomas");
        putUpdateUser.put("lastName", "Shelby");
        putUpdateUser.put("age", 30);

        HttpEntity<Map<String, Object>> updateRequestEntity = new HttpEntity<>(putUpdateUser, requestHeaders);
        ResponseEntity<String> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, updateRequestEntity, String.class);
        System.out.println("Вторая часть кода: " + updateResponse.getBody());
        //DELETE
        HttpEntity<String> deleteRequestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> deleteUser = restTemplate.exchange(url + "/3", HttpMethod.DELETE, deleteRequestEntity, String.class);
        System.out.println("Третья часть кода: " + deleteUser.getBody());

        result = postResponse.getBody().concat(updateResponse.getBody().concat(deleteUser.getBody())).length();

        System.out.println("Сумма символов в кодах: " + result);

    }
}