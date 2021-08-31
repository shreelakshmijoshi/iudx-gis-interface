package iudx.gis.server.apiserver.validation;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static iudx.gis.server.apiserver.util.Constants.*;

public class ValidationFailureHandler implements Handler<RoutingContext> {
  private static final Logger LOGGER = LogManager.getLogger(ValidationFailureHandler.class);

  @Override
  public void handle(RoutingContext context) {
    Throwable failure = context.failure();
    LOGGER.error("error :" + failure);
    if (failure instanceof RuntimeException) {
      // Something went wrong during validation!
      LOGGER.error("error :" + failure.getMessage());
      String validationErrorMessage = MSG_BAD_QUERY;
      context.response().putHeader(CONTENT_TYPE, APPLICATION_JSON)
          .setStatusCode(HttpStatus.SC_BAD_REQUEST)
          .end(validationFailureReponse(validationErrorMessage).toString());
    }
    context.next();
    return;
  }

  private JsonObject validationFailureReponse(String message) {
    return new JsonObject().put(JSON_TYPE, HttpStatus.SC_BAD_REQUEST).put(JSON_TITLE, "Bad Request")
        .put(JSON_DETAIL, message);
  }
}