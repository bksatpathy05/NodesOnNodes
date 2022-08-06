import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Node {

    @JsonProperty("id")
    String id;
    @JsonProperty("child_node_ids")
    ArrayList<String> childIds;
}
