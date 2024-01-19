package com.openclassrooms.starterjwt.payload;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageResponseTest {

    private String message;
    private MessageResponse messageResponse;

    @BeforeEach
    public void setup() {
        messageResponse = new MessageResponse(message);
    }

    @Test
    public void setMessage_WithMessage_ShouldSetMessage() {
        String message2 = "abc";
        messageResponse.setMessage(message2);
        assertThat(messageResponse.getMessage()).isEqualTo(message2);
    }
}
