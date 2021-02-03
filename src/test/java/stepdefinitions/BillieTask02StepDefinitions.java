package stepdefinitions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BillieTask02StepDefinitions {
    private static Response response;
    private static Map<String,Object> reqBody;
    private static Map<String,String> bookingMap;
    private static Map <String,Object> updateBody;
    private static Map<String,String> authToken;
    private static JsonPath jsonPath ;
    private static int id;
    private static String endPoint="https://restful-booker.herokuapp.com/booking/";
    private static String endPointToken="https://restful-booker.herokuapp.com/auth";
    private static String token;

    @Given("user creates a request body")
    public void user_creates_a_request_body() {
        reqBody= new HashMap<>();
        reqBody.put("firstname","Jason");
        reqBody.put("lastname","Kidd");
        reqBody.put("totalprice",105);
        reqBody.put("depositpaid",true);

        bookingMap=new HashMap<>();
        bookingMap.put("checkin","2120-01-01");
        bookingMap.put("checkout","2121-01-28");
        reqBody.put("bookingdates",bookingMap);
        reqBody.put("additionalneeds","Dinner");
    }

    @When("user posts the request body to the endpoint")
    public void user_posts_the_request_body_to_the_endpoint() {
        response= given().
                contentType(ContentType.JSON).
                auth().preemptive().
                basic("admin","password123").
                body(reqBody).
                when().
                post(endPoint);
    }

    @When("user validates status code returns {int}")
    public void user_validates_status_code_returns(Integer statusCode) {
        System.out.println("Status Code ="+statusCode);
        response.then().
                    assertThat().
                    statusCode(statusCode);

    }
    @When("user validates the data in response and request body")
    public void user_validates_the_data_in_response_and_request_body() {
        response.prettyPrint();
        response.then().
                    assertThat().
                    statusCode(200).
                    body("booking.firstname", Matchers.equalTo(reqBody.get("firstname")),
                "booking.lastname",equalTo(reqBody.get("lastname")),
                "booking.totalprice",equalTo(reqBody.get("totalprice")),
                "booking.depositpaid",equalTo(reqBody.get("depositpaid")),
                "booking.bookingdates.checkin", equalTo(bookingMap.get("checkin")),
                "booking.bookingdates.checkout", equalTo(bookingMap.get("checkout")),
                "booking.additionalneeds",equalTo(reqBody.get("additionalneeds")));
    }

    @Then("user validates bookingid field with non-null value")
    public void user_validates_bookingid_field_with_non_null_value() {
        jsonPath = response.jsonPath();
        Assert.assertTrue(!jsonPath.get("bookingid").toString().equals(null));
        id= jsonPath.get("bookingid");
        System.out.println("bookingid of Response Body = "+id);
    }

    @Given("user creates an Auth token")
    public void user_creates_an_Auth_token() {
        authToken= new HashMap<>();

        authToken.put("username","admin");
        authToken.put("password", "password123");

        Response res= given().
                            contentType(ContentType.JSON).
                            auth().
                            basic("admin","password123").
                            body(authToken).
                       when().
                            post(endPointToken);
        res.prettyPrint();

        jsonPath=res.jsonPath();
        token= jsonPath.getString("token");
        System.out.println("Auth token="+ token);
    }

    @Given("user creates a new  body and updates  {string},{string} and {string}")
    public void user_creates_a_new_body_and_updates_and(String updateFirtname, String updateLastname, String updateAdditionalNeeds) {
        updateBody = new HashMap<>();
        updateBody.put("firstname", updateFirtname);
        updateBody.put("lastname", updateLastname);
        updateBody.put("totalprice",105);
        updateBody.put("depositpaid",true);

        bookingMap=new HashMap<>();
        bookingMap.put("checkin","2120-01-01");
        bookingMap.put("checkout","2121-01-28");
        updateBody.put("bookingdates",bookingMap);
        updateBody.put("additionalneeds",updateAdditionalNeeds);
    }

    @When("user puts the updated body")
    public void user_puts_the_updated_body() {
        response= given().
                        auth().
                        preemptive().
                        basic("admin","password123").
                        contentType(ContentType.JSON).
                        body(updateBody).
                   when().
                        put(endPoint+id);

    }

    @Given("user validates the status code {int}")
    public void user_validates_the_status_code(Integer statusCode) {
        System.out.println("Status code after put method="+statusCode);
        response.then().
                    assertThat().
                    statusCode(statusCode);
    }

    @Then("user validates the data in response and updated body")
    public void user_validates_the_data_in_response_and_updated_body() {
        response.then().
                        assertThat().
                        statusCode(200).
                        body("firstname",equalTo(updateBody.get("firstname")),
                            "lastname", equalTo(updateBody.get("lastname")),
                            "totalprice",equalTo(updateBody.get("totalprice")),
                            "depositpaid",equalTo(updateBody.get("depositpaid")),
                            "bookingdates.checkin", equalTo(bookingMap.get("checkin")),
                            "bookingdates.checkout",equalTo(bookingMap.get("checkout")),
                            "additionalneeds",equalTo(updateBody.get("additionalneeds")));

    }


    @Given("user sends a get request with created id")
    public void user_sends_a_get_request_with_created_id() {
        response= given().
                        auth().
                        preemptive().
                        basic("admin","password123").contentType(ContentType.JSON).
                   when().
                        get(endPoint+id);

    }

    @Given("user validates the status code is {int}")
    public void user_validates_the_status_code_is(Integer statusCode) {
        System.out.println("Status code of get method after update="+ statusCode );
        response.then().
                 assertThat().
                 statusCode(statusCode);
    }

    @Then("user validates the request body and the updated data")
    public void user_validates_the_request_body_and_the_updated_data() {
        response.prettyPrint();
        response.then().
                 assertThat().
                 statusCode(200).
                 body("firstname",equalTo(updateBody.get("firstname")),
                     "lastname", equalTo(updateBody.get("lastname")),
                     "totalprice",equalTo(updateBody.get("totalprice")),
                     "depositpaid",equalTo(updateBody.get("depositpaid")),
                     "bookingdates.checkin", equalTo(bookingMap.get("checkin")),
                     "bookingdates.checkout",equalTo(bookingMap.get("checkout")),
                     "additionalneeds",equalTo(updateBody.get("additionalneeds")));
    }
    @Given("user sends a delete request to the endpoint")
    public void user_sends_a_delete_request_to_the_endpoint() {
        response=given().
                        auth().
                        preemptive().
                        basic("admin","password123").
                        contentType(ContentType.JSON).
                  when().
                        delete(endPoint+id);
    }

    @When("user validates the status code returns {int}")
    public void user_validates_the_status_code_returns(Integer statusCode) {
        System.out.println("Status Code after Delete request = "+ statusCode);
        response.then().
                        assertThat().
                        statusCode(201);
    }

    @When("user sends a get request")
    public void user_sends_a_get_request() {
        response=given().
                        auth().
                        preemptive().
                        basic("admin","password123").
                        contentType(ContentType.JSON).
                  when().
                        get(endPoint+id);
    }

    @Then("user validates the last status code returns {int}")
    public void user_validates_the_last_status_code_returns(Integer statusCode) {
        System.out.println("Status Code of last Get request= "+statusCode);
            response.then().
                            assertThat().
                            statusCode(statusCode);
    }



}
