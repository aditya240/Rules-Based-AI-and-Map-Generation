package s3.ai;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeMap {

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        int width = 72, height = 72, label = 0;
        int[] map_objects = {0, 1, 2};
        int[][] map = new int[width][height];
        int[][] eMap = new int[width][height];
        List<Edge> e = new ArrayList<>();
        List<Edge> t = new ArrayList<>();
        List<List<Integer>> S = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            map[0][i] = 0;
            map[i][0] = 0;
            map[width - 1][i] = 0;
            map[i][height - 1] = 0;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                eMap[i][j] = label;
                label++;
            }
        }

        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                Edge e1 = new Edge(eMap[i][j], eMap[i][j + 1]);
                Edge e2 = new Edge(eMap[i][j], eMap[i + 1][j]);
                e.add(e1);
                e.add(e2);
            }
        }

        for(int i = 0; i < (width * height); i++){
            List<Integer> v = new ArrayList<>();
            v.add(i);
            S.add(v);
        }

        for(int i = 0; i < (e.size() - 1); i++){
            for(int j = 0; j < (e.size() - i - 1); j++){
                if(e.get(j + 1).getWeight() < e.get(j).getWeight()){
                    Edge temp = e.get(j);
                    e.set(j, e.get(j + 1));
                    e.set(j + 1, temp);
                }
            }
        }

        int weight = e.get(e.size()/2).getWeight();
        for(int i = 0; i < e.size(); i++){
            Edge edge = e.get(i);
            if(!(S.get(edge.getV()).equals(S.get(edge.getU())))){
                t.add(edge);
                List<Integer> union = new ArrayList<>();
                union.addAll(S.get(edge.getU()));
                union.addAll(S.get(edge.getV()));
                S.set(edge.getU(), union);
                S.set(edge.getV(), union);
            }
        }

        for(int i = 0; i < t.size(); i++){
            Edge curr = t.get(i);
            if(curr.getWeight() == weight) {
                int u = curr.getU();
                int row = 2 * (u / width) + 1;
                int col = 2 * (u % width) + 1;
                Random rand = new Random();
                int x = rand.nextInt(3);
                if(row > 1 && row < (width - 1) && col > 1 && col < (height - 1)) map[row][col] = map_objects[x];
            }
        }

        create(map);
    }

    public static void create(int[][] map) throws ParserConfigurationException, TransformerException {

        String sep = File.separator;
        String path = "../S3-CS387/src/s3/ai/" + sep + "map.xml";

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("gamestate");
        document.appendChild(root);

        Element entity = document.createElement("entity");
        root.appendChild(entity);

        entity.setAttribute("id", "0");

        Element type = document.createElement("type");
        type.appendChild(document.createTextNode("map"));
        entity.appendChild(type);

        Element width = document.createElement("width");
        width.appendChild(document.createTextNode("72"));
        entity.appendChild(width);

        Element height = document.createElement("height");
        height.appendChild(document.createTextNode("32"));
        entity.appendChild(height);

        Element background = document.createElement("background");
        entity.appendChild(background);

        // append each map row
        for (int i = 0; i < map.length; i++) {
            Element rowElement = document.createElement("row");
            StringBuilder sb = new StringBuilder();
            int l = 0;
            while(l < map.length) {
                Random r = new Random();
                int x = r.nextInt(100);
                if(x > 97) sb.append("t");
                else sb.append(".");
                l++;
            }
            rowElement.appendChild(document.createTextNode(sb.toString()));
            background.appendChild(rowElement);
        }

        Element entity1 = document.createElement("entity");
        root.appendChild(entity1);

        entity1.setAttribute("id", "1");

        Element type1 = document.createElement("type");
        type1.appendChild(document.createTextNode("WPlayer"));
        entity1.appendChild(type1);

        Element gold1 = document.createElement("gold");
        gold1.appendChild(document.createTextNode("2000"));
        entity1.appendChild(gold1);

        Element wood1 = document.createElement("wood");
        wood1.appendChild(document.createTextNode("1500"));
        entity1.appendChild(wood1);

        Element owner1 = document.createElement("owner");
        owner1.appendChild(document.createTextNode("player1"));
        entity1.appendChild(owner1);

        Element entity2 = document.createElement("entity");
        root.appendChild(entity2);

        entity2.setAttribute("id", "2");

        Element type2 = document.createElement("type");
        type2.appendChild(document.createTextNode("WPlayer"));
        entity2.appendChild(type2);

        Element gold2 = document.createElement("gold");
        gold2.appendChild(document.createTextNode("2000"));
        entity2.appendChild(gold2);

        Element wood2 = document.createElement("wood");
        wood2.appendChild(document.createTextNode("1500"));
        entity2.appendChild(wood2);

        Element owner2 = document.createElement("owner");
        owner2.appendChild(document.createTextNode("player2"));
        entity2.appendChild(owner2);

        Element entity3 = document.createElement("entity");
        root.appendChild(entity3);

        entity3.setAttribute("id", "4");

        Element type3 = document.createElement("type");
        type3.appendChild(document.createTextNode("WGoldMine"));
        entity3.appendChild(type3);

        Element x3 = document.createElement("x");
        x3.appendChild(document.createTextNode("0"));
        entity3.appendChild(x3);

        Element y3 = document.createElement("y");
        y3.appendChild(document.createTextNode("0"));
        entity3.appendChild(y3);

        Element rg3 = document.createElement("remaining_gold");
        rg3.appendChild(document.createTextNode("50000"));
        entity3.appendChild(rg3);

        Element hp3= document.createElement("current_hitpoints");
        hp3.appendChild(document.createTextNode("25500"));
        entity3.appendChild(hp3);

        Element entity4 = document.createElement("entity");
        root.appendChild(entity4);

        entity4.setAttribute("id", "5");

        Element type4 = document.createElement("type");
        type4.appendChild(document.createTextNode("WGoldMine"));
        entity4.appendChild(type4);

        Element x4 = document.createElement("x");
        x4.appendChild(document.createTextNode("0"));
        entity4.appendChild(x4);

        Element y4 = document.createElement("y");
        y4.appendChild(document.createTextNode("30"));
        entity4.appendChild(y4);

        Element rg4 = document.createElement("remaining_gold");
        rg4.appendChild(document.createTextNode("50000"));
        entity4.appendChild(rg4);

        Element hp4= document.createElement("current_hitpoints");
        hp4.appendChild(document.createTextNode("25500"));
        entity4.appendChild(hp4);

        Element entity5 = document.createElement("entity");
        root.appendChild(entity5);

        entity5.setAttribute("id", "6");

        Element type5 = document.createElement("type");
        type5.appendChild(document.createTextNode("WPeasant"));
        entity5.appendChild(type5);

        Element x5 = document.createElement("x");
        x5.appendChild(document.createTextNode("6"));
        entity5.appendChild(x5);

        Element y5 = document.createElement("y");
        y5.appendChild(document.createTextNode("6"));
        entity5.appendChild(y5);

        Element owner5 = document.createElement("owner");
        owner5.appendChild(document.createTextNode("player1"));
        entity5.appendChild(owner5);

        Element hp5= document.createElement("current_hitpoints");
        hp5.appendChild(document.createTextNode("30"));
        entity5.appendChild(hp5);

        Element entity6 = document.createElement("entity");
        root.appendChild(entity6);

        entity6.setAttribute("id", "7");

        Element type6 = document.createElement("type");
        type6.appendChild(document.createTextNode("WPeasant"));
        entity6.appendChild(type6);

        Element x6 = document.createElement("x");
        x6.appendChild(document.createTextNode("6"));
        entity6.appendChild(x6);

        Element y6 = document.createElement("y");
        y6.appendChild(document.createTextNode("25"));
        entity6.appendChild(y6);

        Element owner6 = document.createElement("owner");
        owner6.appendChild(document.createTextNode("player2"));
        entity6.appendChild(owner6);

        Element hp6= document.createElement("current_hitpoints");
        hp6.appendChild(document.createTextNode("30"));
        entity6.appendChild(hp6);

        // save to XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));
        transformer.transform(domSource, streamResult);
    }
}



