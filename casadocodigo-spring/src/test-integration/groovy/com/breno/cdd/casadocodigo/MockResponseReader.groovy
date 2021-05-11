package com.breno.cdd.casadocodigo

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.mock.web.MockHttpServletResponse

import java.nio.charset.StandardCharsets

class MockResponseReader {
    private final ObjectMapper mapper

    MockResponseReader(ObjectMapper mapper) {
        this.mapper = mapper
    }

    Map read(MockHttpServletResponse response) {
        mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Map)
    }
}
