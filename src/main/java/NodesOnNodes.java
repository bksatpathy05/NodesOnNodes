
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class NodesOnNodes{

    private HashMap<String, Integer> countMap;

    public NodesOnNodes () {
        countMap = new HashMap();
    }

    public HashMap<String, Integer> findAllNodes(String nodeId) throws Exception {

        // Make a GET request to retrieve nodes
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://nodes-on-nodes-challenge.herokuapp.com/nodes/" + nodeId))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Node> nodes = objectMapper.readValue(response.body(), new TypeReference<>() {});
        String children;

        //Count nodes, and recursively call with children nodes
        for (Node node : nodes) {
            System.out.println((node.id));
            if (!countMap.containsKey(node.id)) {
                countMap.put(node.id, 1);
            }
            else {
                countMap.put(node.id, countMap.get(node.id) + 1);
            }

            if (node.childIds.size() == 0)
                continue;

            children = node.childIds.stream().collect(Collectors.joining(","));
            findAllNodes(children);
        }
            return countMap;
    }

    public static void main (String[] args)  throws Exception {
        NodesOnNodes nodes = new NodesOnNodes();
        HashMap<String, Integer> map = nodes.findAllNodes("089ef556-dfff-4ff2-9733-654645be56fe");

        System.out.println("Number of unique Nodes: " + map.size());

        Optional<Map.Entry<String, Integer>> maxEntry = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        System.out.println("Highest shared node: "+ maxEntry.get().getKey() + " with " + maxEntry.get().getValue());
        }
}
