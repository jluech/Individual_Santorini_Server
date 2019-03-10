package ch.uzh.ifi.seal.soprafs19.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad update request not containing new data")
public class BadUpdateRequest extends RuntimeException{
}
