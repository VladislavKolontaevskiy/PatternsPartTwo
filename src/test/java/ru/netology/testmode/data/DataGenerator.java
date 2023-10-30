package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder() //спецификация(требования)
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) { //сам запрос
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() { //рандомный логин
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() { //рандомный пароль
        String password = faker.internet().password();
        return password;
    }

    public static class Registration { //вложенный статичный класс
        private Registration() {
        }
//RegistrationDto - тип класса
        public static RegistrationDto getUser(String status) { // создание пользователя
            var user = new RegistrationDto(getRandomLogin(),getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) { //регистрация пользователя
            var registeredUser = getUser(status);
            sendRequest(registeredUser); //отправка POST запроса на сервер для регистрации пользователя
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
