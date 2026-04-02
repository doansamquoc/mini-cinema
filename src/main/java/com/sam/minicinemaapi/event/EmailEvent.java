package com.sam.minicinemaapi.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class EmailEvent extends ApplicationEvent {
    String to;
    String subject;
    Map<String, Object> variables;

    public EmailEvent(Object source, String to, String subject, Map<String, Object> variables) {
        super(source);
        this.to = to;
        this.subject = subject;
        this.variables = variables;
    }
}
