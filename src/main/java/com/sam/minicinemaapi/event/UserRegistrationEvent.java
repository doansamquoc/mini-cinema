package com.sam.minicinemaapi.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRegistrationEvent extends EmailEvent {
    public UserRegistrationEvent(Object source, String to) {
        super(source, to, "Welcome to our platform", Map.of());
    }
}
