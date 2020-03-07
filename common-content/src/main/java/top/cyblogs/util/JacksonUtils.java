package top.cyblogs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON工具类
 *
 * @author CY
 */
public class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private JacksonUtils() {
    }

    public static JsonNode toJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            return new ObjectNode(null);
        }
    }
}
