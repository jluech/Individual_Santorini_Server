package ch.uzh.ifi.seal.soprafs19.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid token for user")
public class InvalidToken extends RuntimeException {
}
