package com.example.demo.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ReadListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
// import javax.servlet.ServletInputStream;
// import javax.servlet.ReadListener;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;


public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                values[i] = XSSanitizer.stripXSS(values[i]);  
            }
        }
        return values;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value != null) {
            value = XSSanitizer.stripXSS(value); 
        }
        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value != null) {
            value = XSSanitizer.stripXSS(value);  
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream inputStream = super.getInputStream();
        byte[] bodyBytes = inputStream.readAllBytes();
        String requestBody = new String(bodyBytes);

       
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(requestBody);

        
        sanitizeJsonNode(jsonNode);

        String sanitizedBody = mapper.writeValueAsString(jsonNode);

        return new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sanitizedBody.getBytes());

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }
        };
    }

    private void sanitizeJsonNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.fields().forEachRemaining(entry -> {
                JsonNode valueNode = entry.getValue();
                if (valueNode.isTextual()) {
                    objectNode.put(entry.getKey(), XSSanitizer.stripXSS(valueNode.textValue()));  
                } else if (valueNode.isObject() || valueNode.isArray()) {
                    sanitizeJsonNode(valueNode);
                }
            });
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode jsonNode = arrayNode.get(i);
                if (jsonNode.isObject()) {
                    sanitizeJsonNode(jsonNode);
                } else if (jsonNode.isTextual()) {
                    arrayNode.set(i, new TextNode(XSSanitizer.stripXSS(jsonNode.textValue())));  
                }
            }
        }
    }
}
