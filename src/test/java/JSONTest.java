import com.cybershrek.tools.parser.JSON;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JSONTest {

    static class User {
        private String name;
        private int age;
        private boolean active;
        private List<String> tags;

        // Default constructor for Jackson
        public User() {}

        public User(String name, int age, boolean active, List<String> tags) {
            this.name = name;
            this.age = age;
            this.active = active;
            this.tags = tags;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User user = (User) o;
            return age == user.age && active == user.active &&
                    name.equals(user.name) && tags.equals(user.tags);
        }
    }

    @Test
    void testParseToObject() {
        String json = "{\"name\":\"Alice\",\"age\":25,\"active\":true,\"tags\":[\"java\",\"developer\"]}";

        User user = JSON.parse(json, User.class);

        assertEquals("Alice", user.getName());
        assertEquals(25, user.getAge());
        assertTrue(user.isActive());
        assertIterableEquals(List.of("java", "developer"), user.getTags());
    }

    @Test
    void testStringify() {
        User user = new User("Bob", 35, false, List.of("test", "qa"));

        String json = JSON.stringify(user);

        assertTrue(json.contains("\"name\":\"Bob\""));
        assertTrue(json.contains("\"age\":35"));
        assertTrue(json.contains("\"active\":false"));
        assertTrue(json.contains("\"tags\":[\"test\",\"qa\"]"));
    }

    @Test
    void testPrettyPrint() {
        User user = new User("Charlie", 40, true, List.of("manager"));

        String json = JSON.stringify(user, true);

        assertTrue(json.contains("\n"));
        assertTrue(json.contains("  ")); // Indentation
        assertTrue(json.contains("\"name\" : \"Charlie\""));
    }

    @Test
    void testToJsonNode() {
        String json = "{\"user\":{\"name\":\"John\"},\"count\":5}";

        var jsonNode = JSON.parse(json);

        assertEquals("John", jsonNode.get("user").get("name").asText());
        assertEquals(5, jsonNode.get("count").asInt());
    }

    @Test
    void testRoundTrip() {
        User original = new User("Test", 99, true, List.of("a", "b", "c"));

        String json = JSON.stringify(original);
        User restored = JSON.parse(json, User.class);

        assertEquals(original, restored);
    }

    @Test
    void testComplexObject() {
        String json = """
            {
                "users": [
                    {"name": "John", "age": 30},
                    {"name": "Jane", "age": 25}
                ],
                "metadata": {
                    "count": 2,
                    "active": true
                }
            }
            """;

        Map<String, Object> result = JSON.parse(json, Map.class);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> users = (List<Map<String, Object>>) result.get("users");

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).get("name"));
        assertEquals(30, users.get(0).get("age"));

        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) result.get("metadata");

        assertEquals(2, metadata.get("count"));
        assertEquals(true, metadata.get("active"));
    }

    @Test
    void testJsonExceptionOnInvalidJson() {
        String invalidJson = "{ invalid json }";

        assertThrows(RuntimeException.class, () -> {
            JSON.parse(invalidJson, Map.class);
        });

        assertThrows(RuntimeException.class, () -> {
            JSON.parse(invalidJson, User.class);
        });
    }

    @Test
    void testNullAndEmpty() {
        assertThrows(RuntimeException.class, () -> {
            JSON.parse(null, Map.class);
        });

        // Empty object should be valid
        assertTrue(JSON.parse("{}").isEmpty());
    }
}