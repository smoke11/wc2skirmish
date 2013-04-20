package smoke11.wc2skirmish.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import smoke11.DebugView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 11.03.13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class XMLSettingsReader {
    public static HashMap<String, String> paths = new HashMap<String, String>();
    public static HashMap<String, String> imagePaths = new HashMap<String, String>();
    public static HashMap<String, String> recogniseWith = new HashMap<String, String>();
    public static HashMap<String, String> ignoreIfHave = new HashMap<String, String>();
    public static int main(String[] args){ //public static void readXMLSettings() {

        try {
            File fXmlFile;
            String s =args[0];
            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO, XMLSettingsReader.class.getSimpleName(), "Reading File: " + s);
            fXmlFile = new File(s); //settings.xml

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Path");
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Current Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getAttribute("Name");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Name: " + name);
                    String dir = eElement.getAttribute("Dir");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Dir: " + dir);
                    paths.put(name, dir);

                }
            }
            //images
            nList = doc.getElementsByTagName("ImagePath");
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Current Element: " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getAttribute("Name");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Name: " + name);
                    String dir = eElement.getAttribute("Dir");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Dir: " + dir);
                    imagePaths.put(name, dir);
                    String rec = eElement.getAttribute("RecogniseWith");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Name: " + name);
                    recogniseWith.put(name, rec);
                    String ignore = eElement.getAttribute("IgnoreIfHave");
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, XMLSettingsReader.class.getSimpleName(), "Dir: " + dir);
                    ignoreIfHave.put(name,ignore);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
